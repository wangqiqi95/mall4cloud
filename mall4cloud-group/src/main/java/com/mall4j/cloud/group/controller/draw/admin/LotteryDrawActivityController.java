package com.mall4j.cloud.group.controller.draw.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.model.LotteryDrawActivityAwardRecord;
import com.mall4j.cloud.group.model.LotteryDrawActivityShop;
import com.mall4j.cloud.group.service.LotteryDrawActivityBizService;
import com.mall4j.cloud.group.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/lottery_draw")
@Api(tags = "admin-抽奖设置")
public class LotteryDrawActivityController {
    @Resource
    private LotteryDrawActivityBizService lotteryDrawActivityBizService;

    @PutMapping("/base")
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改抽奖活动")
    public ServerResponseEntity<Integer> saveOrUpdate(@RequestBody @Valid LotteryDrawActivityDTO param) {
        String username = AuthUserContext.get().getUsername();
        Integer id = param.getId();
        if (null == id){
            param.setCreateUserName(username);
        }else {
            param.setUpdateUserName(username);
        }
        return lotteryDrawActivityBizService.saveOrUpdateLotteryDrawActivity(param);
    }
    @PutMapping("/prize")
    @ApiOperation(value = "saveOrUpdateAward",notes = "新增或修改抽奖活动奖品配置")
    public ServerResponseEntity<Void> saveOrUpdatePrize(@RequestBody @Valid LotteryDrawActivityPrizeAddDTO param) {
        return lotteryDrawActivityBizService.saveOrUpdateLotteryDrawActivityPrize(param);
    }

    @PutMapping("/game")
    @ApiOperation(value = "saveOrUpdateAward",notes = "新增或修改抽奖活动奖品配置")
    public ServerResponseEntity<Void> saveOrUpdateGame(@RequestBody @Valid LotteryDrawActivityGameDTO param) {
        return lotteryDrawActivityBizService.saveOrUpdateLotteryDrawActivityGame(param);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "抽奖活动详情")
    public ServerResponseEntity<LotteryDrawActivityVO> detail(@PathVariable Integer id){
        return lotteryDrawActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "抽奖活动列表")
    public ServerResponseEntity<PageVO<LotteryDrawListVO>> page(@RequestBody LotteryDrawPageDTO param){
        return lotteryDrawActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return lotteryDrawActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return lotteryDrawActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return lotteryDrawActivityBizService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    public ServerResponseEntity<List<LotteryDrawActivityShop>> getShop(@PathVariable Integer activityId) {
        return lotteryDrawActivityBizService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return lotteryDrawActivityBizService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return lotteryDrawActivityBizService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return lotteryDrawActivityBizService.deleteAllShop(activityId);
    }

    @GetMapping("/census/{id}")
    @ApiOperation(value = "census",notes = "活动数据统计")
    public ServerResponseEntity<LotteryDrawActivityCensusVO> census(@PathVariable Integer id) {
        return lotteryDrawActivityBizService.census(id);
    }

    @PostMapping("/stock/change")
    @ApiOperation(value = "stockChange",notes = "库存变更")
    public ServerResponseEntity<Void> stockChange(@RequestBody LotteryPrizeStockChangeDTO param){
        return lotteryDrawActivityBizService.stockChange(param);
    }

    @GetMapping("/stock/change/log/{id}")
    @ApiOperation(value = "stockChangeLog",notes = "根据活动id获取库存变更记录")
    public ServerResponseEntity<List<LotteryStockChangeLogVO>> stockChangeLog(@PathVariable Integer id){
        return lotteryDrawActivityBizService.stockChangeLog(id);
    }

    @PostMapping("/award/record")
    @ApiOperation(value = "awardRecord",notes = "奖励记录")
    public ServerResponseEntity<PageVO<LotteryAwardRecordVO>> awardRecord(@RequestBody LotteryAwardRecordListDTO param){
        return lotteryDrawActivityBizService.awardRecord(param);
    }

    @PostMapping("/award/updateLogistics")
    @ApiOperation(value = "updateLogistics",notes = "修改物流信息")
    public ServerResponseEntity<Void> updateLogistics(@RequestBody LotteryDrawActivityAwardRecord lotteryDrawActivityAwardRecord){
        return lotteryDrawActivityBizService.updateLogistics(lotteryDrawActivityAwardRecord);
    }

    @GetMapping("/award/record/export")
    @ApiOperation(value = "awardRecordExport",notes = "奖励记录导出")
    public ServerResponseEntity awardRecordExport(HttpServletResponse response, LotteryAwardRecordListDTO param){
       lotteryDrawActivityBizService.awardRecordExport(response,param);
        return ServerResponseEntity.success("操作成功，请转至下载中心下载");
    }
}
