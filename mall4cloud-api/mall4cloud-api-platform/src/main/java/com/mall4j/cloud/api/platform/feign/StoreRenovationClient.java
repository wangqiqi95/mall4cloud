package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.dto.RenovationBurialPointDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 店铺装修Feign接口
 */
@FeignClient(value = "mall4cloud-platform",contextId = "storeRenovation")
public interface StoreRenovationClient {

    /*
    * 修改店铺装修记录上的埋点统计
    * */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/store/renovation")
    ServerResponseEntity<Void> editRenovationBurialPoint(RenovationBurialPointDTO dto);

}
