package com.mall4j.cloud.user.controller.staff;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.vo.TaskSonGroupVO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.StaffGroupTaskPushDTO;
import com.mall4j.cloud.api.user.dto.StaffSaveSonTaskSendRecordDTO;
import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
import com.mall4j.cloud.user.dto.QueryChuDaRenQunDetailDTO;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.TaskSonItemVO;
import com.mall4j.cloud.user.vo.TaskSonUserVO;
import com.mall4j.cloud.user.vo.TaskSonVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController("staffGroupPushTaskController")
@RequestMapping("/s/groupPushTask")
@Api(tags = "导购小程序-群发任务")
@Slf4j
public class GroupPushTaskController {

    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    GroupPushSonTaskService groupPushSonTaskService;
    @Autowired
    private GroupPushSonTaskMediaService groupPushSonTaskMediaService;

    @Autowired
    private GroupPushTaskService groupPushTaskService;

    @Autowired
    private StaffBatchSendCpMsgService staffBatchSendCpMsgService;

    @GetMapping("/getSonTaskPage")
    @ApiOperation(value = "导购端群发任务列表", notes = "导购端群发任务列表")
    public ServerResponseEntity<PageVO<TaskSonVO>> taskList(@RequestParam(value = "taskMode") Integer taskMode, @Valid PageDTO pageDTO) {
        Long staffId = AuthUserContext.get().getUserId();
        String qiweiUserId = "";
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(staffId);
        log.info("当前查询群发任务信息的导购信息为:{}", staffResp.getData());
        if (staffResp.isSuccess()) {
            qiweiUserId = staffResp.getData().getQiWeiUserId();
        }
        if (StrUtil.isEmpty(qiweiUserId)) {
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
        }
        return groupPushSonTaskService.getSonTaskPage(staffId, taskMode, pageDTO);
    }

    @GetMapping("/getSonTaskDetailBySonTaskId")
    @ApiOperation(value = "根据子任务ID获取子任务详情", notes = "根据子任务ID获取子任务详情")
    public ServerResponseEntity<TaskSonItemVO> getSonTaskDetailBySonTaskId(@RequestParam Long sonTaskId) {
        Long staffId = AuthUserContext.get().getUserId();
        String qiweiUserId = "";
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(staffId);
        log.info("当前查询群发任务信息的导购信息为:{}", staffResp.getData());
        if (staffResp.isSuccess()) {
            qiweiUserId = staffResp.getData().getQiWeiUserId();
        }
        if (StrUtil.isEmpty(qiweiUserId)) {
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
        }
        return groupPushSonTaskService.getSonTaskDetailBySonTaskId(staffId, sonTaskId);
    }

    @PostMapping("/getSonTaskUserDetailBySonTaskId")
    @ApiOperation(value = "根据子任务ID获取子任务触达人群")
    public ServerResponseEntity<PageVO<TaskSonUserVO>> getSonTaskUserDetailBySonTaskId(@RequestBody QueryChuDaRenQunDetailDTO params,
        @Valid PageDTO pageDTO) {
        Long staffId = AuthUserContext.get().getUserId();
        String qiweiUserId = "";
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(staffId);
        log.info("当前查询群发任务信息的导购信息为:{}", staffResp.getData());
        if (staffResp.isSuccess()) {
            qiweiUserId = staffResp.getData().getQiWeiUserId();
        }
        if (StrUtil.isEmpty(qiweiUserId)) {
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
        }
        return groupPushSonTaskService.getSonTaskUserDetailBySonTaskId(staffId, params, pageDTO);
    }

    @GetMapping("/getSonTaskGroupDetailBySonTaskId")
    @ApiOperation(value = "根据子任务ID获取子任务触达群")
    public ServerResponseEntity<PageVO<TaskSonGroupVO>> getSonTaskGroupDetailBySonTaskId(
        @RequestParam(value = "sonTaskId") Long sonTaskId,
        @RequestParam(value = "searchKey", required = false) String searchKey,
        @Valid PageDTO pageDTO) {
        Long staffId = AuthUserContext.get().getUserId();
        String qiweiUserId = "";
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(staffId);
        log.info("当前查询群发任务信息的导购信息为:{}", staffResp.getData());
        if (staffResp.isSuccess()) {
            qiweiUserId = staffResp.getData().getQiWeiUserId();
        }
        if (StrUtil.isEmpty(qiweiUserId)) {
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
        }
        return groupPushSonTaskService.getSonTaskGroupDetailBySonTaskId(staffId, sonTaskId, searchKey, pageDTO);
    }

   /* @PostMapping("/staffOncePushTask")
    @ApiOperation(value = "导购端群发任务单个会员触达", notes = "导购端群发任务单个会员触达")
    public ServerResponseEntity staffOncePushTask(@RequestBody StaffGroupTaskPushDTO staffGroupTaskPushDTO) {
        Long staffId = AuthUserContext.get().getUserId();
        // 单传任务ID即为【一键群发】,参数全都有时即为【对单个会员进行发送】; 后方案调整，一键群发和单个会员触达分为两个接口
        staffGroupTaskPushDTO.setStaffId(staffId);
        log.info("导购触发群发任务单个会员触达，进入群发任务单个会员触达方法。当前传参:{}", staffGroupTaskPushDTO);
        // 如果是针对单个会员进行任务触达，那么只执行新增发送记录新增。对单个会员触达由前端直接调用企业微信进行发送
        // 校验前端单个发送是否成功，如果失败那就证明当前导购与那位会员并不是好友关系。这就需要修改任务与用户关联表将好友状态改为【未加好友】
        // 如果发送成功那就添加一条任务发送成功记录
        if(staffGroupTaskPushDTO.getSendStatus().equals(1)){
            // 新增【推送完成记录表】记录
            List<Long> userIds = new ArrayList<>();
            userIds.add(staffGroupTaskPushDTO.getUserId());
            StaffSaveSonTaskSendRecordDTO staffSaveSonTaskSendRecordDTO = new StaffSaveSonTaskSendRecordDTO();
            staffSaveSonTaskSendRecordDTO.setSonTaskId(staffGroupTaskPushDTO.getSonTaskId());
            staffSaveSonTaskSendRecordDTO.setUserId(staffGroupTaskPushDTO.getUserId());
            staffSaveSonTaskSendRecordDTO.setStaffId(staffGroupTaskPushDTO.getStaffId());
            // 调用方法新增记录
            ServerResponseEntity response = groupPushSonTaskService.staffSaveSonTaskSendRecord(staffSaveSonTaskSendRecordDTO);
            if(!response.isSuccess()){
                return ServerResponseEntity.fail(ResponseEnum.EXCEPTION, response.getData());
            }
            return ServerResponseEntity.success("已完成，本次预计触达1人");
        } else {
            groupPushSonTaskService.updateStaffAndUserTaskRelation(staffGroupTaskPushDTO.getTaskId(), staffGroupTaskPushDTO.getStaffId(), staffGroupTaskPushDTO.getUserId());
            return ServerResponseEntity.success("抱歉，发送失败。您与当前会员并不是好友关系");
        }
    }*/

    @PostMapping("/staffGroupPushTask")
    @ApiOperation(value = "导购端群发任务一键群发", notes = "导购端群发任务一键群发")
    public ServerResponseEntity staffGroupPushTask(@RequestParam Long sonTaskId) {
        Long staffId = AuthUserContext.get().getUserId();

        // 单传任务ID即为【一键群发】,参数全都有时即为【对单个会员进行发送】; 后方案调整，一键群发和单个会员触达分为两个接口
        log.info("导购触发群发任务一键群发 进入群发任务一键群发方法 当前触发的子任务ID为 {}, 当前操作导购ID {}", sonTaskId, staffId);
        StartGroupPushDTO startGroupPushDTO = new StartGroupPushDTO();
        startGroupPushDTO.setSonTaskId(sonTaskId);
        startGroupPushDTO.setStaffId(staffId);
        try {
            return groupPushTaskService.startGroupPush(startGroupPushDTO);
        }catch (LuckException e){
            log.error("", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }

    }

    @ApiOperation(value = "导购根据子任务ID获取任务素材内容", notes = "导购根据子任务ID获取任务素材内容")
    @GetMapping("/mediaAttachmentBySonTaskId")
    public ServerResponseEntity<List<Attachment>> mediaAttachmentBySonTaskId(@RequestParam("sonTaskId") Long sonTaskId, @RequestParam(value = "urlFlag",required = false, defaultValue = "1")Integer urlFlag) {
        boolean flag;
        flag = urlFlag.equals(1);
        Long staffId=AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(groupPushSonTaskMediaService.mediaAttachmentBySonTaskId(sonTaskId, staffId,flag));
    }

    @ApiOperation(value = "导购获取自己群发任务数量", notes = "导购获取自己群发任务数量")
    @GetMapping("/staffGetGroupPushTaskCount")
    public ServerResponseEntity<Integer> staffGetGroupPushTaskCount() {
        Long staffId = AuthUserContext.get().getUserId();
        log.info("导购获取自己群发任务数量,当前查询导购为:{}", staffId);
        return groupPushSonTaskService.staffGetGroupPushTaskCount(staffId);
    }

}
