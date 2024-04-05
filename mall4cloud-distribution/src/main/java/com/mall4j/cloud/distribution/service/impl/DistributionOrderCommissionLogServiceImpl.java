package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionOrderCommissionLogDTO;
import com.mall4j.cloud.distribution.mapper.DistributionOrderCommissionLogMapper;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawOrderMapper;
import com.mall4j.cloud.distribution.model.DistributionOrderCommissionLog;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.service.DistributionOrderCommissionLogService;
import com.mall4j.cloud.distribution.vo.DistributionOrderCommissionLogItemVO;
import com.mall4j.cloud.distribution.vo.DistributionOrderCommissionLogVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单佣金流水信息service实现
 *
 * @author Zhang Fan
 * @date 2022/9/9 14:29
 */
@Service
public class DistributionOrderCommissionLogServiceImpl implements DistributionOrderCommissionLogService {

    @Autowired
    private TCouponFeignClient couponFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private DistributionOrderCommissionLogMapper distributionOrderCommissionLogMapper;
    @Autowired
    private DistributionWithdrawOrderMapper distributionWithdrawOrderMapper;

    @Override
    public PageVO<DistributionOrderCommissionLogVO> page(PageDTO pageDTO, DistributionOrderCommissionLogDTO distributionOrderCommissionLogDTO) {
        PageVO<DistributionOrderCommissionLog> pageVO = PageUtil.doPage(pageDTO, () -> distributionOrderCommissionLogMapper.list(distributionOrderCommissionLogDTO));
        List<DistributionOrderCommissionLogVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pageVO.getList())) {
            result = pageVO.getList().parallelStream().map(this::buildCommissionLogVO).collect(Collectors.toList());
        }
        PageVO<DistributionOrderCommissionLogVO> newPageVO = new PageVO<>();
        newPageVO.setPages(pageVO.getPages());
        newPageVO.setTotal(pageVO.getTotal());
        newPageVO.setList(result);
        return newPageVO;
    }

    @Override
    public DistributionOrderCommissionLogVO getById(Long id) {
        DistributionOrderCommissionLog commissionLog = distributionOrderCommissionLogMapper.getById(id);
        if (commissionLog == null) {
            throw new LuckException("获取订单佣金流水日志失败：流水信息不存在");
        }
        return buildCommissionLogVO(commissionLog);
    }

    @Override
    public int save(DistributionOrderCommissionLog log) {
        return distributionOrderCommissionLogMapper.save(log);
    }

    @Override
    public int batchSave(List<DistributionOrderCommissionLog> logList) {
        return distributionOrderCommissionLogMapper.batchSave(logList);
    }

    private DistributionOrderCommissionLogVO buildCommissionLogVO(DistributionOrderCommissionLog commissionLog) {
        ServerResponseEntity<EsOrderBO> esOrderResp = orderFeignClient.getEsDistributionOrder(commissionLog.getOrderId());
        EsOrderBO esOrderBO = esOrderResp.getData();
        if (esOrderResp.isFail() || esOrderBO == null) {
            throw new LuckException("获取订单佣金流水日志失败：订单信息不存在");
        }
        DistributionOrderCommissionLogVO commissionLogVO = BeanUtil.copyProperties(commissionLog, DistributionOrderCommissionLogVO.class);
        commissionLogVO.setPayTime(esOrderBO.getPayTime());
        commissionLogVO.setOrderNumber(esOrderBO.getOrderNumber());
        commissionLogVO.setAmount(esOrderBO.getActualTotal());
        commissionLogVO.setStoreCode(esOrderBO.getStoreCode());
        commissionLogVO.setStoreName(esOrderBO.getStoreName());
        if (commissionLog.getIdentityType() == 1) {
            commissionLogVO.setUsername(esOrderBO.getDistributionUserName());
            commissionLogVO.setPhone(esOrderBO.getDistributionUserMobile());
            commissionLogVO.setWorkNo(esOrderBO.getDistributionUserNo());
        } else {
            commissionLogVO.setUsername(esOrderBO.getDevelopingUserName());
            commissionLogVO.setPhone(esOrderBO.getDevelopingUserMobile());
            commissionLogVO.setWorkNo(esOrderBO.getDevelopingUserNo());
        }
        if (commissionLog.getCommissionStatus() == 2) {
            DistributionWithdrawRecord withdrawRecord = distributionWithdrawOrderMapper.getDistributionWithdrawRecordByOrderId(commissionLog.getOrderId());
            if (withdrawRecord != null) {
                commissionLogVO.setWithdrawOrderNo(withdrawRecord.getWithdrawOrderNo());
            }
        }
        ServerResponseEntity<Boolean> useEntCouponResp = couponFeignClient.isUseEnterpriseCoupon(esOrderBO.getOrderId());
        if (useEntCouponResp.isFail()) {
            throw new LuckException("获取订单佣金流水日志失败：获取订单优惠券使用情况失败");
        }
        commissionLogVO.setUseEntCoupon(useEntCouponResp.getData());
        List<DistributionOrderCommissionLogItemVO> logOrderItems = new ArrayList<>();
        List<OrderItemVO> orderItemList = esOrderBO.getOrderItemList();
        if (CollectionUtils.isNotEmpty(orderItemList)) {
            orderItemList.forEach(orderItemVO -> {
                DistributionOrderCommissionLogItemVO distributionOrderCommissionLogItemVO = new DistributionOrderCommissionLogItemVO();
                distributionOrderCommissionLogItemVO.setSpuName(orderItemVO.getSpuName());
                if (commissionLog.getIdentityType() == 1) {
                    distributionOrderCommissionLogItemVO.setCommissionRate(orderItemVO.getDistributionAmountRate());
                } else {
                    distributionOrderCommissionLogItemVO.setCommissionRate(orderItemVO.getDistributionParentAmountRate());
                }
                commissionLogVO.setRefundOrderNumber(orderItemVO.getFinalRefundNumber());
            });
        }
        commissionLogVO.setLogOrderItems(logOrderItems);
        return commissionLogVO;
    }
}
