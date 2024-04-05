package com.mall4j.cloud.group.controller.gift.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.dto.OrderGiftDTO;
import com.mall4j.cloud.group.model.OrderGiftShop;
import com.mall4j.cloud.group.service.OrderGiftBizService;
import com.mall4j.cloud.group.vo.OrderGiftListVO;
import com.mall4j.cloud.group.vo.OrderGiftVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/order_gift")
@Api(tags = "admin-下单赠品设置")
public class OrderGiftController {
    @Resource
    private OrderGiftBizService orderGiftBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改下单赠品")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid OrderGiftDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        String userName = AuthUserContext.get().getUsername();
        Integer id = param.getId();

        if (null == id){
            param.setCreateUserId(userId);
            param.setCreateUserName(userName);
        }else {
            param.setUpdateUserId(userId);
            param.setUpdateUserName(userName);
        }
        return orderGiftBizService.saveOrUpdateOrderGiftActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "下单赠品详情")
    public ServerResponseEntity<OrderGiftVO> detail(@PathVariable Integer id){
        return orderGiftBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "下单赠品列表")
    public ServerResponseEntity<PageVO<OrderGiftListVO>> page(@RequestBody OpenScreenAdPageDTO param){
        return orderGiftBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return orderGiftBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return orderGiftBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return orderGiftBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<OrderGiftShop>> getShop(@PathVariable Integer activityId) {
        return orderGiftBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return orderGiftBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return orderGiftBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return orderGiftBizService.deleteAllShop(activityId);
    }
}
