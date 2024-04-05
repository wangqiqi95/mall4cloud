package com.mall4j.cloud.api.crm.feign;

import com.mall4j.cloud.api.crm.dto.CrmCouponOverdueQuery;
import com.mall4j.cloud.api.crm.dto.DOInstance50001388DTO;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mall4cloud-crm",contextId = "crmcoupon")
public interface CrmCouponClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/queryHasCrmCouponUsers")
    ServerResponseEntity<List<String>> queryHasCrmCouponUsers(@RequestBody QueryHasCouponUsersRequest queryHasCouponUsersRequest);


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/crmCouponOverdue")
    ServerResponseEntity<List<DOInstance50001388DTO>> crmCouponOverdue(@RequestBody CrmCouponOverdueQuery crmCouponOverdueQuery);

}
