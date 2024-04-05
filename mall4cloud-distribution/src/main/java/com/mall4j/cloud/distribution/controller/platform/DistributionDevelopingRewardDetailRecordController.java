package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetailRecord;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardDetailRecordService;
import com.mall4j.cloud.distribution.vo.DistributionDevelopingRewardDetailRecordVO;
import com.mall4j.cloud.distribution.dto.DistributionDevelopingRewardDetailRecordDTO;

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
 * 分销推广-发展奖励发展明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 21:39:02
 */
@RestController("platformDistributionDevelopingRewardDetailRecordController")
@RequestMapping("/p/distribution_developing_reward_detail_record")
@Api(tags = "平台端-分销推广-发展奖励发展明细")
public class DistributionDevelopingRewardDetailRecordController {

    @Autowired
    private DistributionDevelopingRewardDetailRecordService distributionDevelopingRewardDetailRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-发展奖励发展明细列表", notes = "分页获取分销推广-发展奖励发展明细列表")
	public ServerResponseEntity<PageVO<DistributionDevelopingRewardDetailRecord>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionDevelopingRewardDetailRecord> distributionDevelopingRewardDetailRecordPage = distributionDevelopingRewardDetailRecordService.page(pageDTO);
		return ServerResponseEntity.success(distributionDevelopingRewardDetailRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-发展奖励发展明细", notes = "根据id获取分销推广-发展奖励发展明细")
    public ServerResponseEntity<DistributionDevelopingRewardDetailRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionDevelopingRewardDetailRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-发展奖励发展明细", notes = "保存分销推广-发展奖励发展明细")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionDevelopingRewardDetailRecordDTO distributionDevelopingRewardDetailRecordDTO) {
        DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord = mapperFacade.map(distributionDevelopingRewardDetailRecordDTO, DistributionDevelopingRewardDetailRecord.class);
        distributionDevelopingRewardDetailRecord.setId(null);
        distributionDevelopingRewardDetailRecordService.save(distributionDevelopingRewardDetailRecord);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-发展奖励发展明细", notes = "更新分销推广-发展奖励发展明细")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionDevelopingRewardDetailRecordDTO distributionDevelopingRewardDetailRecordDTO) {
        DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord = mapperFacade.map(distributionDevelopingRewardDetailRecordDTO, DistributionDevelopingRewardDetailRecord.class);
        distributionDevelopingRewardDetailRecordService.update(distributionDevelopingRewardDetailRecord);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-发展奖励发展明细", notes = "根据分销推广-发展奖励发展明细id删除分销推广-发展奖励发展明细")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionDevelopingRewardDetailRecordService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
