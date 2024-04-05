

package com.mall4j.cloud.biz.service.live.impl;


import cn.binarywang.wx.miniapp.bean.live.WxMaLiveGoodInfo;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.dto.live.GoodsInfoRespParam;
import com.mall4j.cloud.biz.mapper.live.LiveProdLogMapper;
import com.mall4j.cloud.biz.mapper.live.LiveProdStoreMapper;
import com.mall4j.cloud.biz.model.live.*;
import com.mall4j.cloud.biz.dto.live.LiveGoodsInfoReqParam;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.biz.service.live.LiveConfig;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveProdStoreService;
import com.mall4j.cloud.biz.service.live.LiveRoomProdService;
import com.mall4j.cloud.biz.util.WxInterfaceUtil;
import com.mall4j.cloud.biz.vo.LiveProdStoreExcelVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@Slf4j
@Service
@AllArgsConstructor
public class LiveProdStoreServiceImpl extends ServiceImpl<LiveProdStoreMapper, LiveProdStore> implements LiveProdStoreService {

    private final LiveProdStoreMapper liveProdStoreMapper;
    // 最大一次查询数量
    private static final int MAX_COUNT = 20;
    private final WxInterfaceUtil wxInterfaceUtil;
    private final MapperFacade mapperFacade;
    private final LiveLogService liveLogService;
    private final LiveRoomProdService liveRoomProdService;
    private final WxConfig wxConfig;
    private final WxInterfaceUtil wxUtil;
    private final LiveProdLogMapper liveProdLogMapper;
    private final LiveConfig liveConfig;
    @Autowired
    private WechatLiveMediaService mediaService;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;




    @Override
    public IPage<LiveProdStore> getPage(PageParam<LiveProdStore> page, LiveProdStore liveProdStore) throws WxErrorException {
        IPage<LiveProdStore> resPage = page(page, new LambdaQueryWrapper<LiveProdStore>().ne(LiveProdStore::getStatus, -1)
                .like(StrUtil.isNotBlank(liveProdStore.getName()), LiveProdStore::getName, liveProdStore.getName())
                .eq( liveProdStore.getShopId() !=null, LiveProdStore::getShopId, liveProdStore.getShopId())
                .orderByDesc(LiveProdStore::getCreateTime)
        );

        List<LiveProdStore> records = resPage.getRecords();
        List<LiveProdStore> updateRecords = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(records)) {
            try {
                List<Integer> goodIds = records.stream().map(LiveProdStore::getGoodsId).collect(Collectors.toList());
                WxMaLiveResult result = wxConfig.getWxMaService().getLiveGoodsService().getGoodsWareHouse(goodIds);
                log.info("goods = {}", result);
                List<WxMaLiveResult.Goods> goods = result.getGoods();
                for (LiveProdStore record : records) {
                    record.setCoverPic(record.getCoverPic());
                    for (WxMaLiveResult.Goods good : goods) {
                        if (good.getGoodsId().equals(record.getGoodsId())) {
                            if(record.getStatus()!=good.getAuditStatus()){
                                LiveProdStore prodStore=new LiveProdStore();
                                prodStore.setLiveProdStoreId(record.getLiveProdStoreId());
                                prodStore.setStatus(good.getAuditStatus());
                                updateRecords.add(prodStore);
                            }
                            record.setStatus(good.getAuditStatus());
                        }
                    }
                }

                if(CollectionUtil.isNotEmpty(updateRecords)){
                    log.info("获取商品的信息与审核状态 同步更新表: {}", JSON.toJSONString(updateRecords));
                    this.updateBatchById(updateRecords);
                }
            }catch (Exception e){
                log.info("获取商品的信息与审核状态 失败：{} {}",e,e.getMessage());
            }
        }

        resPage.setRecords(records);
        return resPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitVerify(LiveProdStore liveProdStore) {
        // 1.校验今日可用次数并保存or修改商家次数记录
        liveLogService.checkNumsAndSaveLog(liveProdStore.getShopId(), LiveInterfaceType.ADD_PROD_AUDIT, "提交审核");

        LiveProdStore update = new LiveProdStore();
        update.setLiveProdStoreId(liveProdStore.getLiveProdStoreId());
        Date date = new Date();
        update.setUpdateTime(date);
        update.setVerifyTime(date);
        update.setStatus(LiveProdStatusType.NO_EXAMINE.value());
        // 发起微信审核, 先获取微信的media_id
        String mediaId = mediaService.getImageMediaId(liveProdStore.getCoverPic());
        update.setConverImgUrl(mediaId);
        LiveGoodsInfoReqParam reqParam = new LiveGoodsInfoReqParam();
        reqParam.setCoverImgUrl(mediaId);
        reqParam.setName(liveProdStore.getName());
        reqParam.setPriceType(liveProdStore.getPriceType());
        reqParam.setPrice(liveProdStore.getPrice());
        reqParam.setPrice2(liveProdStore.getPrice2());
        reqParam.setUrl(liveProdStore.getUrl());
        boolean prodStoreOk = false;
        boolean prodLogOk = false;
        try {
            reqParam.setAccessToken(wxConfig.getWxMaService().getAccessToken());
            GoodsInfoRespParam goodsInfo = wxUtil.prodAddVerify(reqParam);
            update.setGoodsId(Math.toIntExact(goodsInfo.getGoodsId()));
            update.setStatus(LiveProdStatusType.EXAMINING.value());
            // 获取到的goodsId存起来
            prodStoreOk = liveProdStoreMapper.updateById(update) > 0;
            // 添加日志
            LiveProdLog prodLog = new LiveProdLog();
            prodLog.setLiveProdStoreId(liveProdStore.getLiveProdStoreId());
            prodLog.setUpdateTime(date);
            prodLog.setStatus(1);
            prodLog.setGoodsId(Math.toIntExact(goodsInfo.getGoodsId()));
            prodLog.setAuditId(Math.toIntExact(goodsInfo.getAuditId()));
            prodLogOk = liveProdLogMapper.insert(prodLog) > 0;
        } catch (WxErrorException e) {
            log.info("获取access_token异常");
        }
        return prodStoreOk && prodLogOk;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean submitVerify(LiveProdStore liveProdStore) {
//        // 1.校验今日可用次数并保存or修改商家次数记录
//        liveLogService.checkNumsAndSaveLog(liveProdStore.getShopId(), LiveInterfaceType.ADD_PROD_AUDIT, "提交审核");
//
//        LiveProdStore prodStore = new LiveProdStore();
//        prodStore.setLiveProdStoreId(liveProdStore.getLiveProdStoreId());
//        Date date = new Date();
//        prodStore.setUpdateTime(date);
//        prodStore.setVerifyTime(date);
//        prodStore.setStatus(LiveProdStatusType.NO_EXAMINE.value());
//        // 发起微信审核, 先获取微信的media_id
//        String mediaId = wxImageUtil.wxImageUpload(liveProdStore.getCoverPic());
//        WxMaLiveGoodInfo goods = mapperFacade.map(liveProdStore, WxMaLiveGoodInfo.class);
//        goods.setCoverImgUrl(mediaId);
//        WxMaLiveResult wxMaLiveResult;
//        try {
//            goods.setUrl(URLEncoder.encode(liveProdStore.getUrl(),"utf-8"));
//            wxMaLiveResult = wxConfig.getWxMaService().getLiveGoodsService().addGoods(goods);
//        }catch (Exception e){
//            throw new LuckException("yami.examine.fail");
//        }
//        prodStore.setGoodsId(wxMaLiveResult.getGoodsId());
//        prodStore.setStatus(LiveProdStatusType.EXAMINING.value());
//        boolean prodStoreOk = liveProdStoreMapper.updateById(prodStore) > 0;
//        LiveProdLog prodLog = new LiveProdLog();
//        prodLog.setLiveProdStoreId(liveProdStore.getLiveProdStoreId());
//        prodLog.setUpdateTime(date);
//        prodLog.setStatus(1);
//        prodLog.setGoodsId(wxMaLiveResult.getGoodsId());
//        prodLog.setAuditId(wxMaLiveResult.getAuditId());
//        boolean prodLogOk = liveProdLogMapper.insert(prodLog) > 0;
//        return prodStoreOk && prodLogOk;
//    }

//    /**
//     * 不需要去查询微信的入库信息，直接查询我们的数据库即可
//     * @param page 分页
//     * @param status 状态
//     * @param shopId 商家id
//     */
//    @Override
//    public PageParam<RoomResponse> listLiveProdsByStatus(PageParam<RoomResponse> page, Integer status, Long shopId) {
//        getPage()
//        WxRoomProdInfo wxRoomProdInfo = new WxRoomProdInfo();
//        wxRoomProdInfo.setOffset((page.getCurrent()-1) * page.getSize());
//        wxRoomProdInfo.setLimit(page.getSize());
//        wxRoomProdInfo.setStatus(status);
//        WxServerResponse<PageParam<RoomResponse>> pageParamWxServerResponse = wxUtil.pageLivePords(wxRoomProdInfo);
//        if(!pageParamWxServerResponse.isSuccess()){
//            throw new LuckException("获取微信入库商品失败！");
//        }
//        return null;
//    }

    @Override
    public IPage<LiveProdStore> pageProdByRoomId(PageParam<LiveProdStore> page, Integer roomId) {
        IPage<LiveProdStore> liveProdStorePage = liveProdStoreMapper.pageProdByRoomId(page, roomId);
        List<LiveProdStore> records = liveProdStorePage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            for (LiveProdStore record : records) {
                record.setCoverPic(record.getCoverPic().startsWith("/") ? liveConfig.getImgDomain() + record.getCoverPic() : record.getCoverPic());
            }
        }
        liveProdStorePage.setRecords(records);
        return liveProdStorePage;
    }

    @Override
    public void synchronousWxLiveProds() throws WxErrorException {
        List<LiveProdStore> liveProdStores = list(new LambdaQueryWrapper<LiveProdStore>().ne(LiveProdStore::getStatus, LiveProdStatusType.DELETE.value()));
        List<Integer> ids = new ArrayList<>();
        liveProdStores.forEach(liveProdStore -> {
            if (Objects.nonNull(liveProdStore.getGoodsId())) {
                ids.add(liveProdStore.getGoodsId());
            }
        });
        WxMaLiveResult wxMaLiveResult;
        List<WxMaLiveResult.Goods> goods = new ArrayList<>();
        try {
            int count = (int) Math.ceil(Arith.div(ids.size(), 20));
            for (int i = 0; i < count; i++) {
                List<Integer> goodsIds = new ArrayList<>();
                int maxLength = Math.min(MAX_COUNT * (i + 1), ids.size());
                for (int j = MAX_COUNT * i; j < maxLength; j++) {
                    goodsIds.add(ids.get(j));
                }
                wxMaLiveResult = wxConfig.getWxMaService().getLiveGoodsService().getGoodsWareHouse(goodsIds);
                goods.addAll(wxMaLiveResult.getGoods());
            }
        } catch (WxErrorException e) {
            log.error("获取直播间商品状态失败！");
            log.error(e.getMessage());
            return;
        }

        Date date = new Date();
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }
        Map<Integer, Integer> wxGoodsMap = goods.stream().collect(Collectors.toMap(WxMaLiveResult.Goods::getGoodsId, WxMaLiveResult.Goods::getAuditStatus));
        for (LiveProdStore liveProdStore : liveProdStores) {
            if (Objects.isNull(liveProdStore.getGoodsId()) || !wxGoodsMap.containsKey(liveProdStore.getGoodsId()) || Objects.isNull(wxGoodsMap.get(liveProdStore.getGoodsId()))) {
                continue;
            }
            Integer status = wxGoodsMap.get(liveProdStore.getGoodsId());
            // 从任意非审核通过的状态变成审核通过，修改审核时间
            if (!Objects.equals(liveProdStore.getStatus(), status) &&
                    Objects.equals(status, LiveProdStatusType.EXAMINE_SUCCESS.value())) {
                liveProdStore.setSuccessTime(date);
            }
            liveProdStore.setStatus(status);
        }
        // 批量修改商品状态
        updateBatchById(liveProdStores);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeWxLiveProdById(Long liveProdStoreId, Long shopId) throws WxErrorException {
        LiveProdStore liveProdStore = getById(liveProdStoreId);
        // 移除数据库的数据包括关联
        update(new LambdaUpdateWrapper<LiveProdStore>().set(LiveProdStore::getStatus, -1).eq(LiveProdStore::getLiveProdStoreId, liveProdStoreId));
        liveRoomProdService.remove(new LambdaQueryWrapper<LiveRoomProd>().eq(LiveRoomProd::getProdStoreId, liveProdStoreId));
        // 状态为审核通过的商品进行移除微信商品库的数据、扣除次数
        log.info("删除直播商品审核状态 : {} ",liveProdStore.getStatus());
        if (Objects.equals(liveProdStore.getStatus(), LiveProdStatusType.EXAMINE_SUCCESS.value())) {
            // 1.校验今日可用次数并保存or修改商家次数记录
            // liveLogService.checkNumsAndSaveLog(shopId, LiveInterfaceType.DELETE_PROD, "删除商品库商品");
            try {
                boolean deleteGoods = wxConfig.getWxMaService().getLiveGoodsService().deleteGoods(liveProdStore.getGoodsId());
                log.info("删除直播商品 deleteGoods: {}",deleteGoods);
                if (!deleteGoods) {
                    // 删除直播商品失败
                    throw new LuckException("删除直播商品失败");
                }
            }catch (Exception e){
                // 删除直播商品失败
                log.info("删除直播商品失败 : {} {}",e,e.getMessage());
                throw new LuckException("删除直播商品失败: "+e.getMessage());
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWxLiveProdById(LiveProdStore liveProdStore) throws WxErrorException {
        // 两种情况修改
        // 没有提交微信商品库的修改
        if (Objects.isNull(liveProdStore.getProdId())) {
            throw new LuckException("请选择商品");
        }
        log.info("编辑直播间商品 liveProdStore = {} ", liveProdStore);
        if (!Objects.equals(liveProdStore.getStatus(), LiveProdStatusType.DELETE.value())) {
            // 1.校验今日可用次数并保存or修改商家次数记录
            //liveLogService.checkNumsAndSaveLog(liveProdStore.getShopId(), LiveInterfaceType.UPDATE_PROD, "修改商品库商品");
            // 提交到微信商品库的修改
            updateById(liveProdStore);
            log.info("同步微信修改后的商品信息");
            // 修改微信商品库
            WxMaLiveGoodInfo goods = new WxMaLiveGoodInfo();
            String coverMediaId = mediaService.getImageMediaId(liveProdStore.getCoverPic());
            goods.setCoverImgUrl(coverMediaId);
            goods.setUrl(liveProdStore.getUrl());
            goods.setPriceType(liveProdStore.getPriceType());
            goods.setPrice(BigDecimal.valueOf(liveProdStore.getPrice()));
            goods.setPrice2(BigDecimal.valueOf(liveProdStore.getPrice2()));
            goods.setName(liveProdStore.getName());
            goods.setGoodsId(liveProdStore.getGoodsId());

            boolean result = wxConfig.getWxMaService().getLiveGoodsService().updateGoods(goods);
            if (!result) {
                // 更新直播商品库失败
                throw new LuckException("更新直播商品库失败");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(LiveProdStore liveProdStore) {
        try {
            // 新增商品
            WxMaLiveGoodInfo goods = new WxMaLiveGoodInfo();
            String coverMediaId = mediaService.getImageMediaId(liveProdStore.getCoverPic());
            goods.setCoverImgUrl(coverMediaId);
            goods.setUrl(liveProdStore.getUrl());
            goods.setPriceType(liveProdStore.getPriceType());
            goods.setPrice(BigDecimal.valueOf(liveProdStore.getPrice()));
            goods.setPrice2(BigDecimal.valueOf(liveProdStore.getPrice2()));
            goods.setName(liveProdStore.getName());
            WxMaLiveResult result = wxConfig.getWxMaService().getLiveGoodsService().addGoods(goods);

            liveProdStore.setGoodsId(result.getGoodsId());
            liveProdStore.setAuditId(result.getAuditId().intValue());
            save(liveProdStore);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            String errorContent = "新增商品失败";
            if (error.getErrorCode() == 300018) {
                errorContent = "商品图片尺寸过大";
            }
            if (error.getErrorCode() == 300004) {
                errorContent = "商品名称存在违规违法内容";
            }
            if (error.getErrorCode() == 300005) {
                errorContent = "商品图片存在违规违法内容";
            }
            if (error.getErrorCode() == 300006) {
                errorContent = "图片上传失败（如：mediaID过期）";
            }
            if (error.getErrorCode()== 300020){
                errorContent = "微信提示：商品数量达到上限"+e.getMessage();
            }
            if (error.getErrorCode()== 300013){
                errorContent = "微信提示：审核失败"+e.getMessage();
            }
            throw new LuckException(errorContent);
        }
    }

    /**
     * 移除十天前旧的上架商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOldLiveProd() throws WxErrorException {
        // 十天前的时间
        DateTime dateTime = DateUtil.offsetDay(new Date(), -10);
        Date date = new Date();
        // 查询十天前上架的商品
        List<LiveProdStore> prodStoreList = list(new LambdaQueryWrapper<LiveProdStore>().eq(LiveProdStore::getStatus, LiveProdStatusType.EXAMINE_SUCCESS.value())
                .le(LiveProdStore::getSuccessTime, dateTime));
        if (CollectionUtils.isEmpty(prodStoreList)) {
            return;
        }
        List<Long> ids = new ArrayList<>();
        for (LiveProdStore liveProdStore : prodStoreList) {
            liveProdStore.setCancelTime(date);
            liveProdStore.setStatus(LiveProdStatusType.PLATFORM_BREAK.value());
            ids.add(liveProdStore.getLiveProdStoreId());
            try {
                // 移除微信商品库的数据
                wxConfig.getWxMaService().getLiveGoodsService().deleteGoods(liveProdStore.getGoodsId());
                boolean deleteGoods = wxConfig.getWxMaService().getLiveGoodsService().deleteGoods(liveProdStore.getGoodsId());
                if (!deleteGoods) {
                    // 删除直播商品失败
                    throw new Exception();
                }
            } catch (Exception e) {
                log.error("删除直播商品{}失败！", liveProdStore.getGoodsId());
                log.error(e.getMessage());
            }
        }
        // 批量修改
        liveProdStoreMapper.updateBatch(prodStoreList);
        // 删除直播间和商品的关联
        liveRoomProdService.remove(new LambdaQueryWrapper<LiveRoomProd>().in(LiveRoomProd::getProdStoreId, ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<LiveProdStoreExcelVO> excelList(LiveProdStore liveProdStore) {
        List<LiveProdStore> prods = list(new LambdaQueryWrapper<LiveProdStore>().ne(LiveProdStore::getStatus, -1)
                .like(StrUtil.isNotBlank(liveProdStore.getName()), LiveProdStore::getName, liveProdStore.getName())
                .eq( liveProdStore.getShopId() !=null, LiveProdStore::getShopId, liveProdStore.getShopId())
                .orderByDesc(LiveProdStore::getCreateTime));

        List<LiveProdStoreExcelVO> excelList = mapperFacade.mapAsList(prods,LiveProdStoreExcelVO.class);

        List<Long> storeIds = prods.stream().map(LiveProdStore::getShopId).collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storeResonse = storeFeignClient.listByStoreIdList(storeIds);
        Map<Long,StoreVO> storeMap = storeResonse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));

        List<Long> prodIds = prods.stream().map(LiveProdStore::getProdId).collect(Collectors.toList());
        ServerResponseEntity<List<SpuVO>> spuResponse = spuFeignClient.listSpuBySpuIds(prodIds);
        Map<Long,SpuVO> spuMap = spuResponse.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, p -> p));


//        for (LiveProdStoreExcelVO storeExcelVO : excelList) {
//            if(storeExcelVO.getPriceType()==1){
//                storeExcelVO.
//            }else if() {
//
//            }else{
//
//            }
//            storeExcelVO.setPrice();
//        }


        return null;
    }
}
