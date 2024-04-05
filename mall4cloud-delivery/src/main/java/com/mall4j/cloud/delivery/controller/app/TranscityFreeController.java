package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.delivery.model.TranscityFree;
import com.mall4j.cloud.delivery.service.TranscityFreeService;
import com.mall4j.cloud.delivery.vo.TranscityFreeVO;
import com.mall4j.cloud.delivery.dto.TranscityFreeDTO;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 指定条件包邮城市项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("appTranscityFreeController")
@RequestMapping("/transcity_free")
@Api(tags = "指定条件包邮城市项")
public class TranscityFreeController {

    @Autowired
    private TranscityFreeService transcityFreeService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取指定条件包邮城市项列表", notes = "分页获取指定条件包邮城市项列表")
	public ServerResponseEntity<PageVO<TranscityFreeVO>> page(@Valid PageDTO pageDTO) {
		PageVO<TranscityFreeVO> transcityFreePage = transcityFreeService.page(pageDTO);
		return ServerResponseEntity.success(transcityFreePage);
	}

	@GetMapping
    @ApiOperation(value = "获取指定条件包邮城市项", notes = "根据transcityFreeId获取指定条件包邮城市项")
    public ServerResponseEntity<TranscityFreeVO> getByTranscityFreeId(@RequestParam Long transcityFreeId) {
        return ServerResponseEntity.success(transcityFreeService.getByTranscityFreeId(transcityFreeId));
    }

    @PostMapping
    @ApiOperation(value = "保存指定条件包邮城市项", notes = "保存指定条件包邮城市项")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TranscityFreeDTO transcityFreeDTO) {
        TranscityFree transcityFree = mapperFacade.map(transcityFreeDTO, TranscityFree.class);
        transcityFree.setTranscityFreeId(null);
        transcityFreeService.save(transcityFree);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新指定条件包邮城市项", notes = "更新指定条件包邮城市项")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TranscityFreeDTO transcityFreeDTO) {
        TranscityFree transcityFree = mapperFacade.map(transcityFreeDTO, TranscityFree.class);
        transcityFreeService.update(transcityFree);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除指定条件包邮城市项", notes = "根据指定条件包邮城市项id删除指定条件包邮城市项")
    public ServerResponseEntity<Void> delete(@RequestParam Long transcityFreeId) {
        transcityFreeService.deleteById(transcityFreeId);
        return ServerResponseEntity.success();
    }
}
