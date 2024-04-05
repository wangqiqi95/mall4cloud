package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.product.dto.ErpSkuPriceDTO;

import java.util.List;

/**
 * @Date 2022年4月11日, 0011 10:14
 * @Created by eury
 * 处理渠道价
 */
public interface SpuChannelPriceService {


    /**
     * 手动重置渠道价
     */
    void asyncChannelPriceAndCancelTask(String spuCodes);

    /**
     * 定时任务设置渠道价
     */
    void channelPriceTask(String spuCodes,String updateBy);

    /**
     * 定时任务取消渠道价
     */
    void cancelChannelPriceTask(String spuCodes,String updateBy);

}
