package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.PlatformCacheNames;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.TentacleContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZengFanChang
 * @Date 2021/12/18
 */
@RestController
public class TentacleContentFeignController implements TentacleContentFeignClient {

    @Autowired
    private TentacleContentService tentacleContentService;

    @Override
    public ServerResponseEntity<TentacleContentVO> findOrCreateByContent(TentacleContentDTO tentacleContent) {
        return ServerResponseEntity.success(tentacleContentService.findOrCreateByContent(tentacleContent));
    }

    @Override
    public ServerResponseEntity<TentacleContentVO> findByTentacleNo(String tentacleNo){
        return ServerResponseEntity.success(tentacleContentService.findByTentacleNo(tentacleNo));
    }

}
