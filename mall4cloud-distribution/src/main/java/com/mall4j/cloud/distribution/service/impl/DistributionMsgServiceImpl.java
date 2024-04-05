package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionMsg;
import com.mall4j.cloud.distribution.mapper.DistributionMsgMapper;
import com.mall4j.cloud.distribution.service.DistributionMsgService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 分销公共信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
@Service
public class DistributionMsgServiceImpl implements DistributionMsgService {

    @Autowired
    private DistributionMsgMapper distributionMsgMapper;

    @Override
    public PageVO<DistributionMsg> page(PageDTO pageDTO,String msgTitle) {
        return PageUtil.doPage(pageDTO, () -> distributionMsgMapper.list(msgTitle));
    }

    @Override
    public PageVO<DistributionMsg> pageApp(PageDTO pageDTO,Integer isTop) {
        return PageUtil.doPage(pageDTO,() -> distributionMsgMapper.listApp(isTop));
    }

    @Override
    public DistributionMsg getByMsgId(Long msgId) {
        return distributionMsgMapper.getByMsgId(msgId);
    }

    @Override
    public void save(DistributionMsg distributionMsg) {
        distributionMsgMapper.save(distributionMsg);
    }

    @Override
    public void update(DistributionMsg distributionMsg) {
        distributionMsgMapper.update(distributionMsg);
    }

    @Override
    public void deleteById(Long msgId) {
        distributionMsgMapper.deleteById(msgId);
    }

    @Override
    public void deleteBatch(List<Long> msgIds) {
        if (CollectionUtil.isNotEmpty(msgIds)){
            distributionMsgMapper.deleteBatch(msgIds);
        }
    }
}
