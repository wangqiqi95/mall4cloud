package com.mall4j.cloud.product.controller.app;


import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.model.TagActivity;
import com.mall4j.cloud.product.service.ElectronicSignService;
import com.mall4j.cloud.product.service.TagActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 营销标签活动表
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
@RequiredArgsConstructor
@RestController("appTagActivityController")
@RequestMapping("/ua/tag_activity")
@Api(tags = "app-营销标签活动表")
public class TagActivityController {
    private final TagActivityService activityService;
    @Autowired
    ElectronicSignService electronicSignService;


    @GetMapping("/getTagBySpuId")
    @ApiOperation(value = "根据商品id获取权重最高的角标信息", notes = "根据商品id获取权重最高的角标信息")
    public ServerResponseEntity<TagActivity> getTagBySpuId(@RequestParam(required = true) Long spuId,@RequestParam(required = false) Long storeId) {
        Long paramStoreId = AuthUserContext.get()!=null?AuthUserContext.get().getStoreId():null;
        if(paramStoreId==null){
            paramStoreId = storeId;
        }
        return ServerResponseEntity.success(activityService.getTagBySpuId(spuId,paramStoreId));
    }

}
