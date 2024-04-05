package com.mall4j.cloud.biz.task;

import com.mall4j.cloud.biz.service.live.LiveProdStoreService;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.biz.service.live.LiveUserService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Component;

/**
 * 直播的定时任务
 * @author LHD
 */
@Component("liveTask")
@AllArgsConstructor
@Slf4j
public class LiveTask {


    private final LiveRoomService liveRoomService;
    private final LiveProdStoreService liveProdStoreService;
    private final LiveUserService liveUserService;

    /**
     * 定时每分钟去同步微信的直播间接口
     */
    @XxlJob("synchronousWxLiveRoomTask")
    public void synchronousWxLiveRoomTask() throws WxErrorException {
        log.info("==============同步微信的直播间信息开始===================");
        liveRoomService.synchronousWxLiveRoom();
        log.info("==============同步微信的直播间信息结束===================");
    }

    /**
     * 定时每5分钟去同步微信的直播间商品接口
     */
    public void synchronousWxLiveProds() throws WxErrorException {
        log.info("==============同步微信的直播商品开始===================");

        liveProdStoreService.synchronousWxLiveProds();

        log.info("==============同步微信的直播商品结束===================");
    }

    /**
     * 定时每分钟去同步微信的直播间接口
     */
    public void offShelfLiveProds() throws WxErrorException {
        log.info("==============删除十天前上架的微信直播商品开始===================");

        liveProdStoreService.removeOldLiveProd();

        log.info("==============删除十天前上架的微信直播商品结束===================");
    }

    /**
     * 定时每5分钟去同步微信的直播间成员列表接口
     */
    public void synchronousWxLiveUsers() throws WxErrorException {
        log.info("==============同步微信的成员列表开始===================");

        liveUserService.synchronousWxLiveUsers();

        log.info("==============同步微信的成员列表结束===================");
    }
}
