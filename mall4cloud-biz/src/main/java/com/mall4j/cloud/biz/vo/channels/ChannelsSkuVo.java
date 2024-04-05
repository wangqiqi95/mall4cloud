package com.mall4j.cloud.biz.vo.channels;

import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import lombok.Data;

/**
 * @date 2023/3/15
 */
@Data
public class ChannelsSkuVo {
    private ChannelsSku channelsSku;

    private SkuAppVO skuVO;
}
