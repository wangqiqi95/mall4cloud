

package com.mall4j.cloud.biz.service.live;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveProdStore;
import com.mall4j.cloud.biz.vo.LiveProdStoreExcelVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 *
 *
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
public interface LiveProdStoreService extends IService<LiveProdStore> {

    /**
     * 获取直播商品信息
     * @param page 分页信息
     * @param liveProdStore 直播商品信息条件
     * @return 直播商品信息列表
     */
    IPage<LiveProdStore> getPage(PageParam<LiveProdStore> page, LiveProdStore liveProdStore) throws WxErrorException;

    /**
     * 提交审核
     * @param liveProdStore 直播间商品信息
     * @return 结果
     */
    boolean submitVerify(LiveProdStore liveProdStore);


//    PageParam<RoomResponse> listLiveProdsByStatus(PageParam<RoomResponse> page, Integer status, Long shopId);

    /**
     * 通过直播间id获取直播间商品
     * @param page 分页信息
     * @param roomId 直播间id
     * @return 直播间商品
     */
    IPage<LiveProdStore> pageProdByRoomId(PageParam<LiveProdStore> page, Integer roomId);

    /**
     * 同步微信的直播商品
     * @throws WxErrorException 微信异常
     */
    void synchronousWxLiveProds() throws WxErrorException;

    /**
     * 移除微信的直播商品
     * @param liveProdStoreId 直播间id
     * @param shopId 商品id
     * @throws WxErrorException 微信异常
     */
    void removeWxLiveProdById(Long liveProdStoreId, Long shopId) throws WxErrorException;

    /**
     * 修改微信的直播商品
     * @param liveProdStore 直播商品
     * @throws WxErrorException 微信异常
     */
    void updateWxLiveProdById(LiveProdStore liveProdStore) throws WxErrorException;

    /**
     * 删除十天前上架的微信直播商品
     * @throws WxErrorException 微信异常
     */
    void removeOldLiveProd() throws WxErrorException;

    /**
     * 新增直播间商品 并提交审核
     *
     * @param liveProdStore 入参
     */
    void add(LiveProdStore liveProdStore) throws WxErrorException;

    List<LiveProdStoreExcelVO> excelList(LiveProdStore liveProdStore);
}
