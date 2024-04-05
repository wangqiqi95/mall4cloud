package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionDevelopingRewardDTO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingReward;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销推广-发展奖励
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
@RestController("platformDistributionDevelopingRewardController")
@RequestMapping("/p/distribution_developing_reward")
@Api(tags = "平台端-分销推广-发展奖励")
public class DistributionDevelopingRewardController {

    @Autowired
    private DistributionDevelopingRewardService distributionDevelopingRewardService;


	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-发展奖励列表", notes = "分页获取分销推广-发展奖励列表")
	public ServerResponseEntity<PageVO<DistributionDevelopingReward>> page(@Valid PageDTO pageDTO, DistributionDevelopingRewardDTO dto) {
		PageVO<DistributionDevelopingReward> distributionDevelopingRewardPage = distributionDevelopingRewardService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionDevelopingRewardPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-发展奖励", notes = "根据id获取分销推广-发展奖励")
    public ServerResponseEntity<DistributionDevelopingRewardDTO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionDevelopingRewardService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-发展奖励", notes = "保存分销推广-发展奖励")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionDevelopingRewardDTO distributionDevelopingRewardDTO) {
        distributionDevelopingRewardService.save(distributionDevelopingRewardDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-发展奖励", notes = "更新分销推广-发展奖励")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionDevelopingRewardDTO distributionDevelopingRewardDTO) {
        distributionDevelopingRewardService.update(distributionDevelopingRewardDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-发展奖励", notes = "根据分销推广-发展奖励id删除分销推广-发展奖励")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionDevelopingRewardService.deleteById(id);
        return ServerResponseEntity.success();
    }

}
