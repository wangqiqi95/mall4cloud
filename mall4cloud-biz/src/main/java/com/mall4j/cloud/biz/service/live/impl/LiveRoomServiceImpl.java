

package com.mall4j.cloud.biz.service.live.impl;


import cn.binarywang.wx.miniapp.bean.live.WxMaCreateRoomResult;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveRoomInfo;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveSharedCode;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.wx.wx.constant.LiveConstant;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.dto.live.LiveRoomDto;
import com.mall4j.cloud.biz.dto.live.PageLiveRoomInfo;
import com.mall4j.cloud.biz.dto.live.RoomDetailResponse;
import com.mall4j.cloud.biz.dto.live.WxServerResponse;
import com.mall4j.cloud.biz.mapper.live.LiveRoomMapper;
import com.mall4j.cloud.biz.mapper.live.PageAdapter;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveProdStore;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.model.live.LiveRoomProd;
import com.mall4j.cloud.biz.model.live.LiveRoomShop;
import com.mall4j.cloud.biz.dto.live.LiveRoomParam;
import com.mall4j.cloud.biz.dto.live.LiveRoomShopParam;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.biz.service.live.LiveConfig;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveProdStoreService;
import com.mall4j.cloud.biz.service.live.LiveRoomProdService;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.biz.service.live.LiveRoomShopSaleService;
import com.mall4j.cloud.biz.service.live.LiveRoomShopService;
import com.mall4j.cloud.biz.util.ImageUtil;
import com.mall4j.cloud.biz.util.WxInterfaceUtil;
import com.mall4j.cloud.biz.vo.LiveRoomGuideVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.Json;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
@Service
@AllArgsConstructor
@Slf4j
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements LiveRoomService {

    /**
     * 一页的数量
     */
    private static final Long MAX_PAGE_NUM = 80L;
    private final LiveRoomMapper liveRoomMapper;
    private final WxConfig wxConfig;
    private final LiveLogService liveLogService;
    private final MapperFacade mapperFacade;
    private final WxInterfaceUtil wxUtil;
    private final LiveConfig liveConfig;
    private final LiveRoomShopService liveRoomShopService;
    private final LiveRoomShopSaleService liveRoomShopSaleService;
    private final LiveRoomProdService liveRoomProdService;
    @Autowired
    private final LiveProdStoreService liveProdStoreService;

    @Autowired
    private final WechatLiveMediaService mediaService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLiveRoom(LiveRoom liveRoom) throws WxErrorException {
        // 1.校验今日可用次数并保存or修改商家次数记录
        liveLogService.checkNumsAndSaveLog(liveRoom.getShopId(), LiveInterfaceType.CREATE_ROOM, "创建直播间");

        // 校验时间,开始时间需要在当前时间的10分钟后 并且开始时间不能在6个月后,
        // 开始时间和结束时间间隔不得短于30分钟，不得超过24小时
        checkLiveTime(liveRoom);
        // 填充直播间信息
        WxMaLiveRoomInfo roomInfo = getWxMaLiveRoomInfo(liveRoom);
        try {
            WxMaCreateRoomResult wxMaCreateRoomResult = wxConfig.getWxMaService().getLiveService().createRoom(roomInfo);
            log.info("wxMaCreateRoomResult = {}", wxMaCreateRoomResult);
            // 4.保存直播间信息到数据库
            liveRoom.setRoomTools(Json.toJsonString(liveRoom.getRoomToolsVo()));
            liveRoom.setRoomId(wxMaCreateRoomResult.getRoomId());
            save(liveRoom);
        } catch (WxErrorException e) {
            if (Objects.equals(e.getError().getErrorCode(), LiveConstant.WECHAT_NAME_LENGTH_ERROR)) {
                // 名称长度不符合规则！
                throw new LuckException("名称长度不符合规则");
            }
            if (Objects.equals(e.getError().getErrorCode(), LiveConstant.WECHAT_CHECK)) {
                // 主播微信号未实名认证
                throw new LuckException("主播微信号未实名认证");
            }
            if (!Objects.equals(e.getError().getErrorCode(), 0)) {
                log.error("微信返回错误信息={}", e.getError());
                if (e.getError().getErrorCode() == 300038) {
                    throw new LuckException("创建失败，请在小程序管理后台配置客服");
                }
                // 创建失败，请仔细检查直播间的配置
                throw new LuckException("创建失败，请仔细检查直播间的配置");
            }
        }
        if (Objects.isNull(liveRoom.getRoomId())) {
            // 主播微信号未实名认证
            throw new LuckException("主播微信号未实名认证");
        }
        // 5.保存门店信息
        if (liveRoom.getIsAllShop() == 0) {
            if (CollectionUtils.isEmpty(liveRoom.getShopIds())) {
                throw new LuckException("开播门店不能为空");
            }
            List<LiveRoomShop> roomShop = buildShopInfo(liveRoom.getId(), liveRoom.getShopIds());
            liveRoomShopService.saveBatch(roomShop);
        }
        if (liveRoom.getIsAllSaleShop() == 0) {
            if (CollectionUtils.isEmpty(liveRoom.getShopIds())) {
                throw new LuckException("开播门店不能为空");
            }
            List<LiveRoomShop> roomShop = buildShopInfo(liveRoom.getId(), liveRoom.getSaleShopIds());
            liveRoomShopSaleService.saveBatch(roomShop);
        }

        // 6.导入商品
        liveRoomProdService.saveBatchAndRoomId(liveRoom);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLiveRoomShop(LiveRoomShopParam liveRoomShopParam) {
        // 保存门店信息
        if (liveRoomShopParam.getIsAllShop() == 0) {
            if (CollectionUtils.isEmpty(liveRoomShopParam.getShopIds())) {
                throw new LuckException("开播门店不能为空");
            }
            List<LiveRoomShop> roomShop = buildShopInfo(liveRoomShopParam.getRoomId(), liveRoomShopParam.getShopIds());
            liveRoomShopService.remove(new LambdaQueryWrapper<LiveRoomShop>().eq(LiveRoomShop::getRoomId, liveRoomShopParam.getRoomId()));
            liveRoomShopService.saveBatch(roomShop);
            LiveRoom liveRoom = new LiveRoom();
            liveRoom.setId(liveRoomShopParam.getRoomId());
            liveRoom.setIsAllShop(0);
            updateById(liveRoom);
        }
        if (liveRoomShopParam.getIsAllSaleShop() == 0) {
            if (CollectionUtils.isEmpty(liveRoomShopParam.getShopIds())) {
                throw new LuckException("分销门店不能为空");
            }
            List<LiveRoomShop> roomShop = buildShopInfo(liveRoomShopParam.getRoomId(), liveRoomShopParam.getSaleShopIds());
            liveRoomShopSaleService.remove(new LambdaQueryWrapper<LiveRoomShop>().eq(LiveRoomShop::getRoomId, liveRoomShopParam.getRoomId()));
            liveRoomShopSaleService.saveBatch(roomShop);
            LiveRoom liveRoom = new LiveRoom();
            liveRoom.setId(liveRoomShopParam.getRoomId());
            liveRoom.setIsAllSaleShop(0);
            updateById(liveRoom);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLiveRoom(LiveRoom liveRoom) {
        if (liveRoom.getId() == null || liveRoom.getRoomId() == null) {
            throw new LuckException("修改直播间时，直播间id为空");
        }
        // 校验时间
        checkLiveTime(liveRoom);
        // 填充直播间信息
        WxMaLiveRoomInfo roomInfo = getWxMaLiveRoomInfo(liveRoom);

        // 填入直播间id
        roomInfo.setId(liveRoom.getRoomId());
        try {
            wxConfig.getWxMaService().getLiveService().editRoom(roomInfo);
            // 修改直播间信息到数据库
            liveRoom.setRoomTools(Json.toJsonString(liveRoom.getRoomToolsVo()));
            updateById(liveRoom);
        } catch (WxErrorException e) {
            log.error("微信返回错误信息={}", e.getError());
            if (e.getError().getErrorCode() == -1) {
                throw new LuckException("微信内部处理失败");
            }
            // 修改失败
            throw new LuckException("修改直播间失败，请仔细检查直播间的配置");
        }
        //删除门店关系
        liveRoomShopService.remove(new LambdaQueryWrapper<LiveRoomShop>().eq(LiveRoomShop::getRoomId, liveRoom.getId()));
        liveRoomShopSaleService.remove(new LambdaQueryWrapper<LiveRoomShop>().eq(LiveRoomShop::getRoomId, liveRoom.getId()));

        // 保存门店信息
        if (liveRoom.getIsAllShop() == 0) {
            if (CollectionUtils.isEmpty(liveRoom.getShopIds())) {
                throw new LuckException("开播门店不能为空");
            }
            List<LiveRoomShop> roomShop = buildShopInfo(liveRoom.getId(), liveRoom.getShopIds());
            liveRoomShopService.saveBatch(roomShop);
        }
        if (liveRoom.getIsAllSaleShop() == 0) {
            if (CollectionUtils.isEmpty(liveRoom.getShopIds())) {
                throw new LuckException("分销门店不能为空");
            }
            List<LiveRoomShop> roomShop = buildShopInfo(liveRoom.getId(), liveRoom.getSaleShopIds());
            liveRoomShopSaleService.saveBatch(roomShop);
        }
    }

    private WxMaLiveRoomInfo getWxMaLiveRoomInfo(LiveRoom liveRoom) {
        // 校验图片
        this.checkImg(liveRoom);
        // 1.将背景图，封面图，分享图添加到微信临时素材
        // 发起微信审核, 先获取微信的media_id
        if (StringUtils.isBlank(liveRoom.getCoverImgId())) {
            String coverMediaId = mediaService.getImageMediaId(liveRoom.getCoverImg().startsWith("/") ? liveConfig.getImgDomain() + liveRoom.getCoverImg() : liveRoom.getCoverImg());
            liveRoom.setCoverImgId(coverMediaId);
        }
        if (StringUtils.isBlank(liveRoom.getFeedsImgId())) {
            String feedsImgMediaId = mediaService.getImageMediaId(liveRoom.getFeedsImg().startsWith("/") ? liveConfig.getImgDomain() + liveRoom.getFeedsImg() : liveRoom.getFeedsImg());
            liveRoom.setFeedsImgId(feedsImgMediaId);
        }
        if (StringUtils.isBlank(liveRoom.getShareImgId())) {
            String shareImgMediaId = mediaService.getImageMediaId(liveRoom.getShareImg().startsWith("/") ? liveConfig.getImgDomain() + liveRoom.getShareImg() : liveRoom.getShareImg());
            liveRoom.setShareImgId(shareImgMediaId);
        }
        LiveRoom.RoomToolsVO roomToolsVo = liveRoom.getRoomToolsVo();
        // 2.调用微信添加直播间接口添加直播间
        WxMaLiveRoomInfo roomInfo = mapperFacade.map(liveRoom, WxMaLiveRoomInfo.class);
        roomInfo.setStartTime(liveRoom.getStartTime().getTime() / 1000);
        roomInfo.setEndTime(liveRoom.getEndTime().getTime() / 1000);
        roomInfo.setFeedsImg(liveRoom.getFeedsImgId());
        // 设置微信直播间功能设置信息
        roomInfo.setCloseShare(roomToolsVo.getCloseShare());
        roomInfo.setCloseKf(roomToolsVo.getCloseKf());
        roomInfo.setCloseReplay(roomToolsVo.getCloseReplay());
        roomInfo.setCloseComment(roomToolsVo.getCloseComment());
        roomInfo.setCloseGoods(roomToolsVo.getCloseGoods());
        roomInfo.setCloseLike(roomToolsVo.getCloseLike());
        roomInfo.setCoverImg(liveRoom.getCoverImgId());
        roomInfo.setShareImg(liveRoom.getShareImgId());
        return roomInfo;
    }

    private void checkLiveTime(LiveRoom liveRoom) {
        // 校验时间,开始时间需要在当前时间的10分钟后 并且开始时间不能在6个月后,
        // 开始时间和结束时间间隔不得短于30分钟，不得超过24小时
        Date startTime = liveRoom.getStartTime();
        Date endTime = liveRoom.getEndTime();
        // 1. 开始时间是否在当前时间和十分钟内的时间内，如果为true则表示错误，抛出异常
        // 2. 开始时间是否在当前时间和六个月的时间内，如果为false则表示错误，抛出异常
        boolean expired = DateUtil.isIn(startTime, new Date(), DateUtil.offsetMinute(new Date(), 10)) ||
                !DateUtil.isIn(startTime, new Date(), DateUtil.offsetMonth(new Date(), 6));
        // 开始时间和结束时间间隔不得短于30分钟，不得超过24小时
        // 1. 结束时间是否和起始时间间隔不得短于30分钟，如果为true则表示错误，抛出异常
        // 2. 结束时间是否在起始时间开始后的24小时的时间内，如果为false则表示错误，抛出异常
        boolean expiredEndTime = DateUtil.isIn(endTime, startTime, DateUtil.offsetMinute(startTime, 30)) ||
                !DateUtil.isIn(endTime, startTime, DateUtil.offsetHour(startTime, 24));

        if (DateUtil.compare(startTime, endTime) > 0) {
            // 开始时间不能大于结束时间
            throw new LuckException("开始时间不能大于结束时间");
        }
        if (expired) {
            // 开始时间需要在当前时间的10分钟后,并且不能在6个月后
            throw new LuckException("开始时间需要在当前时间的15分钟后,并且不能在6个月后");
        }
        if (expiredEndTime) {
            // 开始时间和结束时间间隔不得短于30分钟，不得超过24小时
            throw new LuckException("开始时间和结束时间间隔不得短于30分钟，不得超过24小时");
        }
    }

    private List<LiveRoomShop> buildShopInfo(Long roomId, List<Long> shopIds) {
        List<LiveRoomShop> result = new ArrayList<>();
        for (Long shopId : shopIds) {
            LiveRoomShop roomShop = new LiveRoomShop();
            roomShop.setRoomId(roomId);
            roomShop.setShopId(shopId);
            result.add(roomShop);
        }
        return result;
    }


    /**
     * 定时每分钟去同步微信的直播间接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronousWxLiveRoom() {
        List<WxMaLiveResult.RoomInfo> liveInfos;
        try {
            liveInfos = wxConfig.getWxMaService().getLiveService().getLiveInfos();
        } catch (Exception e) {
            log.error("同步直播间信息失败！ {}", e.getMessage());
            return;
        }
        if (CollectionUtils.isEmpty(liveInfos)) {
            return;
        }
        log.info("拉取微信直播间数量为{}", liveInfos.size());
        // 更新增量数据
        List<LiveRoom> list = this.list();
        Map<Integer, List<LiveRoom>> roomMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            roomMap = list.stream().collect(Collectors.groupingBy(LiveRoom::getRoomId));
        }
        List<LiveRoom> updateRoom = new ArrayList<>();
        for (WxMaLiveResult.RoomInfo liveInfo : liveInfos) {
            LiveRoom liveRoom = new LiveRoom();
            if (CollectionUtils.isEmpty(roomMap.get(liveInfo.getRoomId()))) {
                log.info("新增微信直播间数据roomId = {}", liveInfo.getRoomId());
                // 填充直播间信息
                fillRoomInfo(liveRoom, liveInfo);
                save(liveRoom);
                // 填充直播间商品关系
                List<WxMaLiveResult.Goods> goods = liveInfo.getGoods();
                if (!CollectionUtils.isEmpty(goods)) {
                    List<LiveRoomProd> liveRoomProds = new ArrayList<>();
                    for (WxMaLiveResult.Goods good : goods) {
                        log.info("同步微信直播间商品关系数据goodsId = {}", good.getGoodsId());
                        LiveRoomProd liveRoomProd = new LiveRoomProd();
                        liveRoomProd.setRoomId(liveRoom.getId());
                        LiveProdStore one = liveProdStoreService.getOne(new LambdaQueryWrapper<LiveProdStore>().eq(LiveProdStore::getGoodsId, good.getGoodsId()), false);
                        if (one != null) {
                            liveRoomProd.setProdStoreId(one.getLiveProdStoreId());
                        } else {
                            LiveProdStore prodStore = new LiveProdStore();
                            fillRoomGoodsInfo(prodStore, good);
                            liveProdStoreService.save(prodStore);
                            liveRoomProd.setProdStoreId(prodStore.getLiveProdStoreId());
                        }
                        liveRoomProds.add(liveRoomProd);
                    }
                    if (!CollectionUtils.isEmpty(liveRoomProds)) {
                        liveRoomProdService.remove(new LambdaQueryWrapper<LiveRoomProd>().eq(LiveRoomProd::getRoomId, liveRoom.getRoomId()));
                        liveRoomProdService.saveBatch(liveRoomProds);
                    }
                }
            } else {
                log.info("修改微信直播间数据roomId = {}", liveInfo.getRoomId());
                // 填充直播间信息
                liveRoom.setId(roomMap.get(liveInfo.getRoomId()).get(0).getId());
                updateRoomInfo(liveRoom, liveInfo);
                updateRoom.add(liveRoom);
            }
        }
        // 批量更新
        if (!CollectionUtils.isEmpty(updateRoom)) {
            updateBatchById(updateRoom);
        }
    }

    private void fillRoomGoodsInfo(LiveProdStore prodStore, WxMaLiveResult.Goods good) {
        prodStore.setName(good.getName());
        prodStore.setCoverPic(good.getCoverImg());
        prodStore.setUrl(good.getUrl());
        prodStore.setPriceType(good.getPriceType());
        prodStore.setPrice(good.getPrice().doubleValue() / 100);
        prodStore.setPrice2(good.getPrice2().doubleValue() / 100);
        prodStore.setGoodsId(good.getGoodsId());
        prodStore.setStatus(2);
        prodStore.setCreateTime(new Date());
    }

    private void fillRoomInfo(LiveRoom liveRoom, WxMaLiveResult.RoomInfo liveInfo) {
        synRoomInfo(liveRoom, liveInfo);
        liveRoom.setIsAllSaleShop(1);
        liveRoom.setIsAllShop(1);
        liveRoom.setApplyTime(new Date());
    }

    private void synRoomInfo(LiveRoom liveRoom, WxMaLiveResult.RoomInfo liveInfo) {
        liveRoom.setRoomId(liveInfo.getRoomId());
        liveRoom.setName(liveInfo.getName());
        liveRoom.setAnchorName(liveInfo.getAnchorName());
        liveRoom.setAnchorWechat(liveInfo.getAnchorWechat());
        liveRoom.setCoverImg(liveInfo.getCoverImg());
        liveRoom.setShareImg(liveInfo.getShareImg());
        liveRoom.setFeedsImg(liveInfo.getFeedsImg());
        liveRoom.setIsFeedsPublic(liveInfo.getIsFeedsPublic());
        liveRoom.setType(liveInfo.getType());
        liveRoom.setScreenType(liveInfo.getScreenType());
        liveRoom.setLiveStatus(liveInfo.getLiveStatus());
        liveRoom.setStartTime(new Date(liveInfo.getStartTime() * 1000));
        liveRoom.setEndTime(new Date(liveInfo.getEndTime() * 1000));
        Map<String, Integer> roomToolsMap = new HashMap<>();
        roomToolsMap.put("closeKf", liveInfo.getCloseKf());
        roomToolsMap.put("closeLike", liveInfo.getCloseLike());
        roomToolsMap.put("closeGoods", liveInfo.getCloseGoods());
        roomToolsMap.put("closeComment", liveInfo.getCloseComment());
        roomToolsMap.put("closeReplay", liveInfo.getCloseReplay());
        roomToolsMap.put("closeShare", 0);
        liveRoom.setRoomTools(JSONObject.toJSONString(roomToolsMap));
    }

    private void updateRoomInfo(LiveRoom liveRoom, WxMaLiveResult.RoomInfo liveInfo) {
        synRoomInfo(liveRoom, liveInfo);
        liveRoom.setUpdateTime(new Date());
    }

    @Override
    public List<LiveRoomParam> getLivingRoomByProdId(Long prodId) {
        return liveRoomMapper.getLivingRoomByProdId(prodId);
    }

    @Override
    public IPage<LiveRoomDto> pageRoomAndDetail(PageParam<LiveRoomDto> page, LiveRoomParam liveRoom) {
        page.setRecords(liveRoomMapper.pageRoomAndDetail(new PageAdapter(page), liveRoom));
        page.setTotal(liveRoomMapper.countRoomAndDetail(liveRoom));
        return page;
    }

    @Override
    public String getMediaId(String url) {
        return mediaService.getImageMediaId(url);
    }

    @Override
    public String getShareCode(Integer id) throws WxErrorException {
        WxMaLiveSharedCode wxMaLiveSharedCode=wxConfig.getWxMaService().getLiveService().getSharedCode(id, null);
        return Objects.nonNull(wxMaLiveSharedCode)?wxMaLiveSharedCode.getCdnUrl():null;
    }

    @Override
    public LiveRoom getDetailById(Long id) {
        LiveRoom liveRoomDb = getById(id);
        LiveRoom.RoomToolsVO roomToolsVO = JSON.parseObject(liveRoomDb.getRoomTools(), LiveRoom.RoomToolsVO.class);
        liveRoomDb.setRoomToolsVo(roomToolsVO);

        // 关联商品信息
        List<LiveRoomProd> list = liveRoomProdService.list(new LambdaQueryWrapper<LiveRoomProd>().eq(LiveRoomProd::getRoomId, id));
        if (!CollectionUtils.isEmpty(list)) {
            List<LiveProdStore> prodStores = liveProdStoreService.list(new LambdaQueryWrapper<LiveProdStore>()
                    .in(LiveProdStore::getLiveProdStoreId, list.stream().map(LiveRoomProd::getProdStoreId).collect(Collectors.toList())));
            liveRoomDb.setLiveProdStores(prodStores);
        }
        // 开播门店
        if (liveRoomDb.getIsAllShop() == 0) {
            List<LiveRoomShop> shopList = liveRoomShopService.list(new LambdaQueryWrapper<LiveRoomShop>().eq(LiveRoomShop::getRoomId, id));
            if (!CollectionUtils.isEmpty(shopList)) {
                liveRoomDb.setShopIds(shopList.stream().map(LiveRoomShop::getShopId).collect(Collectors.toList()));
            }
        }
        // 分销门店
        if (liveRoomDb.getIsAllSaleShop() == 0) {
            List<LiveRoomShop> shopList = liveRoomShopSaleService.list(new LambdaQueryWrapper<LiveRoomShop>().eq(LiveRoomShop::getRoomId, id));
            if (!CollectionUtils.isEmpty(shopList)) {
                liveRoomDb.setSaleShopIds(shopList.stream().map(LiveRoomShop::getShopId).collect(Collectors.toList()));
            }
        }
        return liveRoomDb;
    }

    @Override
    public PageVO<LiveRoomGuideVO> getSaleLivePage(PageDTO pageDTO, Long shopId) {
        com.mall4j.cloud.common.database.util.PageAdapter pageAdapter = new com.mall4j.cloud.common.database.util.PageAdapter(pageDTO);
        List<LiveRoomGuideVO> result = liveRoomMapper.getSaleLivePage(pageAdapter, shopId);
        PageVO<LiveRoomGuideVO> pageVO = new PageVO<>();
        for (LiveRoomGuideVO vo : result) {
            // 计算直播状态
            Date now = new Date();
            if (vo.getStartTime().after(now)) {
                vo.setLiveStatus(102);
            } else if (vo.getStartTime().before(now) && vo.getEndTime().after(now)) {
                vo.setLiveStatus(101);
            } else {
                vo.setLiveStatus(103);
            }
        }
        pageVO.setList(result);
        pageVO.setTotal(org.springframework.util.CollectionUtils.isEmpty(result) ? 0 : liveRoomMapper.getSaleLivePageCount(shopId));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public PageVO<LiveRoomGuideVO> getAppLivePage(PageDTO pageDTO, Integer type, Long shopId) {
        if (type != 101 && type != 102 && type != 103) {
            throw new LuckException("tab类型参数异常");
        }
        log.info("shopId = {} type = {}", shopId, type);
        com.mall4j.cloud.common.database.util.PageAdapter pageAdapter = new com.mall4j.cloud.common.database.util.PageAdapter(pageDTO);
        List<LiveRoomGuideVO> result = liveRoomMapper.getAppLivePage(pageAdapter, type, shopId);

        PageVO<LiveRoomGuideVO> pageVO = new PageVO<>();
        pageVO.setList(result);
        pageVO.setTotal(org.springframework.util.CollectionUtils.isEmpty(result) ? 0 : liveRoomMapper.getAppLivePageCount(type, shopId));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoomById(Long id, Long shopId) throws WxErrorException {
        // 1.校验今日可用次数并保存or修改商家次数记录
        // liveLogService.checkNumsAndSaveLog(shopId, LiveInterfaceType.REMOVE_LIVE_ROOM, "删除直播间");
        LiveRoom liveRoom = getById(id);
        boolean isDelete = wxConfig.getWxMaService().getLiveService().deleteRoom(liveRoom.getRoomId());
        removeById(id);
        return isDelete;
    }

    /**
     * 读取微信直播间列表
     *
     * @param start 从start行开始读取
     * @throws WxErrorException 微信异常
     */
    private WxServerResponse<List<RoomDetailResponse>> getLiveRoomList(Long start) throws WxErrorException {
        String accessToken = wxConfig.getWxMaService().getAccessToken();
        PageLiveRoomInfo wxLiveRoomInfo = new PageLiveRoomInfo();
        wxLiveRoomInfo.setStart(start);
        wxLiveRoomInfo.setLimit(MAX_PAGE_NUM);
        wxLiveRoomInfo.setAccessToken(accessToken);
        return wxUtil.pageLiveRoom(wxLiveRoomInfo);
    }

    /**
     * 核验直播间图片信息
     *
     * @param liveRoom
     */
    private void checkImg(LiveRoom liveRoom) {
        if (Objects.equals(ImageUtil.imgUrlFileType(liveRoom.getCoverImg()), LiveConstant.WEBP) ||
                Objects.equals(ImageUtil.imgUrlFileType(liveRoom.getFeedsImg()), LiveConstant.WEBP) ||
                Objects.equals(ImageUtil.imgUrlFileType(liveRoom.getShareImg()), LiveConstant.WEBP)) {
            throw new LuckException("图片不能使用webp格式,推荐使用jpg或png");
        }
    }
}
