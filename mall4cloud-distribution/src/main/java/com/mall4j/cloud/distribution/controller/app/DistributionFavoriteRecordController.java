package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionFavoriteRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionFavoriteRecord;
import com.mall4j.cloud.distribution.service.DistributionFavoriteRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分享推广-收藏记录
 *
 * @author gww
 * @date 2022-01-30 02:23:13
 */
@RestController("appDistributionFavoriteRecordController")
@RequestMapping("/distribution_favorite_record")
@Api(tags = "分享推广-收藏记录")
public class DistributionFavoriteRecordController {

    @Autowired
    private DistributionFavoriteRecordService distributionFavoriteRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分享推广-收藏记录列表", notes = "分页获取分享推广-收藏记录列表")
	public ServerResponseEntity<PageVO<DistributionFavoriteRecord>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionFavoriteRecord> distributionFavoriteRecordPage = distributionFavoriteRecordService.page(pageDTO);
		return ServerResponseEntity.success(distributionFavoriteRecordPage);
	}

    @PostMapping
    @ApiOperation(value = "保存分享推广-收藏记录", notes = "保存分享推广-收藏记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionFavoriteRecordDTO distributionFavoriteRecordDTO) {
        DistributionFavoriteRecord distributionFavoriteRecord = mapperFacade.map(distributionFavoriteRecordDTO, DistributionFavoriteRecord.class);
        distributionFavoriteRecordService.save(distributionFavoriteRecord);
        return ServerResponseEntity.success();
    }
}
