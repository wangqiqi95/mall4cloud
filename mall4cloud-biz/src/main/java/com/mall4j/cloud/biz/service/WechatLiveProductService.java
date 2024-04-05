package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.LiveProductDTO;
import com.mall4j.cloud.biz.vo.LiveProductVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatLiveProductService {

    /**
     * 分页查询商品列表
     * @param page
     * @param onsale 5-上架 11-自主下架 13-违规下架/风控系统下架
     * @return
     */
    PageVO<LiveProductVO> list(PageDTO page, Integer onsale);

    /**
     * 添加商品
     * @param liveProductDTO
     */
    void add(LiveProductDTO liveProductDTO);

    /**
     * 更新商品
     * @param liveProductDTO
     */
    void update(LiveProductDTO liveProductDTO);

    /**
     * 上架商品
     * @param liveProductDTO
     */
    void onsale(LiveProductDTO liveProductDTO);

    /**
     * 下架商品
     * @param liveProductDTO
     */
    void offsale(LiveProductDTO liveProductDTO);

    /**
     * 删除商品
     * @param liveProductDTO
     */
    void delete(LiveProductDTO liveProductDTO);

    /**
     * 添加商品变更
     */
    void recordChange(String productId);

    /**
     * 查询单个商品
     */
    LiveProductDTO one(String productId);
}
