package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawOrderDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawOrder;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawOrderMapper;
import com.mall4j.cloud.distribution.service.DistributionWithdrawOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 佣金提现订单信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
@Service
public class DistributionWithdrawOrderServiceImpl implements DistributionWithdrawOrderService {

    @Autowired
    private DistributionWithdrawOrderMapper distributionWithdrawOrderMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public PageVO<DistributionWithdrawOrder> page(PageDTO pageDTO) {

        return PageUtil.doPage(pageDTO, () -> distributionWithdrawOrderMapper.list());
    }

    @Override
    public DistributionWithdrawOrder getById(Long id) {
        return distributionWithdrawOrderMapper.getById(id);
    }

    @Override
    public void save(DistributionWithdrawOrder distributionWithdrawOrder) {
        distributionWithdrawOrderMapper.save(distributionWithdrawOrder);
    }

    @Override
    public void update(DistributionWithdrawOrder distributionWithdrawOrder) {
        distributionWithdrawOrderMapper.update(distributionWithdrawOrder);
    }

    @Override
    public void deleteById(Long id) {
        distributionWithdrawOrderMapper.deleteById(id);
    }

    @Override
    public List<DistributionWithdrawOrder> listDistributionWithdrawOrderByRecord(Long recordId) {
        return distributionWithdrawOrderMapper.listDistributionWithdrawOrderByRecord(recordId);
    }

    @Override
    public List<EsOrderBO> listWithdrawOrder(DistributionWithdrawOrderDTO dto) {
        List<DistributionWithdrawOrder> orderList = distributionWithdrawOrderMapper.listDistributionWithdrawOrderByRecord(dto.getRecordId());
        if (CollectionUtils.isEmpty(orderList)){
            return new ArrayList<>();
        }
        DistributionQueryDTO queryDTO = new DistributionQueryDTO();
        queryDTO.setOrderIds(orderList.stream().map(DistributionWithdrawOrder::getOrderId).distinct().collect(Collectors.toList()));
        queryDTO.setProductName(dto.getProductName());
        queryDTO.setDistributionRelation(dto.getDistributionRelation());
        ServerResponseEntity<List<EsOrderBO>> ordersData = orderFeignClient.listDistributionOrders(queryDTO);
        if (ordersData.isSuccess()){
            return ordersData.getData();
        }
        return new ArrayList<>();
    }
}
