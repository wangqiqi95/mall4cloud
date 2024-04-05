package com.mall4j.cloud.product.feign;

import com.mall4j.cloud.api.product.feign.TagActivityClient;
import com.mall4j.cloud.api.product.vo.GetTagBySpuIdVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.model.TagActivity;
import com.mall4j.cloud.product.service.TagActivityService;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwy
 * @date 2022/03/21
 */
@RequiredArgsConstructor
@RestController
public class TagActivityFeignController implements TagActivityClient {

    private  final TagActivityService activityService;

    private final  MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<GetTagBySpuIdVO> getTagBySpuId(Long spuId, Long storeId) {
        Long paramStoreId = AuthUserContext.get()!=null?AuthUserContext.get().getStoreId():null;
        if(paramStoreId==null){
            paramStoreId = storeId;
        }
        GetTagBySpuIdVO getTagBySpuIdVO = null;
        TagActivity tagActivity = activityService.getTagBySpuId(spuId,paramStoreId);
        if(tagActivity!=null){
             getTagBySpuIdVO =  mapperFacade.map(tagActivity,GetTagBySpuIdVO.class);
        }
        return ServerResponseEntity.success(getTagBySpuIdVO);
    }
}
