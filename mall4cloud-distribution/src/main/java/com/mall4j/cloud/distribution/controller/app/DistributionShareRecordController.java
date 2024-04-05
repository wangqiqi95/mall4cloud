package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.distribution.model.DistributionShareRecord;
import com.mall4j.cloud.distribution.service.DistributionShareRecordService;
import com.mall4j.cloud.distribution.vo.DistributionShareRecordVO;
import com.mall4j.cloud.distribution.dto.DistributionShareRecordDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 分享推广-分享记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@RestController("appDistributionShareRecordController")
@RequestMapping("/distribution_share_record")
@Api(tags = "分享推广-威客分享记录")
public class DistributionShareRecordController {

    @Autowired
    private DistributionShareRecordService distributionShareRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分享推广-分享记录列表", notes = "分页获取分享推广-分享记录列表")
	public ServerResponseEntity<PageVO<DistributionShareRecord>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionShareRecord> distributionShareRecordPage = distributionShareRecordService.page(pageDTO);
		return ServerResponseEntity.success(distributionShareRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分享推广-分享记录", notes = "根据id获取分享推广-分享记录")
    public ServerResponseEntity<DistributionShareRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionShareRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分享推广-分享记录", notes = "保存分享推广-分享记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionShareRecordDTO distributionShareRecordDTO) {
        DistributionShareRecord distributionShareRecord = mapperFacade.map(distributionShareRecordDTO, DistributionShareRecord.class);
        distributionShareRecordService.save(distributionShareRecord);
        return ServerResponseEntity.success();
    }

}
