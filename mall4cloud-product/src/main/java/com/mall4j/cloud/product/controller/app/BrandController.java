package com.mall4j.cloud.product.controller.app;

import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.product.dto.ProductSearchLimitDTO;
import com.mall4j.cloud.common.product.vo.app.BrandAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.product.service.BrandService;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@RestController("appBrandController")
@RequestMapping("/ua/brand")
@Api(tags = "app-品牌信息")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @GetMapping("/page")
    @ApiOperation(value = "获取品牌信息列表", notes = "分页获取品牌信息列表")
    public ServerResponseEntity<PageVO<BrandAppVO>> page(@Valid PageDTO pageDTO, BrandDTO brandDTO) {
        brandDTO.setStatus(StatusEnum.ENABLE.value());
        PageVO<BrandAppVO> brandPage = brandService.appPage(pageDTO, brandDTO);
        loadSpuList(brandPage.getList(), brandDTO.getSpuCount());
        return ServerResponseEntity.success(brandPage);
    }


    @GetMapping("/top_brand_list")
    @ApiOperation(value = "置顶品牌列表", notes = "置顶品牌列表")
    public ServerResponseEntity<List<BrandAppVO>> topBrandList() {
        List<BrandAppVO> brandList = brandService.topBrandList(I18nMessage.getLang());
        return ServerResponseEntity.success(brandList);
    }

    @GetMapping("/list_by_category")
    @ApiOperation(value = "分类-推荐品牌信息列表", notes = "分类-推荐品牌信息列表")
    public ServerResponseEntity<List<BrandAppVO>> getTopBrandList(Long categoryId) {
        List<BrandAppVO> brandPage = brandService.listByCategory(categoryId, I18nMessage.getLang());
        return ServerResponseEntity.success(brandPage);
    }

    /**
     * 添加品牌下的商品数据
     * @param brandList
     * @param size
     */
    private void loadSpuList(List<BrandAppVO> brandList, Integer size) {
        if (Objects.isNull(size) || size == 0) {
            return;
        }
        List<Long> brandIds = brandList.stream().map(BrandAppVO::getBrandId).collect(Collectors.toList());
        ProductSearchLimitDTO productSearchLimitDTO = new ProductSearchLimitDTO(size);
        productSearchLimitDTO.setBrandIds(brandIds);
        ServerResponseEntity<List<SpuSearchVO>> response = searchSpuFeignClient.limitSpuList(productSearchLimitDTO);
        if(!Objects.equals(response.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(response.getMsg());
        }
        Map<Long, List<SpuSearchVO>> spuMap = response.getData().stream().collect(Collectors.groupingBy(SpuSearchVO::getBrandId));
        for (BrandAppVO brandVO : brandList) {
            brandVO.setSpuList(spuMap.get(brandVO.getBrandId()));
        }
    }
}
