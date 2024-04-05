

package com.mall4j.cloud.biz.service.live;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.dto.live.LiveRoomDto;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.dto.live.LiveRoomParam;
import com.mall4j.cloud.biz.dto.live.LiveRoomShopParam;
import com.mall4j.cloud.biz.vo.LiveRoomGuideVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
public interface LiveRoomService extends IService<LiveRoom> {

    /**
     * 保存直播间信息
     *
     * @param liveRoom 直播间信息
     * @throws WxErrorException 微信异常
     */
    void saveLiveRoom(LiveRoom liveRoom) throws WxErrorException;

    /**
     * 同步微信的直播间信息
     *
     * @throws WxErrorException 微信异常
     */
    void synchronousWxLiveRoom() throws WxErrorException;

    /**
     * 获取商品可用的直播间信息
     *
     * @param prodId 商品id
     * @return 直播间信息列表
     */
    List<LiveRoomParam> getLivingRoomByProdId(Long prodId);

    /**
     * 获取直播间信息
     *
     * @param page     分页信息
     * @param liveRoom 直播间信息
     * @return 直播间信息列表
     */
    IPage<LiveRoomDto> pageRoomAndDetail(PageParam<LiveRoomDto> page, LiveRoomParam liveRoom);

    /**
     * 删除直播间
     *
     * @param id     直播间id
     * @param shopId 店铺id
     * @return 是否删除成功
     */
    Boolean removeRoomById(Long id, Long shopId) throws WxErrorException;

    /**
     * app获取直播列表
     *
     * @param pageDTO 分页参数
     * @param type    类型
     * @param shopId  关联门店id
     * @return 结果集
     */
    PageVO<LiveRoomGuideVO> getAppLivePage(PageDTO pageDTO, Integer type, Long shopId);

    /**
     * 导购端获取直播列表
     *
     * @param pageDTO 分页参数
     * @param shopId  关联门店id
     * @return 结果集
     */
    PageVO<LiveRoomGuideVO> getSaleLivePage(PageDTO pageDTO, Long shopId);

    /**
     * 获取直播间详情
     *
     * @param id
     * @return
     */
    LiveRoom getDetailById(Long id);

    /**
     * 获取开播二维码
     *
     * @param id 直播间id
     * @return 结果集
     */
    String getShareCode(Integer id) throws WxErrorException;

    /**
     * 获取资源素材id mediaId
     *
     * @param url 地址
     * @return 素材id
     */
    String getMediaId(String url);

    /**
     * 修改直播间
     *
     * @param liveRoom
     */
    void updateLiveRoom(LiveRoom liveRoom) throws WxErrorException;

    /**
     * 设置直播间门店关系
     *
     * @param liveRoomShopParam
     */
    void saveLiveRoomShop(LiveRoomShopParam liveRoomShopParam);
}
