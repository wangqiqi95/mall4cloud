package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.service.DistributionMomentsService;
import com.mall4j.cloud.distribution.vo.DistributionMomentsVO;
import com.mall4j.cloud.distribution.dto.DistributionMomentsDTO;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("platformDistributionMomentsController")
@RequestMapping("/p/distribution_moments")
@Api(tags = "平台端-分销推广-朋友圈")
public class DistributionMomentsController {

    @Autowired
    private DistributionMomentsService distributionMomentsService;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-朋友圈列表", notes = "分页获取分销推广-朋友圈列表")
	public ServerResponseEntity<PageVO<DistributionMomentsVO>> page(@Valid PageDTO pageDTO, DistributionMomentsDTO dto) {
		PageVO<DistributionMomentsVO> distributionMomentsPage = distributionMomentsService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionMomentsPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-朋友圈", notes = "根据id获取分销推广-朋友圈")
    public ServerResponseEntity<DistributionMomentsDTO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionMomentsService.getMomentsById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-朋友圈", notes = "保存分销推广-朋友圈")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionMomentsDTO dto) {
        distributionMomentsService.save(dto);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-朋友圈", notes = "更新分销推广-朋友圈")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionMomentsDTO dto) {
        distributionMomentsService.update(dto);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-朋友圈", notes = "根据分销推广-朋友圈id删除分销推广-朋友圈")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionMomentsService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "批量更新朋友圈状态", notes = "批量更新朋友圈状态")
    @PostMapping("/updateStatusBatch")
    public ServerResponseEntity<Void> updateStatusBatch(@RequestBody DistributionMomentsDTO dto){
	    distributionMomentsService.updateStatusBatch(dto.getIds(), dto.getStatus());
	    return ServerResponseEntity.success();
    }

    @ApiOperation(value = "朋友圈置顶", notes = "朋友圈置顶")
    @PostMapping("/momentsTop")
    public ServerResponseEntity<Void> momentsTop(@RequestBody DistributionMomentsDTO dto){
	    distributionMomentsService.momentsTop(dto.getId(), dto.getTop());
	    return ServerResponseEntity.success();
    }

}
