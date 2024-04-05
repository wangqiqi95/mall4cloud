package com.mall4j.cloud.api.group.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author baiqingtao
 * @date 2023/5/26
 */
@FeignClient(value = "mall4cloud-group",contextId ="groupQuestionnaire")
public interface GroupQuestionnaireFeignClient {


    /**
     * 检查用户是否有问卷资格
     * @param id 问卷ID
     * @param storeId 门店ID
     * @param userId 用户ID
     * @return response 如果用户有资格则返回 TRUE, 否则返回异常状态
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/group/questionnaire/get/info")
    ServerResponseEntity<Boolean> checkUserAuth(@RequestParam(name = "id") Long id,
                                                @RequestParam(name = "storeId") Long storeId,
                                                @RequestParam(name = "userId") Long userId);
}
