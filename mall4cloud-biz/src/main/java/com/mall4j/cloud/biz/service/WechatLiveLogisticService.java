package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.model.WechatLogisticsMappingDO;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatLiveLogisticService {

    /**
     * 查询基本物流列表
     * @param query
     * @return
     */
    List<LiveLogisticsVO> baseList(String query);

    /**
     * 查询物流映射列表
     * @return
     */
    PageVO<LiveLogisticsVO> list(PageDTO page);

    /**
     * 添加物流映射
     * @param liveLogisticsVO
     */
    void add(LiveLogisticsVO liveLogisticsVO);

    /**
     * 删除物流映射
     * @param liveLogisticsVO
     */
    void delete(LiveLogisticsVO liveLogisticsVO);

    LiveLogisticsVO getByDeliveryId(Long deliveryId);

    WechatLogisticsMappingDO getByWechatDeliveryId(String deliveryId);

    WechatLogisticsMappingDO getDefualtWechatDelivery();
    
    LiveLogisticsVO detail(Long id);
    
    void update(LiveLogisticsVO liveLogisticsVO);
}
