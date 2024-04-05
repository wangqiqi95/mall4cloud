package com.mall4j.cloud.user.service.scoreConvert;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterListVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterLogVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterVO;
import javax.servlet.http.HttpServletResponse;

/**
 * 积分兑换
 *
 * @author shijing
 * @date 2021-12-10 18:07:21
 */

public interface ScoreBarterService {

    /**
     * 积分换物列表
     * @param param 查询信息
     */
    ServerResponseEntity<PageInfo<ScoreBarterListVO>> list(ScoreConvertListDTO param);

    /**
     * 保存积分换物活动
     * @param param 新增信息
     */
    ServerResponseEntity<Void> save(ScoreBarterSaveDTO param);

    /**
     * 修改积分兑换
     * @param param 修改信息
     */
    ServerResponseEntity<Void> update(ScoreBarterUpdateDTO param);

    /**
     * 积分兑换详情
     * @param convertId 主键id
     * @return ScoreConvert 积分兑换详情
     */
    ServerResponseEntity<ScoreBarterVO> selectDetail(Long convertId);

    /**
     * 修改积分兑换状态
     * @param convertId 主键id
     * @param status 状态
     */
    ServerResponseEntity<Void> updateConvertStatus(Long convertId, Integer status);

    /**
     * 删除积分兑换活动
     * @param convertId 主键id
     */
    ServerResponseEntity<Void> deleteConvert(Long convertId);

    /**
     * 增加库存
     * @param convertId 主键id
     */
    ServerResponseEntity<Void> addInventory(Long convertId,Long num);

    /**
     * 兑换记录列表
     * @param param 兑换列表搜索参数
     */
    ServerResponseEntity<PageInfo<ScoreBarterLogVO>> logList(ScoreBarterLogListDTO param);

    /**
     * 添加物流编号
     * @param courierCode 物流编号
     */
    ServerResponseEntity<Void> addCourierCode(Long id,String courierCode,String note);

    /**
     * 导出兑换记录
     * @param param 兑换列表搜索参数
     */
    void export(ScoreBarterLogListDTO param, HttpServletResponse response);

}
