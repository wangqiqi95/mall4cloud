package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.delivery.model.Station;
import com.mall4j.cloud.delivery.service.StationService;
import com.mall4j.cloud.delivery.vo.StationVO;
import com.mall4j.cloud.delivery.dto.StationDTO;

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
 * 自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("appStationController")
@RequestMapping("/station")
@Api(tags = "自提点信息")
public class StationController {

    @Autowired
    private StationService stationService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取自提点信息列表", notes = "分页获取自提点信息列表")
	public ServerResponseEntity<PageVO<StationVO>> page(@Valid PageDTO pageDTO) {
		PageVO<StationVO> stationPage = stationService.page(pageDTO);
		return ServerResponseEntity.success(stationPage);
	}

	@GetMapping
    @ApiOperation(value = "获取自提点信息", notes = "根据stationId获取自提点信息")
    public ServerResponseEntity<StationVO> getByStationId(@RequestParam Long stationId) {
        return ServerResponseEntity.success(stationService.getByStationId(stationId));
    }

    @PostMapping
    @ApiOperation(value = "保存自提点信息", notes = "保存自提点信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody StationDTO stationDTO) {
        Station station = mapperFacade.map(stationDTO, Station.class);
        station.setStationId(null);
        stationService.save(station);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新自提点信息", notes = "更新自提点信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody StationDTO stationDTO) {
        Station station = mapperFacade.map(stationDTO, Station.class);
        stationService.update(station);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除自提点信息", notes = "根据自提点信息id删除自提点信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long stationId) {
        stationService.deleteById(stationId);
        return ServerResponseEntity.success();
    }
}
