package com.mall4j.cloud.api.multishop.feign;

import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.bo.ShopSimpleBO;
import com.mall4j.cloud.api.multishop.vo.ShopDetailVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-multishop",contextId = "shopDetail")
public interface ShopDetailFeignClient {

    /**
     * 根据店铺id获取店铺信息
     * @param shopId 店铺id
     * @return 店铺信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopByShopId")
    ServerResponseEntity<EsShopDetailBO> getShopByShopId(@RequestParam("shopId") Long shopId);

    /**
     * 根据手机号获取存在店铺的数量
     * @param mobile 手机号
     * @return 数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/countByMobile")
    ServerResponseEntity<Integer> countByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据店铺id列表， 获取店铺列表信息
     * @param shopIds 店铺id列表
     * @return 店铺列表信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/listByShopIds")
    ServerResponseEntity<List<ShopDetailVO>> listByShopIds(@RequestParam("shopIds") List<Long> shopIds);

    /**
     * 获取店铺信息及扩展信息
     * @param shopId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopExtension")
    ServerResponseEntity<EsShopDetailBO> shopExtensionData(@RequestParam("shopId") Long shopId);

    /**
     * 获取店铺信息及扩展信息
     * @param shopIds 店铺ids
     * @param shopName 店铺名称
     * @return 店铺信息列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopDetailByShopIdAndShopName")
    ServerResponseEntity<List<ShopDetailVO>> getShopDetailByShopIdAndShopName(@RequestParam("shopIds") List<Long> shopIds,
                                                                              @RequestParam(value = "shopName",defaultValue = "") String shopName);

    /**
     * 根据参数获取店铺id列表
     * @param shopSimpleBO
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/listSimple")
    ServerResponseEntity<List<ShopSimpleBO>> listSimple(@RequestBody ShopSimpleBO shopSimpleBO);
}
