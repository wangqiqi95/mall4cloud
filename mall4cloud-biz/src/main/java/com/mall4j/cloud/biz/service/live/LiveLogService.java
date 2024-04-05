

package com.mall4j.cloud.biz.service.live;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveLog;
import com.mall4j.cloud.biz.dto.live.LiveUsableNumParam;

/**
 *
 *
 * @author lhd
 * @date 2020-08-12 16:05:26
 */
public interface LiveLogService extends IService<LiveLog> {

    /**
     * 校验商家和平台可用次数
     * @param shopId 店铺id
     * @param type 校验类型
     * @param desc 备注
     */
    void checkNumsAndSaveLog(Long shopId, LiveInterfaceType type, String desc);

    /**
     * 返回商家和平台可用次数
     * @param shopId 店铺id
     * @param type 校验类型
     * @return 商家和平台可用次数
     */
    LiveUsableNumParam getLiveNum(Long shopId, LiveInterfaceType type);
}
