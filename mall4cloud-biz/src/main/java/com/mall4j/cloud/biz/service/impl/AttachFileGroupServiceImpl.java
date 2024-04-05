package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.mapper.AttachFileMapper;
import com.mall4j.cloud.biz.model.AttachFileGroup;
import com.mall4j.cloud.biz.mapper.AttachFileGroupMapper;
import com.mall4j.cloud.biz.service.AttachFileGroupService;
import com.mall4j.cloud.biz.vo.AttachFileGroupVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author YXF
 * @date 2020-12-04 16:15:02
 */
@Service
public class AttachFileGroupServiceImpl implements AttachFileGroupService {

    @Autowired
    private AttachFileMapper attachFileMapper;
    @Autowired
    private AttachFileGroupMapper attachFileGroupMapper;

    @Override
    public List<AttachFileGroupVO> list(Integer type) {
        if (Objects.equals(AuthUserContext.get().getTenantId(), Constant.DEFAULT_SHOP_ID)) {
            return attachFileGroupMapper.list(null, AuthUserContext.get().getUid(), type);
        }
        return attachFileGroupMapper.list(AuthUserContext.get().getTenantId(), null, type);
    }

    @Override
    public AttachFileGroupVO getByAttachFileGroupId(Long attachFileGroupId) {
        return attachFileGroupMapper.getByAttachFileGroupId(attachFileGroupId);
    }

    @Override
    public void save(AttachFileGroup attachFileGroup) {
        attachFileGroup.setShopId(AuthUserContext.get().getTenantId());
        this.checkGroupNameIsRepeat(attachFileGroup.getName(), attachFileGroup.getShopId(), attachFileGroup.getType(), null);
        attachFileGroup.setUid(AuthUserContext.get().getUid());
        attachFileGroupMapper.save(attachFileGroup);
    }

    @Override
    public void update(AttachFileGroup attachFileGroup) {
        this.checkGroupNameIsRepeat(attachFileGroup.getName(), attachFileGroup.getShopId(), attachFileGroup.getType(), attachFileGroup.getAttachFileGroupId());
        attachFileGroup.setShopId(AuthUserContext.get().getTenantId());
        attachFileGroupMapper.update(attachFileGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long attachFileGroupId) {
        attachFileGroupMapper.deleteById(attachFileGroupId);
        attachFileMapper.updateBatchByAttachFileGroupId(attachFileGroupId);
    }

    @Override
    public void updateShopIdByUid(Long shopId, Long uid) {
        attachFileGroupMapper.updateShopIdByUid(shopId, uid);
    }

    /**
     * 检查分组名是否重复
     * @param name 分组名称
     * @param shopId 店铺id
     * @param type 类型
     * @param attachFileGroupId 分组id
     */
    private void checkGroupNameIsRepeat(String name, Long shopId, Integer type, Long attachFileGroupId) {
        int count = attachFileGroupMapper.countByNameAndShopId(name, shopId, type, attachFileGroupId);
        if (count > 0) {
            throw new LuckException("分组名称重复");
        }
    }
}
