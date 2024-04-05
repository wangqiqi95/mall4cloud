

package com.mall4j.cloud.biz.controller.wx.live.base;


import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.dto.live.LiveUsableNumParam;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
@RestController
@RequestMapping("/mp/live/liveRoomProd")
@Api(tags = "商家端直播间商品接口")
public class LiveRoomProdController {

    @Autowired
    private LiveLogService liveLogService;

    @GetMapping("/getAddRoomProdNum")
    @ApiOperation(value = "查询商家今日剩余可用添加直播间商品次数")
    public ResponseEntity<LiveUsableNumParam> getAddRoomNum() {
        return ResponseEntity.ok(liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.IMPORT_ROOM_PROD));
    }
}
