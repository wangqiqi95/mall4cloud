package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeUserMapper;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeUser;
import com.mall4j.cloud.biz.service.cp.CpAutoGroupCodeUserService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自动拉群员工好友关系表
 *
 * @author gmq
 * @date 2023-11-08 10:20:46
 */
@Service
public class CpAutoGroupCodeUserServiceImpl extends ServiceImpl<CpAutoGroupCodeUserMapper,CpAutoGroupCodeUser> implements CpAutoGroupCodeUserService {

    @Autowired
    private CpAutoGroupCodeUserMapper cpAutoGroupCodeUserMapper;

    @Override
    public PageVO<CpAutoGroupCodeUser> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpAutoGroupCodeUserMapper.list());
    }

    @Override
    public CpAutoGroupCodeUser getById(Long id) {
        return cpAutoGroupCodeUserMapper.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        cpAutoGroupCodeUserMapper.deleteById(id);
    }

    @Override
    public Integer countSendByCodeId(Long codeId) {
        return this.count(new LambdaQueryWrapper<CpAutoGroupCodeUser>()
                .eq(CpAutoGroupCodeUser::getIsDelete,0)
                .eq(CpAutoGroupCodeUser::getCodeId,codeId)
                .eq(CpAutoGroupCodeUser::getSendStatus,1));
    }

    @Override
    public Integer countJoinGroupByCodeId(Long codeId) {
        return this.count(new LambdaQueryWrapper<CpAutoGroupCodeUser>()
                .eq(CpAutoGroupCodeUser::getIsDelete,0)
                .eq(CpAutoGroupCodeUser::getCodeId,codeId)
                .eq(CpAutoGroupCodeUser::getJoinGroup,1));
    }
}
