package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.product.dto.RecommendAdminPageParamsDTO;
import com.mall4j.cloud.product.model.Recommend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 种草信息
 *
 * @author cg
 */
public interface RecommendMapper extends BaseMapper<Recommend> {


    List<RecommendVO> pageList(@Param("page") PageAdapter pageAdapter, @Param("type") Integer type, @Param("userId") Long userId, @Param("title") String title);

    List<RecommendVO> adminPageList(@Param("page") PageAdapter pageAdapter, @Param("params") RecommendAdminPageParamsDTO params);

    Long pageListCount();

    Long adminPageListCount(@Param("params") RecommendAdminPageParamsDTO params);

    List<RecommendVO> myPage(@Param("page") PageAdapter pageAdapter, @Param("userId") Long userId);

    Long myPageCount(@Param("userId") Long userId);
}
