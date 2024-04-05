package com.mall4j.cloud.group.controller.meal.admin;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.MealActivityDTO;
import com.mall4j.cloud.group.dto.MealActivityPageDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.model.MealActivityShop;
import com.mall4j.cloud.group.service.MealActivityBizService;
import com.mall4j.cloud.group.vo.MealActivityListVO;
import com.mall4j.cloud.group.vo.MealActivitySaveVO;
import com.mall4j.cloud.group.vo.MealActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/meal_activity")
@Api(tags = "admin-套餐一口价设置")
public class MealActivityController {
    @Resource
    private MealActivityBizService mealActivityBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate", notes = "新增或修改套餐一口价")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid MealActivityDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        String username = AuthUserContext.get().getUsername();
        Integer id = param.getId();

        if (null == id) {
            param.setCreateUserId(userId);
            param.setCreateUserName(username);
        } else {
            param.setUpdateUserId(userId);
            param.setUpdateUserName(username);
        }
        if (CollectionUtil.isEmpty(param.getPools()) || param.getPools().size() < 2){
            throw new LuckException("商品池数量至少为2个");
        }
            return mealActivityBizService.saveOrUpdateMealActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail", notes = "套餐一口价详情")
    public ServerResponseEntity<MealActivityVO> detail(@PathVariable Integer id) {
        return mealActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page", notes = "套餐一口价列表")
    public ServerResponseEntity<PageVO<MealActivityListVO>> page(@RequestBody MealActivityPageDTO param) {
        return mealActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable", notes = "启用")
    public ServerResponseEntity<MealActivitySaveVO> enable(@PathVariable Integer id) {
        return mealActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable", notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id) {
        return mealActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id) {
        return mealActivityBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<MealActivityShop>> getShop(@PathVariable Integer activityId) {
        return mealActivityBizService.getActivityShop(activityId);
    }

    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return mealActivityBizService.addActivityShop(param);
    }

    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop", notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId, @PathVariable Integer shopId) {
        return mealActivityBizService.deleteActivityShop(activityId, shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop", notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return mealActivityBizService.deleteAllShop(activityId);
    }
}
