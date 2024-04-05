package com.mall4j.cloud.auth.feign;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.mall4j.cloud.api.auth.constant.SocialType;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.dto.AuthSocialDTO;
import com.mall4j.cloud.api.auth.feign.AuthSocialFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.auth.config.WxConfig;
import com.mall4j.cloud.auth.mapper.AuthAccountMapper;
import com.mall4j.cloud.auth.mapper.AuthSocialMapper;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.auth.model.AuthSocial;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
@RestController
public class AuthSocialController implements AuthSocialFeignClient {

    @Autowired
    private AuthSocialMapper authSocialMapper;

    @Autowired
    AuthAccountMapper authAccountMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private WxConfig wxConfig;


    @Override
    public ServerResponseEntity<AuthSocialVO> getByTempUid(String tempUid) {
        AuthSocial authSocial = authSocialMapper.getByTempUid(tempUid);
        return ServerResponseEntity.success(mapperFacade.map(authSocial, AuthSocialVO.class));
    }

    @Override
    public ServerResponseEntity<AuthSocialVO> getByUnionId(String unionId) {
        AuthSocial authSocial = authSocialMapper.getByBizUnionIdAndType(unionId, SocialType.MA.value());
        return ServerResponseEntity.success(mapperFacade.map(authSocial, AuthSocialVO.class));
    }

    @Override
    public ServerResponseEntity<AuthSocialVO> getByOpenId(String openId) {
        AuthSocial authSocial = authSocialMapper.getByBizUserIdAndType(openId, SocialType.MA.value());
        return ServerResponseEntity.success(mapperFacade.map(authSocial, AuthSocialVO.class));
    }

    @Override
    public ServerResponseEntity<String> getMobileByMaInfo(String sessionKey, String encryptedData, String ivStr) {
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxConfig.getWxMaService().getUserService().getPhoneNoInfo(
                sessionKey, encryptedData, ivStr);
        return ServerResponseEntity.success(wxMaPhoneNumberInfo.getPhoneNumber());
    }

    @Override
    public ServerResponseEntity<AuthSocialVO> getById(Long id) {
        AuthSocial authSocial = authSocialMapper.getById(id);
        return ServerResponseEntity.success(mapperFacade.map(authSocial, AuthSocialVO.class));
    }

    @Override
    public ServerResponseEntity<Void> save(AuthSocialDTO authSocialDTO) {
        AuthSocial authSocial = mapperFacade.map(authSocialDTO, AuthSocial.class);
        authSocialMapper.save(authSocial);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> bingUidById(Long uid, Long id) {
        authSocialMapper.bindUidById(uid,id);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateUnionId(Long userId, String unionId) {
        AuthAccount authAccount = authAccountMapper.getByUserIdAndType(userId, SysTypeEnum.ORDINARY.value());
        if(authAccount==null){
            Assert.faild("更新用户unionId失败，查询authAccount记录不存在。");
        }
        authSocialMapper.updateUnionIdByUid(authAccount.getUid(),unionId);
        return ServerResponseEntity.success();
    }
}
