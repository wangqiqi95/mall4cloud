package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.app.RecommendCateVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.RecommendCateDTO;
import com.mall4j.cloud.product.mapper.RecommendCateMapper;
import com.mall4j.cloud.product.model.RecommendCate;
import com.mall4j.cloud.product.service.RecommendCateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 种草信息分类
 *
 * @author cg
 */
@Service
public class RecommendCateServiceImpl extends ServiceImpl<RecommendCateMapper, RecommendCate> implements RecommendCateService {

    @Resource
    private RecommendCateMapper recommendCateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long recommendCateId, Integer type) {
        if (type == 1) {
            // 恢复其他默认数据
            update(new LambdaUpdateWrapper<RecommendCate>().eq(RecommendCate::getDisable, 0)
                    .set(RecommendCate::getIsDefault, 0));
        }
        RecommendCate build = RecommendCate.builder().recommendCateId(recommendCateId).isDefault(type).build();
        updateById(build);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageVO<RecommendCateVO> pageList(PageDTO pageDTO, String name) {
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        // 查询用户收藏记录
        LambdaQueryWrapper<RecommendCate> listWrapper = new LambdaQueryWrapper<RecommendCate>()
                .eq(RecommendCate::getDisable, 0);
        if (StringUtils.isNotBlank(name)) {
            listWrapper.like(RecommendCate::getName, name);
        }
        PageVO<RecommendCateVO> pageVO = new PageVO<>();
        Integer count = recommendCateMapper.selectCount(new LambdaQueryWrapper<RecommendCate>()
                .eq(RecommendCate::getDisable, 0));
        pageVO.setTotal(count.longValue());
        if (count == 0) {
            pageVO.setList(new ArrayList<>());
            return pageVO;
        }

        listWrapper.orderByDesc(RecommendCate::getCreateTime)
                .last("limit " + pageAdapter.getBegin() + " ," + pageAdapter.getSize());
        List<RecommendCate> cateList = list(listWrapper);

        List<RecommendCateVO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cateList)) {
            cateList.forEach(cate -> {
                RecommendCateVO recommendVO = new RecommendCateVO();
                BeanUtils.copyProperties(cate, recommendVO);
                result.add(recommendVO);
            });
        }
        pageVO.setList(result);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public void adminUpdate(RecommendCateDTO recommendDTO) {
        if (recommendDTO.getRecommendCateId() == null) {
            throw new IllegalArgumentException("参数异常");
        }
        RecommendCate recommendCate = new RecommendCate();
        BeanUtils.copyProperties(recommendDTO,recommendCate);
        updateById(recommendCate);
    }

    @Override
    public void add(RecommendCateDTO recommendDTO) {
        RecommendCate recommendCate = new RecommendCate();
        BeanUtils.copyProperties(recommendDTO,recommendCate);
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        recommendCate.setUserId(userInfoInTokenBO.getUserId());
        recommendCate.setUserName(userInfoInTokenBO.getUsername());
        recommendCate.setDisable(0);
        save(recommendCate);
    }
}
