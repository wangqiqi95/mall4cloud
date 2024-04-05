package com.mall4j.cloud.user.controller.app;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.feign.NotifyFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerAddDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerPointFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.user.dto.PerfectDataDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.CheckSmsDTO;
import com.mall4j.cloud.user.dto.UserDTO;
import com.mall4j.cloud.user.dto.UserRegisterDTO;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserDynamicCodeVO;
import com.mall4j.cloud.user.vo.UserInfoVO;
import com.mall4j.cloud.user.vo.UserSimpleInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户地址
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:50:02
 */

@Slf4j
@RestController("appUserController")
@RequestMapping("/user")
@Api(tags = "app-用户信息")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserExtensionService userExtensionService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TCouponFeignClient tCouponFeignClient;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private NotifyFeignClient notifyFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private CrmCustomerFeignClient crmCustomerFeignClient;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private CrmCustomerPointFeignClient crmCustomerPointFeignClient;

    @Resource
    private OnsMQTemplate userPerfectDataTemplate;

    @Autowired
    private FeignShopConfig feignShopConfig;


    public static final String CHECK_UPDATE_PWD_SMS_FLAG = "updatePwdSmsFlag";

    @GetMapping("/simple_info")
    @ApiOperation(value = "用户头像昵称", notes = "用户头像昵称")
    public ServerResponseEntity<UserSimpleInfoVO> getByAddrId() {
        Long userId = AuthUserContext.get().getUserId();

        UserApiVO userApiVO = userService.getByUserId(userId);
        UserSimpleInfoVO userSimpleInfoVO = new UserSimpleInfoVO();
        userSimpleInfoVO.setNickName(userApiVO.getNickName());
        userSimpleInfoVO.setPic(userApiVO.getPic());

        return ServerResponseEntity.success(userSimpleInfoVO);
    }

    @GetMapping("/user_info")
    @ApiOperation(value = "获取用户信息", notes = "返回用户的信息")
    public ServerResponseEntity<UserInfoVO> getUserInfo() {
        Long userId = AuthUserContext.get().getUserId();
//        UserExtension userExtension = userExtensionService.getByUserId(userId);
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setScore(0l);
//        userInfo.setActualBalance(userExtension.getActualBalance());
//        userInfo.setBalance(userExtension.getBalance());
        ServerResponseEntity<Integer> countCanUseCouponResponse = tCouponFeignClient.countCanUseCoupon(userId);
        userInfo.setCouponNum(countCanUseCouponResponse.getData());
        Integer collections = spuFeignClient.countByUserId(userId).getData();
        userInfo.setCollections(collections == null ? 0 : collections);
        // todo 计算未读消息数量
//        Integer num = notifyFeignClient.getUnreadMsg(userId).getData();
//        userInfo.setNotifyNum(num == null ? 0 : num);
        UserApiVO userApiVO = userService.getByUserId(userId);
        if (!Objects.isNull(userApiVO)) {
            userInfo.setStaffId(userApiVO.getStaffId());
            userInfo.setStaffName(userApiVO.getStaffName());
            userInfo.setStaffStoreId(userApiVO.getStaffStoreId());
            userInfo.setStaffStoreName(userApiVO.getStaffStoreName());
            userInfo.setStaffNo(userApiVO.getStaffNo());
            userInfo.setStaffStatus(userApiVO.getStaffStatus());
//            if (!StrUtil.isEmpty(userApiVO.getVipcode())) {
//                CustomerGetDto customerGetDto = new CustomerGetDto();
//                customerGetDto.setVipcode(userApiVO.getVipcode());
//                ServerResponseEntity<CustomerGetVo> customerGetVoResp = crmCustomerFeignClient.customerGet(customerGetDto);
//                ServerResponseEntity<ScoreExpiredGetVo> scoreExpiredGetVoResp = crmCustomerFeignClient.scoreExpiredGet(customerGetDto);
//                if (customerGetVoResp.isSuccess() && Objects.nonNull(customerGetVoResp.getData())) {
//
//                    userInfo.setScore(customerGetVoResp.getData().getCurrent_valid_point());
//                    // 221027新增查询会员年度过期积分
//                    if(scoreExpiredGetVoResp.isSuccess() && Objects.nonNull(scoreExpiredGetVoResp.getData())){
//                        userInfo.setPointValue(scoreExpiredGetVoResp.getData().getPointValue());
//                    }
//
//                    //会员升级提示语：再消费 “X” 元可以升级为 SKECHERS"下一个等级名称"
//                    if(customerGetVoResp.getData().getNext_pay()!=null && StrUtil.isNotBlank(customerGetVoResp.getData().getCurrent_grade_id())){
//                        if (!customerGetVoResp.getData().getCurrent_grade_id().equals(CurrentGradeIdEnum.GRADE_ZQ.getValue())) {
//                            String upgradeHintConfig=feignShopConfig.getUpGradeHint();
//                            PlaceholderResolver defaultResolver = PlaceholderResolver.getDefaultResolver();
//                            String current_grade_id=customerGetVoResp.getData().getCurrent_grade_id();
//                            int gradeId=Integer.parseInt(current_grade_id)+1>4?4:Integer.parseInt(current_grade_id)+1;
//                            String upgradeHint = defaultResolver.resolve(upgradeHintConfig,
//                                    customerGetVoResp.getData().getNext_pay().toString(),
//                                    CurrentGradeIdEnum.getDesc(String.valueOf(gradeId)));
//                            userInfo.setUpgradeHint(upgradeHint);
//                        }
//                        UserLevelVO ordinaryLevel = userLevelService.getOneByTypeAndLevel(
//                                LevelTypeEnum.ORDINARY_USER.value(), Integer.parseInt(customerGetVoResp.getData().getCurrent_grade_id()));
//                        userInfo.setCurrent_grade_id(customerGetVoResp.getData().getCurrent_grade_id());
//                        userInfo.setLevelName(ordinaryLevel.getLevelName());
//                        userInfo.setNext_pay(customerGetVoResp.getData().getNext_pay());
//                    }
//                }
//            }
        }
        return ServerResponseEntity.success(userInfo);
    }

    @GetMapping("/ma/user_detail_info")
    @ApiOperation(value = "获取用户详细信息", notes = "返回用户详细信息")
    public ServerResponseEntity<UserApiVO> getUserDetailInfo() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
        Long userId = userInfoInTokenBO.getUserId();
        UserApiVO userApiVO = userService.getByUserId(userId);
        return ServerResponseEntity.success(userApiVO);
    }

    @PutMapping("/set_user_info")
    @ApiOperation(value = "设置用户信息", notes = "设置用户信息")
    public ServerResponseEntity<Void> setUserInfo(@RequestBody UserDTO userDTO) {
        Long userId = AuthUserContext.get().getUserId();
        Long storeId = AuthUserContext.get().getStoreId();
        userDTO.setUserId(userId);
        User user = mapperFacade.map(userDTO, User.class);
        userService.updateUser(user);

        //同步更新crm
//        userService.updateUserToCrm(user);

        PerfectDataDTO perfectDataDTO = new PerfectDataDTO();
        perfectDataDTO.setUserId(userId);
        perfectDataDTO.setShopId(storeId);
        userPerfectDataTemplate.syncSend(perfectDataDTO);

        return ServerResponseEntity.success();
    }

    @PutMapping("/bind_wx_user_info")
    @ApiOperation(value = "绑定微信头像及昵称", notes = "绑定微信头像及昵称")
    public ServerResponseEntity<Void> bindWxUserInfo(@RequestBody UserDTO userDTO) {
        Long userId = AuthUserContext.get().getUserId();
        userService.lambdaUpdate().set(StrUtil.isNotBlank(userDTO.getNickName()), User::getNickName, userDTO.getNickName())
                .set(StrUtil.isNotBlank(userDTO.getPic()), User::getPic, userDTO.getPic())
                .eq(User::getUserId, userId)
                .update();
        userService.removeUserCacheByUserId(userId);
        return ServerResponseEntity.success();
    }


    @PutMapping("/update_mobile")
    @ApiOperation(value = "修改用户手机号", notes = "修改用户手机号")
    public ServerResponseEntity<String> checkPhoneIsBind(@RequestBody CheckSmsDTO checkSmsDTO) {
        //修改手机号码前后不能相同
        Long userId = AuthUserContext.get().getUserId();
        UserApiVO user = userService.getByUserId(userId);
        if (user.getUserMobile().equals(checkSmsDTO.getMobile())) {
            throw new LuckException("修改手机号码不能与原先相同");
        }
        String checkRegisterSmsFlag = CHECK_UPDATE_PWD_SMS_FLAG + checkSmsDTO.getCheckUpdatePhoneSmsFlag();
        // 看看有没有校验验证码成功的标识
        if (StrUtil.isBlank(checkSmsDTO.getCheckUpdatePhoneSmsFlag())) {
            // 验证码已过期，请重新发送验证码校验
            throw new LuckException("验证码已过期，请重新发送验证码校验");
        } else {
            String checkRegisterSmsFlagMobile = RedisUtil.get(checkRegisterSmsFlag);
            if (!Objects.equals(checkRegisterSmsFlagMobile, checkSmsDTO.getMobile())) {
                // 验证码已过期，请重新发送验证码校验
                throw new LuckException("验证码已过期，请重新发送验证码校验");
            }
        }
        // 修改用户的手机号
        userService.updateUserMobile(AuthUserContext.get().getUserId(), checkSmsDTO.getMobile());
        return ServerResponseEntity.success();
    }

//    @GetMapping("/storeId")
//    @ApiOperation(value = "用户默认访问门店", notes = "返回门店的信息")
//    public ServerResponseEntity<StoreVO> getUserStoreId(@RequestParam(value = "storeId", required = false) Long storeId,
//                                                        @RequestParam(value = "tentacleNo", required = false) String tentacleNo) {
//        Long userId = AuthUserContext.get().getUserId();
//        if (Objects.isNull(storeId)) {
//            UserApiVO byUserId = userService.getByUserId(userId);
//            log.info("getUserStoreId ：当前访问userid:{},UserApiVO:{} ",userId, JSONObject.toJSONString(byUserId));
//            if (Objects.nonNull(byUserId)) {
//                if (Objects.nonNull(byUserId.getStaffId()) && byUserId.getStaffId() != 0) {
//                    //查询导购所属门店作为员工默认访问门店
//                    ServerResponseEntity<StaffVO> staffById = staffFeignClient.getStaffById(byUserId.getStaffId());
//                    log.info("getUserStoreId  查询员工门店参数:{} 返回结果:{} ",byUserId.getStaffId(), JSONObject.toJSONString(staffById));
//                    if (staffById.isSuccess() && staffById.getData()!=null) {
//                        storeId = staffById.getData().getStoreId();
//                    }else{
//                        storeId = byUserId.getStoreId();
//                    }
//                } else {
//                    storeId = byUserId.getStoreId();
//                }
//            }
//        }
//        StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
//        if (byStoreId.getStatus() != 1) {
//            byStoreId = storeFeignClient.findByStoreId(Constant.MAIN_SHOP);
//        }
//        return ServerResponseEntity.success(byStoreId);
//    }

    // 因为会员的serviceStoreId没有更新完，所以这里先注释掉serviceStoreId的逻辑，保持原来的逻辑取进什么门店。
    @GetMapping("/storeId")
    @ApiOperation(value = "用户默认访问门店", notes = "返回门店的信息")
    public ServerResponseEntity<StoreVO> getUserStoreId(@RequestParam(value = "storeId", required = false) Long storeId,
                                                        @RequestParam(value = "tentacleNo", required = false) String tentacleNo) {
        Long userId = AuthUserContext.get().getUserId();
        if (Objects.isNull(storeId)) {
            UserApiVO byUserId = userService.getByUserId(userId);
            log.info("getUserStoreId ：当前访问userid:{},UserApiVO:{} ",userId, JSONObject.toJSONString(byUserId));
            if (Objects.nonNull(byUserId)) {
                if (Objects.nonNull(byUserId.getStaffId()) && byUserId.getStaffId() != 0) {
                    //查询导购所属门店作为员工默认访问门店
                    ServerResponseEntity<StaffVO> staffById = staffFeignClient.getStaffById(byUserId.getStaffId());
                    log.info("getUserStoreId  查询员工门店参数:{} 返回结果:{} ",byUserId.getStaffId(), JSONObject.toJSONString(staffById));
                    if (staffById.isSuccess() && staffById.getData()!=null) {
                        storeId = staffById.getData().getStoreId();
                    }else{
                        storeId = byUserId.getStoreId();
                    }
                } else {
                    storeId = byUserId.getServiceStoreId();
                }
            }
        }
        StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
        log.info("byStoreId：{}",JSONObject.toJSONString(byStoreId));
        if (byStoreId ==null || byStoreId.getStatus()==null || byStoreId.getStatus() != 1) {
            byStoreId = storeFeignClient.findByStoreId(Constant.MAIN_SHOP);
        }
        return ServerResponseEntity.success(byStoreId);
    }

    @GetMapping("/ua/storeId")
    @ApiOperation(value = "免登陆用户默认访问门店", notes = "返回门店的信息")
    public ServerResponseEntity<StoreVO> getUaStoreId(@RequestParam(value = "tentacleNo", required = false) String tentacleNo,
                                                      @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) {
        StoreVO storeVO = storeFeignClient.findByStoreId(Constant.MAIN_SHOP);

        if (storeId != Constant.MAIN_SHOP) {
            StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
            if (byStoreId != null && byStoreId.getStatus() == 1) {
                storeVO = byStoreId;
            }
        }
        return ServerResponseEntity.success(storeVO);
    }

    @GetMapping("/get_score_detail")
    @ApiOperation(value = "获取积分明细")
    public ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> getUserInfo(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
//        return userService.getScoreDetail(pageNo,pageSize,AuthUserContext.get().getUserId());
        return ServerResponseEntity.success();
    }

    @PutMapping("/living_room_id")
    @ApiOperation(value = "设置用户最近一次浏览的直播间id", notes = "设置用户最近一次浏览的直播间id")
    public ServerResponseEntity<String> setUserLivingRoomId(@RequestParam(value = "livingRoomId", required = true) String livingRoomId,
                                                            @RequestParam(value = "openId", required = true) String openId) {
        log.info("记录访问直播间，openid，{}。访问直播间id,{}。",openId,livingRoomId);
        userService.setBorrowLivingRoomId(openId,livingRoomId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/checkUser")
    @ApiOperation(value = "检测用户信息", notes = "检测用户信息, 异常为TRUE,FALSE")
    public ServerResponseEntity<Boolean> checkUser(){
        Long userId = AuthUserContext.get().getUserId();
        User user = userService.getById(userId);
        if(StrUtil.isNotBlank(user.getPhone())){
            return ServerResponseEntity.success(Boolean.FALSE);
        }

        return ServerResponseEntity.success(Boolean.TRUE);
    }


    @ApiOperation(value="完善视频号创建的临时用户信息")
    @PostMapping("/complete")
    public ServerResponseEntity<Void> completeUserInfo(@RequestParam (value = "storeId",required = false) Long storeId,
                                                       @Valid @RequestBody UserRegisterDTO param) {

        log.info("完善视频号创建的临时用户信息，参数信息：storeId:{},UserRegisterDTO:{}",storeId, JSONObject.toJSONString(param));
        Long userId = AuthUserContext.get().getUserId();
        if (StrUtil.isBlank(param.getNickName())) {
            param.setNickName(param.getUserName());
        }

        User user = userService.getById(userId);
        param.setUserName(param.getMobile());
        param.setStoreId(Constant.MAIN_SHOP);
        param.setServiceStoreId(Constant.MAIN_SHOP);
        BeanUtil.copyProperties(param,user);
        user.setUserId(userId);

        user.setPhone(param.getMobile());
        if(StrUtil.isNotBlank(user.getName())){
            user.setCustomerName(user.getName());
        }
        //调用 crm接口进行校验
        CustomerCheckResp customerCheckResp = checkCrmCustomer(param.getMobile());
        if (Objects.equals(customerCheckResp.getIs_exists(),"N")){
            //中台信息不存在 调用注册接口获取 vipCode
            String vipCode = registerCrm(param,user);
            log.info("注册获取vipCode{}",vipCode);
            user.setVipcode(vipCode);
        }else{
            //中台信息存在 ， 增加vipCode 字段为注册参数
            user.setVipcode(customerCheckResp.getVipcode());
        }

        log.info("完善视频号创建的临时用户信息，user：{}",user);
        // 1. 保存账户信息
        userService.update(param, user);
        //清空user缓存
        userService.removeUserCacheByUserId(userId);

        return ServerResponseEntity.success();
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
        if (null != user.getStaffId() && user.getStaffId()>0 ) {
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

    @ApiOperation("获取动态会员码")
    @GetMapping("/dynamicCode")
    public ServerResponseEntity<UserDynamicCodeVO> getDynamicCode() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
       return userService.getDynamicCode(userInfoInTokenBO.getUserId());
    }

    @ApiOperation("rec开关")
    @GetMapping("/isRecEnabled")
    public ServerResponseEntity<Boolean> isRecEnabled() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
        return ServerResponseEntity.success(userService.isRecEnabled(userInfoInTokenBO.getUserId()));
    }

    @ApiOperation("rec开关")
    @PostMapping("/recSwitch")
    public ServerResponseEntity<Void> recSwitch() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
        userService.recSwitch(userInfoInTokenBO.getUserId());
        return ServerResponseEntity.success();
    }

}
