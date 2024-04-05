package com.mall4j.cloud.user.controller.app;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.constant.SocialType;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.auth.feign.AuthSocialFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.api.auth.vo.TokenInfoVO;
import com.mall4j.cloud.api.biz.feign.NotifyFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerAddDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.platform.vo.TentacleVo;
import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.dto.UserPullNewDTO;
import com.mall4j.cloud.user.dto.UserRegisterDTO;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 用户信息
 * @author FrozenWatermelon
 */
@Slf4j
@RestController
@RequestMapping("/ua/user/register")
@Api(tags="app-用户注册接口")
public class UserRegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthSocialFeignClient authSocialFeignClient;
    @Autowired
    private AccountFeignClient accountFeignClient;
    @Autowired
    private NotifyFeignClient notifyFeignClient;
    @Autowired
    private CrmCustomerFeignClient crmCustomerFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OnsMQTemplate sendUserPullNewTemplate;

    /**
     * 注册的头像昵称有几个值得注意的地方：
     * 1. 如果是微信公众号 or 小程序注册，在注册之前，也就是在进入页面之前，需要调用SocialLoginController 这里面的尝试登录的接口，如果可以登录就直接登录了。
     * 2. 关于发送验证码
     *    1) 如果用邮箱进行注册就发送邮箱验证码
     *    2) 手机号注册,要发送验证码
     * 3. 当注册成功的时候，已经返回token，相对来说，已经登录了
     */
    @ApiOperation(value="注册")
    @PostMapping
    public ServerResponseEntity<TokenInfoVO> register(@RequestParam (value = "storeId",required = false) Long storeId, @Valid @RequestBody UserRegisterDTO param) {

        log.info("调用注册的接口，参数信息：storeId:{},UserRegisterDTO:{}",storeId, JSONObject.toJSONString(param));
        // 使用社交账号进行注册
        ServerResponseEntity<AuthSocialVO> authSocialInfo = supplySocialInfoIfNecessary(param);
        if (!authSocialInfo.isSuccess()) {
            return ServerResponseEntity.transform(authSocialInfo);
        }
        //增加逻辑判断当前门店是否存在并且是否为运营中，否则重置成为官店。
        if (!Objects.isNull(storeId)){
            StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
            if (byStoreId != null && byStoreId.getStatus() ==1) {
                param.setStoreId(storeId);
                param.setServiceStoreId(storeId);
            } else {
                param.setStoreId(Constant.MAIN_SHOP);
                param.setServiceStoreId(Constant.MAIN_SHOP);
            }
        }else{
            param.setStoreId(Constant.MAIN_SHOP);
            param.setServiceStoreId(Constant.MAIN_SHOP);
        }
        // 社交信息
        AuthSocialVO authSocial = authSocialInfo.getData();

        if (StrUtil.isBlank(param.getNickName())) {
            param.setNickName(param.getUserName());
        }

        if (StrUtil.isBlank(param.getTempUid())) {
            log.error("注册未带tempUid，注册失败。");
            return ServerResponseEntity.fail(ResponseEnum.NEED_LOGIN,null);
        }



        // 如果手机号不是通过微信小程序的手机号校验的，那就要获取验证码
        if (param.getVerifyType() == null) {
            return ServerResponseEntity.showFailMsg("请选择验证码验证方式");
        }
        // 邮箱校验
//        if (Objects.equals(param.getVerifyType(), VerifyType.EMAIL.value())) {
//
//        }
//        // 手机号校验
//        else if (Objects.equals(param.getVerifyType(), VerifyType.MOBILE.value())) {
//            ServerResponseEntity<Boolean> validCodeResponse = notifyFeignClient.checkValidCodeByFlag(param.getMobile(), param.getCheckRegisterSmsFlag());
//            if (!validCodeResponse.isSuccess() || !validCodeResponse.getData()) {
//                return ServerResponseEntity.fail(ResponseEnum.VERIFICATION_CODE_ERROR);
//            }
//        }
//        else {
//            return ServerResponseEntity.showFailMsg("请选择验证码验证方式");
//        }
        param.setUserName(param.getMobile());
        User user = mapperFacade.map(param, User.class);
        user.setPhone(param.getMobile());
        buildUser(user, param);
        if(authSocial!=null){
            user.setUnionId(authSocial.getBizUnionid());
            user.setOpenId(authSocial.getBizUserId());
        }
        if(StrUtil.isNotBlank(user.getName())){
            user.setCustomerName(user.getName());
        }
        //调用 crm接口进行校验

//        CustomerCheckResp customerCheckResp = checkCrmCustomer(param.getMobile());
//        if (Objects.equals(customerCheckResp.getIs_exists(),"N")){
//            //中台信息不存在 调用注册接口获取 vipCode
//            String vipCode = registerCrm(param,user);
//            log.info("注册获取vipCode{}",vipCode);
//            user.setVipcode(vipCode);
//        }else{
//            //中台信息存在 ， 增加vipCode 字段为注册参数
//            user.setVipcode(customerCheckResp.getVipcode());
//        }

        log.info("小程序用户注册，当前手机号码：{},unionId：{}",param.getMobile(),authSocial.getBizUnionid());
        // 1. 保存账户信息
        Map<String,Long> data = userService.save(param, authSocial,user);

        UserRegisterGiftDTO userRegisterGiftDTO = new UserRegisterGiftDTO();
        userRegisterGiftDTO.setStoreId(param.getStoreId());
        userRegisterGiftDTO.setUserId(param.getUserId());
//        userRegisterGiftTemplate.syncSend(RocketMqConstant.USER_REGISTER_GIFT, new GenericMessage<>(userRegisterGiftDTO));

        if(param.getBizType() != null && param.getBizType().equals(1)){
            UserPullNewDTO userPullNewDTO = new UserPullNewDTO();
            userPullNewDTO.setInviterUserId(param.getBizUserId());
            userPullNewDTO.setInviteeUserId(data.get("userId"));
            userPullNewDTO.setType(1);
            sendUserPullNewTemplate.syncSend(userPullNewDTO);
        }

        // 2. 登录

        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUid(data.get("uid"));
        userInfoInTokenBO.setUserId(data.get("userId"));
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);

        if (authSocial != null) {
            userInfoInTokenBO.setBizUserId(authSocial.getBizUserId());
            userInfoInTokenBO.setBizUid(authSocial.getBizUnionid());
            userInfoInTokenBO.setSocialType(authSocial.getSocialType());
            userInfoInTokenBO.setSessionKey(authSocial.getBizTempSession());
        }

        return accountFeignClient.storeTokenAndGetVo(userInfoInTokenBO);
    }

//    @ApiOperation(value="完善视频号创建的临时用户信息")
//    @PostMapping("/complete")
//    public ServerResponseEntity<Void> completeUserInfo(@RequestParam (value = "storeId",required = false) Long storeId,
//                                                              @Valid @RequestBody UserRegisterDTO param) {
//
//        log.info("完善视频号创建的临时用户信息，参数信息：storeId:{},UserRegisterDTO:{}",storeId, JSONObject.toJSONString(param));
//        Long userId = AuthUserContext.get().getUserId();
//        if (StrUtil.isBlank(param.getNickName())) {
//            param.setNickName(param.getUserName());
//        }
//
//        User user = userService.getById(userId);
//        param.setUserName(param.getMobile());
//        BeanUtil.copyProperties(param,user);
//
//        user.setPhone(param.getMobile());
//        if(StrUtil.isNotBlank(user.getName())){
//            user.setCustomerName(user.getName());
//        }
//        //调用 crm接口进行校验
//        CustomerCheckResp customerCheckResp = checkCrmCustomer(param.getMobile());
//        if (Objects.equals(customerCheckResp.getIs_exists(),"N")){
//            //中台信息不存在 调用注册接口获取 vipCode
//            String vipCode = registerCrm(param,user);
//            log.info("注册获取vipCode{}",vipCode);
//            user.setVipcode(vipCode);
//        }else{
//            //中台信息存在 ， 增加vipCode 字段为注册参数
//            user.setVipcode(customerCheckResp.getVipcode());
//        }
//
//        log.info("完善视频号创建的临时用户信息，当前手机号码：{},unionId：{}",param.getMobile());
//        // 1. 保存账户信息
//        userService.update(param, user);
//
//        return ServerResponseEntity.success();
//    }

    @ApiOperation(value="注册")
    @PostMapping("test")
    public ServerResponseEntity<TokenInfoVO> registerTest(@RequestParam (value = "storeId",required = false) Long storeId, @Valid @RequestBody UserRegisterDTO param) {

        // 使用社交账号进行注册
//        ServerResponseEntity<AuthSocialVO> authSocialInfo = supplySocialInfoIfNecessary(param);
//        if (!authSocialInfo.isSuccess()) {
//            return ServerResponseEntity.transform(authSocialInfo);
//        }
        if (!Objects.isNull(storeId)){
            param.setStoreId(storeId);
        }else{
            param.setStoreId(Constant.MAIN_SHOP);
        }
        // 社交信息
//        AuthSocialVO authSocial = authSocialInfo.getData();

        if (StrUtil.isBlank(param.getNickName())) {
            param.setNickName(param.getUserName());
        }

        // 如果手机号不是通过微信小程序的手机号校验的，那就要获取验证码
        if (param.getVerifyType() == null) {
            return ServerResponseEntity.showFailMsg("请选择验证码验证方式");
        }
        // 邮箱校验
//        if (Objects.equals(param.getVerifyType(), VerifyType.EMAIL.value())) {
//
//        }
//        // 手机号校验
//        else if (Objects.equals(param.getVerifyType(), VerifyType.MOBILE.value())) {
//            ServerResponseEntity<Boolean> validCodeResponse = notifyFeignClient.checkValidCodeByFlag(param.getMobile(), param.getCheckRegisterSmsFlag());
//            if (!validCodeResponse.isSuccess() || !validCodeResponse.getData()) {
//                return ServerResponseEntity.fail(ResponseEnum.VERIFICATION_CODE_ERROR);
//            }
//        }
//        else {
//            return ServerResponseEntity.showFailMsg("请选择验证码验证方式");
//        }

        //调用 crm接口进行校验

        CustomerCheckResp customerCheckResp = checkCrmCustomer(param.getMobile());
        if (Objects.equals(customerCheckResp.getIs_exists(),"N")){
            //中台信息不存在 调用注册接口获取 vipCode
            String vipCode = oldRegisterCrm(param);
            param.setVipCode(vipCode);
        }else{
            //中台信息存在 ， 增加vipCode 字段为注册参数
            param.setVipCode(customerCheckResp.getVipcode());
        }


        // 1. 保存账户信息
        param.setUserName(param.getMobile());
        Long uid = userService.saveTest(param);

        UserRegisterGiftDTO userRegisterGiftDTO = new UserRegisterGiftDTO();
        userRegisterGiftDTO.setStoreId(param.getStoreId());
        userRegisterGiftDTO.setUserId(param.getUserId());
//        userRegisterGiftTemplate.syncSend(RocketMqConstant.USER_REGISTER_GIFT, new GenericMessage<>(userRegisterGiftDTO));
        // 2. 登录

        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUid(uid);
        userInfoInTokenBO.setUserId(param.getUserId());
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);

//        if (authSocial != null) {
//            userInfoInTokenBO.setBizUserId(authSocial.getBizUserId());
//            userInfoInTokenBO.setBizUid(authSocial.getBizUnionid());
//            userInfoInTokenBO.setSocialType(authSocial.getSocialType());
//            userInfoInTokenBO.setSessionKey(authSocial.getBizTempSession());
//        }

        return accountFeignClient.storeTokenAndGetVo(userInfoInTokenBO);
    }

    private String registerCrm(UserRegisterDTO param, User user) {
        CustomerAddDto customerAddDto = new CustomerAddDto();
        customerAddDto.setMobilephone(param.getMobile());
        customerAddDto.setReg_time(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        if(StrUtil.isNotBlank(user.getUnionId())) customerAddDto.setUnion_id(user.getUnionId());
        if(StrUtil.isNotBlank(user.getOpenId())) customerAddDto.setOpenid(user.getOpenId());
        if(StrUtil.isNotBlank(user.getSex())) customerAddDto.setGender(user.getSex());
        if(StrUtil.isNotBlank(user.getBirthDate())) customerAddDto.setBirthday(user.getBirthDate());
        if(StrUtil.isNotBlank(user.getProvince())) customerAddDto.setProvince(user.getProvince());
        if(StrUtil.isNotBlank(user.getCity())) customerAddDto.setCity(user.getCity());
        if(StrUtil.isNotBlank(user.getArea())) customerAddDto.setArea(user.getArea());
        if(StrUtil.isNotBlank(user.getHaveBaby())) customerAddDto.setHave_baby(user.getHaveBaby());
        if(StrUtil.isNotBlank(user.getBabyBirthday())) customerAddDto.setBaby_birthday(user.getBabyBirthday());
        if(StrUtil.isNotBlank(user.getSecondBabySex())) customerAddDto.setSecond_baby_sex(user.getSecondBabySex());
        if(StrUtil.isNotBlank(user.getSecondBabyBirth())) customerAddDto.setSecond_baby_birth(user.getSecondBabyBirth());
        if(StrUtil.isNotBlank(user.getThirdBabyBirth())) customerAddDto.setThird_baby_birth(user.getThirdBabyBirth());
        if(StrUtil.isNotBlank(user.getThirdBabySex())) customerAddDto.setThird_baby_sex(user.getThirdBabySex());
        if (null != user.getStaffId()) {
            ServerResponseEntity<StaffVO> staffById = staffFeignClient.getStaffById(user.getStaffId());
            if (null != staffById && staffById.isSuccess() && staffById.getData() != null) {
                if (StrUtil.isNotBlank(staffById.getData().getStaffNo())) {
                    customerAddDto.setServUser(staffById.getData().getStaffNo());
                    customerAddDto.setRegSaler(staffById.getData().getStaffNo());
                }
                //如果服务门店为虚拟门店，服务门店传官店code
                if(staffById.getData().getStoreInviteType()==0){
                    customerAddDto.setServShop(staffById.getData().getStoreCode());
                }else{
                    customerAddDto.setServShop(staffById.getData().getMainStoreCode());
                }
            }
        } else {
//            ServerResponseEntity<String> storeCodeByStoreId = storeFeignClient.getStoreCodeByStoreId(user.getServiceStoreId());
//            if (storeCodeByStoreId.isSuccess()){
//                customerAddDto.setServShop(storeCodeByStoreId.getData());
//            }
            StoreVO serverShopStoreVO = storeFeignClient.findByStoreId(user.getServiceStoreId());
            if (serverShopStoreVO!=null){
                //判断会员的服务门店是否为虚拟门店，如果为虚拟门店，传官店code
                if(serverShopStoreVO.getStoreInviteType()==0){
                    customerAddDto.setServShop(serverShopStoreVO.getStoreCode());
                }else{
                    customerAddDto.setServShop(serverShopStoreVO.getMainStoreCode());
                }
            }

        }
//        if(user.getCardStaffId() != null)customerAddDto.setRegSaler(user.getCardStaffId().toString());
//        ServerResponseEntity<String> storeCodeByStoreId = storeFeignClient.getStoreCodeByStoreId(param.getStoreId());
        StoreVO storeVO = storeFeignClient.findByStoreId(param.getStoreId());
        if (storeVO!=null){
            //判断会员的注册门店是否为虚拟门店，如果为虚拟门店，传官店code
            if(storeVO.getStoreInviteType()==0){
                customerAddDto.setReg_store_id(storeVO.getStoreCode());
            }else{
                customerAddDto.setReg_store_id(storeVO.getMainStoreCode());
                customerAddDto.setRegShopNickName(storeVO.getName());
                customerAddDto.setVirtualShopCode(storeVO.getStoreCode());
            }
        }

        if(StrUtil.isNotBlank(user.getCustomerName())) {
            customerAddDto.setCustomer_name(user.getCustomerName());
        }else if (StrUtil.isNotBlank(user.getNickName())) {
            customerAddDto.setCustomer_name(user.getNickName());
        }
        log.info("注册推送数据:{}",customerAddDto.toString());
        ServerResponseEntity<CustomerAddResp> customerAddRespServerResponseEntity = crmCustomerFeignClient.customerAdd(customerAddDto);
        if (customerAddRespServerResponseEntity.isFail()){
            throw new LuckException(customerAddRespServerResponseEntity.getMsg());
        }
        return customerAddRespServerResponseEntity.getData().getVipcode();

    }

    private String oldRegisterCrm(UserRegisterDTO param) {
        CustomerAddDto customerAddDto = new CustomerAddDto();
        customerAddDto.setMobilephone(param.getMobile());
        customerAddDto.setReg_time(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        ServerResponseEntity<String> storeCodeByStoreId = storeFeignClient.getStoreCodeByStoreId(param.getStoreId());
        if (storeCodeByStoreId.isSuccess()){
            customerAddDto.setReg_store_id(storeCodeByStoreId.getData());
        }

        customerAddDto.setCustomer_name(param.getMobile());
        ServerResponseEntity<CustomerAddResp> customerAddRespServerResponseEntity = crmCustomerFeignClient.customerAdd(customerAddDto);
        if (customerAddRespServerResponseEntity.isFail()){
            throw new LuckException(customerAddRespServerResponseEntity.getMsg());
        }
        return customerAddRespServerResponseEntity.getData().getVipcode();

    }

    private CustomerCheckResp checkCrmCustomer(String mobile) {
        CustomerCheckDto inputParam = new CustomerCheckDto();
        inputParam.setMobilephone(mobile);
        ServerResponseEntity<CustomerCheckResp> customerCheckRespServerResponseEntity = crmCustomerFeignClient.customerCheck(inputParam);
        if (!customerCheckRespServerResponseEntity.isSuccess()){
            throw  new LuckException(customerCheckRespServerResponseEntity.getMsg());
        }
        return customerCheckRespServerResponseEntity.getData();
    }

    /**
     * 补充社交登录信息，如果有必要的话
     */
    private ServerResponseEntity<AuthSocialVO> supplySocialInfoIfNecessary(UserRegisterDTO param) {
        AuthSocialVO authSocial = null;
        if (StrUtil.isNotBlank(param.getTempUid())) {
            ServerResponseEntity<AuthSocialVO> authSocialResponse = authSocialFeignClient.getByTempUid(param.getTempUid());
            if (!authSocialResponse.isSuccess()) {
                return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
            }
            authSocial = authSocialResponse.getData();

            // authSocial.getUid() != null 这个是说明已经绑定好用户了，不知道为什么没判断出来
            if (authSocial == null || authSocial.getUid() != null) {
                log.info("使用社交账号进行注册:使用tempId【{}】未获取到authSocial信息",param.getTempUid());
                return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
            }

            // 公众号注册
            if (Objects.equals(authSocial.getSocialType(), SocialType.MP.value())) {
                param.setNickName(authSocial.getNickName());
                param.setImg(authSocial.getImageUrl());
            }
        }

        return ServerResponseEntity.success(authSocial);
    }

    private void buildUser(User user, UserRegisterDTO param) {

        user.setName(param.getName());
        user.setPic(param.getImg());
        user.setNickName(param.getNickName());
        user.setLevel(Constant.USER_LEVEL_INIT);
        user.setLevelType(LevelTypeEnum.ORDINARY_USER.value());
        user.setStatus(1);

        user.setStoreId(param.getStoreId());
        user.setServiceStoreId(param.getStoreId());
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffByMobile(user.getPhone());
        log.info("查询导购参数：{}结果:{}",user.getPhone(),JSONObject.toJSONString(staffResp));
        if (staffResp.isSuccess() && Objects.nonNull(staffResp.getData())
                && staffResp.getData().getStatus() == 0) {
            // 当前会员如果是导购 所属导购为自己
            user.setStaffId(staffResp.getData().getId());
            user.setServiceStoreId(staffResp.getData().getStoreId());
        } else {
            if (StrUtil.isNotBlank(param.getTentacleNo())) {
                //查询触点携带的门店id及staffId
                ServerResponseEntity<TentacleContentVO> byTentacleNo = tentacleContentFeignClient.findByTentacleNo(param.getTentacleNo());
                if (byTentacleNo.isSuccess()) {
                    TentacleVo tentacle = byTentacleNo.getData().getTentacle();
                    if (tentacle.getTentacleType() == 4) {
                        user.setStaffId(tentacle.getBusinessId());
                        ServerResponseEntity<StaffVO> staffById = staffFeignClient.getStaffById(tentacle.getBusinessId());
                        if (staffById.isSuccess() && Objects.nonNull(staffById.getData())
                            && staffById.getData().getStatus() == 0) {
                            user.setServiceStoreId(staffById.getData().getStoreId());
                        }
                    }
                }
            }
        }
        //其余拓展字段
        user.setVipcode(param.getVipCode());
        user.setCardStaffId(user.getStaffId());
    }

}
