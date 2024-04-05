package com.mall4j.cloud.group.controller.follow.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.model.FollowWechatActivityShop;
import com.mall4j.cloud.group.service.FollowWechatBizService;
import com.mall4j.cloud.group.vo.FollowWechatListVO;
import com.mall4j.cloud.group.vo.FollowWechatVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/follow_wechat")
@Api(tags = "admin-关注公众号有礼活动")
public class FollowWechatController {
    @Resource
    private FollowWechatBizService followWechatBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改关注公众号有礼")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid FollowWechatDTO param) {
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
        return followWechatBizService.saveOrUpdateRegisterActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "关注公众号有礼详情")
    public ServerResponseEntity<FollowWechatVO> detail(@PathVariable Integer id){
        return followWechatBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "查询关注公众号有礼列表")
    public ServerResponseEntity<PageVO<FollowWechatListVO>> page(@RequestBody FollowWechatPageDTO param){
        return followWechatBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用活动")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return followWechatBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用活动")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return followWechatBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete",notes = "删除活动")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return followWechatBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<FollowWechatActivityShop>> getShop(@PathVariable Integer activityId) {
        return followWechatBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return followWechatBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return followWechatBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return followWechatBizService.deleteAllShop(activityId);
    }
}
