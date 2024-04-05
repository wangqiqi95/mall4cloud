package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionStoreActivityUser;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 门店活动-报名用户
 *
 * @author gww
 * @date 2021-12-27 13:42:16
 */
@RestController("platformDistributionStoreActivityUserController")
@RequestMapping("/p/distribution_store_activity_user")
@Api(tags = "平台端-门店活动-报名用户")
public class DistributionStoreActivityUserController {

    @Autowired
    private DistributionStoreActivityUserService distributionStoreActivityUserService;

	@GetMapping("/page")
	@ApiOperation(value = "获取门店活动-报名用户列表", notes = "分页获取门店活动-报名用户列表")
	public ServerResponseEntity<PageVO<DistributionStoreActivityUser>> page(@Valid PageDTO pageDTO, @Valid DistributionStoreActivityUserQueryDTO queryDTO) {
		PageVO<DistributionStoreActivityUser> distributionStoreActivityUserPage = distributionStoreActivityUserService.page(pageDTO, queryDTO);
		return ServerResponseEntity.success(distributionStoreActivityUserPage);
	}

	@ApiOperation(value = "导出", notes = "导出")
	@GetMapping("/export")
	public ServerResponseEntity<Void> export(HttpServletResponse response, @Valid DistributionStoreActivityUserQueryDTO queryDTO) {
		distributionStoreActivityUserService.export(response, queryDTO);
		return ServerResponseEntity.success();
	}



}
