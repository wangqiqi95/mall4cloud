package com.mall4j.cloud.docking.feign.zhls;

import com.mall4j.cloud.api.docking.dto.zhls.product.BaseProductRespDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.CategoriesReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.SaveSkuDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.validation.AddSku;
import com.mall4j.cloud.api.docking.dto.zhls.product.validation.UpdateSku;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.BaseRecommendDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetRespDto;
import com.mall4j.cloud.api.docking.feign.zhls.RecommendFeignClient;
import com.mall4j.cloud.docking.service.IZhlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 有数商品推荐
 * @Author axin
 * @Date 2023-03-15 13:54
 **/
@RestController
public class RecommendController implements RecommendFeignClient {

    @Autowired
    private IZhlsService zhlsService;


    @Override
    public BaseProductRespDto<Void> productCategoriesAdd(@RequestBody @Validated CategoriesReqDto reqDto) {
        return zhlsService.productCategoriesAdd(reqDto);
    }

    @Override
    public BaseProductRespDto<Void> addSkuInfo(@RequestBody @Validated(AddSku.class) SaveSkuDto reqDto) {
        return zhlsService.skuInfoAdd(reqDto);
    }

    @Override
    public BaseProductRespDto<Void> updateSkuInfo(@RequestBody @Validated(UpdateSku.class) SaveSkuDto reqDto) {
        return zhlsService.skuInfoUpdate(reqDto);
    }

    @Override
    public BaseRecommendDto<RecommendGetRespDto> recommendGet(@RequestBody @Validated RecommendGetReqDto reqDto) {
        return zhlsService.recommendGet(reqDto);
    }
}
