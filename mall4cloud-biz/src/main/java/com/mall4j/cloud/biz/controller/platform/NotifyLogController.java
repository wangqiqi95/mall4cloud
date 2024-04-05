package com.mall4j.cloud.biz.controller.platform;

import com.mall4j.cloud.biz.dto.NotifyLogDTO;
import com.mall4j.cloud.biz.service.NotifyLogService;
import com.mall4j.cloud.biz.vo.NotifyLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 *
 * @author lhd
 * @date 2021-05-14 09:35:32
 */
@RestController("platformNotifyLogController")
@RequestMapping("/p/notify_log")
@Api(tags = "消息日志")
public class NotifyLogController {

    @Autowired
    private NotifyLogService notifyLogService;

	@GetMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<PageVO<NotifyLogVO>> page(@Valid PageDTO pageDTO, NotifyLogDTO notifyLogDTO) {
		PageVO<NotifyLogVO> notifyLogPage = notifyLogService.page(pageDTO,notifyLogDTO);
		return ServerResponseEntity.success(notifyLogPage);
	}
}
