package com.mall4j.cloud.platform.controller.platform;

import com.mall4j.cloud.api.platform.dto.DownloadCenterQueryDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.DownloadCenter;
import com.mall4j.cloud.platform.service.DownloadCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/p/downloadCenter")
@Api(tags = "platform-下载中心")
public class DownloadCenterController {

	@Autowired
	private DownloadCenterService downloadCenterService;

	@GetMapping("/page")
	@ApiOperation(value = "获取下载中心列表", notes = "获取下载中心列表")
	public ServerResponseEntity<PageVO<DownloadCenter>> getOfflineHandleEventByShopId(@Valid PageDTO pageDTO, DownloadCenterQueryDTO downloadCenterQueryDTO) {
		PageVO<DownloadCenter> downloadCenterPageVO = downloadCenterService.page(pageDTO, downloadCenterQueryDTO);
		return ServerResponseEntity.success(downloadCenterPageVO);
	}

	@DeleteMapping
	@ApiOperation(value = "删除", notes = "根据id删除")
	public ServerResponseEntity<Void> delete(@RequestParam Long id) {
		downloadCenterService.deleteById(id);
		return ServerResponseEntity.success();
	}
}
