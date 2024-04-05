package com.mall4j.cloud.distribution.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import java.util.Date;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.docking.jos.dto.*;
import com.mall4j.cloud.api.docking.jos.enums.JosBusiness;
import com.mall4j.cloud.api.docking.jos.feign.MemberFeignClient;
import com.mall4j.cloud.api.docking.jos.feign.SettlementFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderRefundRespDTO;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderRefundSearchDTO;
import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderSearchDTO;
import com.mall4j.cloud.api.order.dto.JointVentureCommissionOrderSettledDTO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.api.order.vo.OrderRefundVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionApplyDTO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionSearchDTO;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionApplyMapper;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionApplyOrderMapper;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionApplyRefundOrderMapper;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionCustomerMapper;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApply;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApplyOrder;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApplyRefundOrder;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionApplyService;
import com.mall4j.cloud.distribution.vo.DistributionJointVentureCommissionApplyExcelVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 联营分佣申请service实现
 *
 * @author Zhang Fan
 * @date 2022/8/5 15:20
 */
@Slf4j
@Service
public class DistributionJointVentureCommissionApplyServiceImpl implements DistributionJointVentureCommissionApplyService {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private OrderRefundFeignClient orderRefundFeignClient;
    @Autowired
    private MemberFeignClient memberFeignClient;
    @Autowired
    private SettlementFeignClient settlementFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    private DistributionJointVentureCommissionApplyMapper distributionJointVentureCommissionApplyMapper;
    @Autowired
    private DistributionJointVentureCommissionApplyOrderMapper distributionJointVentureCommissionApplyOrderMapper;
    @Autowired
    private DistributionJointVentureCommissionCustomerMapper distributionJointVentureCommissionCustomerMapper;
    @Autowired
    private DistributionJointVentureCommissionApplyRefundOrderMapper distributionJointVentureCommissionApplyRefundOrderMapper;

    @Override
    public PageVO<DistributionJointVentureCommissionApply> page(PageDTO pageDTO, DistributionJointVentureCommissionSearchDTO searchDTO) {
        DistributionJointVentureCommissionApply distributionJointVentureCommissionApply = mapperFacade.map(searchDTO, DistributionJointVentureCommissionApply.class);
        PageVO<DistributionJointVentureCommissionApply> pageVO = PageUtil.doPage(pageDTO, () -> distributionJointVentureCommissionApplyMapper.selectBySelective(distributionJointVentureCommissionApply));
        return pageVO;
    }

    @Override
    public DistributionJointVentureCommissionApply getById(Long id) {
        DistributionJointVentureCommissionApply byId = distributionJointVentureCommissionApplyMapper.selectByPrimaryKey(id);
        List<DistributionJointVentureCommissionApplyOrder> applyOrders = distributionJointVentureCommissionApplyOrderMapper.selectByApplyId(id);
        if(CollUtil.isNotEmpty(applyOrders)){
            List<Long> orderIds = applyOrders.stream().map(DistributionJointVentureCommissionApplyOrder::getOrderId).collect(Collectors.toList());
            byId.setSelectedOrderIdList(orderIds);
        }

        // 第三方发佣状态为发佣中，获取详情的时候再去调用第三方更新新状态
        if (byId != null && StringUtils.isNotEmpty(byId.getRequestId()) && byId.getStatus() == 1) {
            QuerySettlementStatusDto querySettlementStatusDto = new QuerySettlementStatusDto();
            querySettlementStatusDto.setRequestId(byId.getRequestId());
            QuerySettlementStatusResp querySettlementStatusResp = settlementFeignClient.querySettlementStatus(querySettlementStatusDto);
            log.info("联营分佣查询发佣金状态查询参数-->{}，查询结果:-->{}", JSONObject.toJSONString(querySettlementStatusDto),JSONObject.toJSONString(querySettlementStatusResp));
            if (querySettlementStatusResp.getCode() == 1) {
                Integer status = querySettlementStatusResp.getData().getStatus();
                String failReason = querySettlementStatusResp.getData().getRemark();
                //查询的状态和系统状态不一致 或者 查询的失败原因有值并且跟系统里的失败原因不一致的情况下更新提现状态和失败原因
                if (!byId.getStatus().equals(status) || (StringUtils.isNotEmpty(failReason) && !failReason.equals(byId.getTransferFailReason()))) {
                    byId.setStatus(status);
                    byId.setTransferFailReason(failReason);
                    distributionJointVentureCommissionApplyMapper.updateByPrimaryKeySelective(byId);
                }
            }
        }
        return byId;
    }

    @Transactional
    @Override
    public void launchApply(DistributionJointVentureCommissionApplyDTO applyDTO) {
        DistributionJointVentureCommissionApply jointVentureCommissionApply = mapperFacade.map(applyDTO, DistributionJointVentureCommissionApply.class);
        DistributionJointVentureCommissionCustomer jointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper.getById(applyDTO.getCustomerId());
        validateJointVentureCustomer(jointVentureCommissionCustomer);
        if (CollectionUtils.isEmpty(applyDTO.getSelectedOrderIdList())) {
            throw new LuckException("联营分佣订单列表为空");
        }

        validateJointVentureOrderIsItApplied(applyDTO.getSelectedOrderIdList());
        ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.getEsOrderList(applyDTO.getSelectedOrderIdList().stream().distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(orderListData.getData()) || applyDTO.getSelectedOrderIdList().size() != orderListData.getData().size()) {
            throw new LuckException("联营分佣订单数据不存在");
        }

        jointVentureCommissionApply.setCustomerName(jointVentureCommissionCustomer.getCustomerName());
        jointVentureCommissionApply.setCustomerPhone(jointVentureCommissionCustomer.getPhone());
        jointVentureCommissionApply.setApplyNo(DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + jointVentureCommissionCustomer.getId()).substring(1));
        long sum = orderListData.getData().stream().mapToLong(EsOrderBO::getActualTotal).sum();
        jointVentureCommissionApply.setOrderTurnover(sum);
        long commissionAmount = (long) (sum * (new BigDecimal(applyDTO.getCommissionRate()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue()));
        jointVentureCommissionApply.setCommissionAmount(commissionAmount);
        jointVentureCommissionApply.setStatus(0);
        distributionJointVentureCommissionApplyMapper.insertSelective(jointVentureCommissionApply);

        // 处理订单
        List<DistributionJointVentureCommissionApplyOrder> jointVentureCommissionApplyOrderList = applyDTO.getSelectedOrderIdList().stream().map(orderId -> {
            DistributionJointVentureCommissionApplyOrder jointVentureCommissionApplyOrder = new DistributionJointVentureCommissionApplyOrder();
            jointVentureCommissionApplyOrder.setOrderId(orderId);
            jointVentureCommissionApplyOrder.setCustomerId(jointVentureCommissionCustomer.getId());
            jointVentureCommissionApplyOrder.setApplyId(jointVentureCommissionApply.getId());
            return jointVentureCommissionApplyOrder;
        }).collect(Collectors.toList());
        distributionJointVentureCommissionApplyOrderMapper.batchInsertSelective(jointVentureCommissionApplyOrderList);
    }

    @Override
    public void launchApply(DistributionJointVentureOrderSearchDTO searchDTO) {
        DistributionJointVentureCommissionCustomer jointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper.getById(searchDTO.getJointVentureCommissionCustomerId());
        validateJointVentureCustomer(jointVentureCommissionCustomer);

        searchDTO.setJointVentureCommissionStatus(0);
        // 获取订单数据
        ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.listDistributionJointVentureOrders(searchDTO);
        if (orderListData.isFail()) {
            throw new LuckException(orderListData.getMsg());
        }
        if (CollectionUtils.isEmpty(orderListData.getData())) {
            throw new LuckException("联营分佣订单数据不存在");
        }

        long needApplyAmount = 0;
        long needRefundAmount = 0;
        List<EsOrderBO> needApplendOrderList = new ArrayList<>();
        for (EsOrderBO esOrderBO : orderListData.getData()) {
            boolean isSuccessOrder = esOrderBO.getStatus().equals(OrderStatus.SUCCESS.value());
            boolean isCloseOrder = esOrderBO.getStatus().equals(OrderStatus.CLOSE.value());
            boolean isNeedRefundOrder = esOrderBO.getJointVentureRefundStatus() == 1;
            if (isSuccessOrder) {
                if (isNeedRefundOrder) {
                    needRefundAmount += calculateNeedRefundAmount(Collections.singletonList(esOrderBO));
                }
                List<EsOrderItemBO> needApplyItemList = getNeedApplyJointVentureItemList(esOrderBO);
                if (CollectionUtils.isEmpty(needApplyItemList)) {
                    continue;
                }
                needApplyAmount += needApplyItemList.stream().mapToLong(EsOrderItemBO::getActualTotal).sum();
                needApplendOrderList.add(esOrderBO);
            }
            if (isCloseOrder && isNeedRefundOrder) {
                needRefundAmount += calculateNeedRefundAmount(Collections.singletonList(esOrderBO));
            }
        }
        List<Long> needAppendOrderIdList = needApplendOrderList.stream()
                .map(EsOrderBO::getOrderId)
                .collect(Collectors.toList());
        validateJointVentureOrderIsItApplied(needAppendOrderIdList);

        log.info("发起联营分佣金额计算：inParam:{} needApplyAmount:{} needRefundAmount:{}", searchDTO.toString(), needApplyAmount, needRefundAmount);
        if (needRefundAmount > needApplyAmount) {
            throw new LuckException("已分佣订单需退佣金大于本次结算分佣佣金");
        }

        DistributionJointVentureCommissionApply jointVentureCommissionApply = new DistributionJointVentureCommissionApply();
        jointVentureCommissionApply.setCustomerId(jointVentureCommissionCustomer.getId());
        jointVentureCommissionApply.setCustomerName(jointVentureCommissionCustomer.getCustomerName());
        jointVentureCommissionApply.setCustomerPhone(jointVentureCommissionCustomer.getPhone());
        jointVentureCommissionApply.setCommissionRate(jointVentureCommissionCustomer.getCommissionRate());
        jointVentureCommissionApply.setOrderTimeUpperBound(searchDTO.getPayEndTime());
        jointVentureCommissionApply.setOrderTimeLowerBound(searchDTO.getPayStartTime());
        jointVentureCommissionApply.setApplyNo(DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + jointVentureCommissionCustomer.getId()).substring(1));
        jointVentureCommissionApply.setOrderTurnover(needApplyAmount);
        jointVentureCommissionApply.setOrderRefundTurnover(needRefundAmount);
        long commissionAmount = (long) (needApplyAmount * (new BigDecimal(jointVentureCommissionCustomer.getCommissionRate()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue()));
        jointVentureCommissionApply.setCommissionAmount(commissionAmount);
        long commissionRefundAmount = (long) (needRefundAmount * (new BigDecimal(jointVentureCommissionCustomer.getCommissionRate()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue()));
        jointVentureCommissionApply.setCommissionRefundAmount(commissionRefundAmount);
        jointVentureCommissionApply.setStatus(0);
        distributionJointVentureCommissionApplyMapper.insertSelective(jointVentureCommissionApply);

        // 处理订单
        List<DistributionJointVentureCommissionApplyOrder> jointVentureCommissionApplyOrderList = orderListData.getData().stream().map(order -> {
            DistributionJointVentureCommissionApplyOrder jointVentureCommissionApplyOrder = new DistributionJointVentureCommissionApplyOrder();
            jointVentureCommissionApplyOrder.setOrderId(order.getOrderId());
            jointVentureCommissionApplyOrder.setCustomerId(jointVentureCommissionCustomer.getId());
            jointVentureCommissionApplyOrder.setApplyId(jointVentureCommissionApply.getId());
            return jointVentureCommissionApplyOrder;
        }).collect(Collectors.toList());
        distributionJointVentureCommissionApplyOrderMapper.batchInsertSelective(jointVentureCommissionApplyOrderList);

        // 处理订单状态为分佣中
        JointVentureCommissionOrderSettledDTO jointVentureCommissionOrderSettledDTO = new JointVentureCommissionOrderSettledDTO();
        jointVentureCommissionOrderSettledDTO.setOrderIds(orderListData.getData().stream().map(EsOrderBO::getOrderId).collect(Collectors.toList()));
        jointVentureCommissionOrderSettledDTO.setJointVentureCommissionStatus(1);
        ServerResponseEntity<Void> orderSettledResponse = orderFeignClient.jointVentureCommissionOrderSettled(jointVentureCommissionOrderSettledDTO);
        if (orderSettledResponse.isFail()) {
            throw new LuckException("联营分佣审核失败：" + orderSettledResponse.getMsg());
        }
    }

    /**
     * 【当前逻辑】 2023-7-10日前
     * 联营分佣
     * 成交金额=支付时间内并且没有未处理售后单的支付金额 -（ 这批支付订单的）退款金额
     * 例如：6月29日，对五月分佣：  查询支付时间五月的订单（状态已完成即用户确认收货） - 五月的这批订单的退款金额
     *
     * 【修改后逻辑】
     * 联营分佣
     * 成交金额=统计时间内支付金额-统计时间内退款成功金额
     * 例如：6月29日对五月分佣：查询支付时间五月的订单 - 发生在五月的退款金额
     * @param searchDTO
     */
    public void launchApplyV2(DistributionJointVentureOrderSearchDTO searchDTO){
        DistributionJointVentureCommissionCustomer jointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper.getById(searchDTO.getJointVentureCommissionCustomerId());
        validateJointVentureCustomer(jointVentureCommissionCustomer);

        searchDTO.setJointVentureCommissionStatus(0);
        // 获取订单数据
        ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.listDistributionJointVentureOrdersV2(searchDTO);
        if (orderListData.isFail()) {
            throw new LuckException(orderListData.getMsg());
        }
        if (CollectionUtils.isEmpty(orderListData.getData())) {
            throw new LuckException("联营分佣订单数据不存在");
        }

        long totalApplyAmount= 0;
        long needApplyAmount = 0;
        long needRefundAmount = 0;

        //查询所有成功订单和失败订单
        totalApplyAmount = orderListData.getData().stream().mapToLong(EsOrderBO::getActualTotal).sum();
        long totalFreightAmount = orderListData.getData().stream().mapToLong(EsOrderBO::getFreightAmount).sum();

        //查询所有退款成功订单
        DistributionJointVentureOrderRefundSearchDTO distributionJointVentureOrderRefundSearchDTO = new DistributionJointVentureOrderRefundSearchDTO();
        distributionJointVentureOrderRefundSearchDTO.setRefundStartTime(searchDTO.getPayStartTime());
        distributionJointVentureOrderRefundSearchDTO.setRefundEndTime(searchDTO.getPayEndTime());
        distributionJointVentureOrderRefundSearchDTO.setStoreIdList(searchDTO.getStoreIdList());
        ServerResponseEntity<List<DistributionJointVentureOrderRefundRespDTO>> refundOrderData = orderFeignClient.listDistributionJointVentureRefundOrders(distributionJointVentureOrderRefundSearchDTO);
        if(refundOrderData.isSuccess() && CollectionUtils.isNotEmpty(refundOrderData.getData())){
            needRefundAmount = refundOrderData.getData().stream().mapToLong(DistributionJointVentureOrderRefundRespDTO::getRefundAmount).sum();
        }
        needApplyAmount=totalApplyAmount-totalFreightAmount-needRefundAmount;

        DistributionJointVentureCommissionApply jointVentureCommissionApply = new DistributionJointVentureCommissionApply();
        jointVentureCommissionApply.setCustomerId(jointVentureCommissionCustomer.getId());
        jointVentureCommissionApply.setCustomerName(jointVentureCommissionCustomer.getCustomerName());
        jointVentureCommissionApply.setCustomerPhone(jointVentureCommissionCustomer.getPhone());
        jointVentureCommissionApply.setCommissionRate(jointVentureCommissionCustomer.getCommissionRate());
        jointVentureCommissionApply.setOrderTimeUpperBound(searchDTO.getPayEndTime());
        jointVentureCommissionApply.setOrderTimeLowerBound(searchDTO.getPayStartTime());
        jointVentureCommissionApply.setApplyNo(DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + jointVentureCommissionCustomer.getId()).substring(1));
        jointVentureCommissionApply.setOrderTurnover(needApplyAmount);
        jointVentureCommissionApply.setOrderRefundTurnover(needRefundAmount);
        long commissionAmount = (long) (needApplyAmount * (new BigDecimal(jointVentureCommissionCustomer.getCommissionRate()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue()));
        jointVentureCommissionApply.setCommissionAmount(commissionAmount);
        long commissionRefundAmount = (long) (needRefundAmount * (new BigDecimal(jointVentureCommissionCustomer.getCommissionRate()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue()));
        jointVentureCommissionApply.setCommissionRefundAmount(commissionRefundAmount);
        jointVentureCommissionApply.setStatus(0);
        distributionJointVentureCommissionApplyMapper.insertSelective(jointVentureCommissionApply);

        // 处理订单
        List<DistributionJointVentureCommissionApplyOrder> jointVentureCommissionApplyOrderList = orderListData.getData().stream().map(order -> {
            DistributionJointVentureCommissionApplyOrder jointVentureCommissionApplyOrder = new DistributionJointVentureCommissionApplyOrder();
            jointVentureCommissionApplyOrder.setOrderId(order.getOrderId());
            jointVentureCommissionApplyOrder.setCustomerId(jointVentureCommissionCustomer.getId());
            jointVentureCommissionApplyOrder.setApplyId(jointVentureCommissionApply.getId());
            return jointVentureCommissionApplyOrder;
        }).collect(Collectors.toList());
        distributionJointVentureCommissionApplyOrderMapper.batchInsertSelective(jointVentureCommissionApplyOrderList);

        // 处理订单状态为分佣中
        JointVentureCommissionOrderSettledDTO jointVentureCommissionOrderSettledDTO = new JointVentureCommissionOrderSettledDTO();
        jointVentureCommissionOrderSettledDTO.setOrderIds(orderListData.getData().stream().map(EsOrderBO::getOrderId).collect(Collectors.toList()));
        jointVentureCommissionOrderSettledDTO.setJointVentureCommissionStatus(1);
        ServerResponseEntity<Void> orderSettledResponse = orderFeignClient.jointVentureCommissionOrderSettled(jointVentureCommissionOrderSettledDTO);
        if (orderSettledResponse.isFail()) {
            throw new LuckException("联营分佣审核失败：" + orderSettledResponse.getMsg());
        }

        if(CollectionUtils.isNotEmpty(refundOrderData.getData())) {
            List<DistributionJointVentureCommissionApplyRefundOrder> refundOrders = Lists.newArrayList();
            for (DistributionJointVentureOrderRefundRespDTO respDTO : refundOrderData.getData()) {
                DistributionJointVentureCommissionApplyRefundOrder refundOrder = new DistributionJointVentureCommissionApplyRefundOrder();
                refundOrder.setApplyId(jointVentureCommissionApply.getId());
                refundOrder.setCustomerId(jointVentureCommissionCustomer.getId());
                refundOrder.setRefundId(respDTO.getRefundId());
                refundOrder.setCreateTime(new Date());
                refundOrder.setUpdateTime(new Date());
                refundOrders.add(refundOrder);
            }
            distributionJointVentureCommissionApplyRefundOrderMapper.batchInsert(refundOrders);

            orderRefundFeignClient.updateJointVentureRefundOrder(refundOrderData.getData().stream().map(DistributionJointVentureOrderRefundRespDTO::getRefundId).collect(Collectors.toList()), 1);
        }
    }

    private List<EsOrderItemBO> getNeedApplyJointVentureItemList(EsOrderBO esOrderBO) {
        if (esOrderBO == null || CollectionUtils.isEmpty(esOrderBO.getOrderItems())) {
            return Collections.emptyList();
        }
        List<EsOrderItemBO> boList = new ArrayList<>();
        for (EsOrderItemBO orderItem : esOrderBO.getOrderItems()) {
            //如果没有发起退款。且没有分佣过
            if(orderItem.getFinalRefundId() == null && orderItem.getJointVentureCommissionStatus() == 0
                    && orderItem.getJointVentureRefundStatus() == 0){
                boList.add(orderItem);
                continue;
            }
            //如果发起退款成功，则修正orderItem中的实付金额  实付金额 = 实付金额 - 退款金额
            if(orderItem.getReturnMoneySts()!=null && orderItem.getReturnMoneySts() == 5){
                orderItem.setActualTotal(orderItem.getActualTotal() - orderItem.getRefundAmount());
                boList.add(orderItem);
            }else{
                boList.add(orderItem);
            }
        }

        return boList;
    }

    private long calculateNeedRefundAmount(List<EsOrderBO> orderListData) {
        long needRefundAmount = 0;
        for (EsOrderBO esOrderBO : orderListData) {
            ServerResponseEntity<List<OrderRefundVO>> orderRefundList = orderRefundFeignClient.getOrderRefundByOrderIdAndRefundStatus(esOrderBO.getOrderId(), 5);
            if (!orderRefundList.isSuccess()) {
                throw new LuckException("发起联营分佣失败：{}", orderRefundList.getMsg());
            }

            if (CollectionUtils.isEmpty(orderRefundList.getData())) {
                continue;
            }

            // 已结算的退款项id集合
            List<Long> needSkipRefundItemIdList = esOrderBO.getOrderItems()
                    .stream()
                    .filter(item -> item.getJointVentureCommissionStatus() != 0
                            && item.getJointVentureRefundStatus() == 1)
                    .map(EsOrderItemBO::getOrderItemId)
                    .collect(Collectors.toList());

            // 根据订单项id为key聚合的订单项Map
            Map<Long, EsOrderItemBO> groupByOrderItemIdOrderItemMap = esOrderBO.getOrderItems()
                    .stream()
                    .collect(
                            Collectors.toMap(EsOrderItemBO::getOrderItemId, esOrderItemBO -> esOrderItemBO)
                    );
            // 根据订单项id为key聚合的退款数量Map
            Map<Long, Integer> groupByOrderItemIdRefundCountMap = orderRefundList.getData()
                    .stream()
                    .collect(
                            Collectors.groupingBy(OrderRefundVO::getOrderItemId, Collectors.summingInt(OrderRefundVO::getRefundCount)
                            )
                    );

            for (Map.Entry<Long, Integer> orderItemIdRefundCountEntry : groupByOrderItemIdRefundCountMap.entrySet()) {
                if (needSkipRefundItemIdList.contains(orderItemIdRefundCountEntry.getKey())
                        || (orderItemIdRefundCountEntry.getKey() == 0 && !needSkipRefundItemIdList.isEmpty())) {
                    log.info("跳过处理，已退还佣金，发起联营分佣退单成功商品 orderItemId:{}, refundCount:{}", orderItemIdRefundCountEntry.getKey(), orderItemIdRefundCountEntry.getValue());
                    continue;
                }
                log.info("发起联营分佣退单成功商品 orderItemId:{}, refundCount:{}", orderItemIdRefundCountEntry.getKey(), orderItemIdRefundCountEntry.getValue());
                // 全部退单
                if (orderItemIdRefundCountEntry.getKey() == 0) {
                    needRefundAmount += (esOrderBO.getActualTotal() - esOrderBO.getFreightAmount());
                    break;
                } else {
                    EsOrderItemBO orderItemBO = groupByOrderItemIdOrderItemMap.get(orderItemIdRefundCountEntry.getKey());
                    // 当前商品全部退
                    if (orderItemIdRefundCountEntry.getValue() == 0) {
                        orderItemBO = groupByOrderItemIdOrderItemMap.get(orderItemIdRefundCountEntry.getKey());
                        needRefundAmount += orderItemBO.getActualTotal();
                    } else if (orderItemIdRefundCountEntry.getValue().equals(orderItemBO.getCount())) {
                        orderItemBO = groupByOrderItemIdOrderItemMap.get(orderItemIdRefundCountEntry.getKey());
                        needRefundAmount += orderItemBO.getActualTotal();
                    } else {
                        needRefundAmount += orderItemBO.getActualTotal() / orderItemBO.getCount() * orderItemIdRefundCountEntry.getValue();

                    }
                }
            }
        }
        return needRefundAmount;
    }

    private void validateJointVentureCustomer(DistributionJointVentureCommissionCustomer jointVentureCommissionCustomer) {
        if (jointVentureCommissionCustomer == null) {
            throw new LuckException("联营分佣客户不存在");
        }
        jointVentureCommissionCustomer.validate();
    }

    @Transactional
    @Override
    public void auditApply(Long id, Integer status) {
        DistributionJointVentureCommissionApply jointVentureCommissionApply = getById(id);
        if (jointVentureCommissionApply == null) {
            throw new LuckException("联营分佣申请记录不存在");
        }
        if (jointVentureCommissionApply.getStatus() != 0) {
            throw new LuckException("联营分佣申请记录已处理");
        }
        if (status == 1) {
            // 审核通过
            DistributionJointVentureCommissionCustomer jointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper.getById(jointVentureCommissionApply.getCustomerId());
            validateJointVentureCustomer(jointVentureCommissionCustomer);
            // 检验客户京东益世状态
            //doValidateCustomerJDYSStatus(jointVentureCommissionCustomer);
            // fixme：分布式事务问题
            doSubCommissionByJDYS(jointVentureCommissionCustomer, jointVentureCommissionApply);
        }
        jointVentureCommissionApply.setStatus(status);
        jointVentureCommissionApply.setAuditUserId(AuthUserContext.get().getUserId());
        jointVentureCommissionApply.setAuditTime(new Date());
        distributionJointVentureCommissionApplyMapper.updateByPrimaryKeySelective(jointVentureCommissionApply);

        // 修改订单
        List<DistributionJointVentureCommissionApplyOrder> applyOrders = distributionJointVentureCommissionApplyOrderMapper.selectByApplyId(jointVentureCommissionApply.getId());
        if (!applyOrders.isEmpty()) {
            JointVentureCommissionOrderSettledDTO jointVentureCommissionOrderSettledDTO = new JointVentureCommissionOrderSettledDTO();
            jointVentureCommissionOrderSettledDTO.setOrderIds(applyOrders.stream().map(DistributionJointVentureCommissionApplyOrder::getOrderId).collect(Collectors.toList()));
            jointVentureCommissionOrderSettledDTO.setJointVentureCommissionStatus(status == 1 ? 2 : 0);
            orderFeignClient.jointVentureCommissionOrderSettled(jointVentureCommissionOrderSettledDTO);
        }

        //修改退款单
        List<DistributionJointVentureCommissionApplyRefundOrder> refundOrders = distributionJointVentureCommissionApplyRefundOrderMapper.selectList(Wrappers.lambdaQuery(DistributionJointVentureCommissionApplyRefundOrder.class)
                .eq(DistributionJointVentureCommissionApplyRefundOrder::getApplyId, jointVentureCommissionApply.getId()));
        if(CollectionUtils.isNotEmpty(refundOrders)){
            orderRefundFeignClient.updateJointVentureRefundOrder(refundOrders.stream().map(DistributionJointVentureCommissionApplyRefundOrder::getRefundId).collect(Collectors.toList()),status == 1 ? 2 : 0);
        }

    }

    @Override
    public void updateApplyBankCard(String applyId, String cardNo) {
        DistributionJointVentureCommissionApply distributionJointVentureCommissionApply = distributionJointVentureCommissionApplyMapper.selectByAppyNo(applyId);
        if(distributionJointVentureCommissionApply==null){
            Assert.faild("联营分佣记录不存在。");
        }
        DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper.getById(distributionJointVentureCommissionApply.getCustomerId());
        if(distributionJointVentureCommissionCustomer==null){
            Assert.faild("联营分佣客户数据异常，请检查数据后重试。");
        }

        // 调用京东益世修改发佣申请信息
        SettlementUpdateDto settlementUpdateDto = new SettlementUpdateDto();
        settlementUpdateDto.setRequestId(distributionJointVentureCommissionApply.getRequestId());
        settlementUpdateDto.setCardNo(cardNo);
        log.info("分销佣金提现修改发佣信息请求参数 settlementUpdateDto:{}", settlementUpdateDto.toString());
        SettlementUpdateResp settlementUpdateResp = settlementFeignClient.settlementUpdate(settlementUpdateDto);
        log.info("分销佣金提现修改发佣信息响应参数 settlementUpdateResp:{}", settlementUpdateResp.toString());
        if (settlementUpdateResp.getCode() != 1) {
            throw new LuckException(settlementUpdateResp.getMsg());
        }
        // 同步修改用户账户表上的银行卡信息
        distributionJointVentureCommissionCustomer.setBankCardNo(cardNo);
        distributionJointVentureCommissionCustomerMapper.update(distributionJointVentureCommissionCustomer);
        // 启动一个定时任务，1分钟后去查询一次状态变化
//        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
//            if (StringUtils.isNotEmpty(distributionJointVentureCommissionApply.getApplyNo())) {
//                QuerySettlementStatusDto querySettlementStatusDto = new QuerySettlementStatusDto();
//                querySettlementStatusDto.setRequestId(distributionJointVentureCommissionApply.getApplyNo());
//                QuerySettlementStatusResp querySettlementStatusResp = settlementFeignClient.querySettlementStatus(querySettlementStatusDto);
//                if (querySettlementStatusResp.getCode() == 1) {
//                    distributionJointVentureCommissionApply.setStatus();
//                    distributionJointVentureCommissionApplyMapper.updateByPrimaryKey(distributionJointVentureCommissionApply);
//                }
//            }
//        }, 1, TimeUnit.MINUTES);

    }

    private void doValidateCustomerJDYSStatus(DistributionJointVentureCommissionCustomer jointVentureCommissionCustomer) {
        QueryMemberAuditStatusByCertNoDto queryMemberAuditStatusByCertNoDto = new QueryMemberAuditStatusByCertNoDto();
        queryMemberAuditStatusByCertNoDto.setCertNo(jointVentureCommissionCustomer.getIdCard());
        queryMemberAuditStatusByCertNoDto.setCertType("201");
        QueryMemberAuditStatusByCertNoResp queryMemberAuditStatusByCertNoResp = memberFeignClient.queryMemberAuditStatusByCertNo(queryMemberAuditStatusByCertNoDto);
        if (queryMemberAuditStatusByCertNoResp.getCode() != 1) {
            throw new LuckException(queryMemberAuditStatusByCertNoResp.getMsg());
        } else if (queryMemberAuditStatusByCertNoResp.getData().getResult() != 1) {
            throw new LuckException("联营分佣客户京东益世审核状态未通过：" + queryMemberAuditStatusByCertNoResp.getData().getRemark());
        }
    }

    protected void doSubCommissionByJDYS(DistributionJointVentureCommissionCustomer jointVentureCustomer, DistributionJointVentureCommissionApply jointVentureCommissionApply) {
        // 调用京东益市分佣
        SettlementApplyDto dto = new SettlementApplyDto(JosBusiness.JointVentureCommission);
//        dto.setRequestId(UUID.randomUUID().toString().replace("-", ""));
        //调用京东的发佣接口，这里requestId传申请编号，确保一个申请记录只放款一次。
        dto.setRequestId(jointVentureCommissionApply.getApplyNo());
        dto.setBusinessNo(jointVentureCommissionApply.getApplyNo());
        dto.setCertNo(jointVentureCustomer.getIdCard());
        dto.setCertType("201");
        dto.setPayType("ENTERPRISES_PAY");
        dto.setCardName(jointVentureCustomer.getCardholderName());
        dto.setCardNo(jointVentureCustomer.getBankCardNo());
        dto.setBankName(jointVentureCustomer.getBankSimpleName());
        dto.setAccountType(1);
        dto.setCardPhone(jointVentureCustomer.getPhone());
        dto.setAmountWay(0);
        dto.setAmount(new BigDecimal(jointVentureCommissionApply.getCommissionAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        log.info("联营分佣审核分佣第三方请求参数 SettlementApplyDto:{}", dto);
        SettlementApplyResp settlementApplyResp = settlementFeignClient.memberAndProtocoInfo(dto);
        log.info("联营分佣审核分佣第三方返回参数 SettlementApplyResp:{}", settlementApplyResp.toString());
        if (settlementApplyResp.getCode() == 1) {
            jointVentureCommissionApply.setRequestId(settlementApplyResp.getData().getRequestId());
        } else {
            //jointVentureCommissionApply.setRequestRespInfo(settlementApplyResp.getMsg());
            throw new LuckException(settlementApplyResp.getMsg());
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, DistributionJointVentureCommissionSearchDTO searchDTO) {
        ExcelUtil.soleExcel(response, listExportExcelDataList(searchDTO), DistributionJointVentureCommissionApplyExcelVO.EXCEL_NAME, DistributionJointVentureCommissionApplyExcelVO.MERGE_ROW_INDEX, DistributionJointVentureCommissionApplyExcelVO.MERGE_COLUMN_INDEX, DistributionJointVentureCommissionApplyExcelVO.class);
    }

    @Override
    public void exportExcelInDownloadCenter(DistributionJointVentureCommissionSearchDTO searchDTO) {
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DistributionJointVentureCommissionApplyExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }
        List<DistributionJointVentureCommissionApplyExcelVO> applyExcelVOS = listExportExcelDataList(searchDTO);

        FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        if (CollUtil.isEmpty(applyExcelVOS)) {
            finishDownLoadDTO.setRemarks("没有可导出的数据");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("没有可导出的数据");
            return;
        }
        File file = null;
        try {
            int calCount = applyExcelVOS.size();
            log.info("导出数据行数 【{}】", calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行联营分佣申请记录生成excel 总条数【{}】", calCount);
            String pathExport = SkqUtils.getExcelFilePath() + "/" + SkqUtils.getExcelName() + ".xls";
            EasyExcel.write(pathExport, DistributionJointVentureCommissionApplyExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(applyExcelVOS);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行联营分佣申请记录生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime, applyExcelVOS.size(), pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file = new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO != null) {
                log.info("上传文件地址：" + responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                String fileName = DateUtil.format(new Date(), "yyyyMMddHHmmss") + "" + DistributionJointVentureCommissionApplyExcelVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("导出订单信息错误: " + e.getMessage(), e);
            finishDownLoadDTO.setRemarks("导出联营分佣申请记录失败，信息错误：" + e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    private List<DistributionJointVentureCommissionApplyExcelVO> listExportExcelDataList(DistributionJointVentureCommissionSearchDTO searchDTO) {
        DistributionJointVentureCommissionApply search = mapperFacade.map(searchDTO, DistributionJointVentureCommissionApply.class);
        List<DistributionJointVentureCommissionApply> jointVentureCommissionApplyList = distributionJointVentureCommissionApplyMapper.selectBySelective(search);
        if (CollectionUtils.isEmpty(jointVentureCommissionApplyList)) {
            return Collections.emptyList();
        }
        List<DistributionJointVentureCommissionApplyExcelVO> jointVentureCommissionApplyExcelVOList = jointVentureCommissionApplyList.stream().map(apply -> {
            DistributionJointVentureCommissionApplyExcelVO applyExcelVO = new DistributionJointVentureCommissionApplyExcelVO();
            applyExcelVO.setCustomerName(apply.getCustomerName());
            applyExcelVO.setCustomerPhone(apply.getCustomerPhone());
            applyExcelVO.setApplyNo(apply.getApplyNo());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM日dd日hh:mm");
            applyExcelVO.setOrderTime(dateFormat.format(apply.getOrderTimeLowerBound()) + "至" + dateFormat.format(apply.getOrderTimeUpperBound()));
            applyExcelVO.setOrderTurnover(String.valueOf(new BigDecimal(Optional.ofNullable(apply.getOrderTurnover()).orElse(0L)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
            applyExcelVO.setCommissionRate(apply.getCommissionRate() + "%");
            applyExcelVO.setCommissionAmount(String.valueOf(new BigDecimal(Optional.ofNullable(apply.getCommissionAmount()).orElse(0L)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
            String status = "";
            switch (apply.getStatus()) {
                case 0:
                    status = "待审核";
                    break;
                case 1:
                    status = "待付款";
                    break;
                case 2:
                    status = "付款成功";
                    break;
                case 9:
                    status = "审核不通过";
                    break;
                default:
                    status = apply.getRequestRespInfo();
            }
            applyExcelVO.setStatus(status);
            return applyExcelVO;
        }).collect(Collectors.toList());
        return jointVentureCommissionApplyExcelVOList;
    }

    private void validateJointVentureOrderIsItApplied(List<Long> applyOrderIdList) {
        if (CollectionUtils.isEmpty(applyOrderIdList)) {
            return;
        }
        int citedNum = distributionJointVentureCommissionApplyOrderMapper.countProcessingOrderByOrderIdList(applyOrderIdList);
        if (citedNum > 0) {
            throw new LuckException("存在申请中的订单");
        }
    }
}
