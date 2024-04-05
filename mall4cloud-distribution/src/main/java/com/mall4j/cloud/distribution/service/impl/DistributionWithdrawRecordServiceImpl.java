package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.api.docking.jos.dto.*;
import com.mall4j.cloud.api.docking.jos.enums.JosBusiness;
import com.mall4j.cloud.api.docking.jos.feign.SettlementFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawRecordMapper;
import com.mall4j.cloud.distribution.mapper.OrderCommissionHistoryMapper;
import com.mall4j.cloud.distribution.model.*;
import com.mall4j.cloud.distribution.service.*;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawRecordVO;
import com.mall4j.cloud.distribution.vo.MemberWithdrawRecordExcelVo;
import com.mall4j.cloud.distribution.vo.StaffWithdrawRecordExcelVo;
import com.mall4j.cloud.distribution.vo.WithdrawRecordExcelVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 佣金管理-佣金提现记录
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
@Service
@Slf4j
public class DistributionWithdrawRecordServiceImpl implements DistributionWithdrawRecordService {

    @Resource
    private DistributionWithdrawRecordMapper distributionWithdrawRecordMapper;

    @Resource
    private OrderCommissionHistoryMapper orderCommissionHistoryMapper;

    @Autowired
    private DistributionWithdrawConfigService distributionWithdrawConfigService;

    @Autowired
    private DistributionWithdrawOrderService distributionWithdrawOrderService;

    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;
    @Autowired
    private DistributionOrderCommissionLogService distributionOrderCommissionLogService;
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private SettlementFeignClient settlementFeignClient;

    @Autowired
    private DistributionCommissionAccountAuthService distributionCommissionAccountAuthService;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());


    @Override
    public PageVO<DistributionWithdrawRecordVO> page(PageDTO pageDTO, DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        PageVO<DistributionWithdrawRecordVO> page = PageUtil.doPage(pageDTO, () -> distributionWithdrawRecordMapper.list(distributionWithdrawRecordDTO));
        if (CollectionUtils.isEmpty(page.getList())){
            return new PageVO<>();
        }
        page.getList().forEach(distributionWithdrawRecordVO -> {
            StoreVO storeVO = storeFeignClient.findByStoreId(distributionWithdrawRecordVO.getStoreId());
            if (null != storeVO) {
                distributionWithdrawRecordVO.setStoreName(storeVO.getName());
                distributionWithdrawRecordVO.setStoreCode(storeVO.getStoreCode());
            }
            DistributionCommissionAccountAuth accountAuth = distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(distributionWithdrawRecordVO.getIdentityType(), distributionWithdrawRecordVO.getUserId());
            if (accountAuth != null) {
                distributionWithdrawRecordVO.setAuthUsername(accountAuth.getName());
            }
        });
        return page;
    }

    @Override
    public DistributionWithdrawRecord getById(Long id) {
        return distributionWithdrawRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionWithdrawRecord distributionWithdrawRecord) {
        distributionWithdrawRecordMapper.save(distributionWithdrawRecord);
    }

    @Override
    public void update(DistributionWithdrawRecord distributionWithdrawRecord) {
        distributionWithdrawRecordMapper.update(distributionWithdrawRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionWithdrawRecordMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @GlobalTransactional(rollbackFor = Exception.class)
    public void applyWithdraw(DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        Integer identityType = distributionWithdrawRecordDTO.getIdentityType();
        Long userId = distributionWithdrawRecordDTO.getUserId();

        // 校验提现账户信息
        DistributionCommissionAccountAuth accountAuth = distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(identityType, userId);
        if (null == accountAuth || accountAuth.getAuthStatus() != 1) {
            throw new LuckException("提现身份认证信息有误");
        }
        if (StringUtils.isAnyBlank(accountAuth.getCardName(), accountAuth.getCardNo())) {
            throw new LuckException("请先完善银行卡信息");
        }
        DistributionCommissionAccount commissionAccount = distributionCommissionAccountService.getByUser(userId, identityType);
        if (commissionAccount == null) {
            throw new LuckException("佣金账户不存在");
        }

        // 获取系统提现风控配置
        DistributionWithdrawConfig withdrawConfig = distributionWithdrawConfigService.getByIdentityType(identityType);
        if (null == withdrawConfig) {
            throw new LuckException("提现配置有误,请联系商家");
        }

        // 校验提现订单
        if (CollectionUtils.isEmpty(distributionWithdrawRecordDTO.getOrderIdList())) {
            throw new LuckException("请勾选提现订单");
        }
        ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.getEsOrderList(distributionWithdrawRecordDTO.getOrderIdList().stream().distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(orderListData.getData())) {
            throw new LuckException("申请提现订单列表为空");
        }
        AtomicLong commissionAmount = new AtomicLong(0L);
        List<Long> distributionOrderIds = new ArrayList<>();
        List<Long> developingOrderIds = new ArrayList<>();
        orderListData.getData().forEach(esOrderBO -> {
            if (identityType == 1) {
                if (esOrderBO.getDistributionUserType() == 1) {
                    if (esOrderBO.getDistributionStatus() != 1) {
                        throw new LuckException("申请提现订单佣金状态异常");
                    }
                    if (esOrderBO.getDistributionAmount() <= 0) {
                        throw new LuckException("订单分销佣金异常");
                    }
                    commissionAmount.getAndAdd(esOrderBO.getDistributionAmount());
                    distributionOrderIds.add(esOrderBO.getOrderId());
                } else {
                    if (esOrderBO.getDevelopingStatus() != 1){
                        throw new LuckException("申请提现订单佣金状态异常");
                    }
                    if (esOrderBO.getDevelopingAmount() <= 0){
                        throw new LuckException("订单发展佣金异常");
                    }
                    commissionAmount.getAndAdd(esOrderBO.getDevelopingAmount());
                    developingOrderIds.add(esOrderBO.getOrderId());
                }
            } else {
                if (esOrderBO.getDistributionUserType() == 1){
                    throw new LuckException("申请提现订单异常");
                }
                if (esOrderBO.getDistributionStatus() != 1) {
                    throw new LuckException("申请提现订单佣金状态异常");
                }
                if (esOrderBO.getDistributionAmount() <= 0) {
                    throw new LuckException("订单分销佣金异常");
                }
                commissionAmount.getAndAdd(esOrderBO.getDistributionAmount());
                distributionOrderIds.add(esOrderBO.getOrderId());
            }
        });

        // 获取该用户已提现需退款金额
        long needRefundCommissionAmount = getWithdrawNeedRefundAmount(identityType, userId);

        // 最终提现金额
        long finalWithdrawAmount = commissionAmount.longValue() - needRefundCommissionAmount;
        log.info("微客发起佣金申请：提现订单佣金：{}, 已提现需退佣金：{}, 最终提现金额：{}",
                commissionAmount.longValue(), needRefundCommissionAmount, finalWithdrawAmount);
        if (finalWithdrawAmount <= 0) {
            throw new LuckException("发起提现申请失败：提现金额小于或等于0");
        }

        // 校验系统提现风控配置
        checkSystemWithdrawWindControlConfig(withdrawConfig, userId, identityType, finalWithdrawAmount);

        // 生成提现记录
        DistributionWithdrawRecord record = new DistributionWithdrawRecord();
        BeanUtils.copyProperties(distributionWithdrawRecordDTO, record);
        record.setWithdrawOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + userId).substring(1));
        record.setWithdrawAmount(finalWithdrawAmount);
        record.setWithdrawNeedRefundAmount(needRefundCommissionAmount);
        record.setTransferType(1);
        record.setApplyTime(new Date());
        record.setWithdrawStatus(0);
        record.setHistoryOrder(0);
        if (identityType == 1) {
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(record.getUserId());
            if (staffData.isSuccess() && null != staffData.getData()) {
                record.setUsername(staffData.getData().getStaffName());
                record.setUserNumber(staffData.getData().getStaffNo());
                record.setMobile(staffData.getData().getMobile());
                record.setStoreId(staffData.getData().getStoreId());
            }
        } else {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(record.getUserId());
            if (userData.isSuccess() && null != userData.getData()) {
                record.setUsername(userData.getData().getNickName());
                record.setMobile(userData.getData().getUserMobile());
                record.setStoreId(userData.getData().getStoreId());
            }
        }
        distributionWithdrawRecordMapper.save(record);
        // 生成提现订单记录
        distributionWithdrawRecordDTO.getOrderIdList().forEach(orderId -> {
            DistributionWithdrawOrder order = new DistributionWithdrawOrder();
            order.setRecordId(record.getId());
            order.setUserId(record.getUserId());
            order.setIdentityType(record.getIdentityType());
            order.setOrderId(orderId);
            distributionWithdrawOrderService.save(order);
        });
        // 修改订单对应分销状态
        if (CollectionUtils.isNotEmpty(distributionOrderIds)) {
            orderFeignClient.updateDistributionStatusBatchById(distributionOrderIds, 3, null, null);
        }
        if (CollectionUtils.isNotEmpty(developingOrderIds)) {
            orderFeignClient.updateDevelopingStatusBatchById(developingOrderIds, 3, null, null);
        }
        // 扣减可提现佣金
        distributionCommissionAccountService.updateCommission(commissionAccount, needRefundCommissionAmount, CommissionChangeTypeEnum.REDUCE_WITHDRAW_NEED_REFUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @GlobalTransactional(rollbackFor = Exception.class)
    public Boolean processWithdraw(DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        // 校验提现记录
        DistributionWithdrawRecord record = distributionWithdrawRecordMapper.getRecordByNo(distributionWithdrawRecordDTO.getWithdrawOrderNo());
        if (null == record) {
            throw new LuckException("提现记录不存在");
        }
        if (record.getWithdrawStatus() != 0) {
            throw new LuckException("提现申请已处理");
        }
        // 校验是否存在提现失败的记录（京东不支持存在提现失败的发佣继续申请新的发佣）
        boolean exist = distributionWithdrawRecordMapper.isExistWithdrawFailRecord(record.getUserId(), record.getIdentityType());
        if (exist) {
            throw new LuckException("存在提现失败的记录，请先处理失败的提现记录");
        }
        // 校验提现佣金账户
        DistributionCommissionAccount account = distributionCommissionAccountService.getByUser(record.getUserId(), record.getIdentityType());
        if (account.getCanWithdraw() < record.getWithdrawAmount()) {
            throw new LuckException("可提现佣金不足");
        }
        DistributionCommissionAccountAuth accountAuth = distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(record.getIdentityType(), record.getUserId());
        if (null == accountAuth || accountAuth.getAuthStatus() != 1) {
            throw new LuckException("提现身份认证信息失败");
        }
        // 处理提现订单
        List<Long> distributionOrderIds = new ArrayList<>();
        List<Long> developingOrderIds = new ArrayList<>();
        List<OrderCommissionHistory> orderCommissionHistories = new ArrayList<>();
        if (record.getHistoryOrder() == 0) {
            List<DistributionWithdrawOrder> orderList = distributionWithdrawOrderService.listDistributionWithdrawOrderByRecord(record.getId());
            if (CollectionUtils.isEmpty(orderList)) {
                throw new LuckException("申请提现订单列表为空");
            }
            ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.getEsOrderList(orderList.stream().map(DistributionWithdrawOrder::getOrderId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(orderListData.getData())) {
                throw new LuckException("申请提现订单列表为空");
            }
            orderListData.getData().forEach(esOrderBO -> {
                if (distributionWithdrawRecordDTO.getIdentityType() == 1){
                    if (esOrderBO.getDistributionUserType() == 1){
                        if (esOrderBO.getDistributionStatus() != 3) {
                            throw new LuckException("申请提现订单佣金状态异常");
                        }
                        distributionOrderIds.add(esOrderBO.getOrderId());
                    } else {
                        if (esOrderBO.getDevelopingStatus() != 3) {
                            throw new LuckException("申请提现订单佣金状态异常");
                        }
                        developingOrderIds.add(esOrderBO.getOrderId());
                    }
                } else {
                    if (esOrderBO.getDistributionUserType() == 1){
                        throw new LuckException("申请提现订单异常");
                    }
                    if (esOrderBO.getDistributionStatus() != 3) {
                        throw new LuckException("申请提现订单佣金状态异常");
                    }
                    distributionOrderIds.add(esOrderBO.getOrderId());
                }
            });
        } else {
            List<DistributionWithdrawOrder> orderList = distributionWithdrawOrderService.listDistributionWithdrawOrderByRecord(record.getId());
            if (CollectionUtils.isEmpty(orderList)){
                throw new LuckException("申请历史提现订单列表为空");
            }
            orderCommissionHistories = orderCommissionHistoryMapper.listOrderInIds(orderList.stream().map(DistributionWithdrawOrder::getOrderId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(orderCommissionHistories)) {
                throw new LuckException("申请提现历史订单列表为空");
            }
            if (orderCommissionHistories.stream().anyMatch(orderCommissionHistory -> orderCommissionHistory.getStatus() != 1)){
                throw new LuckException("申请提现历史订单状态有误");
            }
        }

        record.setProcessTime(new Date());
        UserInfoInTokenBO userInfo = AuthUserContext.get();
        record.setReviewerId(userInfo.getUserId());
        if (distributionWithdrawRecordDTO.getProcessType() == 1) {
            SettlementApplyDto dto = new SettlementApplyDto(JosBusiness.MicroCustomerCommission);
            dto.setBusinessNo(record.getWithdrawOrderNo());
//            dto.setRequestId(UUID.randomUUID().toString());
            dto.setRequestId(record.getWithdrawOrderNo());
            dto.setCertNo(accountAuth.getCertNo());
            dto.setCertType("201");
            dto.setPayType("ENTERPRISES_PAY");
            dto.setCardName(accountAuth.getCardName());
            dto.setCardNo(accountAuth.getCardNo());
            dto.setBankName("");
            dto.setAccountType(1);
            dto.setCardPhone(accountAuth.getTelephone());
            dto.setAmountWay(0);
            dto.setAmount(new BigDecimal(record.getWithdrawAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            log.info("审核提现第三方请求参数 SettlementApplyDto:{}", dto);
            SettlementApplyResp settlementApplyResp = settlementFeignClient.memberAndProtocoInfo(dto);
            log.info("审核提现第三方返回参数 SettlementApplyResp:{}", settlementApplyResp.toString());
            if (settlementApplyResp.getCode() == 1) {
                record.setWithdrawStatus(1);
                record.setApplyId(settlementApplyResp.getData().getRequestId());
            } else {
                record.setWithdrawStatus(3);
                record.setTransferFailReason(settlementApplyResp.getMsg());
            }
        } else {
            record.setWithdrawStatus(4);
            record.setRejectReason(distributionWithdrawRecordDTO.getRejectReason());
            if (record.getHistoryOrder() == 0) {
                // 拒绝 修改订单佣金状态:已结算
                if (CollectionUtils.isNotEmpty(distributionOrderIds)) {
                    orderFeignClient.updateDistributionStatusBatchById(distributionOrderIds, 1, null, null);
                }
                if (CollectionUtils.isNotEmpty(developingOrderIds)) {
                    orderFeignClient.updateDevelopingStatusBatchById(developingOrderIds, 1, null, null);
                }
                // 拒绝 处理佣金需退还
                distributionCommissionAccountService.updateCommission(account, record.getWithdrawNeedRefundAmount(), CommissionChangeTypeEnum.REDUCE_WITHDRAW_NEED_REFUND);
            } else {
                orderCommissionHistories.forEach(orderCommissionHistory -> {
                    orderCommissionHistory.setStatus(0);
                    orderCommissionHistoryMapper.update(orderCommissionHistory);
                });
            }
        }
        distributionWithdrawRecordMapper.update(record);
        return record.getWithdrawStatus() == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @GlobalTransactional(rollbackFor = Exception.class)
    public void successAfterWithdraw(DistributionWithdrawRecord record){
        DistributionCommissionAccount account = distributionCommissionAccountService.getByUser(record.getUserId(), record.getIdentityType());
        if (account.getCanWithdraw() < record.getWithdrawAmount()) {
            throw new LuckException("可提现佣金不足");
        }
        if (record.getHistoryOrder() == 0){
            List<DistributionWithdrawOrder> orderList = distributionWithdrawOrderService.listDistributionWithdrawOrderByRecord(record.getId());
            if (CollectionUtils.isEmpty(orderList)) {
                throw new LuckException("申请提现订单列表为空");
            }
            ServerResponseEntity<List<EsOrderBO>> orderListData = orderFeignClient.getEsOrderList(orderList.stream().map(DistributionWithdrawOrder::getOrderId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(orderListData.getData())) {
                throw new LuckException("申请提现订单列表为空");
            }
            List<Long> distributionOrderIds = new ArrayList<>();
            List<Long> developingOrderIds = new ArrayList<>();
            List<DistributionOrderCommissionLog> logList = new ArrayList<>();
            orderListData.getData().forEach(esOrderBO -> {
                DistributionOrderCommissionLog log = new DistributionOrderCommissionLog();
                log.setCommissionStatus(2);
                log.setOrderId(esOrderBO.getOrderId());
                log.setIdentityType(account.getIdentityType());
                log.setUserId(account.getUserId());
                log.setStoreId(account.getStoreId());

                // FIXME：这里的日志金额可能会因为先提现申请后售后再审核导致这里的日志金额和提现金额不一致
                if (record.getIdentityType() == 1) {
                    if (esOrderBO.getDistributionUserType() == 1) {
                        if (esOrderBO.getDistributionStatus() != 3) {
                            throw new LuckException("申请提现订单佣金状态异常");
                        }
                        distributionOrderIds.add(esOrderBO.getOrderId());
                        log.setCommissionAmount(esOrderBO.getDistributionAmount());
                        log.setCommissionType(1);
                    } else {
                        if (esOrderBO.getDevelopingStatus() != 3){
                            throw new LuckException("申请提现订单佣金状态异常");
                        }
                        developingOrderIds.add(esOrderBO.getOrderId());
                        log.setCommissionAmount(esOrderBO.getDevelopingAmount());
                        log.setCommissionType(2);
                    }
                } else {
                    if (esOrderBO.getDistributionUserType() == 1){
                        throw new LuckException("申请提现订单异常");
                    }
                    if (esOrderBO.getDistributionStatus() != 3) {
                        throw new LuckException("申请提现订单佣金状态异常");
                    }
                    distributionOrderIds.add(esOrderBO.getOrderId());
                    log.setCommissionAmount(esOrderBO.getDistributionAmount());
                    log.setCommissionType(1);
                }
                logList.add(log);
            });
            // 修改订单佣金状态:已提现
            if (CollectionUtils.isNotEmpty(distributionOrderIds)) {
                orderFeignClient.updateDistributionStatusBatchById(distributionOrderIds, 2, null, new Date());
            }
            if (CollectionUtils.isNotEmpty(developingOrderIds)) {
                orderFeignClient.updateDevelopingStatusBatchById(developingOrderIds, 2, null, new Date());
            }
            // 保存日志
            if (CollectionUtils.isNotEmpty(logList)) {
                distributionOrderCommissionLogService.batchSave(logList);
            }
        } else {
            List<DistributionWithdrawOrder> orderList = distributionWithdrawOrderService.listDistributionWithdrawOrderByRecord(record.getId());
            if (CollectionUtils.isEmpty(orderList)){
                throw new LuckException("申请提现订单列表为空");
            }
            List<OrderCommissionHistory> orderCommissionHistories = orderCommissionHistoryMapper.listOrderInIds(orderList.stream().map(DistributionWithdrawOrder::getOrderId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(orderCommissionHistories)) {
                throw new LuckException("申请提现历史订单列表为空");
            }
            if (orderCommissionHistories.stream().anyMatch(orderCommissionHistory -> orderCommissionHistory.getStatus() != 1)){
                throw new LuckException("申请提现历史订单状态有误");
            }
            orderCommissionHistories.forEach(orderCommissionHistory -> {
                orderCommissionHistory.setStatus(2);
                orderCommissionHistoryMapper.update(orderCommissionHistory);
            });
        }
        distributionCommissionAccountService.updateCommission(account, record.getWithdrawAmount(), CommissionChangeTypeEnum.ADD_ALREADY_WITHDRAW);
    }


    /**
     * 校验提现
     *
     * @param withdrawConfig   提现配置信息
     * @param userId           用户ID
     * @param identityType     身份类型 1导购 2威客
     * @param commissionAmount 提现佣金
     * @return
     */
    private void checkSystemWithdrawWindControlConfig(DistributionWithdrawConfig withdrawConfig, Long userId, Integer identityType, Long commissionAmount) {
        if (withdrawConfig.getFrequencyType() == 1) {
            Date startTime = new Date();
            Date endTime = new Date();
            if (withdrawConfig.getScopeType() == 1) {
                startTime = DateUtil.beginOfDay(startTime);
                endTime = DateUtil.endOfDay(endTime);
            } else if (withdrawConfig.getScopeType() == 2) {
                startTime = DateUtil.beginOfWeek(startTime);
                endTime = DateUtil.endOfWeek(new Date());
            } else if (withdrawConfig.getScopeType() == 3) {
                startTime = DateUtil.beginOfMonth(startTime);
                endTime = DateUtil.endOfMonth(endTime);
            }
            List<DistributionWithdrawRecord> withdrawRecords = distributionWithdrawRecordMapper.listWithdrawRecordByTime(identityType, userId, startTime, endTime);
            if (withdrawRecords.size() >= withdrawConfig.getScopeCount()) {
                throw new LuckException("提现次数已上限");
            }
        } else if (withdrawConfig.getFrequencyType() == 2){
            if (LocalDateTime.now().getDayOfMonth() != withdrawConfig.getSpecifyDate()) {
                throw new LuckException("今日不可提现");
            }
            List<DistributionWithdrawRecord> withdrawRecords = distributionWithdrawRecordMapper.listWithdrawRecordByTime(identityType, userId, DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()));
            if (withdrawRecords.size() >= withdrawConfig.getScopeCount()) {
                throw new LuckException("提现次数已上限");
            }
        }
        if (commissionAmount < withdrawConfig.getWithdrawMin() || commissionAmount > withdrawConfig.getWithdrawMax()) {
            throw new LuckException("已超出提现范围");
        }
    }


    @Override
    public List<DistributionWithdrawRecord> listWithdrawRecordByTime(Integer identityType, Long userId, Date startTime, Date endTime) {
        return distributionWithdrawRecordMapper.listWithdrawRecordByTime(identityType, userId, startTime, endTime);
    }

    @Override
    public DistributionWithdrawRecord getRecordByNo(String withdrawOrderNo) {
        return distributionWithdrawRecordMapper.getRecordByNo(withdrawOrderNo);
    }

    @Override
    public void withdrawRecordExcel(HttpServletResponse response, DistributionWithdrawRecordDTO dto) {
        if (dto.getIdentityType() == 1) {
            ExcelUtil.soleExcel(response, listStaffWithdrawRecordExcel(dto), StaffWithdrawRecordExcelVo.EXCEL_NAME, StaffWithdrawRecordExcelVo.MERGE_ROW_INDEX, StaffWithdrawRecordExcelVo.MERGE_COLUMN_INDEX, StaffWithdrawRecordExcelVo.class);
        } else {
            ExcelUtil.soleExcel(response, listMemberWithdrawRecordExcel(dto), MemberWithdrawRecordExcelVo.EXCEL_NAME, MemberWithdrawRecordExcelVo.MERGE_ROW_INDEX, MemberWithdrawRecordExcelVo.MERGE_COLUMN_INDEX, MemberWithdrawRecordExcelVo.class);
        }
    }

    @Override
    public List<DistributionWithdrawRecord> listWithdrawRecordByStatus(Integer status) {
        return distributionWithdrawRecordMapper.listWithdrawRecordByStatus(status);
    }

    @Override
    public void applyHistoryWithdraw(DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        if (CollectionUtils.isEmpty(distributionWithdrawRecordDTO.getOrderIdList())) {
            throw new LuckException("请勾选提现历史订单");
        }
        DistributionWithdrawConfig withdrawConfig = distributionWithdrawConfigService.getByIdentityType(distributionWithdrawRecordDTO.getIdentityType());
        if (null == withdrawConfig) {
            throw new LuckException("提现配置有误,请联系商家");
        }
        DistributionCommissionAccountAuth accountAuth = distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(distributionWithdrawRecordDTO.getIdentityType(), distributionWithdrawRecordDTO.getUserId());
        if (null == accountAuth || accountAuth.getAuthStatus() != 1){
            throw new LuckException("提现身份认证信息有误");
        }
        List<OrderCommissionHistory> orderCommissionHistories = orderCommissionHistoryMapper.listOrderInIds(distributionWithdrawRecordDTO.getOrderIdList());
        if (CollectionUtils.isEmpty(orderCommissionHistories)) {
            throw new LuckException("申请提现历史订单列表为空");
        }
        if (orderCommissionHistories.stream().anyMatch(orderCommissionHistory -> orderCommissionHistory.getStatus() != 0)){
            throw new LuckException("申请提现历史订单状态有误");
        }
        Long commissionAmount = orderCommissionHistories.stream().mapToLong(OrderCommissionHistory::getCommission).sum();
        checkSystemWithdrawWindControlConfig(withdrawConfig, distributionWithdrawRecordDTO.getUserId(), distributionWithdrawRecordDTO.getIdentityType(), commissionAmount);
        DistributionWithdrawRecord record = new DistributionWithdrawRecord();
        BeanUtils.copyProperties(distributionWithdrawRecordDTO, record);
        record.setWithdrawOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + distributionWithdrawRecordDTO.getUserId()).substring(1));
        record.setWithdrawAmount(commissionAmount);
        record.setTransferType(1);
        record.setApplyTime(new Date());
        record.setWithdrawStatus(0);
        record.setHistoryOrder(1);
        if (distributionWithdrawRecordDTO.getIdentityType() == 1){
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(record.getUserId());
            if (staffData.isSuccess() && null != staffData.getData()){
                record.setUsername(staffData.getData().getStaffName());
                record.setUserNumber(staffData.getData().getStaffNo());
                record.setMobile(staffData.getData().getMobile());
                record.setStoreId(staffData.getData().getStoreId());
            }
        } else {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(record.getUserId());
            if (userData.isSuccess() && null != userData.getData()){
                record.setUsername(userData.getData().getNickName());
                record.setMobile(userData.getData().getUserMobile());
                record.setStoreId(userData.getData().getStoreId());
                record.setUserNumber(userData.getData().getVipcode());
            }
        }
        distributionWithdrawRecordMapper.save(record);
        orderCommissionHistories.forEach(orderCommissionHistory -> {
            orderCommissionHistory.setStatus(1);
            orderCommissionHistoryMapper.update(orderCommissionHistory);
            DistributionWithdrawOrder order = new DistributionWithdrawOrder();
            order.setRecordId(record.getId());
            order.setUserId(record.getUserId());
            order.setIdentityType(record.getIdentityType());
            order.setOrderId(orderCommissionHistory.getId());
            distributionWithdrawOrderService.save(order);
        });
    }

    @Override
    public Long getWithdrawNeedRefundAmount(Integer identityType, Long userId) {
        DistributionCommissionAccount commissionAccount = distributionCommissionAccountService.getByUser(userId, identityType);
        if (commissionAccount == null) {
            throw new LuckException("获取已提现待退还佣金失败：佣金账户不存在");
        }
        return commissionAccount.getWithdrawNeedRefund();
    }

    @Override
    public void updateWithdrawRecordBankCard(String applyId, String cardNo) {
        DistributionWithdrawRecord withdrawRecord = distributionWithdrawRecordMapper.getByApplyId(applyId);
        if (withdrawRecord == null) {
            throw new LuckException("修改银行卡信息失败：提现记录不存在");
        } else if (withdrawRecord.getWithdrawStatus() != 3) {
            throw new LuckException("提现申请非发佣失败状态不支持修改银行卡信息");
        }

        DistributionCommissionAccountAuth accountAuth = distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(withdrawRecord.getIdentityType(), withdrawRecord.getUserId());
        if (accountAuth == null) {
            throw new LuckException("修改银行卡信息失败：佣金账户认证信息不存在");
        }

        // 调用京东益世修改发佣申请信息
        SettlementUpdateDto settlementUpdateDto = new SettlementUpdateDto();
        settlementUpdateDto.setRequestId(applyId);
        settlementUpdateDto.setCardNo(cardNo);
        log.info("分销佣金提现修改发佣信息请求参数 settlementUpdateDto:{}", settlementUpdateDto.toString());
        SettlementUpdateResp settlementUpdateResp = settlementFeignClient.settlementUpdate(settlementUpdateDto);
        log.info("分销佣金提现修改发佣信息响应参数 settlementUpdateResp:{}", settlementUpdateResp.toString());
        if (settlementUpdateResp.getCode() != 1) {
            throw new LuckException(settlementUpdateResp.getMsg());
        }
        // 同步修改用户账户表上的银行卡信息
        accountAuth.setCardNo(cardNo);
        distributionCommissionAccountAuthService.update(accountAuth);
        // 启动一个定时任务，1分钟后去查询一次状态变化
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            if (StringUtils.isNotEmpty(withdrawRecord.getApplyId())) {
                QuerySettlementStatusDto querySettlementStatusDto = new QuerySettlementStatusDto();
                querySettlementStatusDto.setRequestId(withdrawRecord.getApplyId());
                QuerySettlementStatusResp querySettlementStatusResp = settlementFeignClient.querySettlementStatus(querySettlementStatusDto);
                if (querySettlementStatusResp.getCode() == 1) {
                    withdrawRecord.setWithdrawStatus(2);
                    distributionWithdrawRecordMapper.update(withdrawRecord);
                }
            }
        }, 1, TimeUnit.MINUTES);
    }

    private List<StaffWithdrawRecordExcelVo> listStaffWithdrawRecordExcel(DistributionWithdrawRecordDTO dto) {
        List<DistributionWithdrawRecordVO> list = distributionWithdrawRecordMapper.list(dto);
        List<StaffWithdrawRecordExcelVo> staffWithdrawRecordExcelVos = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return staffWithdrawRecordExcelVos;
        }
        list.forEach(record -> {
            StaffWithdrawRecordExcelVo vo = new StaffWithdrawRecordExcelVo();
            buildWithdrawRecordExcel(vo, record);
            vo.setName(record.getUsername());
            vo.setNumber(record.getUserNumber());
            staffWithdrawRecordExcelVos.add(vo);
        });
        return staffWithdrawRecordExcelVos;
    }

    private List<MemberWithdrawRecordExcelVo> listMemberWithdrawRecordExcel(DistributionWithdrawRecordDTO dto) {
        List<DistributionWithdrawRecordVO> list = distributionWithdrawRecordMapper.list(dto);
        List<MemberWithdrawRecordExcelVo> memberWithdrawRecordExcelVos = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return memberWithdrawRecordExcelVos;
        }
        list.forEach(record -> {
            MemberWithdrawRecordExcelVo vo = new MemberWithdrawRecordExcelVo();
            buildWithdrawRecordExcel(vo, record);
            vo.setName(record.getUsername());
            vo.setNumber(record.getUserNumber());
            memberWithdrawRecordExcelVos.add(vo);
        });
        return memberWithdrawRecordExcelVos;
    }

    private void buildWithdrawRecordExcel(WithdrawRecordExcelVo vo, DistributionWithdrawRecordVO record) {
        vo.setMobile(record.getMobile());
        vo.setWithdrawOrderNo(record.getWithdrawOrderNo());
        vo.setWithdrawAmount(conversionPrices(record.getWithdrawAmount()));
        vo.setTransferType("第三方转账");
        vo.setApplyTime(record.getApplyTime());
        vo.setProcessTime(record.getProcessTime());
        switch (record.getWithdrawStatus()) {
            case 0:
                vo.setWithdrawStatus("待处理");
                break;
            case 1:
                vo.setWithdrawStatus("提现中");
                break;
            case 2:
                vo.setWithdrawStatus("提现成功");
                break;
            case 3:
                vo.setWithdrawStatus("提现失败");
                vo.setTransferFailReason(record.getTransferFailReason());
                break;
            case 4:
                vo.setWithdrawStatus("拒绝提现");
                vo.setRejectReason(record.getRejectReason());
                break;
            default:
                vo.setWithdrawStatus("未知状态");
        }
        if (null != record.getStoreId()){
            StoreVO s = storeFeignClient.findByStoreId(record.getStoreId());
            if (null != s){
                vo.setStoreName(s.getName());
                vo.setStoreCode(s.getStoreCode());
            }
        }
    }

    private String conversionPrices(Long num) {
        return String.valueOf(new BigDecimal(Optional.ofNullable(num).orElse(0L)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
    }

}
