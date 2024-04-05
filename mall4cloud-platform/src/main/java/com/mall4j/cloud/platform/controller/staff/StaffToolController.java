package com.mall4j.cloud.platform.controller.staff;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.StaffToolDTO;
import com.mall4j.cloud.platform.model.StaffTool;
import com.mall4j.cloud.platform.service.StaffToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 员工企微工具信息
 *
 * @author ZengFanChang
 * @date 2022-02-12 00:13:55
 */
@RestController("appStaffToolController")
@RequestMapping("/s/staff_tool")
@Api(tags = "导购小程序-员工企微工具信息")
public class StaffToolController {

    @Autowired
    private StaffToolService staffToolService;

    @GetMapping("/getStaffTool")
    @ApiOperation(value = "获取员工企微工具信息", notes = "根据id获取员工企微工具信息")
    public ServerResponseEntity<StaffTool> getStaffTool() {
        return ServerResponseEntity.success(staffToolService.getStaffToolByStaff(AuthUserContext.get().getUserId()));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存员工企微工具信息", notes = "保存员工企微工具信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody StaffToolDTO staffToolDTO) {
        staffToolDTO.setStaffId(AuthUserContext.get().getUserId());
        staffToolService.save(staffToolDTO);
        return ServerResponseEntity.success();
    }
}
