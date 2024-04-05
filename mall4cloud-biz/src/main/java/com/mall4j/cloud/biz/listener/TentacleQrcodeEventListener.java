package com.mall4j.cloud.biz.listener;

import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.event.TentacleQrcodeEvent;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleStoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Date 2022年6月8日, 0008 14:02
 * @Created by eury
 */
@Slf4j
@Component("TentacleQrcodeEventListener")
@AllArgsConstructor
public class TentacleQrcodeEventListener {

    @Autowired
    private WeixinQrcodeTentacleStoreService tentacleStoreService;

    @Async
    @EventListener(TentacleQrcodeEvent.class)
    public void soldSpuEvent(TentacleQrcodeEvent event) {
        log.info("----异步 保存门店及生成二维码信息--------");

        WeixinQrcodeTentacleDTO tentacleDTO=event.getTentacleDTO();

        WeixinQrcodeTentacle weixinQrcodeTentacle=event.getWeixinQrcodeTentacle();

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(tentacleDTO.getDownLoadHisId());

        tentacleStoreService.saveTentacleStore(weixinQrcodeTentacle,tentacleDTO.getStoreIds(),tentacleDTO.getDownLoadHisId());

    }

}
