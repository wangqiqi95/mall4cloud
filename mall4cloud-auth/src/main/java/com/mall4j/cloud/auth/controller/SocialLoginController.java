package com.mall4j.cloud.auth.controller;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.constant.SocialType;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.vo.TokenInfoVO;
import com.mall4j.cloud.api.biz.feign.NotifyFeignClient;
import com.mall4j.cloud.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.mall4j.cloud.api.rbac.feign.PermissionFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.auth.config.WxConfig;
import com.mall4j.cloud.auth.dto.BindSocialDTO;
import com.mall4j.cloud.auth.manager.TokenStore;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.auth.model.AuthSocial;
import com.mall4j.cloud.auth.service.AuthAccountService;
import com.mall4j.cloud.auth.service.AuthSocialService;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 尝试使用社交账户登录
 * 如果登录成功，返回token信息
 * 如果登录失败，返回一个临时的uid，同时把openid之类的信息保存到数据库
 * @author FrozenWatermelon
 * @date 2021/1/16
 */
@Slf4j
@RequestMapping("/ua/social")
@RestController
@Api(tags = "社交账号登录")
public class SocialLoginController {


    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private AuthSocialService authSocialService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthAccountService authAccountService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private NotifyFeignClient notifyFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;

    @PostMapping("/bind")
    @ApiOperation(value = "绑定社交账号")
    public ServerResponseEntity<?> bind(@RequestBody BindSocialDTO bindSocial) {
        ServerResponseEntity<Boolean> validCodeResponse = notifyFeignClient.checkValidCode(bindSocial.getValidAccount(), bindSocial.getValidCode(), SendTypeEnum.VALID);
        if (!validCodeResponse.isSuccess() || !validCodeResponse.getData()) {
            return ServerResponseEntity.fail(ResponseEnum.VERIFICATION_CODE_ERROR);
        }

        AuthSocial authSocial = authSocialService.getByTempUid(bindSocial.getTempUid());
        if (authSocial == null || authSocial.getUid() != null) {
            return ServerResponseEntity.showFailMsg("请重启应用后进行重新操作~");
        }
        AuthAccount authAccount = authAccountService.getAccountByInputUserName(bindSocial.getValidAccount(), SysTypeEnum.ORDINARY.value());
        if (authAccount == null) {
            return ServerResponseEntity.fail(ResponseEnum.ACCOUNT_NOT_REGISTER);
        }
        Long uid = authAccount.getUid();
        int bindCount = authSocialService.countByUidAndSocialType(uid, authSocial.getSocialType());
        if (bindCount > 0) {
            return ServerResponseEntity.showFailMsg("该账号已被绑定，请换个账号试试~");
        }
        // 绑定uid
        authSocial.setUid(uid);
        authSocialService.saveOrUpdate(authSocial);

        return getTokenVo(authSocial);
    }

    @PostMapping("/mp")
    @ApiOperation(value = "公众号登录", notes = "通过公众号进行登录")
    public ServerResponseEntity<?> mp(@RequestBody String code) throws WxErrorException {
        Integer socialType = SocialType.MP.value();
        WxOAuth2AccessToken wxAccessToken = wxConfig.getWxMpService().getOAuth2Service().getAccessToken(code);

        AuthSocial authSocial = authSocialService.getByBizUserId(wxAccessToken.getOpenId(), socialType);
        if (authSocial != null && authSocial.getUid() != null) {
            return getTokenVo(authSocial);
        }
        String tempUid = IdUtil.simpleUUID();
        WxOAuth2UserInfo wxUserInfo = wxConfig.getWxMpService().getOAuth2Service().getUserInfo(wxAccessToken, null);
        if (authSocial == null) {
            authSocial = new AuthSocial();
        }
        authSocial.setSocialType(socialType);
        authSocial.setTempUid(tempUid);
        authSocial.setNickName(wxUserInfo.getNickname());
        authSocial.setImageUrl(wxUserInfo.getHeadImgUrl());
        authSocial.setBizUserId(wxUserInfo.getOpenid());
        authSocial.setBizUnionid(wxUserInfo.getUnionId());
        authSocialService.saveOrUpdate(authSocial);

        return ServerResponseEntity.fail(ResponseEnum.SOCIAL_ACCOUNT_NOT_BIND, tempUid);

    }

    @PostMapping("/ma")
    @ApiOperation(value = "小程序登录", notes = "通过小程序进行登录")
    public ServerResponseEntity<?> ma(@RequestBody String code) throws WxErrorException {
        Integer socialType = SocialType.MA.value();
        long startTime = System.currentTimeMillis();

        log.info("/ua/social/ma 小程序登录 code1【{}】", code);
        code = code.replace("\"", "");
        log.info("/ua/social/ma 小程序登录 code2【{}】", code);

        WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(code);

        log.info("小程序登录，获取用户session【{}】", session != null ? JSON.toJSONString(session) : "未获取到session信息");

        AuthSocial authSocial = authSocialService.getByBizUserId(session.getOpenid(), socialType);

        if (authSocial == null && StrUtil.isNotBlank(session.getUnionid())) {
            authSocial = authSocialService.getByBizUnionIdAndType(session.getUnionid(), socialType);
            log.info("根据unionid查询用户数据，耗时：{}ms ", System.currentTimeMillis() - startTime);
        }
        if (authSocial != null && authSocial.getUid() != null) {
            log.info("根据openId查询用户数据，耗时：{}ms ", System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
            // 根据openId 为获取到用户时 更新用户openId
            authSocial.setBizUserId(session.getOpenid());
            authSocialService.saveOrUpdate(authSocial);
            AuthAccount account = authAccountService.getByUid(authSocial.getUid());
            log.info("查询用户账号，耗时：{}ms,UserId:{}", System.currentTimeMillis() - startTime, account.getUserId());
            ServerResponseEntity<UserApiVO> responseEntity = userFeignClient.getInsiderUserData(account.getUserId());

            log.info("responseEntity:{}", JSONObject.toJSONString(responseEntity));
            if (responseEntity != null && responseEntity.isSuccess() && responseEntity.getData() != null) {
                if (!session.getOpenid().equals(responseEntity.getData().getOpenId())) {
//                if (!responseEntity.getData().getOpenId().equals(session.getOpenid())){
                    responseEntity.getData().setOpenId(session.getOpenid());
                    userFeignClient.updateUserOpenId(responseEntity.getData());
                    log.info("查询并更新用户信息，耗时：{}ms ", System.currentTimeMillis() - startTime);
                }
            }
            return getTokenVo(authSocial);
        }

        log.info("小程序登录，根据openId,unionId均未获取到 AuthSocial  openId【{}】  unionId【{}】", session.getOpenid(), session.getUnionid());

        String tempUid = IdUtil.simpleUUID();
        if (authSocial == null) {
            authSocial = new AuthSocial();
        }
        authSocial.setSocialType(socialType);
        authSocial.setTempUid(tempUid);
        authSocial.setBizTempSession(session.getSessionKey());
        authSocial.setBizUserId(session.getOpenid());
        authSocial.setBizUnionid(session.getUnionid());
        authSocialService.saveOrUpdate(authSocial);
        log.info("登录总耗时：{}ms  tempUid【{}】", System.currentTimeMillis() - startTime, tempUid);
        return ServerResponseEntity.fail(ResponseEnum.SOCIAL_ACCOUNT_NOT_BIND, tempUid);
    }

    @PostMapping("/ma/loginByPhone")
    @ApiOperation(value = "小程序手机号登录", notes = "小程序通过手机号码登录（内部测试使用）")
    public ServerResponseEntity<?> maLoginByPhone(@RequestBody String phone) throws WxErrorException {
        Integer socialType = SocialType.MA.value();
        long startTime = System.currentTimeMillis();
        ServerResponseEntity<UserApiVO> serverResponse = userFeignClient.getUserByMobile(phone);
        if (serverResponse == null || serverResponse.isFail()) {
            Assert.faild("查询会员信息失败。");
        }
        UserApiVO userApiVO = serverResponse.getData();
        if (userApiVO == null) {
            Assert.faild("会员信息不存在。");
        }

        AuthSocial authSocial = authSocialService.getByBizUserId(userApiVO.getOpenId(), socialType);
        if (authSocial == null) {
            Assert.faild("会员authSocial信息不存在。");
        }
        return getTokenVo(authSocial);
    }

    private ServerResponseEntity<TokenInfoVO> getTokenVo(AuthSocial authSocial) {
        long startTime = System.currentTimeMillis();
        AuthAccount authAccount = authAccountService.getByUid(authSocial.getUid());
        log.info("查询用户账号，耗时：{}ms,UserId:{}", System.currentTimeMillis() - startTime, authAccount.getUserId());
        startTime = System.currentTimeMillis();
        UserInfoInTokenBO data = mapperFacade.map(authAccount, UserInfoInTokenBO.class);
        data.setBizUid(authSocial.getBizUnionid());
        data.setBizUserId(authSocial.getBizUserId());
        data.setSessionKey(authSocial.getBizTempSession());
        data.setSocialType(authSocial.getSocialType());

        ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO = new ClearUserPermissionsCacheDTO();
        clearUserPermissionsCacheDTO.setSysType(data.getSysType());
        clearUserPermissionsCacheDTO.setUserId(data.getUserId());
        // 将以前的权限清理了,以免权限有缓存
        permissionFeignClient.clearUserPermissionsCache(clearUserPermissionsCacheDTO);
        log.info("清除用户权限缓存，耗时：{}ms,UserId:{}", System.currentTimeMillis() - startTime, authAccount.getUserId());

        // 保存token，返回token数据给前端
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(data));
    }
}
