package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.MaterialTypeDTO;
import com.mall4j.cloud.biz.model.cp.MaterialType;
import com.mall4j.cloud.biz.service.cp.MaterialTypeService;
import com.mall4j.cloud.biz.vo.cp.MaterialTypeVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 素材分类表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@RequiredArgsConstructor
@RestController("platformMaterialTypeController")
@RequestMapping("/p/cp/material_type")
@Api(tags = "素材分类表")
public class MaterialTypeController {

    private final MaterialTypeService materialTypeService;

	private final MapperFacade mapperFacade;

//	@GetMapping("/listParent")
//	@ApiOperation(value = "查询全部的一级分类", notes = "查询全部的一级分类")
//	public ServerResponseEntity<List<MaterialType>> listParent() {
//		return ServerResponseEntity.success(materialTypeService.listParent());
//	}
//    @GetMapping("/listChildren")
//    @ApiOperation(value = "查询全部的二级分类", notes = "查询全部的二级分类")
//    public ServerResponseEntity<List<MaterialType>> listChildren(@RequestParam Long id) {
//        return ServerResponseEntity.success(materialTypeService.listChildren(id));
//    }
    @GetMapping("/listParentContainChildren")
    @ApiOperation(value = "返回一级类型 包含 子类型的list", notes = "返回一级类型 包含 子类型的list")
    public ServerResponseEntity<List<MaterialTypeVO>> listParentContainChildren() {
        return ServerResponseEntity.success(materialTypeService.listParentContainChildren());
    }

	@GetMapping
    @ApiOperation(value = "获取素材分类表", notes = "根据id获取素材分类表")
    public ServerResponseEntity<MaterialType> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(materialTypeService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存素材分类表", notes = "保存素材分类表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody MaterialTypeDTO materialTypeDTO) {
        MaterialType materialType = mapperFacade.map(materialTypeDTO, MaterialType.class);
        materialType.setCreateTime(new Date());
        materialType.setUpdateTime(materialType.getCreateTime());
        materialType.setFlag(StatusType.WX.getCode());
        materialType.setStatus(StatusType.YX.getCode());
        materialTypeService.save(materialType);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新素材分类表", notes = "更新素材分类表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody MaterialTypeDTO materialTypeDTO) {
        MaterialType materialType = mapperFacade.map(materialTypeDTO, MaterialType.class);
        materialType.setUpdateTime(new Date());
        materialTypeService.update(materialType);
        return ServerResponseEntity.success();
    }

//    @DeleteMapping
//    @ApiOperation(value = "删除素材分类表", notes = "根据素材分类表id删除素材分类表")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        materialTypeService.deleteById(id);
//        return ServerResponseEntity.success();
//    }

    @PostMapping("/deleteMaterialType")
    @ApiOperation(value = "删除素材分类", notes = "删除素材分类信息(逻辑删)")
    public ServerResponseEntity disableMat(@Valid @RequestBody MaterialTypeDTO materialTypeDTO) {
        // 校验数据是否允许删除
        Integer checkMaterial = materialTypeService.checkMaterial(materialTypeDTO);
        if(checkMaterial > 0){
            return ServerResponseEntity.showDefinedMsg(ResponseEnum.SHOW_FAIL, "该分类下仍有素材，无法删除！");
        }
        MaterialType materialType = mapperFacade.map(materialTypeDTO, MaterialType.class);
        materialType.setUpdateTime(new Date());
        materialType.setFlag(1);
        materialTypeService.update(materialType);
        return ServerResponseEntity.success();
    }

}
