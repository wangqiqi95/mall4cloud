package com.mall4j.cloud.group.controller.pay.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.PayActivityDTO;
import com.mall4j.cloud.group.dto.PayActivityPageDTO;
import com.mall4j.cloud.group.model.OrderGiftShop;
import com.mall4j.cloud.group.model.PayActivityShop;
import com.mall4j.cloud.group.service.PayActivityBizService;
import com.mall4j.cloud.group.vo.PayActivityListVO;
import com.mall4j.cloud.group.vo.PayActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/pay_activity")
@Api(tags = "admin-支付有礼活动接口")
public class PayActivityController {
    @Resource
    private PayActivityBizService payActivityBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改支付有礼活动")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid PayActivityDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        String username = AuthUserContext.get().getUsername();
        Integer id = param.getId();

        if (null == id){
            param.setCreateUserId(userId);
            param.setCreateUserName(username);
        }else {
            param.setUpdateUserId(userId);
            param.setUpdateUserName(username);
        }
        return payActivityBizService.saveOrUpdatePayActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "支付有礼活动详情")
    public ServerResponseEntity<PayActivityVO> detail(@PathVariable Integer id){
        return payActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "查询支付有礼活动列表")
    public ServerResponseEntity<PageVO<PayActivityListVO>> page(@RequestBody PayActivityPageDTO param){
        return payActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return payActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return payActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return payActivityBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<PayActivityShop>> getShop(@PathVariable Integer activityId) {
        return payActivityBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return payActivityBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return payActivityBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return payActivityBizService.deleteAllShop(activityId);
    }

}
