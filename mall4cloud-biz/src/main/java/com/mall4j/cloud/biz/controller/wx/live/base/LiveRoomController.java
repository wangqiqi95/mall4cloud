

package com.mall4j.cloud.biz.controller.wx.live.base;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.biz.wx.wx.util.CharUtil;
import com.mall4j.cloud.biz.wx.wx.constant.LiveConstant;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.model.live.LiveRoomBack;
import com.mall4j.cloud.biz.model.live.LiveRoomStatusType;
import com.mall4j.cloud.biz.dto.live.LiveRoomShopParam;
import com.mall4j.cloud.biz.dto.live.LiveUsableNumParam;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveRoomBackService;
import com.mall4j.cloud.biz.service.live.LiveRoomProdService;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mp/multishop/live/liveRoom")
@Api(tags = "商家端直播间接口")
@Slf4j
public class LiveRoomController {

    @Autowired
    private final LiveRoomBackService liveRoomBackService;
    @Autowired
    private final LiveRoomProdService liveRoomProdService;
    @Autowired
    private LiveRoomService liveRoomService;
    @Autowired
    private final LiveLogService liveLogService;

    @GetMapping("/page")
//    @PreAuthorize("@pms.hasPermission('live:liveRoom:page')")
    @ApiOperation(value = "分页查找直播间信息")
    public ResponseEntity<IPage<LiveRoom>> getLiveRoomPage(PageParam<LiveRoom> page, LiveRoom liveRoom) {
        IPage<LiveRoom> roomPage = liveRoomService.page(page, new LambdaQueryWrapper<LiveRoom>()
                .orderByDesc(LiveRoom::getUpdateTime)
                .like(StrUtil.isNotBlank(liveRoom.getName()), LiveRoom::getName, liveRoom.getName())
                .like(StrUtil.isNotBlank(liveRoom.getAnchorName()), LiveRoom::getAnchorName, liveRoom.getAnchorName())
                .eq(Objects.nonNull(liveRoom.getLiveStatus()), LiveRoom::getLiveStatus, liveRoom.getLiveStatus())
                .ge(Objects.nonNull(liveRoom.getStartTime()), LiveRoom::getStartTime, liveRoom.getStartTime())
                .le(Objects.nonNull(liveRoom.getStartTime()), LiveRoom::getStartTime, liveRoom.getEndTime())
                .orderByDesc(LiveRoom::getStartTime)
        );
        if (CollectionUtils.isNotEmpty(roomPage.getRecords())) {
            for (LiveRoom liveRoomDb : roomPage.getRecords()) {
                LiveRoom.RoomToolsVO roomToolsVO = JSON.parseObject(liveRoomDb.getRoomTools(), LiveRoom.RoomToolsVO.class);
                liveRoomDb.setRoomToolsVo(roomToolsVO);
                // 计算直播状态
                Date now = new Date();
                if (liveRoomDb.getStartTime().after(now)) {
                    liveRoomDb.setLiveStatus(102);
                } else if (liveRoomDb.getStartTime().before(now) && liveRoomDb.getEndTime().after(now)) {
                    liveRoomDb.setLiveStatus(101);
                } else {
                    liveRoomDb.setLiveStatus(103);
                }

            }
        }
        return ResponseEntity.ok(roomPage);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "通过id查询直播间")
    @ApiImplicitParam(name = "id", value = "直播间id", required = true, dataType = "Long")
    public ResponseEntity<LiveRoom> getById(@PathVariable("id") Long id) {
        LiveRoom liveRoomDb = liveRoomService.getDetailById(id);
        return ResponseEntity.ok(liveRoomDb);
    }

    @GetMapping("/getPlayBack/{id}")
    @ApiOperation(value = "通过id查询直播间回放信息")
    @ApiImplicitParam(name = "id", value = "直播间id", required = true, dataType = "Long")
    public ResponseEntity<LiveRoomBack> getPlayBackByRoomId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(liveRoomBackService.getOne(new LambdaQueryWrapper<LiveRoomBack>().eq(LiveRoomBack::getRoomId, id)));
    }

    @GetMapping("/getShareCode/{id}")
    @ApiOperation(value = "通过id查询直播间分享二维码")
    @ApiImplicitParam(name = "id", value = "直播间id", required = true, dataType = "Long")
    public ResponseEntity<String> getShareCode(@PathVariable("id") Integer id) throws WxErrorException {
        String result = liveRoomService.getShareCode(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ApiOperation(value = "新增直播间")
    public ResponseEntity<Boolean> save(@RequestBody @Valid LiveRoom liveRoom) throws WxErrorException {
        checkName(liveRoom);
        liveRoom.setUpdateTime(new Date());
        liveRoom.setApplyTime(new Date());
        liveRoomService.saveLiveRoom(liveRoom);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setShop")
    @ApiOperation(value = "设置直播间门店")
    public ResponseEntity<Boolean> setShop(@RequestBody LiveRoomShopParam liveRoomShopParam) throws WxErrorException {
        liveRoomService.saveLiveRoomShop(liveRoomShopParam);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @ApiOperation(value = "修改直播间")
    public ResponseEntity<Boolean> update(@RequestBody @Valid LiveRoom liveRoom) throws WxErrorException {
        checkName(liveRoom);
        liveRoom.setUpdateTime(new Date());
        liveRoomService.updateLiveRoom(liveRoom);
        return ResponseEntity.ok().build();
    }

    private void checkName(@RequestBody @Valid LiveRoom liveRoom) {
        if (CharUtil.length(liveRoom.getName()) < LiveConstant.ROOM_NAME_MIN_LENGTH || CharUtil.length(liveRoom.getName()) > LiveConstant.ROOM_NAME_MAX_LENGTH) {
            // 直播间名称长度非法,最短3个汉字，最长17个汉字！
            throw new LuckException("直播间名称长度非法,最短3个汉字，最长17个汉字！");
        }
        if (CharUtil.length(liveRoom.getAnchorName()) < LiveConstant.USER_NAME_MIN_LENGTH || CharUtil.length(liveRoom.getAnchorName()) > LiveConstant.USER_NAME_MAX_LENGTH) {
            // 主播昵称长度非法，最短2个汉字，最长15个汉字
            throw new LuckException("主播昵称长度非法，最短2个汉字，最长15个汉字");
        }
        liveRoom.setLiveStatus(LiveRoomStatusType.NO_START.value());
    }

    @PostMapping("/addLiveRoomProd")
    @ApiOperation(value = "新增直播间商品")
    public ResponseEntity<Boolean> addLiveRoomProd(@RequestBody @Valid LiveRoom liveRoom) throws WxErrorException {
        liveRoomProdService.saveBatchAndRoomId(liveRoom);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getMediaId")
    @ApiOperation(value = "获取图片mediaId")
    public ResponseEntity<String> getMediaId(@RequestParam(value = "url") String url) throws WxErrorException {
        String result = liveRoomService.getMediaId(url);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAddRoomNum")
    @ApiOperation(value = "查询商家今日剩余可用添加直播间次数")
    public ResponseEntity<LiveUsableNumParam> getAddRoomNum() {
        LiveUsableNumParam liveNum = liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.CREATE_ROOM);
        LiveUsableNumParam deleteLiveNum = liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.REMOVE_LIVE_ROOM);
        liveNum.setDeleteShopNum(deleteLiveNum.getShopNum());
        liveNum.setDeleteTotalNum(deleteLiveNum.getTotalNum());
        return ResponseEntity.ok(liveNum);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过id删除直播间信息")
    @ApiImplicitParam(name = "id", value = "直播间id", required = true, dataType = "Long")
    public ResponseEntity<Boolean> removeById(@PathVariable Long id) throws WxErrorException {
        return ResponseEntity.ok(liveRoomService.removeRoomById(id, null));
    }
}
