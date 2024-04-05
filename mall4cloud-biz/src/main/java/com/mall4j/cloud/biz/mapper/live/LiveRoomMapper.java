
package com.mall4j.cloud.biz.mapper.live;


import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.live.LiveRoomDto;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.dto.live.LiveRoomParam;
import com.mall4j.cloud.biz.vo.LiveRoomGuideVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {

    /**
     * 批量修改直播间信息
     * @param roomInfos 直播间信息
     */
    void updateBatchByRoomId(@Param("roomInfos") List<WxMaLiveResult.RoomInfo> roomInfos);

    /**
     * 通过商品id获取直播间信息
     * @param prodId 商品id
     * @return 直播间信息
     */
    List<LiveRoomParam> getLivingRoomByProdId(@Param("prodId") Long prodId);

    /**
     * 获取直播间详细信息
     * @param page 分页信息
     * @param liveRoom 直播间信息
     * @return 直播间信息
     */
    List<LiveRoomDto> pageRoomAndDetail(@Param("adapter") PageAdapter page, @Param("liveRoom") LiveRoomParam liveRoom);

    /**
     * 获取直播间数量
     * @param liveRoom 直播间信息
     * @return 直播间数量
     */
    long countRoomAndDetail(@Param("liveRoom") LiveRoomParam liveRoom);

    /**
     * app获取直播列表
     *
     * @param pageAdapter 分页参数
     * @param type 类型
     * @param shopId 关联门店id
     * @return 结果集
     */
    List<LiveRoomGuideVO> getAppLivePage(@Param("page") com.mall4j.cloud.common.database.util.PageAdapter pageAdapter, @Param("type") Integer type, @Param("shopId") Long shopId);

    /**
     * app获取直播列表
     *
     * @param type 类型
     * @param shopId 关联门店id
     * @return 结果集
     */
    Long getAppLivePageCount(@Param("type") Integer type, @Param("shopId") Long shopId);

    /**
     * 导购端获取直播列表
     *
     * @param pageAdapter 分页参数
     * @param shopId 关联门店id
     * @return 结果集
     */
    List<LiveRoomGuideVO> getSaleLivePage(@Param("page") com.mall4j.cloud.common.database.util.PageAdapter pageAdapter, @Param("shopId") Long shopId);

    /**
     * 导购端获取直播列表
     *
     * @param shopId 关联门店id
     * @return 结果集
     */
    Long getSaleLivePageCount(@Param("shopId") Long shopId);
}
