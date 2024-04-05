package com.mall4j.cloud.biz.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.biz.constant.channels.LiveStoreSharerBindStatus;
import com.mall4j.cloud.biz.dto.channels.sharer.SharerBindReqDto;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSharerMapper;
import com.mall4j.cloud.biz.model.channels.LiveStoreSharer;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * @Description 视频号分享员同步任务
 * @Author axin
 * @Date 2023-02-23 18:09
 **/
@Component
@Slf4j
@RequestMapping
public class ChannelsSharerTask {
    @Autowired
    private ChannelsSharerService channelsSharerService;
    @Autowired
    private ChannelsSharerMapper channelsSharerMapper;

    @XxlJob("syncSharerSuccBind")
    public void syncSharerSuccBind(){
        log.info("同步分享员绑定任务开始======================");
        channelsSharerService.syncSuccBind();
        log.info("同步分享员绑定任务结束======================");
    }

    @XxlJob("syncSharerBind")
    public void syncSharerBind(){
        log.info("同步分享员创建绑定任务开始======================");
        List<LiveStoreSharer> liveStoreSharers = channelsSharerMapper.selectList(Wrappers.lambdaQuery(LiveStoreSharer.class)
                .eq(LiveStoreSharer::getBindStatus, LiveStoreSharerBindStatus.INIT.getValue()));
        if(CollectionUtils.isNotEmpty(liveStoreSharers)) {
            for (LiveStoreSharer liveStoreSharer : liveStoreSharers) {
                channelsSharerService.syncBind(liveStoreSharer);
            }
        }
        log.info("同步分享员创建绑定任务结束======================");
    }

    @XxlJob("syncSharerExpire")
    public void syncSharerExpire(){
        log.info("同步分享员过期任务开始======================");
        List<LiveStoreSharer> liveStoreSharers = channelsSharerMapper.selectList(Wrappers.lambdaQuery(LiveStoreSharer.class)
                .lt(LiveStoreSharer::getQrcodeImgExpireTime, new Date())
                .isNull(LiveStoreSharer::getBindTime)
                .eq(LiveStoreSharer::getBindStatus,LiveStoreSharerBindStatus.BIND_WAIT.getValue())
        );
        if(CollectionUtils.isNotEmpty(liveStoreSharers)){
            for (LiveStoreSharer liveStoreSharer : liveStoreSharers) {
                liveStoreSharer.setBindStatus(LiveStoreSharerBindStatus.BIND_EXPIRE.getValue());
                channelsSharerMapper.updateById(liveStoreSharer);
            }
        }
        log.info("同步分享员过期任务结束======================");
    }

}
