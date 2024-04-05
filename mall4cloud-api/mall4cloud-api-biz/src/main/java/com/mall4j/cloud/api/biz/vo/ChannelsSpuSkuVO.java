package com.mall4j.cloud.api.biz.vo;

import lombok.Data;

import java.util.List;

/**
 *
 * @date 2023/3/15
 */
@Data
public class ChannelsSpuSkuVO {

    private ChannelsSpuVO channelsSpuVO;

    private List<ChannelsSkuVO> channelsSkuVO;
}
