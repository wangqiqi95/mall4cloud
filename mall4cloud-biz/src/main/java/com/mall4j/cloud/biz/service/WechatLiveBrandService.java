package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.LiveBrandDTO;
import com.mall4j.cloud.biz.vo.LiveBrandVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatLiveBrandService {

    /**
     * 查询品牌列表
     * @return
     */
    PageVO<LiveBrandVO> list(PageDTO pageDTO);

    /**
     * 添加品牌
     * @param liveBrandDTO
     */
    void add(LiveBrandDTO liveBrandDTO);

    int syncAuditResult(String auditId,String auditContent,Integer status,Long wxBrandId);

    /**
     * 查询已审核的品牌列表
     * @return
     */
    List<LiveBrandVO> auditedList();
}
