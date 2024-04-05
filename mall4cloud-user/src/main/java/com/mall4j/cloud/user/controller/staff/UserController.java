package com.mall4j.cloud.user.controller.staff;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerGetDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.user.dto.StaffSelectUserDTO;
import com.mall4j.cloud.user.dto.StaffSelectUserForTaskDTO;
import com.mall4j.cloud.user.dto.UpdateVipRelationFriendStateDTO;
import com.mall4j.cloud.user.dto.UserBindStaffDTO;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.GroupPushTaskVipRelationService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.vo.StaffGetUserDetailByMaterialVO;
import com.mall4j.cloud.user.vo.StaffUserDetailVO;
import com.mall4j.cloud.user.vo.StaffUserVo;
import com.mall4j.cloud.user.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
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
@RestController("staffUserController")
@RequestMapping("/s/user")
@Api(tags = "导购小程序-会员")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private CrmCustomerFeignClient crmCustomerFeignClient;
    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private WxCpApiFeignClient wxCpApiFeignClient;

    @Autowired
    private GroupPushTaskVipRelationService groupPushTaskVipRelationService;

    @GetMapping("/ua/userDetail")
    @ApiOperation(value = "会员详情", notes = "通过会员id查询会员详情")
    public ServerResponseEntity<StaffUserDetailVO> userDetail(Long userId) {
        StaffUserDetailVO staffUserDetailVO = new StaffUserDetailVO();
        UserApiVO userDetail = userService.getByUserId(userId);
        if (Objects.nonNull(userDetail)) {
            UserInfoVO userInfo = new UserInfoVO();
            // 会员积分
            CustomerGetDto customerGetDto = new CustomerGetDto();
            customerGetDto.setMobile(userDetail.getPhone());
            ServerResponseEntity<CustomerGetVo> customerRep = crmCustomerFeignClient.customerGet(customerGetDto);
            if (customerRep.isSuccess()) {
                userInfo.setScore(customerRep.getData().getCurrent_valid_point());
            }
            // 会员消费数据
            ServerResponseEntity<Long> totalRep =  orderFeignClient.countOrderAmountByUserId(userDetail.getUserId(),
                    null, null);
            if (totalRep.isSuccess()) {
                userInfo.setTotalConsume(totalRep.getData());
            }
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime start = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear());
            Date startTime = Date.from(start.atZone(zone).toInstant());
            Date endTime = Date.from(LocalDateTime.now().atZone(zone).toInstant());
            ServerResponseEntity<Long> yearRep =  orderFeignClient.countOrderAmountByUserId(userDetail.getUserId(),
                    startTime, endTime);
            if (totalRep.isSuccess()) {
                userInfo.setYearConsume(yearRep.getData());
            }

            //会员（企微会员信息）
            if(Objects.nonNull(userDetail.getStaffId())){
                UserStaffCpRelation userStaffCpRelation=userStaffCpRelationService.getByStaffAndUser(userDetail.getStaffId(), userId);
                if(Objects.nonNull(userStaffCpRelation)){
                    userDetail.setUserUnionId(userStaffCpRelation.getUserUnionId());
                    userDetail.setQiWeiUserId(userStaffCpRelation.getQiWeiUserId());
                }

                //是否添加微信
                userDetail.setAddWechat(userStaffCpRelationService.getByStaffAndUser(userDetail.getStaffId(), userId) == null ? 0 : 1);
            }


            staffUserDetailVO.setUserDetail(userDetail);
            staffUserDetailVO.setUserInfo(userInfo);
        }
        return ServerResponseEntity.success(staffUserDetailVO);
    }

    @GetMapping("/ua/getByQiWeiUserId")
    @ApiOperation(value = "换取userId", notes = "换取userId")
    public ServerResponseEntity<UserStaffCpRelation> getByQiWeiUserId(String qiWeiUserId, Long staffId) {
        try {
            log.info("换取userId qiWeiUserId:{} staffId{}",qiWeiUserId,staffId);
            UserStaffCpRelation userStaffCpRelation=userStaffCpRelationService.getByQiWeiUserId(qiWeiUserId,staffId);
            if(Objects.isNull(userStaffCpRelation)){
//                throw new LuckException("换取userId失败");
                log.info("换取userId失败 执行往下修复");
            }
            if(Objects.isNull(userStaffCpRelation)){
                long startTime = System.currentTimeMillis();
                log.info("未获取到好友与导购绑定好友关系--1> 客户id:{} 导购id:{}",qiWeiUserId,staffId);
                StaffVO staffVO=staffFeignClient.getStaffById(staffId).getData();
                log.info("未获取到好友与导购绑定好友关系--2> 客户id:{} 导购信息:{}",qiWeiUserId, JSON.toJSON(staffVO));
                if(Objects.isNull(staffVO) && StrUtil.isBlank(staffVO.getQiWeiUserId())){
                    throw new LuckException("换取userId失败");
                }
                JSONObject externalcontactDetail=wxCpApiFeignClient.externalcontactDetail(qiWeiUserId,staffVO.getQiWeiUserId()).getData();
                log.info("未获取到好友与导购绑定好友关系--3> 客户id:{} 客户详情:{}",qiWeiUserId, JSON.toJSON(externalcontactDetail));
                if(Objects.isNull(externalcontactDetail)){
                    throw new LuckException("换取userId失败");
                }
                String unionId=externalcontactDetail.getString("unionId");
                UserApiVO userApiVO=userService.getByUnionId(unionId);
                log.info("未获取到好友与导购绑定好友关系--3> 客户id:{} 客户注册系统信息:{}",qiWeiUserId, JSON.toJSON(userApiVO));
                //绑定好友关系
                userStaffCpRelation=new UserStaffCpRelation();
                userStaffCpRelation.setUserId(Objects.nonNull(userApiVO)?userApiVO.getUserId():null);
                userStaffCpRelation.setUserUnionId(unionId);
                userStaffCpRelation.setQiWeiUserId(qiWeiUserId);
                userStaffCpRelation.setQiWeiNickName(externalcontactDetail.getString("name"));
                userStaffCpRelation.setStaffId(staffId);
                userStaffCpRelation.setQiWeiStaffId(staffVO.getQiWeiUserId());
                userStaffCpRelation.setStatus(1);
                userStaffCpRelation.setCreateTime(new Date());
                userStaffCpRelationService.save(userStaffCpRelation);
                UpdateVipRelationFriendStateDTO stateDTO = new UpdateVipRelationFriendStateDTO();
                stateDTO.setStaffId(staffId);
                stateDTO.setVipUserId(userApiVO.getUserId());
                stateDTO.setVipCpUserId(userApiVO.getQiWeiUserId());
                groupPushTaskVipRelationService.asyncUpdateFriendState(stateDTO);
                log.info("好友与导购绑定好友关系成功--4> 耗时：{}ms ",System.currentTimeMillis() - startTime);
            }
            log.info("好友与导购绑定好友关系成功--5> back信息：{}",Objects.nonNull(userStaffCpRelation)?JSON.toJSON(userStaffCpRelation):null);
            return ServerResponseEntity.success(userStaffCpRelation);
        }catch (Exception e){
            log.info("换取userId失败 {} {}",e,e.getMessage());
            throw new LuckException("换取userId失败");
        }
    }

    @GetMapping("/pageUserByStaff")
    @ApiOperation(value = "当前导购会员列表", notes = "当前导购会员列表")
    public ServerResponseEntity<PageVO<UserApiVO>> pageUserByStaff(@Valid PageDTO pageDTO, UserQueryDTO queryDTO){
        Long userId = AuthUserContext.get().getUserId();
        queryDTO.setStaffId(userId);
//        queryDTO.setLevelType(0);
        queryDTO.setStoreId(null);
        return ServerResponseEntity.success(userService.pageUserByStaff(pageDTO, queryDTO));
    }

    @ApiOperation("当前导购本月生日会员列表")
    @GetMapping("/pageBirthdayUserByStaff")
    public ServerResponseEntity<PageVO<UserApiVO>> pageBirthdayUserByStaff(@Valid PageDTO pageDTO, UserQueryDTO queryDTO){
        Long userId = AuthUserContext.get().getUserId();
        queryDTO.setStaffId(userId);
//        queryDTO.setLevelType(0);
//        queryDTO.setBirthStartDate(DateUtils.getMonthFirstOrLastDay(new Date(), 0));
//        queryDTO.setBirthEndDate(DateUtils.getMonthFirstOrLastDay(new Date(), 1));
        queryDTO.setBirthDateFlag(1);
        return ServerResponseEntity.success(userService.pageUserByStaff(pageDTO, queryDTO));
    }

    @PostMapping("/pageCouponUserByStaff")
    @ApiOperation(value = "当前导购会员优惠券列表", notes = "当前导购会员优惠券列表")
    public ServerResponseEntity<PageVO<UserApiVO>> pageCouponUserByStaff(@RequestBody PageDTO pageDTO, @RequestBody QueryHasCouponUsersRequest queryHasCouponUsersRequest){
        return ServerResponseEntity.success(userService.pageCouponUserByStaff(pageDTO, queryHasCouponUsersRequest));
    }


    @ApiOperation(value = "会员分析-新增数据", notes = "会员分析-新增数据")
    @GetMapping("/countStaffUserNum")
    public ServerResponseEntity<StaffUserVo> countStaffUserNum(){
        UserQueryDTO queryDTO = new UserQueryDTO();
        StaffUserVo vo = new StaffUserVo();
        queryDTO.setStaffId(AuthUserContext.get().getUserId());
        queryDTO.setLevelType(0);
        vo.setTotalNum(userService.countStaffUser(queryDTO));
        queryDTO.setStartTime(DateUtils.getMonthFirstOrLastDay(new Date(), 0));
        queryDTO.setEndTime(DateUtils.getMonthFirstOrLastDay(new Date(), 1));
        vo.setMonthNum(userService.countStaffUser(queryDTO));
        queryDTO.setStartTime(DateUtils.dateToFastOrLast(new Date(), 1));
        queryDTO.setEndTime(DateUtils.dateToFastOrLast(new Date(), 2));
        vo.setTodayNum(userService.countStaffUser(queryDTO));
        return ServerResponseEntity.success(vo);
    }


    @GetMapping("/countStaffUser")
    @ApiOperation(value = "当前导购下会员数量统计", notes = "当前导购下会员数量统计")
    public ServerResponseEntity<Integer> countStaffUser(UserQueryDTO queryDTO){
        queryDTO.setStaffId(AuthUserContext.get().getUserId());
        queryDTO.setLevelType(0);
        return ServerResponseEntity.success(userService.countStaffUser(queryDTO));
    }

    /**
     * 导购招募会员图信息
     * @return
     */
    @GetMapping("/listStaffUser")
    public ServerResponseEntity<List<StaffUserVo>> listStaffUser(@Valid PageDTO pageDTO, UserQueryDTO queryDTO){
        queryDTO.setStaffId(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(userService.listStaffUser(pageDTO, queryDTO));
    }


    @GetMapping("/pageUserWithUnBindStaff")
    @ApiOperation(value = "当前门店未分配会员列表", notes = "当前门店未分配会员列表")
    public ServerResponseEntity<PageVO<UserApiVO>> pageUserWithUnBindStaff(@Valid PageDTO pageDTO, UserQueryDTO queryDTO) {
        // queryDTO.setStoreId(AuthUserContext.get().getStoreId());
        queryDTO.setStoreId(null);
        queryDTO.setServiceStoreId(AuthUserContext.get().getStoreId());
        queryDTO.setUnBindStaff(true);
        return ServerResponseEntity.success(userService.pageUserByStaff(pageDTO, queryDTO));
    }

    @PostMapping("/bindStaff")
    @ApiOperation(value = "分配导购", notes = "分配导购")
    public ServerResponseEntity<List<UserBindStaffDTO>> bindStaff(@Valid @RequestBody UserBindStaffDTO userBindStaffDTO) {
        if (userBindStaffDTO.getBindType() == 1) {
            if (userBindStaffDTO.getStaffId() == null) {
                throw new LuckException("staffId不能为空");
            }
            if (StringUtils.isEmpty(userBindStaffDTO.getStaffName())) {
                throw new LuckException("staffName不能为空");
            }
        }
        // 清除用户缓存
        List<UserBindStaffDTO> userBindStaffDTOS = userService.bindStaff(userBindStaffDTO);
        if (!CollectionUtils.isEmpty(userBindStaffDTO.getUserDataList())) {
            userBindStaffDTO.getUserDataList().forEach(user ->{
                userService.removeUserCacheByUserId(user.getUserId());
            });
        }
        return ServerResponseEntity.success(userBindStaffDTOS);
    }

    /**
     * 导购端导购筛选查询我的会员
     * @param staffSelectUserDTO 导购端导购筛选查询我的会员
     * @return
     */
    @PostMapping("/staffScreenUser")
    @ApiOperation(value = "导购筛选查询我的会员", notes = "导购筛选查询我的会员")
    public ServerResponseEntity<PageVO<UserApiVO>> staffSelectUser(@RequestBody StaffSelectUserDTO staffSelectUserDTO) {
        return ServerResponseEntity.success(userService.staffSelectUser(staffSelectUserDTO));
    }

    /**
     * 导购端群发任务页面导购筛选查询我的会员
     * @param staffSelectUserForTaskDTO 导购端群发任务页面导购筛选查询我的会员
     * @return
     */
    @PostMapping("/staffScreenUserForTask")
    @ApiOperation(value = "导购端群发任务页面导购筛选查询我的会员", notes = "导购端群发任务页面导购筛选查询我的会员")
    public ServerResponseEntity<PageVO<UserApiVO>> staffScreenUserForTask(@RequestBody StaffSelectUserForTaskDTO staffSelectUserForTaskDTO) {
        return ServerResponseEntity.success(userService.staffScreenUserForTask(staffSelectUserForTaskDTO));
    }

    @GetMapping("/staffGetUserDetailByMaterial")
    @ApiOperation(value = "素材中心场景-导购获取用户信息", notes = "素材中心场景-导购获取用户信息")
    public ServerResponseEntity<StaffGetUserDetailByMaterialVO> staffGetUserDetailByMaterial(Long userId) {
        StaffGetUserDetailByMaterialVO staffGetUserDetailByMaterialVO = userService.staffGetUserDetailByMaterialVO(userId);
        if (Objects.nonNull(staffGetUserDetailByMaterialVO)) {
            // 会员积分
            CustomerGetDto customerGetDto = new CustomerGetDto();
            customerGetDto.setMobile(staffGetUserDetailByMaterialVO.getPhone());
            CustomerGetVo customerRep = crmCustomerFeignClient.customerGet(customerGetDto).getData();
            if (Objects.nonNull(customerRep)) {
                staffGetUserDetailByMaterialVO.setScore(customerRep.getCurrent_valid_point());
                staffGetUserDetailByMaterialVO.setCurrentGradeId(customerRep.getCurrent_grade_id());
            }
        }
        return ServerResponseEntity.success(staffGetUserDetailByMaterialVO);
    }

}
