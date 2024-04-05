package com.mall4j.cloud.group.controller.register.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.RegisterActivityDTO;
import com.mall4j.cloud.group.dto.RegisterActivityPageDTO;
import com.mall4j.cloud.group.model.PerfectDataActivityShop;
import com.mall4j.cloud.group.model.RegisterActivityShop;
import com.mall4j.cloud.group.service.RegisterActivityBizService;
import com.mall4j.cloud.group.vo.RegisterActivityListVO;
import com.mall4j.cloud.group.vo.RegisterActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/register_activity")
@Api(tags = "admin-注册有礼活动接口")
public class RegisterActivityController {
    @Resource
    private RegisterActivityBizService registerActivityBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改注册有礼活动")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid RegisterActivityDTO param) {
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
        return registerActivityBizService.saveOrUpdateRegisterActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "注册有礼活动详情")
    public ServerResponseEntity<RegisterActivityVO> detail(@PathVariable Integer id){
        return registerActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "查询注册有礼活动列表")
    public ServerResponseEntity<PageVO<RegisterActivityListVO>> page(@RequestBody RegisterActivityPageDTO param){
        return registerActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return registerActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return registerActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return registerActivityBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<RegisterActivityShop>> getShop(@PathVariable Integer activityId) {
        return registerActivityBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return registerActivityBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return registerActivityBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return registerActivityBizService.deleteAllShop(activityId);
    }
}
