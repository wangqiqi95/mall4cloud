package com.mall4j.cloud.api.group.feign;

import com.mall4j.cloud.api.group.feign.dto.OpenCommodityDTO;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 限时折扣
 *
 * @luzhengxiang
 * @create 2022-03-12 3:58 PM
 **/
@FeignClient(value = "mall4cloud-group",contextId ="timeDiscount")
public interface TimeDiscountFeignClient {

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/convertActivityPrice")
    ServerResponseEntity<List<TimeDiscountActivityVO>> convertActivityPrice(@RequestBody TimeDiscountActivityDTO params);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/convertActivityPricesNoFilter")
    ServerResponseEntity<List<TimeDiscountActivityVO>> convertActivityPricesNoFilter(@RequestBody TimeDiscountActivityDTO params);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/currentActivityBySpuId")
    ServerResponseEntity<List<TimeDiscountActivityVO>> currentActivityBySpuId(@RequestBody TimeDiscountActivityDTO params);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/discount/getOpenCommoditys")
    ServerResponseEntity<List<Long>> getOpenCommoditys(@RequestBody OpenCommodityDTO openCommodityDTO);
}
