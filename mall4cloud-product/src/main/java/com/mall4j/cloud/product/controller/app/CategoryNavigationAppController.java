package com.mall4j.cloud.product.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.CategoryNavigationSpuRelationDTO;
import com.mall4j.cloud.product.dto.CategoryNavigationStatusDTO;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.model.CategoryNavigation;
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
@RequestMapping("/ma/category-navigation")
@Api(tags = "app-分类导航")
public class CategoryNavigationAppController {

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

    @ApiOperation(value = "查询分类下的商品", notes = "查询分类下的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "storeId", value = "门店", required = false, dataType = "int", paramType = "query"),
    })
    @GetMapping("/page_spu")
    public ServerResponseEntity<PageVO<SpuAppPageVO>> pageSpu(@Valid SpuPageSearchDTO spuPageSearchDTO,
                                                              @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId,
                                                              Long categoryId) {
        PageVO<SpuAppPageVO> pageVO = categoryNavigationService.appPageSpu(spuPageSearchDTO, categoryId, storeId);
        return ServerResponseEntity.success(pageVO);
    }

}
