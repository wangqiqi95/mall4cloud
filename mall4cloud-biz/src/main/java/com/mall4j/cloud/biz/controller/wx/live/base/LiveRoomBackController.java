

package com.mall4j.cloud.biz.controller.wx.live.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveRoomBack;
import com.mall4j.cloud.biz.service.live.LiveRoomBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lhd
 * @date 2020-08-12 10:04:53
 */
@RestController
@RequestMapping("/mp/live/liveRoomBack")
@Api(tags = "商家端直播间回放接口")
public class LiveRoomBackController {

    @Autowired
    private LiveRoomBackService liveRoomBackService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查找直播间回放信息")
    public ResponseEntity<IPage<LiveRoomBack>> getLiveRoomBackPage(PageParam<LiveRoomBack> page, LiveRoomBack liveRoomBack) {
        return ResponseEntity.ok(liveRoomBackService.page(page, new LambdaQueryWrapper<LiveRoomBack>()));
    }

    @GetMapping("/info/{backId}")
    @ApiOperation(value = "通过id查询直播间回放信息")
    @ApiImplicitParam(name = "backId", value = "直播间回放id", required = true, dataType = "Long")
    public ResponseEntity<LiveRoomBack> getById(@PathVariable("backId") Long backId) {
        return ResponseEntity.ok(liveRoomBackService.getById(backId));
    }

    @PostMapping
//    @PreAuthorize("@pms.hasPermission('live:liveRoomBack:save')")
    @ApiOperation(value = "新增直播间回放信息")
    public ResponseEntity<Boolean> save(@RequestBody @Valid LiveRoomBack liveRoomBack) {
        return ResponseEntity.ok(liveRoomBackService.save(liveRoomBack));
    }

    @PutMapping
//    @PreAuthorize("@pms.hasPermission('live:liveRoomBack:update')")
    @ApiOperation(value = "修改直播间回放信息")
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid LiveRoomBack liveRoomBack) {
        return ResponseEntity.ok(liveRoomBackService.updateById(liveRoomBack));
    }

    @DeleteMapping("/{backId}")
//    @PreAuthorize("@pms.hasPermission('live:liveRoomBack:delete')")
    @ApiOperation(value = "通过id删除直播间回放信息")
    @ApiImplicitParam(name = "backId", value = "直播间回放id", required = true, dataType = "Long")
    public ResponseEntity<Boolean> removeById(@PathVariable Long backId) {
        return ResponseEntity.ok(liveRoomBackService.removeById(backId));
    }
}
