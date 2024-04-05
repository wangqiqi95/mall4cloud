package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionPosterDTO;
import com.mall4j.cloud.distribution.model.DistributionPoster;
import com.mall4j.cloud.distribution.service.DistributionPosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销推广-推广海报
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
@RestController("staffDistributionPosterController")
@RequestMapping("/s/distribution_poster")
@Api(tags = "导购小程序-分销推广-推广海报")
public class DistributionPosterController {

    @Autowired
    private DistributionPosterService distributionPosterService;

//	@GetMapping("/page")
//	@ApiOperation(value = "获取分销推广-推广海报列表", notes = "分页获取分销推广-推广海报列表")
//	public ServerResponseEntity<PageVO<DistributionPoster>> page(@Valid PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO) {
//		PageVO<DistributionPoster> distributionPosterPage = distributionPosterService.page(pageDTO, distributionPosterDTO);
//		return ServerResponseEntity.success(distributionPosterPage);
//	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-推广海报", notes = "根据id获取分销推广-推广海报")
    public ServerResponseEntity<DistributionPoster> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionPosterService.getById(id));
    }


	@GetMapping("/pageEffect")
	@ApiOperation(value = "获取分销推广-推广海报生效列表", notes = "分页获取分销推广-推广海报生效列表")
	public ServerResponseEntity<PageVO<DistributionPoster>> pageEffect(@Valid PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO) {
//		distributionPosterDTO.setQueryStoreId(AuthUserContext.get().getStoreId());
		PageVO<DistributionPoster> distributionPosterPage = distributionPosterService.pageEffect(pageDTO, distributionPosterDTO);
		return ServerResponseEntity.success(distributionPosterPage);
	}

}
