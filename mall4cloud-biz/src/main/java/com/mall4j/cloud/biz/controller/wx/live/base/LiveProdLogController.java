

package com.mall4j.cloud.biz.controller.wx.live.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveProdLog;
import com.mall4j.cloud.biz.service.live.LiveProdLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@RestController
@RequestMapping("/mp/live/liveProdLog")
@Api(tags = "商家端直播间商品日志接口")
public class LiveProdLogController {

    @Autowired
    private LiveProdLogService liveProdLogService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查找直播间商品列表日志信息")
    public ResponseEntity<IPage<LiveProdLog>> getLiveProdLogPage(PageParam<LiveProdLog> page, LiveProdLog liveProdLog) {
        return ResponseEntity.ok(liveProdLogService.page(page, new LambdaQueryWrapper<LiveProdLog>()));
    }

    @GetMapping("/info/{liveProdLogId}")
    @ApiOperation(value = "通过id查询直播间商品日志")
    @ApiImplicitParam(name = "liveProdLogId", value = "直播间商品记录id", required = true, dataType = "Long")
    public ResponseEntity<LiveProdLog> getById(@PathVariable("liveProdLogId") Long liveProdLogId) {
        return ResponseEntity.ok(liveProdLogService.getById(liveProdLogId));
    }

    @PostMapping
    @ApiOperation(value = "新增直播间商品日志")
    public ResponseEntity<Boolean> save(@RequestBody @Valid LiveProdLog liveProdLog) {
        return ResponseEntity.ok(liveProdLogService.save(liveProdLog));
    }

    @PutMapping
    @ApiOperation(value = "修改直播间商品日志")
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid LiveProdLog liveProdLog) {
        return ResponseEntity.ok(liveProdLogService.updateById(liveProdLog));
    }

    @DeleteMapping("/{liveProdLogId}")
    @ApiOperation(value = "通过id删除直播间商品日志")
    @ApiImplicitParam(name = "liveProdLogId", value = "直播间商品记录id", required = true, dataType = "Long")
    public ResponseEntity<Boolean> removeById(@PathVariable Long liveProdLogId) {
        return ResponseEntity.ok(liveProdLogService.removeById(liveProdLogId));
    }
}
