

package com.mall4j.cloud.biz.service.live;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.model.live.LiveRoomProd;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 *
 *
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
public interface LiveRoomProdService extends IService<LiveRoomProd> {

    /**
     * 批量保存直播间商品信息
     * @param liveRoom 直播间信息
     * @throws WxErrorException 微信返回异常
     */
    void saveBatchAndRoomId(LiveRoom liveRoom) throws WxErrorException;
}
