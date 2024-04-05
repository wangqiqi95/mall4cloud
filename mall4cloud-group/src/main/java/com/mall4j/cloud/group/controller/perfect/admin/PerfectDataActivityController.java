package com.mall4j.cloud.group.controller.perfect.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityPageDTO;
import com.mall4j.cloud.group.model.PayActivityShop;
import com.mall4j.cloud.group.model.PerfectDataActivityShop;
import com.mall4j.cloud.group.service.PerfectDataActivityBizService;
import com.mall4j.cloud.group.vo.PerfectDataActivityListVO;
import com.mall4j.cloud.group.vo.PerfectDataActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/perfect_data_activity")
@Api(tags = "admin-完善资料活动接口")
public class PerfectDataActivityController {
    @Resource
    private PerfectDataActivityBizService perfectDataActivityBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改完善资料活动")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid PerfectDataActivityDTO param) {
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
        return perfectDataActivityBizService.saveOrUpdatePerfectDataActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "完善资料活动详情")
    public ServerResponseEntity<PerfectDataActivityVO> detail(@PathVariable Integer id){
        return perfectDataActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "查询完善资料活动列表")
    public ServerResponseEntity<PageVO<PerfectDataActivityListVO>> page(@RequestBody PerfectDataActivityPageDTO param){
        return perfectDataActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return perfectDataActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return perfectDataActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return perfectDataActivityBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<PerfectDataActivityShop>> getShop(@PathVariable Integer activityId) {
        return perfectDataActivityBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return perfectDataActivityBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return perfectDataActivityBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return perfectDataActivityBizService.deleteAllShop(activityId);
    }
}
