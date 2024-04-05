package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.delivery.model.Transfee;
import com.mall4j.cloud.delivery.service.TransfeeService;
import com.mall4j.cloud.delivery.vo.TransfeeVO;
import com.mall4j.cloud.delivery.dto.TransfeeDTO;

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
 * 运费项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("appTransfeeController")
@RequestMapping("/transfee")
@Api(tags = "运费项")
public class TransfeeController {

    @Autowired
    private TransfeeService transfeeService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取运费项列表", notes = "分页获取运费项列表")
	public ServerResponseEntity<PageVO<TransfeeVO>> page(@Valid PageDTO pageDTO) {
		PageVO<TransfeeVO> transfeePage = transfeeService.page(pageDTO);
		return ServerResponseEntity.success(transfeePage);
	}

	@GetMapping
    @ApiOperation(value = "获取运费项", notes = "根据transfeeId获取运费项")
    public ServerResponseEntity<TransfeeVO> getByTransfeeId(@RequestParam Long transfeeId) {
        return ServerResponseEntity.success(transfeeService.getByTransfeeId(transfeeId));
    }

    @PostMapping
    @ApiOperation(value = "保存运费项", notes = "保存运费项")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TransfeeDTO transfeeDTO) {
        Transfee transfee = mapperFacade.map(transfeeDTO, Transfee.class);
        transfee.setTransfeeId(null);
        transfeeService.save(transfee);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新运费项", notes = "更新运费项")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TransfeeDTO transfeeDTO) {
        Transfee transfee = mapperFacade.map(transfeeDTO, Transfee.class);
        transfeeService.update(transfee);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除运费项", notes = "根据运费项id删除运费项")
    public ServerResponseEntity<Void> delete(@RequestParam Long transfeeId) {
        transfeeService.deleteById(transfeeId);
        return ServerResponseEntity.success();
    }
}
