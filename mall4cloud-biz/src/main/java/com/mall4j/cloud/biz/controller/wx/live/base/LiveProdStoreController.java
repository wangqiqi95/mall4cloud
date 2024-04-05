
package com.mall4j.cloud.biz.controller.wx.live.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.biz.wx.wx.util.CharUtil;
import com.mall4j.cloud.biz.wx.wx.constant.LiveConstant;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveProdStatusType;
import com.mall4j.cloud.biz.model.live.LiveProdStore;
import com.mall4j.cloud.biz.model.live.LiveRoomProd;
import com.mall4j.cloud.biz.dto.live.LiveUsableNumParam;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveProdStoreService;
import com.mall4j.cloud.biz.service.live.LiveRoomProdService;
import com.mall4j.cloud.biz.vo.LiveProdStoreExcelVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mp/live/liveProdStore")
@Api(tags = "商家端直播间商品库接口")
public class LiveProdStoreController {

    @Autowired
    private final LiveProdStoreService liveProdStoreService;
    @Autowired
    private final LiveLogService liveLogService;
    @Autowired
    private final LiveRoomProdService liveRoomProdService;
    @Autowired
    private WechatLiveMediaService mediaService;

    @GetMapping("/page")
//    @PreAuthorize("@pms.hasPermission('live:liveProdStore:page')")
    @ApiOperation(value = "分页查找直播间商品库信息")
    public ResponseEntity<IPage<LiveProdStore>> getLiveProdStorePage(PageParam<LiveProdStore> page, LiveProdStore liveProdStore) throws WxErrorException {
        IPage<LiveProdStore> resPage = liveProdStoreService.getPage(page, liveProdStore);
        return ResponseEntity.ok(resPage);
    }

    @GetMapping("/info/{liveProdStoreId}")
    @ApiOperation(value = "通过id查询直播间商品库")
    @ApiImplicitParam(name = "liveProdStoreId", value = "直播间商品库id", required = true, dataType = "Long")
    public ResponseEntity<LiveProdStore> getById(@PathVariable("liveProdStoreId") Long liveProdStoreId) {
        LiveProdStore liveProdStore = liveProdStoreService.getById(liveProdStoreId);

        return ResponseEntity.ok(liveProdStore);
    }

    @PostMapping
//    @PreAuthorize("@pms.hasPermission('live:liveProdStore:save')")
    @ApiOperation(value = "新增直播间商品")
    public ResponseEntity<Boolean> save(@RequestBody @Valid LiveProdStore liveProdStore) throws WxErrorException {
        if (CharUtil.length(liveProdStore.getName()) < LiveConstant.PROD_NAME_MIN_LENGTH
                || CharUtil.length(liveProdStore.getName()) > LiveConstant.PROD_NAME_MAX_LENGTH) {
            // 名称长度非法,最少3个汉字或者6个字符、最大14个汉字！
            throw new LuckException("名称长度非法,最少3个汉字或者6个字符、最大14个汉字！");
        }
        if (StrUtil.isBlank(liveProdStore.getUrl())) {
            // 请选择商品！
            throw new LuckException("请选择商品！");
        }
        Date date = new Date();
        liveProdStore.setCreateTime(date);
        liveProdStore.setStatus(LiveProdStatusType.NO_EXAMINE.value());
        liveProdStore.setUpdateTime(date);
        liveProdStore.setVerifyTime(null);
        liveProdStoreService.add(liveProdStore);
        return ResponseEntity.ok(true);
    }

    @PutMapping
//    @PreAuthorize("@pms.hasPermission('live:liveProdStore:update')")
    @ApiOperation(value = "修改直播间商品")
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid LiveProdStore liveProdStore) throws WxErrorException {
        if (CharUtil.length(liveProdStore.getName()) < LiveConstant.PROD_NAME_MIN_LENGTH || CharUtil.length(liveProdStore.getName()) > LiveConstant.PROD_NAME_MAX_LENGTH) {
            throw new LuckException("名称长度非法,最少3个汉字或者6个字符、最大14个汉字！");
        }
        liveProdStore.setUpdateTime(new Date());
        liveProdStoreService.updateWxLiveProdById(liveProdStore);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/submitVerify")
//    @PreAuthorize("@pms.hasPermission('live:liveProdStore:submitVerify')")
    @ApiOperation(value = "提交审核")
    public ResponseEntity<Boolean> submitVerify(@RequestBody LiveProdStore liveProdStore) {
        liveProdStore = liveProdStoreService.getById(liveProdStore.getLiveProdStoreId());
        if (Objects.equals(LiveProdStatusType.EXAMINING.value(), liveProdStore.getStatus())) {
            // 已提交审核
            throw new LuckException("已提交审核");
        }

        boolean b = liveProdStoreService.submitVerify(liveProdStore);
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{liveProdStoreId}")
//    @PreAuthorize("@pms.hasPermission('live:liveProdStore:delete')")
    @ApiOperation(value = "通过id删除直播间商品库")
    @ApiImplicitParam(name = "liveProdStoreId", value = "直播间商品库id", required = true, dataType = "Long")
    public ResponseEntity<Boolean> removeById(@PathVariable Long liveProdStoreId) throws WxErrorException {
        liveProdStoreService.removeWxLiveProdById(liveProdStoreId, null);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listAppleProds")
    @ApiOperation(value = "获取微信入库的商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "商品状态 0：未审核。1：审核中，2：审核通过，3：审核驳回", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "商品名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "直播间id", required = true, dataType = "Integer")
    })
    public ResponseEntity<IPage<LiveProdStore>> listAppleProds(PageParam<LiveProdStore> page, Integer status, String name, Integer id) throws WxErrorException {
        if (Objects.isNull(status)) {
            status = 2;
        }
        LiveProdStore liveProdStore = new LiveProdStore();
        liveProdStore.setStatus(status);
        liveProdStore.setName(name);
        IPage<LiveProdStore> resPage = liveProdStoreService.getPage(page, liveProdStore);
        // 筛选掉已经选择的商品
        List<LiveRoomProd> prodList = liveRoomProdService.list(new LambdaQueryWrapper<LiveRoomProd>().eq(LiveRoomProd::getRoomId, id));
        List<LiveProdStore> liveProdStores = resPage.getRecords();
        // 筛选
        List<LiveProdStore> filterProdStores = liveProdStores.stream().filter(student -> prodList.stream().noneMatch(
                stuClass -> stuClass.getProdStoreId().equals(student.getLiveProdStoreId()))).collect(Collectors.toList());
        resPage.setRecords(filterProdStores);
        return ResponseEntity.ok(resPage);
    }

    @GetMapping("/pageProdsByRoomId")
    @ApiOperation(value = "根据直播间id获取直播间已经选择的商品")
    @ApiImplicitParam(name = "id", value = "直播间id", required = true, dataType = "Long")
    public ResponseEntity<IPage<LiveProdStore>> pageProdsByRoomId(PageParam<LiveProdStore> page, Integer id) {
        IPage<LiveProdStore> resPage = liveProdStoreService.pageProdByRoomId(page, id);
        return ResponseEntity.ok(resPage);
    }

    @GetMapping("/getAddProdNum")
    @ApiOperation(value = "查询商家今日剩余可用添加直播商品、可删除商品库商品次数")
    public ResponseEntity<LiveUsableNumParam> getAddRoomNum() {
        LiveUsableNumParam liveNum = liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.ADD_PROD_AUDIT);
        LiveUsableNumParam deleteLiveNum = liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.DELETE_PROD);
        liveNum.setDeleteShopNum(deleteLiveNum.getShopNum());
        liveNum.setDeleteTotalNum(deleteLiveNum.getTotalNum());
        return ResponseEntity.ok(liveNum);
    }

    @GetMapping("/getUpdateProdNum")
    @ApiOperation(value = "查询商家今日剩余可用修改直播商品次数")
    public ResponseEntity<LiveUsableNumParam> getUpdateProdNum() {
        return ResponseEntity.ok(liveLogService.getLiveNum(AuthUserContext.get().getStoreId(), LiveInterfaceType.UPDATE_PROD));
    }

    @GetMapping("/getMediaId/{liveProdStoreId}")
    @ApiOperation(value = "根据直播间商品库id获取信息")
    @ApiImplicitParam(name = "liveProdStoreId", value = "直播间商品库id", required = true, dataType = "Long")
    public ResponseEntity<String> getMediaId(@PathVariable("liveProdStoreId") Long liveProdStoreId) {
        LiveProdStore liveProdStore = liveProdStoreService.getById(liveProdStoreId);
        return ResponseEntity.ok(mediaService.getImageMediaId(liveProdStore.getCoverPic()));
    }

    @GetMapping("/sold_excel")
    @ApiOperation(value = "导出excel", notes = "导出直播商品excel")
    public ServerResponseEntity<Void> orderSoldExcel(HttpServletResponse response, LiveProdStore liveProdStore){
        List<LiveProdStoreExcelVO> list = liveProdStoreService.excelList(liveProdStore);
//        ExcelUtil.soleExcel(response, list, LiveProdStoreExcelVO.EXCEL_NAME, LiveProdStoreExcelVO.MERGE_ROW_INDEX, LiveProdStoreExcelVO.MERGE_COLUMN_INDEX, LiveProdStoreExcelVO.class);
        return ServerResponseEntity.success();
    }
}
