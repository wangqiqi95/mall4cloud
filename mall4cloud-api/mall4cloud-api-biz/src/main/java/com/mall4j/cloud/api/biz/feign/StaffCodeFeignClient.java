package com.mall4j.cloud.api.biz.feign;


import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.File;


/**
 * @author gmq
 * @date 2022/08/25
 */
@FeignClient(value = "mall4cloud-biz",contextId = "StaffCodeFegin")
public interface StaffCodeFeignClient {

}
