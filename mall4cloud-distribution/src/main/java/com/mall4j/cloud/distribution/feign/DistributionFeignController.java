package com.mall4j.cloud.distribution.feign;

import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionAccountDTO;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.feign.DistributionFeignClient;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccount;
import com.mall4j.cloud.distribution.model.DistributionOrderCommissionLog;
import com.mall4j.cloud.distribution.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author cl
 * @date 2021-08-05 15:38:50
 */
@RestController
public class DistributionFeignController implements DistributionFeignClient {

    @Autowired
    private DistributionSpuService distributionSpuService;
    @Autowired
    private DistributionUserService distributionUserService;
    @Autowired
    private DistributionCommissionService distributionCommissionService;
    @Autowired
    private DistributionOrderCommissionLogService distributionOrderCommissionLogService;
    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;

    @Override
    public ServerResponseEntity<DistributionSpuVO> getByDistributionId(Long distributionId) {
        DistributionSpuVO distributionSpuVO = distributionSpuService.getByDistributionSpuId(distributionId);
        return ServerResponseEntity.success(distributionSpuVO);
    }

    @Override
    public ServerResponseEntity<DistributionSpuVO> getBySpuId(Long spuId) {
        DistributionSpuVO distributionSpuVO = distributionSpuService.getBySpuId(spuId);
        return ServerResponseEntity.success(distributionSpuVO);
    }

    @Override
    public ServerResponseEntity<Map<Long, DistributionCommissionRateVO>> getDistributionCommissionRate(
            DistributionCommissionRateDTO distributionCommissionRateDTO) {
        Map<Long, DistributionCommissionRateVO> distributionCommissionRateVOMap = distributionCommissionService
                .getDistributionCommissionRate(distributionCommissionRateDTO);
        return ServerResponseEntity.success(distributionCommissionRateVOMap);
    }

    @Override
    public ServerResponseEntity<Void> initCommissionAccount(DistributionCommissionAccountDTO distributionCommissionAccountDTO) {
        DistributionCommissionAccount oldAccount = distributionCommissionAccountService.getByUser(distributionCommissionAccountDTO.getUserId(), distributionCommissionAccountDTO.getIdentityType());
        if (null != oldAccount){
            return ServerResponseEntity.success();
        }
        distributionCommissionAccountDTO.setWaitCommission(0L);
        distributionCommissionAccountDTO.setCanWithdraw(0L);
        distributionCommissionAccountDTO.setAlreadyWithdraw(0L);
        distributionCommissionAccountDTO.setTotalWithdraw(0L);
        distributionCommissionAccountDTO.setWithdrawNeedRefund(0L);
        DistributionCommissionAccount account = new DistributionCommissionAccount();
        BeanUtils.copyProperties(distributionCommissionAccountDTO, account);
        distributionCommissionAccountService.save(account);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateCommission(Integer identityType, Long userId, Long value, Integer changeType) {
        DistributionCommissionAccount commissionAccount = distributionCommissionAccountService.getByUser(userId, identityType);
        if (commissionAccount == null) {
            throw new LuckException("佣金账户不存在");
        }
        distributionCommissionAccountService.updateCommission(commissionAccount, value, CommissionChangeTypeEnum.instance(changeType));
        return ServerResponseEntity.success();
    }

    @Transactional
    @Override
    public ServerResponseEntity<Void> updateCommissionWithLogByOneOrder(Integer identityType, Long userId, Long value, Integer changeType, Long orderId, Integer commissionType, Integer commissionStatus) {
        DistributionCommissionAccount commissionAccount = distributionCommissionAccountService.getByUser(userId, identityType);
        if (commissionAccount == null) {
            throw new LuckException("佣金账户不存在");
        }
        distributionCommissionAccountService.updateCommission(commissionAccount, value, CommissionChangeTypeEnum.instance(changeType));

        DistributionOrderCommissionLog log = new DistributionOrderCommissionLog();
        log.setCommissionAmount(value);
        log.setCommissionStatus(commissionStatus);
        log.setCommissionType(commissionType);
        log.setOrderId(orderId);
        log.setIdentityType(commissionAccount.getIdentityType());
        log.setUserId(commissionAccount.getUserId());
        log.setStoreId(commissionAccount.getStoreId());
        distributionOrderCommissionLogService.save(log);
        return ServerResponseEntity.success();
    }
}
