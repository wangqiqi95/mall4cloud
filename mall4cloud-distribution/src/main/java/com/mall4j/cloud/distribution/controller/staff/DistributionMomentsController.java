package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionMomentsDTO;
import com.mall4j.cloud.distribution.model.DistributionMoments;
import com.mall4j.cloud.distribution.service.DistributionMomentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("staffDistributionMomentsController")
@RequestMapping("/s/distribution_moments")
@Slf4j
@Api(tags = "导购小程序-分销推广-朋友圈")
public class DistributionMomentsController {

    @Autowired
    private DistributionMomentsService distributionMomentsService;

    @Autowired
	private MapperFacade mapperFacade;

//	@GetMapping("/page")
//	@ApiOperation(value = "获取分销推广-朋友圈列表", notes = "分页获取分销推广-朋友圈列表")
//	public ServerResponseEntity<PageVO<DistributionMomentsVO>> page(@Valid PageDTO pageDTO, DistributionMomentsDTO dto) {
//		PageVO<DistributionMomentsVO> distributionMomentsPage = distributionMomentsService.page(pageDTO, dto);
//		return ServerResponseEntity.success(distributionMomentsPage);
//	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-朋友圈", notes = "根据id获取分销推广-朋友圈")
    public ServerResponseEntity<DistributionMomentsDTO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionMomentsService.getMomentsById(id));
    }

	@GetMapping("/pageEffect")
	@ApiOperation(value = "获取分销推广-朋友圈生效列表", notes = "分页获取分销推广-朋友圈列表")
	public ServerResponseEntity<PageVO<DistributionMoments>> pageEffect(@Valid PageDTO pageDTO, DistributionMomentsDTO dto) {
		dto.setQueryStoreId(AuthUserContext.get().getStoreId());
		log.info("朋友圈查询门店ID：{},{}",dto.getQueryStoreId(),AuthUserContext.get().getUserId());
		dto.setDistributionType(1);
		PageVO<DistributionMoments> distributionMomentsPage = distributionMomentsService.pageEffect(pageDTO, dto);
		return ServerResponseEntity.success(distributionMomentsPage);
	}
}
