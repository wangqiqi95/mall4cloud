package com.mall4j.cloud.biz.vo.channels;

import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author baiqingtao
 * @date 2023/2/28
 */
@Data
public class ChannelsSpuSkuDetailVO{
    private ChannelsSpu spu;

    private List<ChannelsSkuVo> skus;

    public ChannelsSpuSkuDetailVO() {
    }

    public ChannelsSpuSkuDetailVO(ChannelsSpu spu, List<ChannelsSkuVo> skus) {
        this.spu = spu;
        this.skus = skus;
    }
}
