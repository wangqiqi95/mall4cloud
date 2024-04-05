package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.platform.feign.SysUserClient;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserBanDTO;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.mapper.DistributionUserBanMapper;
import com.mall4j.cloud.distribution.service.DistributionUserBanService;
import com.mall4j.cloud.distribution.vo.DistributionUserBanVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分销封禁记录
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
@Service
public class DistributionUserBanServiceImpl implements DistributionUserBanService {

    @Autowired
    private DistributionUserBanMapper distributionUserBanMapper;

    @Autowired
    private SysUserClient sysUserClient;

    @Override
    public PageVO<DistributionUserBanVO> page(PageDTO pageDTO, DistributionUserBanDTO distributionUserBanDTO) {
        PageVO<DistributionUserBanVO> banPage = PageUtil.doPage(pageDTO, () -> distributionUserBanMapper.list(distributionUserBanDTO));
        List<DistributionUserBanVO> banPageList = banPage.getList();
        if (CollUtil.isEmpty(banPageList)){
            return banPage;
        }
        List<Long> modifiers = banPageList.stream().map(DistributionUserBanVO::getModifier).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(modifiers)){
            return banPage;
        }
        ServerResponseEntity<List<SysUserVO>> sysUserData = sysUserClient.getSysUserList(modifiers);
        if (!sysUserData.isSuccess()){
            throw new LuckException(sysUserData.getMsg());
        }
        List<SysUserVO> sysUserList = sysUserData.getData();
        Map<Long, String> modifierMap = sysUserList.stream().collect(Collectors.toMap(SysUserVO::getSysUserId, SysUserVO::getNickName));
        for (DistributionUserBanVO distributionUserBanVO : banPageList) {
            Long modifier = distributionUserBanVO.getModifier();
            if (Objects.nonNull(modifier)){
                String modifierName = modifierMap.get(modifier);
                if (Objects.nonNull(modifierName)){
                    distributionUserBanVO.setModifierName(modifierName);
                }
            }
        }
        return banPage;
    }

    @Override
    public DistributionUserBan getByBanId(Long banId) {
        return distributionUserBanMapper.getByBanId(banId);
    }

    @Override
    public void save(DistributionUserBan distributionUserBan) {
        distributionUserBanMapper.save(distributionUserBan);
    }

    @Override
    public void update(DistributionUserBan distributionUserBan) {
        distributionUserBanMapper.update(distributionUserBan);
    }

    @Override
    public void deleteById(Long banId) {
        distributionUserBanMapper.deleteById(banId);
    }
}
