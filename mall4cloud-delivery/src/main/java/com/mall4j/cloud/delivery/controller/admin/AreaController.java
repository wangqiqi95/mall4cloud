package com.mall4j.cloud.delivery.controller.admin;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.service.AreaService;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 省市区地区信息
 *
 * @author YXF
 * @date 2020-11-25 14:48:52
 */
@RestController("adminAreaController")
@RequestMapping("/mp/area")
@Api(tags = "店铺-地区信息")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/list")
	@ApiOperation(value = "获取省市区地区信息列表", notes = "分页获取省市区地区信息列表")
	public ServerResponseEntity<List<AreaVO>> list() {
        List<AreaVO> list = areaService.list();
        return ServerResponseEntity.success(list);
	}

	@GetMapping
    @ApiOperation(value = "获取省市区地区信息", notes = "根据areaId获取省市区地区信息")
    public ServerResponseEntity<AreaVO> getByAreaId(@RequestParam Long areaId) {
        return ServerResponseEntity.success(areaService.getByAreaId(areaId));
    }

    @GetMapping("/list_by_pid")
    @ApiOperation(value = "通过父级id获取区域列表", notes = "通过父级id获取区域列表")
    public ServerResponseEntity<List<AreaVO>> listByPid(Long pid) {
        List<AreaVO> list = areaService.listByPid(pid);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/area_list_info")
    @ApiOperation(value = "获取省市信息", notes = "获取省市信息")
    public ServerResponseEntity<List<AreaVO>> getAreaListInfo() {
        return ServerResponseEntity.success(areaService.getAreaListInfo());
    }

    @GetMapping("/list_area_of_enable")
    @ApiOperation(value = "获取可用的省市区列表", notes = "获取可用的省市区列表")
    public ServerResponseEntity<List<AreaVO>> listAreaOfEnable() {
	    List<AreaVO> list = areaService.listAreaOfEnable();
        List<AreaVO> res = new ArrayList<>();
        for (AreaVO pArea : list) {
            res.add(pArea);
            for (AreaVO cArea : pArea.getAreas()) {
                res.add(cArea);
                res.addAll(cArea.getAreas());
            }
        }
        return ServerResponseEntity.success(res);
    }
}
