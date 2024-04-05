package com.mall4j.cloud.biz.controller.app.staff;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.service.cp.TaskSendDetailService;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController("staffTaskSendDetailController")
@RequestMapping("/s/task_send_detail")
@Api(tags = "导购小程序-待办任务")
public class TaskSendDetailController {

    @Autowired
    private TaskSendDetailService taskSendDetailService;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @GetMapping("/page")
    @ApiOperation(value = "列表", notes = "列表")
    public ServerResponseEntity<List<TaskAttachmentVO>> taskList(Integer status) {
        Long staffId = AuthUserContext.get().getUserId();
        String qiweiUserId = "";
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (staffResp.isSuccess()) {
            qiweiUserId = staffResp.getData().getQiWeiUserId();
        }
        if (StrUtil.isEmpty(qiweiUserId)) {
            throw new LuckException("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
//            throw new LuckException("员工未绑定企业微信");
        }
        return ServerResponseEntity.success(taskSendDetailService.queryTaskList(staffId, qiweiUserId, status));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "详情", notes = "详情")
    public ServerResponseEntity<TaskAttachmentVO> detail(Long id, Integer type) {
        return ServerResponseEntity.success(taskSendDetailService.getTaskDetail(id, type));
    }

    @PutMapping("/completeTask")
    @ApiOperation(value = "执行任务", notes = "执行任务")
    public ServerResponseEntity<Void> completeTask(Long id, Integer type) {
        log.info(id+"========="+type);
        taskSendDetailService.completeTask(id, type);
        return ServerResponseEntity.success();
    }

}
