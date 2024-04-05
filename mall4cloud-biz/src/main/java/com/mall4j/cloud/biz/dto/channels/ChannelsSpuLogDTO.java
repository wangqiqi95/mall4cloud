package com.mall4j.cloud.biz.dto.channels;

import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @date 2023/3/10
 */
@Data
public class ChannelsSpuLogDTO {
    private ChannelsSpu channelsSpu;

    private List<ChannelsSku> channelsSkus;

    /**
     * 1参数更新 2库存更新
     */
    private Integer updateType;

    @Override
    public String toString() {
        return "ChannelsSpuLogDTO{" +
                "channelsSpu=" + channelsSpu +
                ", channelsSkus=" + channelsSkus +
                ", updateType=" + updateType +
                '}';
    }

    public ChannelsSpuLogDTO() {
    }

    public ChannelsSpuLogDTO(ChannelsSpu channelsSpu, List<ChannelsSku> channelsSkus, Integer updateType) {
        this.channelsSpu = channelsSpu;
        this.channelsSkus = channelsSkus;
        this.updateType = updateType;
    }
}
