package com.mall4j.cloud.user.controller.platform;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SoldStaffSessionVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.user.dto.UserFollowUpDTO;
import com.mall4j.cloud.user.dto.UserJourneysDTO;
import com.mall4j.cloud.user.dto.UserStaffCpRelationSetTagRequest;
import com.mall4j.cloud.user.manager.UserStaffCpRelationManager;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.service.UserStaffRelationFollowUpService;
import com.mall4j.cloud.user.task.UserStaffRelationTask;
import com.mall4j.cloud.user.vo.CrmUserManagerVO;
import com.mall4j.cloud.user.vo.SoldUserStaffRelVo;
import com.mall4j.cloud.user.vo.UserJourneysVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController("platformUserStaffCpRelationController")
@RequestMapping("/p/user_staff_cp_relation")
@Api(tags = "platform-好友关系数据")
public class UserStaffCpRelationController {


    @Autowired
    UserStaffCpRelationService userStaffCpRelationService;
    @Autowired
    UserStaffRelationFollowUpService userStaffRelationFollowUpService;
    @Autowired
    UserStaffCpRelationManager userStaffCpRelationManager;
    @Autowired
    private UserStaffRelationTask userStaffRelationTask;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @PostMapping("/user_page")
    @ApiOperation(value = "获取会员管理列表", notes = "分页获取会员管理列表")
    public ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> userPage(@RequestBody UserStaffCpRelationSearchDTO userManagerDTO) {
        if(Objects.isNull(userManagerDTO.getDuplicateType())){
            userManagerDTO.setDuplicateType(1);
        }
        PageVO<UserStaffCpRelationListVO> pageVO = userStaffCpRelationService.pageWithStaff(userManagerDTO);
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/SoldExcelUserStaffRel")
    @ApiOperation(value = "会员管理-导出", notes = "会员管理-导出")
    public void soldExcel(UserStaffCpRelationSearchDTO dto, HttpServletResponse response) {
        List<SoldUserStaffRelVo> list=userStaffCpRelationService.soldStaffUser(dto);
        String fileName="客户管理-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            ExcelUtil.exportExcel(SoldUserStaffRelVo.class, list, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @PostMapping("/user_info")
    @ApiOperation(value = "客户详情", notes = "客户详情")
    public ServerResponseEntity<CrmUserManagerVO> info(@RequestParam Long relationId) {
        CrmUserManagerVO result = userStaffCpRelationService.getCrmUserManagerVO(relationId);
        return ServerResponseEntity.success(result);
    }

    @PostMapping("/user_info_by_unionId")
    @ApiOperation(value = "unionId获取客户详情", notes = "unionId获取客户详情")
    public ServerResponseEntity<CrmUserManagerVO> user_info_by_unionId(@RequestParam String unionId) {
        UserStaffCpRelation userStaffCpRelation = userStaffCpRelationService.getOne(Wrappers.lambdaQuery(UserStaffCpRelation.class)
                .eq(UserStaffCpRelation::getUserUnionId, unionId).orderByDesc(UserStaffCpRelation::getCreateTime),false);

        Assert.isNull(userStaffCpRelation,"当前unionId不存在，请检查数据后再试");
        CrmUserManagerVO result = userStaffCpRelationService.getCrmUserManagerVO(userStaffCpRelation.getId());
        return ServerResponseEntity.success(result);
    }

    @PostMapping("/user_journeys")
    @ApiOperation(value = "客户旅程列表数据", notes = "客户旅程列表数据")
    public ServerResponseEntity<List<UserJourneysVO>> journeys(@RequestBody UserJourneysDTO request) {
        ServerResponseEntity<StaffVO> responseEntity=staffFeignClient.getBySysUserId(AuthUserContext.get().getUserId().toString());
        Long adminStaffId=responseEntity.getData().getId();
        Assert.isNull(request.getRelationId(),"会员id不允许为空。");
        return ServerResponseEntity.success(userStaffCpRelationService.journeys(request,adminStaffId));
    }

    @PostMapping("/user_follow_up")
    @ApiOperation(value = "客户跟进", notes = "客户跟进")
    public ServerResponseEntity<Void> followUp(@RequestBody UserFollowUpDTO request) {
        ServerResponseEntity<StaffVO> responseEntity=staffFeignClient.getBySysUserId(AuthUserContext.get().getUserId().toString());
        Long adminStaffId=responseEntity.getData().getId();
        userStaffCpRelationManager.followUp(request,adminStaffId);
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

    @GetMapping("/staffDimission")
    @ApiOperation(value = "staffDimission", notes = "staffDimission")
    public ServerResponseEntity<Void> staffDimission(List<Long> staffIds) {
        userStaffCpRelationService.staffDimission(staffIds);
        return ServerResponseEntity.success();
    }

    @PostMapping("/removeStage")
    @ApiOperation(value = "删除客户所在阶段", notes = "删除客户所在阶段")
    public ServerResponseEntity<Void> removeStage(@Valid @RequestBody UserStaffCpRelationSetTagRequest request) {
        userStaffCpRelationService.removeStage(request);
        return ServerResponseEntity.success();
    }

    @PostMapping("/syncUserStaffRelation")
    @ApiOperation(value = "同步历史客户与联系人数据", notes = "同步历史客户与联系人数据")
    public ServerResponseEntity<Void> syncUserStaffRelation() {
        userStaffRelationTask.syncUserStaffRelation();
        return ServerResponseEntity.success();
    }



}
