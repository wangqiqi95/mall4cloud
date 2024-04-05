package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityService;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
@RestController("staffDistributionStoreActivityController")
@RequestMapping("/s/distribution_store_activity")
@Api(tags = "导购小程序-分销推广-门店活动")
public class DistributionStoreActivityController {

    @Autowired
    private DistributionStoreActivityService distributionStoreActivityService;

	@GetMapping("/page")
	@ApiOperation(value = "活动列表", notes = "分页获取门店活动列表")
	public ServerResponseEntity<PageVO<DistributionStoreActivity>> page(@Valid PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO) {
		PageVO<DistributionStoreActivity> distributionStoreActivityPage = distributionStoreActivityService.pageEffect(pageDTO, distributionStoreActivityQueryDTO);
		return ServerResponseEntity.success(distributionStoreActivityPage);
	}

	@GetMapping("/count")
	@ApiOperation(value = "活动统计", notes = "活动统计")
	public ServerResponseEntity<DistributionStoreActivityCountVO> count() {
		return ServerResponseEntity.success(distributionStoreActivityService.count());
	}

	@GetMapping("/getById")
    @ApiOperation(value = "活动详情", notes = "根据id获取门店活动")
    public ServerResponseEntity<DistributionStoreActivity> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionStoreActivityService.getById(id));
    }

}
