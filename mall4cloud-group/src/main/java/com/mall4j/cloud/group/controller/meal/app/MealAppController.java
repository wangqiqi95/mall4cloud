package com.mall4j.cloud.group.controller.meal.app;

import com.mall4j.cloud.common.order.dto.OrderDTO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.service.MealActivityBizService;
import com.mall4j.cloud.group.vo.app.MealInfoAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/meal_activity")
@Api(tags = "app-套餐一口价")
public class MealAppController {
    @Resource
    private MealActivityBizService mealActivityBizService;

    @GetMapping("/info/{shopId}")
    @ApiOperation(value = "套餐一口价信息", notes = "套餐一口价信息")
    public ServerResponseEntity<MealInfoAppVO> info(@RequestParam(value = "storeId", defaultValue = "1") Long storeId,
                                                    @PathVariable String shopId){
        log.info("套餐一口价信息 shopId:【{}】 storeId:【{}】",shopId,storeId);
        return mealActivityBizService.info(shopId);
    }

    @PostMapping("/confirm/{activityId}")
    @ApiOperation(value = "确认套餐一口价订单信息", notes = "确认套餐一口价订单信息")
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(@RequestParam(value = "storeId", defaultValue = "1") Long storeId,
                                                               @PathVariable Integer activityId,
                                                               @RequestBody OrderDTO param){
        Long userId = AuthUserContext.get().getUserId();
        param.setUserId(userId);
        if(Objects.isNull(param.getStoreId())){
            param.setStoreId(storeId);
        }
        return mealActivityBizService.submit(activityId,param);
    }
}
