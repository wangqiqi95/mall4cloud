package com.mall4j.cloud.flow.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.flow.model.UserVisitProdAnalysis;
import com.mall4j.cloud.flow.mapper.UserVisitProdAnalysisMapper;
import com.mall4j.cloud.flow.service.UserVisitProdAnalysisService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 流量分析—用户流量商品数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Service
public class UserVisitProdAnalysisServiceImpl implements UserVisitProdAnalysisService {

    @Autowired
    private UserVisitProdAnalysisMapper userVisitProdAnalysisMapper;

    @Override
    public PageVO<UserVisitProdAnalysis> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userVisitProdAnalysisMapper.list());
    }

    @Override
    public UserVisitProdAnalysis getByUserAnalysisId(Long userAnalysisId) {
        return userVisitProdAnalysisMapper.getByUserAnalysisId(userAnalysisId);
    }

    @Override
    public void save(UserVisitProdAnalysis userVisitProdAnalysis) {
        userVisitProdAnalysisMapper.save(userVisitProdAnalysis);
    }

    @Override
    public void update(UserVisitProdAnalysis userVisitProdAnalysis) {
        userVisitProdAnalysisMapper.update(userVisitProdAnalysis);
    }

    @Override
    public void deleteById(Long userAnalysisId) {
        userVisitProdAnalysisMapper.deleteById(userAnalysisId);
    }
}
