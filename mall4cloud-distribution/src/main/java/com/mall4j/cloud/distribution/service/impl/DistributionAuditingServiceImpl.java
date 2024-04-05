package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.SumAmountVO;
import com.mall4j.cloud.api.platform.feign.SysUserClient;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.constant.DistributionAuditingState;
import com.mall4j.cloud.distribution.constant.DistributionUserStateEnum;
import com.mall4j.cloud.distribution.dto.DistributionAuditingDTO;
import com.mall4j.cloud.distribution.mapper.DistributionUserBindMapper;
import com.mall4j.cloud.distribution.mapper.DistributionUserMapper;
import com.mall4j.cloud.distribution.model.DistributionAuditing;
import com.mall4j.cloud.distribution.mapper.DistributionAuditingMapper;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.service.DistributionAuditingService;
import com.mall4j.cloud.distribution.vo.DistributionAuditingVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分销员申请信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
@Service
public class DistributionAuditingServiceImpl implements DistributionAuditingService {

    @Autowired
    private DistributionAuditingMapper distributionAuditingMapper;
    @Autowired
    private SysUserClient sysUserClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private DistributionUserMapper distributionUserMapper;
    @Autowired
    private DistributionUserBindMapper distributionUserBindMapper;

    @Override
    public PageVO<DistributionAuditingVO> pageDistributionAuditing(PageDTO pageDTO, DistributionAuditingDTO distributionAuditingDTO) {
        PageVO<DistributionAuditingVO> distributionAuditingPage = PageUtil.doPage(pageDTO, () -> distributionAuditingMapper.listDistributionAuditing(distributionAuditingDTO));
        List<DistributionAuditingVO> auditingPageList = distributionAuditingPage.getList();
        if (CollUtil.isEmpty(auditingPageList)){
            return distributionAuditingPage;
        }
        List<Long> modifierIds = auditingPageList.stream().map(DistributionAuditingVO::getModifier).filter(item->Objects.nonNull(item)).distinct().collect(Collectors.toList());
        List<Long> userIds = auditingPageList.stream().map(DistributionAuditingVO::getUserId).collect(Collectors.toList());
        if (CollUtil.isEmpty(modifierIds) || CollUtil.isEmpty(userIds)) {
            return distributionAuditingPage;
        }
        //根据修改人的用户id查询姓名以及订单信息[积累消费金额，积累消费笔数]
        ServerResponseEntity<List<SysUserVO>> sysUserData = sysUserClient.getSysUserList(modifierIds);
        if (!sysUserData.isSuccess()) {
            throw new LuckException(sysUserData.getMsg());
        }
        ServerResponseEntity<List<SumAmountVO>> sumAmountData = orderFeignClient.listSumDataByUserIds(userIds);
        if (!sumAmountData.isSuccess()) {
            throw new LuckException(sumAmountData.getMsg());
        }
        List<SumAmountVO> sumDataList = sumAmountData.getData();
        List<SysUserVO> sysUserList = sysUserData.getData();
        Map<Long, SysUserVO> userMap = sysUserList.stream().collect(Collectors.toMap(SysUserVO::getSysUserId, s -> s));
        Map<Long, SumAmountVO> sumDataMap = sumDataList.stream().collect(Collectors.toMap(SumAmountVO::getUserId, s -> s));
        for (DistributionAuditingVO distributionAuditingVO : auditingPageList) {
            if (Objects.nonNull(distributionAuditingVO.getModifier())) {
                SysUserVO sysUserVO = userMap.get(distributionAuditingVO.getModifier());
                distributionAuditingVO.setModifierName(sysUserVO.getNickName());
            }
            if (Objects.nonNull(distributionAuditingVO.getUserId())){
                SumAmountVO sumAmountVO = sumDataMap.get(distributionAuditingVO.getUserId());
                if (Objects.isNull(sumAmountVO)){
                    //积累消费金额
                    distributionAuditingVO.setSumOfConsumption(0.00);
                    //积累消费笔数
                    distributionAuditingVO.setExpenseNumber(0.00);
                } else {
                    //积累消费金额
                    distributionAuditingVO.setSumOfConsumption(sumAmountVO.getSumOfConsumption());
                    //积累消费笔数
                    distributionAuditingVO.setExpenseNumber(sumAmountVO.getExpenseNumber());
                }
            }
        }
        return distributionAuditingPage;
    }

    @Override
    public DistributionAuditing getByAuditingId(Long auditingId) {
        return distributionAuditingMapper.getByAuditingId(auditingId);
    }

    @Override
    public void save(DistributionAuditing distributionAuditing) {
        distributionAuditingMapper.save(distributionAuditing);
    }

    @Override
    public void update(DistributionAuditing distributionAuditing) {
        distributionAuditingMapper.update(distributionAuditing);
    }

    @Override
    public void deleteById(Long auditingId) {
        distributionAuditingMapper.deleteById(auditingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void examine(DistributionAuditing distributionAuditing) {
        DistributionUser distributionUser = new DistributionUser();
        distributionUser.setDistributionUserId(distributionAuditing.getDistributionUserId());
        distributionUser.setBindTime(new Date());
        distributionUser.setStateRecord(null);
        //判断是否通过
        if (Objects.equals(DistributionAuditingState.PASS.value(),distributionAuditing.getState())){
            DistributionUser beforeDistributionUser = distributionUserMapper.getByDistributionUserId(distributionAuditing.getDistributionUserId());
            if (Objects.nonNull(beforeDistributionUser) && Objects.nonNull(beforeDistributionUser.getStateRecord())
                    && Objects.equals(beforeDistributionUser.getStateRecord(), DistributionUserStateEnum.BAN.value())){
                //如果之前是暂时封禁状态，现在改成正常状态,恢复以前的绑定用户。将失效的绑定用户设为正常。
                distributionUserBindMapper.updateBindUserByDistributionUserId(distributionAuditing.getDistributionUserId());
            }
            //通过审核，设为正常状态
            distributionUser.setState(DistributionUserStateEnum.NORMAL.value());
        } else if (Objects.equals(DistributionAuditingState.UN_PASS.value(),distributionAuditing.getState())){
            //未通过审核
            distributionUser.setState(DistributionUserStateEnum.FAIL_AUDIT.value());
        }
        //更新分销员状态
        distributionUserMapper.updateStatus(distributionUser);
        distributionAuditingMapper.update(distributionAuditing);
    }
}
