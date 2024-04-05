package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.app.RecommendCateVO;
import com.mall4j.cloud.product.dto.RecommendCateDTO;
import com.mall4j.cloud.product.model.RecommendCate;

/**
 * 种草信息分类
 *
 * @author cg
 */
public interface RecommendCateService extends IService<RecommendCate> {

    /**
     * 添加
     *
     * @param recommendDTO 入参
     */
    void add(RecommendCateDTO recommendDTO);

    /**
     * 修改
     *
     * @param recommendDTO 入参
     */
    void adminUpdate(RecommendCateDTO recommendDTO);

    /**
     * @param pageDTO 分页
     * @return 结果
     */
    PageVO<RecommendCateVO> pageList(PageDTO pageDTO, String name);

    /**
     * 设置默认种草分类
     *
     * @param recommendCateId 种草分类id
     * @param type 1-是 0-否
     */
    void setDefault(Long recommendCateId, Integer type);
}
