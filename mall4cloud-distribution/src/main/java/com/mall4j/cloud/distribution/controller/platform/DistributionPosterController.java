package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionPosterDTO;
import com.mall4j.cloud.distribution.model.DistributionPoster;
import com.mall4j.cloud.distribution.service.DistributionPosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销推广-推广海报
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
@RestController("platformDistributionPosterController")
@RequestMapping("/p/distribution_poster")
@Api(tags = "平台端-分销推广-推广海报")
public class DistributionPosterController {

    @Autowired
    private DistributionPosterService distributionPosterService;


	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-推广海报列表", notes = "分页获取分销推广-推广海报列表")
	public ServerResponseEntity<PageVO<DistributionPoster>> page(@Valid PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO) {
		PageVO<DistributionPoster> distributionPosterPage = distributionPosterService.page(pageDTO, distributionPosterDTO);
		return ServerResponseEntity.success(distributionPosterPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-推广海报", notes = "根据id获取分销推广-推广海报")
    public ServerResponseEntity<DistributionPoster> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionPosterService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-推广海报", notes = "保存分销推广-推广海报")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionPosterDTO distributionPosterDTO) {
        distributionPosterService.save(distributionPosterDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-推广海报", notes = "更新分销推广-推广海报")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionPosterDTO distributionPosterDTO) {
        distributionPosterService.update(distributionPosterDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-推广海报", notes = "根据分销推广-推广海报id删除分销推广-推广海报")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionPosterService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
