package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.WXShortLinkDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 短链工具
 */
@FeignClient(value = "mall4cloud-biz",contextId = "shortLink")
public interface ShortLinkClient {

    /*
    * 生成短链
    * */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/wx/shortLink")
    ServerResponseEntity<String> generateShortLink(@RequestBody WXShortLinkDTO dto);
}
