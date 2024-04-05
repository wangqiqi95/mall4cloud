

package com.mall4j.cloud.biz.mapper.live;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.biz.dto.live.PageParam;
import com.mall4j.cloud.biz.model.live.LiveProdStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
public interface LiveProdStoreMapper extends BaseMapper<LiveProdStore> {

    /**
     * 获取直播商品分页信息
     * @param page 分页信息
     * @param liveProdStore 直播商品信息
     * @return 直播商品分页信息
     */
    IPage<LiveProdStore> getPage(@Param("page") PageParam<LiveProdStore> page, @Param("liveProdStore") LiveProdStore liveProdStore);

    /**
     * 获取直播商品分页信息
     * @param liveProdStore 直播商品信息
     * @return 直播商品分页信息
     */
    Long getPageCount(@Param("liveProdStore") LiveProdStore liveProdStore);

    /**
     * 根据直播间id获取直播间的商品信息
     * @param page 分页信息
     * @param roomId 房间id
     * @return 直播间商品信息
     */
    IPage<LiveProdStore> pageProdByRoomId(PageParam<LiveProdStore> page, @Param("roomId") Integer roomId);

    /**
     * 批量修改直播商品信息
     * @param prodStoreList 直播商品列表
     */
    void updateBatch(@Param("prodStoreList") List<LiveProdStore> prodStoreList);
}
