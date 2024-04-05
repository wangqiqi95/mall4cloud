package com.mall4j.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.mapper.MembershipRulesMapper;
import com.mall4j.cloud.user.model.MembershipRules;
import com.mall4j.cloud.user.service.MembershipRulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MembershipRulesServiceImpl implements MembershipRulesService {

    @Resource
    private MembershipRulesMapper membershipRulesMapper;

    @Override
    public ServerResponseEntity<Void> saveOrUpdate(MembershipRules param) {
        param.setCreateId(AuthUserContext.get().getUserId());
        param.setCreateName(AuthUserContext.get().getUsername());

        if (ObjectUtils.isEmpty(param.getId())){
            membershipRulesMapper.insert(param);
        }else {
            membershipRulesMapper.updateById(param);
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<MembershipRules> detail() {
        List<MembershipRules> membershipRules = membershipRulesMapper.selectList(new LambdaQueryWrapper<>());
        MembershipRules result = membershipRules.get(0);
        return ServerResponseEntity.success(result);
    }
}
