package com.mall4j.cloud.group.controller.ad.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.model.OpenScreenAdShop;
import com.mall4j.cloud.group.model.OrderGiftShop;
import com.mall4j.cloud.group.service.OpenScreenAdBizService;
import com.mall4j.cloud.group.vo.OpenScreenAdListVO;
import com.mall4j.cloud.group.vo.OpenScreenAdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/open_screen")
@Api(tags = "admin-开屏广告设置")
public class OpenScreenAdController {
    @Resource
    private OpenScreenAdBizService openScreenAdBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改开屏广告")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid OpenScreenAdDTO param) {
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
        return openScreenAdBizService.saveOrUpdateOpenScreenAdActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "开屏广告详情")
    public ServerResponseEntity<OpenScreenAdVO> detail(@PathVariable Integer id){
        return openScreenAdBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "开屏广告列表")
    public ServerResponseEntity<PageVO<OpenScreenAdListVO>> page(@RequestBody OpenScreenAdPageDTO param){
        return openScreenAdBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return openScreenAdBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return openScreenAdBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return openScreenAdBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<OpenScreenAdShop>> getShop(@PathVariable Integer activityId) {
        return openScreenAdBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return openScreenAdBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return openScreenAdBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return openScreenAdBizService.deleteAllShop(activityId);
    }
}
