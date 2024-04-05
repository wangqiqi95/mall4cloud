package com.mall4j.cloud.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.constant.SocialType;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.vo.TokenInfoVO;
import com.mall4j.cloud.api.biz.feign.WxCpAuth2FeignClient;
import com.mall4j.cloud.api.biz.vo.WxCpUserInfoVO;
import com.mall4j.cloud.api.platform.dto.StaffBindQiWeiDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.auth.dto.LoginByQiWei2Request;
import com.mall4j.cloud.auth.manager.TokenStore;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RequestMapping("/ua/staff")
@RestController
@Api(tags = "导购登录")
public class StaffController {

    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private WxCpAuth2FeignClient wxCpAuth2FeignClient;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;

    @PostMapping("/loginByQiWei")
    @ApiOperation(value = "企微授权登录", notes = "通过企微授权登录")
    public ServerResponseEntity<?> loginByQiWei(@RequestParam String code) {
        ServerResponseEntity<WxCpUserInfoVO> wxCpUserInfoVO = wxCpAuth2FeignClient.getUserInfo(code);
        log.info("loginByQiWei wxCpUserInfoVO 1: {}", wxCpUserInfoVO);
        if (!wxCpUserInfoVO.isSuccess()) {
            Assert.faild("未在企业通讯录内，拒绝访问。");
        }
        log.info("loginByQiWei wxCpUserInfoVO 2: {}", JSONObject.toJSONString(wxCpUserInfoVO.getData()));
        ServerResponseEntity<StaffVO> staffVOResp = staffFeignClient.getStaffByQiWeiUserId(wxCpUserInfoVO.getData().getUserid());
        if (!staffVOResp.isSuccess()) {
            return ServerResponseEntity.fail(ResponseEnum.QI_WEI_ACCOUNT_NOT_BIND_STAFF);
        }
        StaffVO staffVO = staffVOResp.getData();
        if (Objects.isNull(staffVO)) {
            return ServerResponseEntity.fail(ResponseEnum.QI_WEI_ACCOUNT_NOT_BIND_STAFF,wxCpUserInfoVO);
//            log.info("loginByQiWei---> 企微授权登录-->根据手机号未获取到员工：【{}】",wxCpUserInfoVO.getData().getMobile());
//            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
//            return ServerResponseEntity.fail(ResponseEnum.SOCIAL_ACCOUNT_NOT_BIND);
        }
        if (staffVO.getStatus() == 1) {
            throw new LuckException("员工已离职");
        }

        StoreVO storeVO = storeFeignClient.findByStoreId(staffVO.getStoreId());
        if(storeVO==null || storeVO.getStatus()!=1){
            Assert.faild("导购所在门店已闭店或者不存在，不允许登录导购端。");
        }


        Integer qwStatus = wxCpUserInfoVO.getData().getStatus();
        String qwUserId = wxCpUserInfoVO.getData().getUserid();
        if (StringUtils.isEmpty(staffVO.getQiWeiUserId())
                || staffVO.getQiWeiUserStatus() == null
                || (staffVO.getQiWeiUserStatus() != null && qwStatus != staffVO.getQiWeiUserStatus())){
            // 更新员工企微信息
            StaffBindQiWeiDTO staffBindQiWeiDTO = new StaffBindQiWeiDTO();
            staffBindQiWeiDTO.setStaffId(staffVO.getId());
            staffBindQiWeiDTO.setQiWeiUserStatus(qwStatus);
            staffBindQiWeiDTO.setQiWeiUserId(qwUserId);
            staffFeignClient.bindStaffQiWeiUserId(staffBindQiWeiDTO);
        }
        // 企微好友关系绑定员工id
        userStaffCpRelationFeignClient.bindStaffId(qwUserId, staffVO.getId());
        return getTokenVo(staffVO);
    }

    @PostMapping("/loginByQiWei2")
    @ApiOperation(value = "企微授权登录- 获取手机号", notes = "通过企微授权登录-获取手机号")
    public ServerResponseEntity<?> loginByQiWei2(@RequestBody LoginByQiWei2Request request) {
        log.info("loginByQiWei2 request : {}", request);
        ServerResponseEntity<WxCpUserInfoVO> wxCpUserInfoVO = wxCpAuth2FeignClient.getUserInfo2(request.getSessionKey(),request.getEncryptedData(),request.getIvStr());
        log.info("loginByQiWei2 wxCpUserInfoVO 1: {}", wxCpUserInfoVO);
        if (!wxCpUserInfoVO.isSuccess()) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        log.info("loginByQiWei2 wxCpUserInfoVO 2: {}", JSONObject.toJSONString(wxCpUserInfoVO.getData()));
        ServerResponseEntity<StaffVO> staffVOResp = staffFeignClient.getStaffByMobile(wxCpUserInfoVO.getData().getMobile());
        if (!staffVOResp.isSuccess()) {
            return ServerResponseEntity.fail(ResponseEnum.QI_WEI_ACCOUNT_NOT_BIND_STAFF);
        }
        StaffVO staffVO = staffVOResp.getData();
        if (Objects.isNull(staffVO)) {
            log.info("loginByQiWei---> 企微授权登录-->根据手机号未获取到员工：【{}】",wxCpUserInfoVO.getData().getMobile());
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
//            return ServerResponseEntity.fail(ResponseEnum.SOCIAL_ACCOUNT_NOT_BIND);
        }
        if (staffVO.getStatus() == 1) {
            throw new LuckException("员工已离职");
        }

        StoreVO storeVO = storeFeignClient.findByStoreId(staffVO.getStoreId());
        if(storeVO==null || storeVO.getStatus()!=1){
            Assert.faild("导购所在门店已闭店或者不存在，不允许登录导购端。");
        }

        Integer qwStatus = request.getQiweiStatus();
        String qwUserId = request.getUserid();
        if (StringUtils.isEmpty(staffVO.getQiWeiUserId())
                || staffVO.getQiWeiUserStatus() == null
                || (staffVO.getQiWeiUserStatus() != null && qwStatus != staffVO.getQiWeiUserStatus())){
            // 更新员工企微信息
            StaffBindQiWeiDTO staffBindQiWeiDTO = new StaffBindQiWeiDTO();
            staffBindQiWeiDTO.setStaffId(staffVO.getId());
            staffBindQiWeiDTO.setQiWeiUserStatus(qwStatus);
            staffBindQiWeiDTO.setQiWeiUserId(qwUserId);
            staffFeignClient.bindStaffQiWeiUserId(staffBindQiWeiDTO);
            //todo 首次登录，为当前员工创建员工活码
//            List<StaffCodeRefPDTO> refPDTOS=new ArrayList<>();
//
//            refPDTOS.add(new StaffCodeRefPDTO(staffVO.getId(),staffVO.getStoreId(),staffVO.getStaffName(),staffVO.getStaffNo(),staffVO.getMobile()));
//            staffCodeFeignClient.syncStaffCodeSUP(new StaffCodeCreateDTO(0L,"定时任务同步员工",1,refPDTOS));
        }
        // 企微好友关系绑定员工id
        userStaffCpRelationFeignClient.bindStaffId(qwUserId, staffVO.getId());
        return getTokenVo(staffVO);
    }

    @PostMapping("/loginByMobile")
    @ApiOperation(value = "手机号登录", notes = "通过手机号登录(内部测试使用)")
    public ServerResponseEntity<?> loginByMobile(@RequestParam String mobile) {
        ServerResponseEntity<StaffVO> staffVOResp = staffFeignClient.getStaffByMobile(mobile);
        if (!staffVOResp.isSuccess()) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        StaffVO staffVO = staffVOResp.getData();
        if (Objects.isNull(staffVO)) {
            log.info("loginByMobile---> 手机号登录-->根据手机号未获取到员工：【{}】",mobile);
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
//            return ServerResponseEntity.fail(ResponseEnum.SOCIAL_ACCOUNT_NOT_BIND);
        }
        if (staffVO.getStatus() == 1) {
            throw new LuckException("员工已离职");
        }
        return getTokenVo(staffVO);
    }

    private ServerResponseEntity<TokenInfoVO> getTokenVo(StaffVO staffVO) {
        UserInfoInTokenBO data = new UserInfoInTokenBO();
        data.setSysType(SysTypeEnum.STAFF.value());
        data.setSocialType(SocialType.QW.value());
        data.setUid(staffVO.getId());
        data.setUserId(staffVO.getId());
        data.setStoreId(staffVO.getStoreId());
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(data));
    }

}
