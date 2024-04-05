package com.mall4j.cloud.api.biz.feign;


import com.mall4j.cloud.api.biz.vo.WaitMatterCountVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author gmq
 * @date 2022/08/25
 */
@FeignClient(value = "mall4cloud-biz",contextId = "StaffTaskFeignClient")
public interface StaffTaskFeignClient {

    /**
     * 移动端待办事项统计：标签建群、手机号任务
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/staff/waitMatterCount")
    ServerResponseEntity<WaitMatterCountVO> waitMatterCount();

}
