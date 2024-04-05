package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.CalculateDistributionCommissionResultVO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountDTO;
import com.mall4j.cloud.distribution.dto.OrderCommissionHistoryDTO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionAccountMapper;
import com.mall4j.cloud.distribution.model.*;
import com.mall4j.cloud.distribution.service.*;
import com.mall4j.cloud.distribution.vo.CommissionAccountExcelVo;
import com.mall4j.cloud.distribution.vo.DistributionCommissionAccountVO;
import com.mall4j.cloud.distribution.vo.MemberCommissionExcelVo;
import com.mall4j.cloud.distribution.vo.StaffCommissionExcelVo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 佣金管理-佣金账户
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
@Service
@Slf4j
public class DistributionCommissionAccountServiceImpl implements DistributionCommissionAccountService {

    @Resource
    private DistributionCommissionAccountMapper distributionCommissionAccountMapper;

    @Autowired
    private DistributionCommissionLogService distributionCommissionLogService;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;

    @Autowired
    private DistributionWithdrawOrderService distributionWithdrawOrderService;

    @Autowired
    private OrderCommissionHistoryService orderCommissionHistoryService;


    @Override
    public PageVO<DistributionCommissionAccountDTO> page(PageDTO pageDTO, DistributionCommissionAccountDTO distributionCommissionAccountDTO) {
        PageVO<DistributionCommissionAccountDTO> page = PageUtil.doPage(pageDTO, () -> distributionCommissionAccountMapper.list(distributionCommissionAccountDTO));
        if (CollectionUtils.isEmpty(page.getList())) {
            return new PageVO<>();
        }
        page.getList().forEach(account -> {
            StoreVO storeVO = storeFeignClient.findByStoreId(account.getStoreId());
            if (null != storeVO) {
                account.setStoreName(storeVO.getName());
            }
            if (account.getIdentityType() == 1) {
                ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(account.getUserId());
                if (staffData.isSuccess() && null != staffData.getData()) {
                    account.setUserNumber(staffData.getData().getStaffNo());
                }
            }
            ServerResponseEntity<CalculateDistributionCommissionResultVO> calculateResult = orderFeignClient.calculateDistributionCommissionByUserId(account.getUserId());
            if (calculateResult.isFail() || calculateResult.getData() == null) {
                throw new LuckException("统计分销、发展佣金明细失败：" + calculateResult.getMsg());
            }
            account.setCanWithdrawDistribution(calculateResult.getData().getCanWithdrawDistribution());
            account.setCanWithdrawDeveloping(calculateResult.getData().getCanWithdrawDeveloping());
            account.setWaitDistributionCommission(calculateResult.getData().getWaitDistributionCommission());
            account.setWaitDevelopingCommission(calculateResult.getData().getWaitDevelopingCommission());
        });
        return page;
    }

    @Override
    public DistributionCommissionAccount getById(Long id) {
        return distributionCommissionAccountMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionCommissionAccount distributionCommissionAccount) {
        DistributionCommissionAccount account = distributionCommissionAccountMapper.getByUser(distributionCommissionAccount.getUserId(), distributionCommissionAccount.getIdentityType());
        if (null != account) {
            return;
        }
        distributionCommissionAccount.setStatus(1);
        distributionCommissionAccountMapper.save(distributionCommissionAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionCommissionAccount distributionCommissionAccount) {
        distributionCommissionAccountMapper.update(distributionCommissionAccount);
    }

    @Override
    public void deleteById(Long id) {
        distributionCommissionAccountMapper.deleteById(id);
    }

    @Override
    public void commissionExcel(HttpServletResponse response, DistributionCommissionAccountDTO dto) {
        if (dto.getIdentityType() == 1) {
            ExcelUtil.soleExcel(response, listStaffCommissionExcel(dto), StaffCommissionExcelVo.EXCEL_NAME, StaffCommissionExcelVo.MERGE_ROW_INDEX, StaffCommissionExcelVo.MERGE_COLUMN_INDEX, StaffCommissionExcelVo.class);
        } else {
            ExcelUtil.soleExcel(response, listMemberCommissionExcel(dto), MemberCommissionExcelVo.EXCEL_NAME, MemberCommissionExcelVo.MERGE_ROW_INDEX, MemberCommissionExcelVo.MERGE_COLUMN_INDEX, MemberCommissionExcelVo.class);
        }
    }

    @Override
    public DistributionCommissionAccount getByUser(Long userId, Integer identityType) {
        return distributionCommissionAccountMapper.getByUser(userId, identityType);
    }

    @Override
    public void updateCommission(DistributionCommissionAccount account, Long value, CommissionChangeTypeEnum changeTypeEnum) {
        DistributionCommissionLog log = new DistributionCommissionLog();
        switch (changeTypeEnum) {
            case ADD_WAIT_SETTLE:
                account.setWaitCommission(account.getWaitCommission() + value);
                break;
            case REDUCE_WAIT_SETTLE:
                account.setWaitCommission(account.getWaitCommission() - value);
                break;
            case ADD_CAN_WITHDRAW:
                account.setWaitCommission(account.getWaitCommission() - value);
                account.setCanWithdraw(account.getCanWithdraw() + value);
                break;
            case REDUCE_CAN_WITHDRAW:
                account.setCanWithdraw(account.getCanWithdraw() - value);
                break;
            case ADD_ALREADY_WITHDRAW:
                account.setCanWithdraw(account.getCanWithdraw() - value);
                account.setAlreadyWithdraw(account.getAlreadyWithdraw() + value);
                account.setTotalWithdraw(account.getTotalWithdraw() + value);
                break;
            case REDUCE_ALREADY_WITHDRAW:
                account.setAlreadyWithdraw(account.getAlreadyWithdraw() - value);
                account.setTotalWithdraw(account.getTotalWithdraw() - value);
                break;
            case ADD_WITHDRAW_NEED_REFUND:
                account.setWithdrawNeedRefund(account.getWithdrawNeedRefund() + value);
                break;
            case REDUCE_WITHDRAW_NEED_REFUND:
                account.setWithdrawNeedRefund(account.getWithdrawNeedRefund() - value);
                break;
            default:
                return;
        }
        distributionCommissionAccountMapper.update(account);
        log.setIdentityType(account.getIdentityType());
        log.setUserId(account.getUserId());
        log.setOperation(changeTypeEnum.getOperation());
        log.setType(changeTypeEnum.getType());
        log.setChangeValue(value);
        distributionCommissionLogService.save(log);
    }

    @Override
    public DistributionCommissionAccountVO info(Long userId, Integer identityType) {
        DistributionCommissionAccountVO distributionCommissionAccountVO = new DistributionCommissionAccountVO();
        DistributionCommissionAccount account = distributionCommissionAccountMapper.getByUser(userId, identityType);
        if (!Objects.isNull(account)) {
            distributionCommissionAccountVO.setAlreadyWithdraw(account.getAlreadyWithdraw() == null ? 0l : account.getAlreadyWithdraw());
            distributionCommissionAccountVO.setCanWithdraw(account.getCanWithdraw() == null ? 0l : account.getCanWithdraw());
            distributionCommissionAccountVO.setWaitCommission(account.getWaitCommission() == null ? 0l : account.getWaitCommission());
            distributionCommissionAccountVO.setAlreadyCommission(account.getCanWithdraw() == null ? 0l : account.getCanWithdraw());
            distributionCommissionAccountVO.setTotalCommission(distributionCommissionAccountVO.getCanWithdraw() + distributionCommissionAccountVO.getAlreadyWithdraw());
        }
        return distributionCommissionAccountVO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void staffWithdraw() {
        List<DistributionCommissionAccount> accountList = distributionCommissionAccountMapper.listStaffCommissionAccount();
        if (CollectionUtils.isEmpty(accountList)) {
            return;
        }
        Date date = DateUtils.getBeforeLastMonthdate();
        List<Long> updateDistributionIds = new ArrayList<>();
        List<Long> updateDevelopingIds = new ArrayList<>();
        accountList.forEach(account -> {
            ServerResponseEntity<List<EsOrderBO>> responseEntity = orderFeignClient.listByStaffAndTime(account.getUserId(), date);
            if (responseEntity.isSuccess() || CollectionUtils.isNotEmpty(responseEntity.getData())) {
                List<DistributionWithdrawOrder> orderList = new ArrayList<>();
                AtomicReference<Long> commission = new AtomicReference<>(0L);
                responseEntity.getData().forEach(esOrderBO -> {
                    if (esOrderBO.getDistributionUserType() == 1) {
                        commission.set(commission.get() + esOrderBO.getDistributionAmount());
                        updateDistributionIds.add(esOrderBO.getOrderId());
                    }
                    if (esOrderBO.getDevelopingAmount() > 0) {
                        commission.set(commission.get() + esOrderBO.getDistributionAmount());
                        updateDevelopingIds.add(esOrderBO.getOrderId());
                    }
                    DistributionWithdrawOrder order = new DistributionWithdrawOrder();
                    order.setUserId(account.getUserId());
                    order.setIdentityType(1);
                    order.setOrderId(esOrderBO.getOrderId());
                    orderList.add(order);
                });
                if (commission.get() > 0) {
                    Long recordId = saveWithdrawRecord(account, commission.get());
                    orderList.forEach(distributionWithdrawOrder -> {
                        distributionWithdrawOrder.setRecordId(recordId);
                        distributionWithdrawOrderService.save(distributionWithdrawOrder);
                    });
                    this.updateCommission(account, commission.get(), CommissionChangeTypeEnum.ADD_ALREADY_WITHDRAW);
                } else {
                    updateDistributionIds.clear();
                    updateDevelopingIds.clear();
                }
            }
            OrderCommissionHistoryDTO orderCommissionHistoryDTO = new OrderCommissionHistoryDTO();
            orderCommissionHistoryDTO.setIdentityType(1);
            orderCommissionHistoryDTO.setUserId(account.getUserId());
            orderCommissionHistoryDTO.setStatus(0);
            List<OrderCommissionHistory> orderCommissionHistories = orderCommissionHistoryService.listByUserAndStatus(orderCommissionHistoryDTO);
            if (CollectionUtils.isNotEmpty(orderCommissionHistories)){
                orderCommissionHistories.forEach(orderCommissionHistory -> {
                    orderCommissionHistory.setStatus(2);
                    orderCommissionHistoryService.update(orderCommissionHistory);
                });
            }
        });
        if (CollectionUtils.isNotEmpty(updateDistributionIds)) {
            orderFeignClient.updateDistributionStatusBatchById(updateDistributionIds, 2, null, new Date());
        }
        if (CollectionUtils.isNotEmpty(updateDevelopingIds)) {
            orderFeignClient.updateDevelopingStatusBatchById(updateDevelopingIds, 2, null, new Date());
        }
    }


    private Long saveWithdrawRecord(DistributionCommissionAccount account, Long commission) {
        DistributionWithdrawRecord record = new DistributionWithdrawRecord();
        record.setIdentityType(1);
        record.setUserId(account.getUserId());
        record.setUsername(account.getUsername());
        record.setUserNumber(account.getUserNumber());
        record.setMobile(account.getMobile());
        record.setStoreId(account.getStoreId());
        record.setWithdrawOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + account.getUserId()).substring(1));
        record.setWithdrawAmount(commission);
        record.setTransferType(2);
        Date now = new Date();
        record.setApplyTime(now);
        record.setProcessTime(now);
        record.setWithdrawStatus(1);
        record.setReviewerId(0L);
        record.setReviewerName("系统处理");
        distributionWithdrawRecordService.save(record);
        return record.getId();
    }


    private List<StaffCommissionExcelVo> listStaffCommissionExcel(DistributionCommissionAccountDTO dto) {
        List<DistributionCommissionAccountDTO> list = distributionCommissionAccountMapper.list(dto);
        List<StaffCommissionExcelVo> staffCommissionExcelVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return staffCommissionExcelVoList;
        }
        list.forEach(distributionCommissionAccount -> {
            StaffCommissionExcelVo vo = new StaffCommissionExcelVo();
            buildCommissionExcel(vo, distributionCommissionAccount);
            vo.setName(distributionCommissionAccount.getUsername());
            vo.setNumber(distributionCommissionAccount.getUserNumber());
            staffCommissionExcelVoList.add(vo);
        });
        return staffCommissionExcelVoList;
    }

    private List<MemberCommissionExcelVo> listMemberCommissionExcel(DistributionCommissionAccountDTO dto) {
        List<DistributionCommissionAccountDTO> list = distributionCommissionAccountMapper.list(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<MemberCommissionExcelVo> memberCommissionExcelVos = new ArrayList<>();
        list.forEach(distributionCommissionAccount -> {
            MemberCommissionExcelVo vo = new MemberCommissionExcelVo();
            buildCommissionExcel(vo, distributionCommissionAccount);
            vo.setName(distributionCommissionAccount.getUsername());
            vo.setNumber(distributionCommissionAccount.getUserNumber());
            memberCommissionExcelVos.add(vo);
        });
        return memberCommissionExcelVos;
    }

    private void buildCommissionExcel(CommissionAccountExcelVo vo, DistributionCommissionAccountDTO distributionCommissionAccount) {
        vo.setMobile(distributionCommissionAccount.getMobile());
        vo.setWaitCommission(conversionPrices(distributionCommissionAccount.getWaitCommission()));
        vo.setCanWithdraw(conversionPrices(distributionCommissionAccount.getCanWithdraw()));
        vo.setTotalWithdraw(conversionPrices(distributionCommissionAccount.getTotalWithdraw()));
        vo.setStatus(distributionCommissionAccount.getStatus() == 1 ? "启用" : "禁用");
        vo.setWithdrawNeedRefund(conversionPrices(distributionCommissionAccount.getWithdrawNeedRefund()));
        StoreVO storeVO = storeFeignClient.findByStoreId(distributionCommissionAccount.getStoreId());
        if (null != storeVO) {
            vo.setStoreName(storeVO.getName());
            vo.setStoreCode(storeVO.getStoreCode());
        }
    }

    private String conversionPrices(Long num) {
        return String.valueOf(new BigDecimal(Optional.ofNullable(num).orElse(0L)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
    }
}
