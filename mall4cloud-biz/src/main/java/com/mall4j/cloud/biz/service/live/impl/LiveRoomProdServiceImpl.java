
package com.mall4j.cloud.biz.service.live.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.mapper.live.LiveProdStoreMapper;
import com.mall4j.cloud.biz.mapper.live.LiveRoomMapper;
import com.mall4j.cloud.biz.mapper.live.LiveRoomProdMapper;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveProdStore;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.model.live.LiveRoomProd;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveProdStoreService;
import com.mall4j.cloud.biz.service.live.LiveRoomProdService;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.biz.util.WxInterfaceUtil;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
@Service
@AllArgsConstructor
@Slf4j
public class LiveRoomProdServiceImpl extends ServiceImpl<LiveRoomProdMapper, LiveRoomProd> implements LiveRoomProdService {

    private final LiveLogService liveLogService;
    private final WxConfig wxConfig;
    private final LiveRoomMapper liveRoomService;
    @Resource
    private LiveProdStoreMapper liveProdStoreMapper;

    /**
     * 批量保存直播间和商品的关系
     * @param liveRoom 直播间信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchAndRoomId(LiveRoom liveRoom) throws WxErrorException {
        // 1.校验今日可用次数并保存or修改商家次数记录
        liveLogService.checkNumsAndSaveLog(liveRoom.getShopId(), LiveInterfaceType.IMPORT_ROOM_PROD, "导入直播间商品");

        List<LiveProdStore> liveProdStores = liveRoom.getLiveProdStores();
        if(CollectionUtils.isEmpty(liveProdStores)){
            // 请选择商品
            throw new LuckException("请选择商品");
        }
        // 2.保存直播间商品关联表
        List<LiveRoomProd> liveRoomProds = new ArrayList<>();
        List<Integer> liveProdIds = new ArrayList<>();
        for (LiveProdStore liveProdStore : liveProdStores) {
            LiveRoomProd liveRoomProd = new LiveRoomProd();
            liveRoomProd.setRoomId(liveRoom.getId());
            if (liveProdStore.getGoodsId() == null) {
                throw new LuckException("未审核通过的商品不允许导入直播间");
            }
            LiveProdStore one = liveProdStoreMapper.selectOne(new LambdaQueryWrapper<LiveProdStore>().eq(LiveProdStore::getGoodsId, liveProdStore.getGoodsId()));
            liveRoomProd.setProdStoreId(one.getLiveProdStoreId());
            liveRoomProds.add(liveRoomProd);
            liveProdIds.add(liveProdStore.getGoodsId());
        }
        saveBatch(liveRoomProds);
        // 3.获取直播间信息
        log.info("liveRoom = {}", liveRoom);
        LiveRoom liveRoomDb;
        if (liveRoom.getRoomId() != null && liveRoom.getRoomId() != 0) {
            liveRoomDb = liveRoomService.selectOne(new LambdaQueryWrapper<LiveRoom>().eq(LiveRoom::getRoomId, liveRoom.getRoomId()));
        } else if (liveRoom.getId() != null && liveRoom.getId() != 0) {
            liveRoomDb = liveRoomService.selectById(liveRoom.getId());
        } else {
            // 添加微信直播间商品失败
            throw new LuckException("添加微信直播间商品失败");
        }

        // 4.导入商品
        boolean addStatus = wxConfig.getWxMaService().getLiveService().addGoodsToRoom(liveRoomDb.getRoomId(), liveProdIds);
        if(!addStatus){
            // 添加微信直播间商品失败
            throw new LuckException("添加微信直播间商品失败");
        }
    }
}
