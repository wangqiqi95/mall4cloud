package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.LiveCategoryDTO;
import com.mall4j.cloud.api.biz.dto.livestore.response.ThirdCatList;
import com.mall4j.cloud.biz.model.WechatCatogoryQualificationDO;
import com.mall4j.cloud.biz.vo.LiveCategoryVO;

import java.util.List;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatLiveCategoryService {

    /**
     * 查询腾讯的类目列表
     * @param query 查询关键字
     * @param all 1-查询全部 0(默认)-只查需要资质的
     * @return
     */
    List<LiveCategoryVO> baseList(String query, String all);



    /**
     * 查询腾讯的类目列表
     * @param query 查询关键字
     * @return
     */
    List<LiveCategoryVO> applyList(String query);

    /**
     * 查询基本类目信息
     * @param wxCategoryId .
     * @return
     */
    ThirdCatList baseOne(String wxCategoryId);

    /**
     * 查询类目列表
     * @return .
     */
    List<LiveCategoryVO> list();

    /**
     * 添加类目
     * @param liveCategoryDTO .
     */
    void add(LiveCategoryDTO liveCategoryDTO);

    /**
     * 根据微信类目id查询数据库中类目
     * @param wxCategoryId
     * @return
     */
    WechatCatogoryQualificationDO one(String wxCategoryId);

    /**
     * 类目审核结果更新
     */
    void auditResult(String auditId,int status,String reason);
}
