package com.mall4j.cloud.api.feign;

import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.ProductSearchLimitDTO;
import com.mall4j.cloud.common.product.vo.search.ProductSearchVO;
import com.mall4j.cloud.common.product.vo.search.SpuAdminVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品搜索feign连接
 * @author YXF
 * @date 2020/12/07
 */
@FeignClient(value = "mall4cloud-search",contextId = "searchSpu")
public interface SearchSpuFeignClient {

    /**
     * 商品搜索
     * @param productSearch
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/search")
    ServerResponseEntity<EsPageVO<ProductSearchVO>> search(@RequestBody ProductSearchDTO productSearch);

    /**
     * 商品搜索-通用查询
     * @param productSearch
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/commonSearch")
    ServerResponseEntity<EsPageVO<SpuCommonVO>> commonSearch(@RequestBody ProductSearchDTO productSearch);

    /**
     * 根据spuId列表， 获取spu列表信息
     * @param spuIds 商品id列表
     * @return 商品列表
     */
//    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/getSpusBySpuIds")
//    ServerResponseEntity<List<SpuSearchVO>> listSpuBySpuIds(@RequestParam("spuIds") List<Long> spuIds);
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/getSpusBySpuIds")
    ServerResponseEntity<List<SpuSearchVO>> listSpuBySpuIds(@RequestBody List<Long> spuIds);

    /**
     * 根据店铺，获取商品分页信息
     * @param pageNum 分页数
     * @param pageSize 每页大小
     * @param shopId 店铺id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/spuPage")
    ServerResponseEntity<EsPageVO<ProductSearchVO>> spuPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("shopId") Long shopId);

    /**
     * 后台商品搜索
     * @param productSearch 搜索筛选条件
     * @return 商品分页信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/adminPage")
    ServerResponseEntity<EsPageVO<SpuAdminVO>> adminPage(@RequestBody ProductSearchDTO productSearch);

    /**
     * 获取指定数量的的spu列表
     * @param productSearchLimitDTO
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/limitSizeListByShopIds")
    ServerResponseEntity<List<SpuSearchVO>> limitSpuList(@RequestBody ProductSearchLimitDTO productSearchLimitDTO);

    /**
     * 根据id列表，获取每个id指定数量的spu列表
     * @param productSearchLimitDTO
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/limitSpuMap")
    ServerResponseEntity<Map<Long, List<SpuSearchVO>>> limitSpuMap(@RequestBody ProductSearchLimitDTO productSearchLimitDTO);

    /**
     * 获取不为删除状态的商品列表
     * @param spuIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/listOfAdmin")
    ServerResponseEntity<List<SpuSearchVO>> listNotDeleteSpu(@RequestParam("spuIds") List<Long> spuIds);

    /**
     * 商品列表（仅包含商品id，更新时间）
     * @param productSearchDTO
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/simpleList")
    ServerResponseEntity<List<SpuSearchVO>> simpleList(@RequestBody ProductSearchDTO productSearchDTO);
}
