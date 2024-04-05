package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.delivery.model.Transcity;
import com.mall4j.cloud.delivery.service.TranscityService;
import com.mall4j.cloud.delivery.vo.TranscityVO;
import com.mall4j.cloud.delivery.dto.TranscityDTO;

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
 * 运费项和运费城市关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("appTranscityController")
@RequestMapping("/transcity")
@Api(tags = "运费项和运费城市关联信息")
public class TranscityController {

    @Autowired
    private TranscityService transcityService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取运费项和运费城市关联信息列表", notes = "分页获取运费项和运费城市关联信息列表")
	public ServerResponseEntity<PageVO<TranscityVO>> page(@Valid PageDTO pageDTO) {
		PageVO<TranscityVO> transcityPage = transcityService.page(pageDTO);
		return ServerResponseEntity.success(transcityPage);
	}

	@GetMapping
    @ApiOperation(value = "获取运费项和运费城市关联信息", notes = "根据transcityId获取运费项和运费城市关联信息")
    public ServerResponseEntity<TranscityVO> getByTranscityId(@RequestParam Long transcityId) {
        return ServerResponseEntity.success(transcityService.getByTranscityId(transcityId));
    }

    @PostMapping
    @ApiOperation(value = "保存运费项和运费城市关联信息", notes = "保存运费项和运费城市关联信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TranscityDTO transcityDTO) {
        Transcity transcity = mapperFacade.map(transcityDTO, Transcity.class);
        transcity.setTranscityId(null);
        transcityService.save(transcity);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新运费项和运费城市关联信息", notes = "更新运费项和运费城市关联信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TranscityDTO transcityDTO) {
        Transcity transcity = mapperFacade.map(transcityDTO, Transcity.class);
        transcityService.update(transcity);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除运费项和运费城市关联信息", notes = "根据运费项和运费城市关联信息id删除运费项和运费城市关联信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long transcityId) {
        transcityService.deleteById(transcityId);
        return ServerResponseEntity.success();
    }
}
