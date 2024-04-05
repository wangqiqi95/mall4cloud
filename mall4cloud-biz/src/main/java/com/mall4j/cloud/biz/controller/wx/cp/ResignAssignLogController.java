package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.dto.cp.ResignAssignLogQueryDTO;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import com.mall4j.cloud.biz.service.cp.ResignAssignLogService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 离职分配日志表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@RestController("platformResignAssignLogController")
@RequestMapping("/p/cp/resign_assign_log")
@Api(tags = "离职分配日志表")
public class ResignAssignLogController {

    private final ResignAssignLogService resignAssignLogService;
	@GetMapping("/page")
	@ApiOperation(value = "获取离职分配日志表列表", notes = "分页获取离职分配日志表列表")
	public ServerResponseEntity<PageVO<ResignAssignLog>> page(@Valid PageDTO pageDTO, ResignAssignLogQueryDTO request) {
		PageVO<ResignAssignLog> resignAssignLogPage = resignAssignLogService.page(pageDTO,request);
		return ServerResponseEntity.success(resignAssignLogPage);
	}
}
