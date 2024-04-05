package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.constant.WithdrawProcessExportError;
import com.mall4j.cloud.distribution.dto.WithdrawProcessExcelDTO;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawProcessLogMapper;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.service.DistributionWithdrawRecordService;
import com.mall4j.cloud.distribution.service.WithdrawProcessExcelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author ZengFanChang
 * @Date 2021/12/10
 */
@Service
public class WithdrawProcessExcelServiceImpl implements WithdrawProcessExcelService {

    public static final Logger log = LoggerFactory.getLogger(WithdrawProcessExcelServiceImpl.class);

    @Resource
    private DistributionWithdrawProcessLogMapper distributionWithdrawProcessLogMapper;

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;

    @Override
    public void exportWithdrawProcessExcel(List<WithdrawProcessExcelDTO> list, Map<Integer, Integer> errorMap, Long processId) {
        if (CollectionUtils.isEmpty(list)) {
            throw new LuckException("导入数据为空");
        }
        AtomicReference<Integer> success = new AtomicReference<>(0);
        AtomicReference<Integer> fail = new AtomicReference<>(0);
        list.forEach(w -> {
            try {
                if (saveWithdrawProcessLog(w, processId)) {
                    success.getAndSet(success.get() + 1);

                } else {
                    fail.getAndSet(fail.get() + 1);
                }
            } catch (Exception e) {
                log.error("提现处理批量导入异常 processExcel:{}", w, e);
                fail.getAndSet(fail.get() + 1);
            }

        });
        errorMap.put(WithdrawProcessExportError.SUCCESS_QUANTITY.value(), success.get());
        errorMap.put(WithdrawProcessExportError.FAIL_QUANTITY.value(), fail.get());
    }


    private Boolean saveWithdrawProcessLog(WithdrawProcessExcelDTO excelDTO, Long processId) {
        DistributionWithdrawProcessLog log = new DistributionWithdrawProcessLog();
        log.setProcessId(processId);
        log.setWithdrawOrderNo(excelDTO.getWithdrawOrderNo());
        log.setProcessType(Optional.ofNullable(excelDTO.getWithdrawStatus()).orElse(1) == 0 ? 1 : 2);
        log.setFailReason(excelDTO.getRejectReason());
        if (StringUtils.isEmpty(excelDTO.getWithdrawOrderNo())) {
            log.setStatus(1);
            log.setFailReason("提现单号为空");
        }
        DistributionWithdrawRecord record = distributionWithdrawRecordService.getRecordByNo(excelDTO.getWithdrawOrderNo());
        if (null == record) {
            log.setStatus(1);
            log.setFailReason("提现记录为空");
        }
        log.setStatus(0);
        distributionWithdrawProcessLogMapper.save(log);
        return log.getStatus() == 0;
    }
}
