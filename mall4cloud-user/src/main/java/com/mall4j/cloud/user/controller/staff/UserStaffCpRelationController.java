package com.mall4j.cloud.user.controller.staff;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.StaffTaskFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WaitMatterCountVO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.StaffUserRelStatusEnum;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.UserFollowUpDTO;
import com.mall4j.cloud.user.dto.UserJourneysDTO;
import com.mall4j.cloud.user.dto.UserStaffCpRelationSetTagRequest;
import com.mall4j.cloud.user.manager.UserStaffCpRelationManager;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.model.UserStaffRelationFollowUp;
import com.mall4j.cloud.user.service.GroupPushSonTaskService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.service.UserStaffRelationFollowUpService;
import com.mall4j.cloud.user.vo.CrmUserManagerVO;
import com.mall4j.cloud.user.vo.UserJourneysVO;
import com.mall4j.cloud.user.vo.UserStaffCpRelationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController("staffUserStaffCpRelationController")
@RequestMapping("/s/crm/user_")
@Api(tags = "staff-用户信息")
public class UserStaffCpRelationController {


    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;
    @Autowired
    private UserStaffRelationFollowUpService userStaffRelationFollowUpService;
    @Autowired
    private GroupPushSonTaskService groupPushSonTaskService;
    @Autowired
    private WxCpApiFeignClient wxCpApiFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStaffCpRelationManager userStaffCpRelationManager;
    @Autowired
    private StaffTaskFeignClient staffTaskFeignClient;


    @PostMapping("/user_page")
    @ApiOperation(value = "获取会员管理别表列表", notes = "分页获取会员管理别表列表")
    public ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> userPage(@RequestBody UserStaffCpRelationSearchDTO userManagerDTO) {
        Long userId = AuthUserContext.get().getUserId();
        userManagerDTO.setStaffId(userId);

        if(Objects.isNull(userManagerDTO.getStatus())){
            //默认查询绑定中的客户
            userManagerDTO.setStatus(StaffUserRelStatusEnum.BIND.getCode());
        }
        PageVO<UserStaffCpRelationListVO> pageVO = userStaffCpRelationService.pageWithStaff(userManagerDTO);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("/pageStaffByUser")
    @ApiOperation(value = "获取企业好友列表", notes = "分页获取企业好友列表")
    public ServerResponseEntity<PageVO<UserStaffCpRelationVO>> pageStaffByUser(@RequestBody UserStaffCpRelationSearchDTO userManagerDTO) {
        PageVO<UserStaffCpRelationVO> pageVO = userStaffCpRelationService.pageStaffByUser(userManagerDTO);
        return ServerResponseEntity.success(pageVO);
    }

    @PostMapping("/ua/user_info")
    @ApiOperation(value = "客户详情", notes = "客户详情")
    public ServerResponseEntity<CrmUserManagerVO> info(@RequestParam Long relationId) {
        CrmUserManagerVO result = userStaffCpRelationService.getMobileCrmUserManagerVO(relationId);

        return ServerResponseEntity.success(result);
    }

    @PostMapping("/user_journeys")
    @ApiOperation(value = "客户旅程列表数据", notes = "客户旅程列表数据")
    public ServerResponseEntity<List<UserJourneysVO>> journeys(@RequestBody UserJourneysDTO request) {
        Assert.isNull(request.getRelationId(),"会员id不允许为空。");
        return ServerResponseEntity.success(userStaffCpRelationService.journeys(request,AuthUserContext.get().getUserId()));
    }

    @PostMapping("/user_follow_up")
    @ApiOperation(value = "客户跟进", notes = "客户跟进")
    public ServerResponseEntity<Void> followUp(@RequestBody UserFollowUpDTO request) {
        userStaffCpRelationManager.followUp(request,AuthUserContext.get().getUserId());
        return ServerResponseEntity.success();
    }


    @PostMapping("/setTag")
    @ApiOperation(value = "打标签", notes = "打标签")
    public ServerResponseEntity<Void> setTag(@Valid @RequestBody UserStaffCpRelationSetTagRequest request) {
        userStaffCpRelationService.setTag(request);
        return ServerResponseEntity.success();
    }

//    @PostMapping("/setTags")
//    @ApiOperation(value = "批量打标签", notes = "批量打标签")
//    public ServerResponseEntity<Void> setTags(@Valid @RequestBody List<UserStaffCpRelationSetTagRequest> requests) {
//        for (UserStaffCpRelationSetTagRequest request : requests) {
//            userStaffCpRelationService.setTag(request);
//        }
//        return ServerResponseEntity.success();
//    }

    @PostMapping("/setStage")
    @ApiOperation(value = "批量转移分组", notes = "批量转移分组")
    public ServerResponseEntity<Void> setStage(@Valid @RequestBody UserStaffCpRelationSetTagRequest request) {
        userStaffCpRelationService.setStage(request);
        return ServerResponseEntity.success();
    }

//    @PostMapping("/removeStage")
//    @ApiOperation(value = "删除客户所在阶段", notes = "删除客户所在阶段")
//    public ServerResponseEntity<Void> removeStage(@Valid @RequestBody UserStaffCpRelationSetTagRequest request) {
//        userStaffCpRelationService.removeStage(request);
//        return ServerResponseEntity.success();
//    }


    @GetMapping("/getByQiWeiUserId")
    @ApiOperation(value = "换取userId", notes = "换取userId")
    public ServerResponseEntity<UserStaffCpRelation> getByQiWeiUserId(String qiWeiUserId) {
        Long staffId = AuthUserContext.get().getUserId();
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
//                UpdateVipRelationFriendStateDTO stateDTO = new UpdateVipRelationFriendStateDTO();
//                stateDTO.setStaffId(staffId);
//                stateDTO.setVipUserId(userApiVO.getUserId());
//                stateDTO.setVipCpUserId(userApiVO.getQiWeiUserId());
//                groupPushTaskVipRelationService.asyncUpdateFriendState(stateDTO);
                log.info("好友与导购绑定好友关系成功--4> 耗时：{}ms ",System.currentTimeMillis() - startTime);
            }
            log.info("好友与导购绑定好友关系成功--5> back信息：{}",Objects.nonNull(userStaffCpRelation)?JSON.toJSON(userStaffCpRelation):null);
            return ServerResponseEntity.success(userStaffCpRelation);
        }catch (Exception e){
            log.info("换取userId失败 {} {}",e,e.getMessage());
            throw new LuckException("换取userId失败");
        }
    }

    @PostMapping("/waitMatterCount")
    @ApiOperation(value = "首页-待办事项统计", notes = "首页-待办事项统计")
    public ServerResponseEntity<WaitMatterCountVO> waitMatterCount() {
        /**
         * 当前员工未 执行完的任务
         */
        //Long userId = AuthUserContext.get().getUserId();//员工id
        WaitMatterCountVO waitMatterCountVO = new WaitMatterCountVO();


        //客户群 群群发
        WaitMatterCountVO sendTaskCount = groupPushSonTaskService.getStaffSendTaskCount();
        waitMatterCountVO.setCustomSendCount(sendTaskCount.getCustomSendCount());
        waitMatterCountVO.setGroupSendCount(sendTaskCount.getGroupSendCount());

        //标签建群
        //添加客户【手机号引流任务，员工关联未过期任务】
        ServerResponseEntity<WaitMatterCountVO> responseEntity=staffTaskFeignClient.waitMatterCount();
        ServerResponseEntity.checkResponse(responseEntity);
        waitMatterCountVO.setTaskPhoneCount(responseEntity.getData().getTaskPhoneCount());
        waitMatterCountVO.setTagGroupCount(responseEntity.getData().getTagGroupCount());

        return ServerResponseEntity.success(waitMatterCountVO);
    }


}
