package com.mall4j.cloud.platform.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.manager.WxCpManager;
import com.mall4j.cloud.platform.manager.WxCpStaffManager;
import com.mall4j.cloud.platform.task.StaffTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企微信息
 */
@RestController("WxCpManagerController")
@RequestMapping("/p/wx/cp/manager")
@Api(tags = "企微信息")
public class WxCpManagerController {

    @Autowired
    private WxCpManager wxCpManager;
    @Autowired
    private WxCpStaffManager wxCpStaffManager;
    @Autowired
    private StaffTask staffTask;


	@GetMapping("/departs")
	@ApiOperation(value = "同步企微部门信息", notes = "同步企微部门信息")
	public ServerResponseEntity<Void> departs() {
        wxCpManager.getDeparts();
		return ServerResponseEntity.success();
	}

    @GetMapping("/getUnassignedList")
    @ApiOperation(value = "同步企微离职员工信息", notes = "同步企微离职员工信息")
    public ServerResponseEntity<Void> getUnassignedList() {
        wxCpStaffManager.getUnassignedList();
        return ServerResponseEntity.success();
    }

    @GetMapping("/staffs")
    @ApiOperation(value = "同步企微员工信息", notes = "同步企微员工信息")
    public ServerResponseEntity<Void> staffs() {
        wxCpManager.getStaffs();
        return ServerResponseEntity.success();
    }

    @GetMapping("/refeshCpMsgAudit")
    @ApiOperation(value = "更新员工开启会话存档状态", notes = "更新员工开启会话存档状态")
    public ServerResponseEntity<Void> refeshCpMsgAudit() {
        staffTask.refeshCpMsgAudit();
        return ServerResponseEntity.success();
    }


}
