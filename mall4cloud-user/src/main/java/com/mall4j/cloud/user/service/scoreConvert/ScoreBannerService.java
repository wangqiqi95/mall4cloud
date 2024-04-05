package com.mall4j.cloud.user.service.scoreConvert;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.vo.scoreConvert.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 积分Banner
 * @author shijing
 */

public interface ScoreBannerService {

    /**
     * 积分Banner列表
     * @param param 查询信息
     */
    ServerResponseEntity<PageInfo<ScoreBannerListVO>> list(ScoreConvertListDTO param);

    /**
     * 保存换券活动
     * @param param 新增信息
     */
    ServerResponseEntity<Void> save(BannerSaveOrUpdateDTO param);

    ServerResponseEntity<List<BannerShopDTO>> selectActivityShop(BannerSaveOrUpdateDTO param);

    void export(List<BannerShopDTO> list,Long downLoadHisId, HttpServletResponse response);


    /**
     * 修改积分Banner
     * @param param 修改信息
     */
    ServerResponseEntity<Void> update(BannerSaveOrUpdateDTO param);


    /**
     * 修改积分Banner
     * @param param 修改信息
     */
    ServerResponseEntity<Void> updateWeight(BannerSaveOrUpdateDTO param);

    /**
     * 修改积分Banner状态
     * @param id 主键id
     * @param status 状态
     */
    ServerResponseEntity<Void> updateStatus(Long id, Integer status);

    /**
     * 删除积分兑换活动
     * @param convertId 主键id
     */
    ServerResponseEntity<Void> delete(Long id);

    /**
     * 积分Banner活动详情
     * @param id 主键id
     */
    ServerResponseEntity<BannerDetailVO> selectDetail(Long id);


    /**
     * 积分Banner图（商城小程序）
     * @param
     */
    ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerListForApp();

    ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerData(Long shopId,Integer type);



}
