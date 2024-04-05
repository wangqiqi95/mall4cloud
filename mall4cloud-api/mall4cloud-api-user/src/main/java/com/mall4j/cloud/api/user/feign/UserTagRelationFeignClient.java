package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mall4cloud-user",contextId = "user-tag-relation")
public interface UserTagRelationFeignClient {

    /**
     * 校验标签集合中的标签是否属于用户
     * @param tagIdList 标签id集合
     * @return 标签列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/check/user/tag/list")
    ServerResponseEntity<List<Long>> checkUserTagRelationByTagIdList(@RequestParam("tagIdList") List<Long> tagIdList);

}
