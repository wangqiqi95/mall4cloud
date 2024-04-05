package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.vo.GetTagBySpuIdVO;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hwy
 * @date 2022/03/21
 */
@FeignClient(value = "mall4cloud-product",contextId = "tag")
public interface TagActivityClient {
    /**
     * 通过spuId获取配置的商品标签图标信息
     * @param spuId spuId
     * @param storeId storeId
     * @return 标签信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tag/getTagBySpuId")
    ServerResponseEntity<GetTagBySpuIdVO> getTagBySpuId(@RequestParam("spuId") Long spuId, @RequestParam(value = "storeId",required = false) Long storeId);
}
