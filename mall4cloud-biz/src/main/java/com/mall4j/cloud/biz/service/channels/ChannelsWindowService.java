package com.mall4j.cloud.biz.service.channels;

/**
 * 视频号4.0 橱窗
 * @date 2023/3/9
 */
public interface ChannelsWindowService {
    /**
     * 橱窗添加商品
     * @param channelsSpuId 内部商品ID
     * @param isHideForWindow 个人橱窗是否隐藏，默认false
     */
    void addProduct(Long channelsSpuId, Boolean isHideForWindow);

    /**
     * 橱窗下架商品
     * @param channelsSpuId 内部商品ID
     */
    void offProduct(Long channelsSpuId);
}
