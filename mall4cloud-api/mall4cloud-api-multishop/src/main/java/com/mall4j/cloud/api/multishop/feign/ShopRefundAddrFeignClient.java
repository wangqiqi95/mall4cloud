package com.mall4j.cloud.api.multishop.feign;

import com.mall4j.cloud.api.multishop.vo.ShopRefundAddrVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FrozenWatermelon
 * @date 2020/03/09
 */
@FeignClient(value = "mall4cloud-multishop",contextId = "shopRefundAddr")
public interface ShopRefundAddrFeignClient {


    /**
     * 获取店铺退货地址
     * @param refundAddrId 店铺id
     * @return 店铺退货地址
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/shopRefundAddr/getShopRefundAddrByRefundAddrId")
    ServerResponseEntity<ShopRefundAddrVO> getShopRefundAddrByRefundAddrId(@RequestParam("refundAddrId") Long refundAddrId);

    /**
     * 获取官店退货地址
     * @return 店铺退货地址
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopRefundAddr/getMainRefundAddr")
    ServerResponseEntity<ShopRefundAddrVO> getMainRefundAddr();

}
