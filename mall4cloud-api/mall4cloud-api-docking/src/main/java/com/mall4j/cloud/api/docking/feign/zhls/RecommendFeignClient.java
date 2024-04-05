package com.mall4j.cloud.api.docking.feign.zhls;

import com.mall4j.cloud.api.docking.dto.zhls.product.BaseProductRespDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.CategoriesReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.SaveSkuDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.validation.AddSku;
import com.mall4j.cloud.api.docking.dto.zhls.product.validation.UpdateSku;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.BaseRecommendDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetRespDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mall4cloud-docking",contextId = "recommend")
public interface RecommendFeignClient {

	/**
	 * 添加及修改商品类目
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product_categories/add")
	BaseProductRespDto<Void> productCategoriesAdd(@RequestBody @Validated CategoriesReqDto reqDto);

	/**
	 * 添加sku到商品池
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku_info/add")
	BaseProductRespDto<Void>  addSkuInfo(@RequestBody @Validated(AddSku.class) SaveSkuDto reqDto);

	/**
	 * 修改商品池sku
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku_info/update")
	BaseProductRespDto<Void>  updateSkuInfo(@RequestBody @Validated(UpdateSku.class) SaveSkuDto reqDto);


	/**
	 * 获取推荐商品
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/recommend/get")
	BaseRecommendDto<RecommendGetRespDto> recommendGet(@RequestBody @Validated RecommendGetReqDto reqDto);


}
