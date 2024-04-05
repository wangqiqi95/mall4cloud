package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.group.model.GroupSku;
import com.mall4j.cloud.group.mapper.GroupSkuMapper;
import com.mall4j.cloud.group.service.GroupSkuService;
import com.mall4j.cloud.group.vo.GroupSkuVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 拼团活动商品规格
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
@Service
public class GroupSkuServiceImpl implements GroupSkuService {

    @Autowired
    private GroupSkuMapper groupSkuMapper;

    @Override
    public PageVO<GroupSku> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> groupSkuMapper.list());
    }

    @Override
    public GroupSku getByGroupSkuId(Long groupSkuId) {
        return groupSkuMapper.getByGroupSkuId(groupSkuId);
    }

    @Override
    public void save(GroupSku groupSku) {
        groupSkuMapper.save(groupSku);
    }

    @Override
    public void update(GroupSku groupSku) {
        groupSkuMapper.update(groupSku);
    }

    @Override
    public void deleteById(Long groupSkuId) {
        groupSkuMapper.deleteById(groupSkuId);
    }

    @Override
    public void batchSave(List<GroupSku> groupSkuList, Long groupActivityId) {
        if (CollUtil.isEmpty(groupSkuList)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        for (GroupSku groupSku : groupSkuList) {
            groupSku.setGroupActivityId(groupActivityId);
        }
        groupSkuMapper.batchSave(groupSkuList);
    }

    @Override
    public void removeGroupSkuByGroupActivityId(Long groupActivityId) {
        groupSkuMapper.removeGroupSkuByGroupActivityId(groupActivityId);
    }

    @Override
    public void batchUpdate(List<GroupSku> groupSkuList, Long groupActivityId) {
        if (CollUtil.isEmpty(groupSkuList)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        for (GroupSku groupSku : groupSkuList) {
            groupSku.setGroupActivityId(groupActivityId);
        }
        groupSkuMapper.batchUpdate(groupSkuList);
    }

    @Override
    public List<GroupSkuVO> listByGroupActivityId(Long groupActivityId) {
        return groupSkuMapper.listByGroupActivityId(groupActivityId);
    }
}
