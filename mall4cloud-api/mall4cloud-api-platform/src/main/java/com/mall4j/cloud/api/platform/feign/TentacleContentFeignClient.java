package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ZengFanChang
 * @Date 2021/12/18
 */
@FeignClient(value = "mall4cloud-platform", contextId = "tentacleContent")
public interface TentacleContentFeignClient {

    /**
     * 创建或者查询触点对象
     * C端  分享之前调用生成触点对象
     * C端  A分享给B , B访问时  生成触点对象
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/findOrCreateByContent")
    ServerResponseEntity<TentacleContentVO> findOrCreateByContent(@RequestBody TentacleContentDTO tentacleContent);


    /**
     * 根据触点号查询触点信息
     *
     * @param tentacleNo 触点号
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/findByTentacleNo")
    ServerResponseEntity<TentacleContentVO> findByTentacleNo(@RequestParam("tentacleNo") String tentacleNo);

}
