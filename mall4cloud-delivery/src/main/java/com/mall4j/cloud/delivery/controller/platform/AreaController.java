package com.mall4j.cloud.delivery.controller.platform;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.dto.AreaDTO;
import com.mall4j.cloud.delivery.model.Area;
import com.mall4j.cloud.delivery.service.AreaService;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 省市区地区信息
 *
 * @author YXF
 * @date 2020-11-25 14:48:52
 */
@RestController("platformAreaController")
@RequestMapping("/p/area")
@Api(tags = "店铺-地区信息")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Autowired
	private MapperFacade mapperFacade;

    @PostMapping
    @ApiOperation(value = "保存省市区地区信息", notes = "保存省市区地区信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody AreaDTO areaDTO) {
        Area area = mapperFacade.map(areaDTO, Area.class);
        area.setAreaId(null);
        // 查询相同上级的当前地区名称数量
        Integer count = areaService.countByNameAndParentId(areaDTO.getAreaName(),areaDTO.getLevel(),areaDTO.getParentId());
        if(count > 0){
            throw new LuckException("同一上级的当前地区名称已经存在");
        }
        areaService.save(area);
        areaService.removeAllCache(area.getParentId());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新省市区地区信息", notes = "更新省市区地区信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AreaDTO areaDTO) {
        Area area = mapperFacade.map(areaDTO, Area.class);
        AreaVO dbArea = areaService.getByAreaId(area.getAreaId());
        // 查询相同上级的当前地区名称数量
        Integer count = areaService.countByNameAndParentId(areaDTO.getAreaName(), areaDTO.getLevel(), areaDTO.getParentId());
        if(count > 0){
            throw new LuckException("同一上级的当前地区名称已经存在");
        }
        areaService.update(area);
        areaService.removeAllCache(dbArea.getParentId());
        areaService.removeAreaCacheByParentId(area.getParentId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除省市区地区信息", notes = "根据省市区地区信息id删除省市区地区信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long areaId) {
        AreaVO dbArea = areaService.getByAreaId(areaId);
        areaService.deleteById(areaId);
        areaService.removeAllCache(dbArea.getParentId());
        return ServerResponseEntity.success();
    }
}
