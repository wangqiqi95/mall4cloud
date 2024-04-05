package com.mall4j.cloud.distribution.task;

import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusResp;
import com.mall4j.cloud.api.docking.jos.feign.SettlementFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionWithdrawOrder;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.service.DistributionCommissionAccountService;
import com.mall4j.cloud.distribution.service.DistributionUserIncomeService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawOrderService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawRecordService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分销佣金结算
 * @author cl
 * @date 2021-08-23 09:26:53
 */
@Component
public class DistributionCommissionSettlementTask {

    private static final Logger log = LoggerFactory.getLogger(DistributionCommissionSettlementTask.class);

    @Autowired
    private DistributionUserIncomeService distributionUserIncomeService;

    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;

    @Autowired
    private DistributionWithdrawOrderService distributionWithdrawOrderService;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private SettlementFeignClient settlementFeignClient;

    /**
     * 确认收货15天后分销订单结算
     */
    @XxlJob("distributionCommissionSettlement")
    public void distributionCommissionSettlement() {
        log.info("开始执行分销订单结算任务》》》》》》》》》》》》》》》》》》》》》");

//        distributionUserIncomeService.commissionSettlementHandle(DateUtil.offsetDay(new Date(), -Constant.DISTRIBUTION_SETTLEMENT_TIME));
        distributionCommissionAccountService.staffWithdraw();
        log.info("结束执行分销订单结算任务》》》》》》》》》》》》》》》》》》》》》");
    }

    /**
     * 分销佣金提现成功查询
     */
    @XxlJob("distributionCommissionWithdraw")
    public void distributionCommissionWithdraw(){
        log.info("开始分销佣金提现成功查询任务》》》》》》》》》》》》》》》》》》》》》");
        List<DistributionWithdrawRecord> distributionWithdrawRecords = distributionWithdrawRecordService.listWithdrawRecordByStatus(1);
        if (CollectionUtils.isEmpty(distributionWithdrawRecords)){
            return;
        }
        distributionWithdrawRecords.forEach(record -> {
            QuerySettlementStatusDto querySettlementStatusDto = new QuerySettlementStatusDto();
            querySettlementStatusDto.setAppCode("");
            querySettlementStatusDto.setRequestId(record.getApplyId());
            log.info("分销佣金提现第三方查询请求参数 querySettlementStatusDto:{}", querySettlementStatusDto.toString());
            QuerySettlementStatusResp querySettlementStatusResp = settlementFeignClient.querySettlementStatus(querySettlementStatusDto);
            log.info("分销佣金提现第三方查询返回参数 querySettlementStatusResp:{}", querySettlementStatusResp.toString());
            if (querySettlementStatusResp.getCode() != 1 && null == querySettlementStatusResp.getData()){
                return;
            }
            if (querySettlementStatusResp.getData().getStatus() == 2){
                record.setWithdrawStatus(2);
            } else if (querySettlementStatusResp.getData().getStatus() == 3){
                record.setWithdrawStatus(3);
                record.setTransferFailReason(querySettlementStatusResp.getData().getRemark());
//                correctOrderDistributionStatusToReApply(record);
            } else if (querySettlementStatusResp.getData().getStatus() == 100) {
                record.setWithdrawStatus(5);
                record.setTransferFailReason(querySettlementStatusResp.getData().getRemark());
            }
            distributionWithdrawRecordService.update(record);
            if (record.getWithdrawStatus() == 2) {
                try {
                    distributionWithdrawRecordService.successAfterWithdraw(record);
                } catch (Exception e) {
                    log.info("分销佣金提现成功业务处理异常》》》》》》》》》》》》》》》》》》》》》", e);
                }
            }
        });
        log.info("结束分销佣金提现成功查询任务》》》》》》》》》》》》》》》》》》》》》");
    }

    private void correctOrderDistributionStatusToReApply(DistributionWithdrawRecord record) {
        // 非历史订单
        if (record.getHistoryOrder() == 0) {
            List<DistributionWithdrawOrder> orderList = distributionWithdrawOrderService.listDistributionWithdrawOrderByRecord(record.getId());
            if (CollectionUtils.isEmpty(orderList)) {
                log.error("分销发佣状态回查-修改订单状态失败：申请提现订单列表为空，提现单id：{}", record.getId());
            }
            ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.getEsOrderList(orderList.stream().map(DistributionWithdrawOrder::getOrderId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(orderListData.getData())) {
                log.error("分销发佣状态回查-修改订单状态失败：申请提现订单列表为空，提现单id：{}", record.getId());
            }
            List<Long> distributionOrderIds = new ArrayList<>();
            List<Long> developingOrderIds = new ArrayList<>();
            orderListData.getData().forEach(esOrderBO -> {
                if (record.getIdentityType() == 1) {
                    if (esOrderBO.getDistributionUserType() == 1) {
                        distributionOrderIds.add(esOrderBO.getOrderId());
                    } else {
                        developingOrderIds.add(esOrderBO.getOrderId());
                    }
                } else {
                    distributionOrderIds.add(esOrderBO.getOrderId());
                }
            });
            if (CollectionUtils.isNotEmpty(distributionOrderIds)){
                orderFeignClient.updateDistributionStatusBatchById(distributionOrderIds, 1, null, null);
            }
            if (CollectionUtils.isNotEmpty(developingOrderIds)){
                orderFeignClient.updateDevelopingStatusBatchById(developingOrderIds, 1, null, null);
            }
        }
    }
}
