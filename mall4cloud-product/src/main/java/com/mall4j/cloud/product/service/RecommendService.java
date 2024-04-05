package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.app.RecommendDetailVO;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.product.dto.RecommendAdminDTO;
import com.mall4j.cloud.product.dto.RecommendAdminPageParamsDTO;
import com.mall4j.cloud.product.dto.RecommendDTO;
import com.mall4j.cloud.product.model.Recommend;

/**
 * 种草信息
 *
 * @author cg
 */
public interface RecommendService extends IService<Recommend> {

    /**
     * app保存种草信息
     *
     * @param recommendDTO 种草
     */
    void add(RecommendDTO recommendDTO);

    /**
     * admin保存种草信息
     *
     * @param recommendDTO 种草
     */
    void add(RecommendAdminDTO recommendDTO);

    /**
     * app种草列表
     *
     * @param pageDTO 分页信息
     * @param type 类型：1-最新 | 2-最热
     * @param title 标题
     * @return 结果
     */
    PageVO<RecommendVO> pageList(PageDTO pageDTO, Integer type, String title);

    /**
     * 我发布的种草列表
     *
     * @param pageDTO 分页信息
     * @return 结果
     */
    PageVO<RecommendVO> myPage(PageDTO pageDTO);

    /**
     * 点赞
     *
     * @param recommendId 种草id
     * @param type 1-点赞 |0-取消点赞
     */
    void praise(Long recommendId, Integer type);

    /**
     * 收藏
     *
     * @param recommendId 种草id
     * @param type 1-收藏 |0-取消收藏
     */
    void collect(Long recommendId, Integer type);

    /**
     * 分享
     *
     * @param recommendId 种草id
     */
    void share(Long recommendId);

    /**
     * 我收藏的种草列表
     *
     * @param pageDTO 分页
     * @return 结果
     */
    PageVO<RecommendVO> collectPage(PageDTO pageDTO);

    /**
     * app查看详情
     *
     * @param recommendId 种草id
     * @return 结果
     */
    RecommendDetailVO detail(Long recommendId);

    /**
     * app删除种草
     *
     * @param recommendId 种草id
     */
    void appDelete(Long recommendId);

    /**
     * app修改种草
     *
     * @param recommendDTO 种草信息
     */
    void appUpdate(RecommendDTO recommendDTO);

    /**
     * app浏览种草
     *
     * @param recommendId 种草id
     */
    void read(Long recommendId);

    /**
     * admin删除种草
     *
     * @param recommendId 种草id
     */
    void adminDelete(Long recommendId);

    /**
     * 审核种草
     *
     * @param recommendId 种草id
     * @param status 2-审核通过 | 3-已驳回
     */
    void examine(Long recommendId, Integer status);

    /**
     * admin修改种草
     *
     * @param recommendDTO 种草信息
     */
    void adminUpdate(RecommendAdminDTO recommendDTO);

    /**
     * admin查询种草列表
     *
     * @param pageDTO 分页
     * @param params 查询参数
     * @return 结果
     */
    PageVO<RecommendVO> adminPageList(PageDTO pageDTO, RecommendAdminPageParamsDTO params);
}
