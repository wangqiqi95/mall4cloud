package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.CategoryNavigationSpuRelationDTO;
import com.mall4j.cloud.product.dto.CategoryNavigationSpuUpdateDTO;
import com.mall4j.cloud.product.dto.CategoryNavigationStatusDTO;
import com.mall4j.cloud.product.model.CategoryNavigation;
import com.mall4j.cloud.product.model.CategoryNavigationSpuRelation;
import com.mall4j.cloud.product.service.CategoryNavigationService;
import com.mall4j.cloud.product.vo.CategoryNavigationVO;
import com.mall4j.cloud.product.vo.CategorySpuVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @date 2023/6/9
 */
@RestController()
@RequestMapping("/mp/category-navigation")
@Api(tags = "admin-分类导航")
public class CategoryNavigationController {

    @Autowired
    private CategoryNavigationService categoryNavigationService;

    @Autowired
    private MapperFacade mapperFacade;

    @ApiOperation(value = "返回所有分类", notes = "返回所有分类")
    @GetMapping("/all")
    public ServerResponseEntity<List<CategoryNavigationVO>> listAll(){
        List<CategoryNavigationVO> categoryNavigationVOList = categoryNavigationService.listAllOrder();
        return ServerResponseEntity.success(categoryNavigationVOList);
    }

    @ApiOperation(value = "返回单个", notes = "返回单个")
    @GetMapping("/{categoryId}")
    public ServerResponseEntity<CategoryNavigationVO> get(@PathVariable(value = "categoryId")Long categoryId){
        CategoryNavigation categoryNavigation = categoryNavigationService.getById(categoryId);
        CategoryNavigationVO vo = mapperFacade.map(categoryNavigation, CategoryNavigationVO.class);
        return ServerResponseEntity.success(vo);
    }

    @ApiOperation(value = "新增分类", notes = "分类导航")
    @PostMapping()
    public ServerResponseEntity<Void> save(@RequestBody @Valid CategoryNavigationVO categoryNavigationVO){
        categoryNavigationService.saveCategory(categoryNavigationVO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "修改分类", notes = "修改分类")
    @PutMapping()
    public ServerResponseEntity<Void> update(@RequestBody CategoryNavigationVO categoryNavigationVO){
        categoryNavigationService.updateCategory(categoryNavigationVO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "启用或禁用分类", notes = "启用或禁用分类")
    @PostMapping("/enable_or_disable")
    public ServerResponseEntity<Void> enableOrDisable(@RequestBody CategoryNavigationStatusDTO categoryNavigationStatusDTO){
        categoryNavigationService.enableOrDisable(categoryNavigationStatusDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "删除分类", notes = "删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "int", paramType = "query"),
    })
    @DeleteMapping("/{categoryId}")
    public ServerResponseEntity<Void> delete(@PathVariable(value = "categoryId") Long categoryId){
        categoryNavigationService.removeCategory(categoryId);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "添加商品", notes = "添加商品")
    @PostMapping("/add_spu")
    public ServerResponseEntity<Void> addSpu(@RequestBody CategoryNavigationSpuRelationDTO categoryNavigationSpuRelationDTO){
        categoryNavigationService.addSpu(categoryNavigationSpuRelationDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "移除商品", notes = "移除商品")
    @DeleteMapping("/remove_spu")
    public ServerResponseEntity<Void> removeSpu(@RequestBody CategoryNavigationSpuRelationDTO categoryNavigationSpuRelationDTO) {
        categoryNavigationService.removeSpu(categoryNavigationSpuRelationDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "查询分类下的商品", notes = "查询分类下的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "storeId", value = "门店", required = false, dataType = "int", paramType = "query"),
    })
    @GetMapping("/page_spu")
    public ServerResponseEntity<PageVO<CategorySpuVO>> pageSpu(@Valid PageDTO pageDTO,
                                                           @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId,
                                                           Long categoryId) {
        PageVO<CategorySpuVO> pageVO = categoryNavigationService.pageSpu(pageDTO, categoryId, storeId);
        return ServerResponseEntity.success(pageVO);
    }

    @ApiOperation(value = "查询商品下的分类", notes = "查询商品下的分类")
    @GetMapping("/spu_category")
    public ServerResponseEntity<List<CategoryNavigationVO>> listServerResponseEntity(@RequestParam Long spuId) {
        List<CategoryNavigationVO> list = categoryNavigationService.listCategoryBySpuId(spuId);
        return ServerResponseEntity.success(list);
    }

    @ApiOperation(value = "修改商品所属分类", notes = "修改商品所属分类")
    @PutMapping("/spu_category")
    public ServerResponseEntity<Void> updateSpuCategory(@RequestBody CategoryNavigationSpuUpdateDTO categoryNavigationSpuUpdateDTO) {
        categoryNavigationService.updateSpuCategory(categoryNavigationSpuUpdateDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "查询级联分类(带缓存)", notes = "查询级联分类(带缓存)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "主分类ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isCascade", value = "是否级联查询 1是 0否", required = false, dataType = "int", paramType = "query"),
    })
    @GetMapping("/cache_list")
    public ServerResponseEntity<List<CategoryNavigationVO>> listCacheCategory(@RequestParam Long categoryId,
                                                                              @RequestParam(defaultValue = "1") Integer isCascade) {
        List<CategoryNavigationVO> categoryNavigationVOList = categoryNavigationService.listCacheCascadeCategory(categoryId, isCascade);
        return ServerResponseEntity.success(categoryNavigationVOList);
    }
}
