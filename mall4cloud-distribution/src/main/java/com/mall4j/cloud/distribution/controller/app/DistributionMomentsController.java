package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.distribution.model.DistributionMoments;
import com.mall4j.cloud.distribution.service.DistributionMomentsService;
import com.mall4j.cloud.distribution.dto.DistributionMomentsDTO;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("appDistributionMomentsController")
@RequestMapping("/distribution_moments")
@Slf4j
@Api(tags = "分销推广-朋友圈")
public class DistributionMomentsController {

    @Autowired
    private DistributionMomentsService distributionMomentsService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
	private UserFeignClient userFeignClient;

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
		ServerResponseEntity<UserApiVO> responseEntity = userFeignClient.getUserData(AuthUserContext.get().getUserId());
		if (responseEntity.getData() != null && responseEntity.isSuccess()) {
			dto.setQueryStoreId(responseEntity.getData().getStaffStoreId());
		} else {
			dto.setQueryStoreId(1L);
		}
		log.info("微客端朋友圈查询门店ID：{},{}",dto.getQueryStoreId(),AuthUserContext.get().getUserId());
		dto.setDistributionType(2);
		PageVO<DistributionMoments> distributionMomentsPage = distributionMomentsService.pageEffect(pageDTO, dto);
		return ServerResponseEntity.success(distributionMomentsPage);
	}
}
