package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * @author gmq
 */
@FeignClient(value = "mall4cloud-platform", contextId = "tztag")
public interface TzTagFeignClient {


    /**
     *
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tag/listTagByStaffId")
    ServerResponseEntity<List<TzTagDetailVO>> listTagByStaffId(@RequestParam("staffId") Long staffId);


}
