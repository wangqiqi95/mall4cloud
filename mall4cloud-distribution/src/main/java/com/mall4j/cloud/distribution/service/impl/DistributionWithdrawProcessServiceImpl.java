package com.mall4j.cloud.distribution.service.impl;

import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.constant.WithdrawProcessExportError;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawProcessDTO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;
import com.mall4j.cloud.distribution.dto.WithdrawProcessExcelDTO;
import com.mall4j.cloud.distribution.listener.WithdrawProcessExcelListener;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawProcessLogMapper;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawProcessMapper;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcess;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;
import com.mall4j.cloud.distribution.service.DistributionWithdrawProcessService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawRecordService;
import com.mall4j.cloud.distribution.service.WithdrawProcessExcelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 佣金处理批次
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
@Service
public class DistributionWithdrawProcessServiceImpl implements DistributionWithdrawProcessService {

    public static final Logger LOG = LoggerFactory.getLogger(DistributionWithdrawProcessServiceImpl.class);

    @Autowired
    private DistributionWithdrawProcessMapper distributionWithdrawProcessMapper;

    @Autowired
    private WithdrawProcessExcelService withdrawProcessExcelService;

    @Resource
    private DistributionWithdrawProcessLogMapper distributionWithdrawProcessLogMapper;

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;


    @Override
    public PageVO<DistributionWithdrawProcess> page(PageDTO pageDTO, DistributionWithdrawProcessDTO distributionWithdrawProcessDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionWithdrawProcessMapper.list(distributionWithdrawProcessDTO));
    }

    @Override
    public DistributionWithdrawProcess getById(Long id) {
        return distributionWithdrawProcessMapper.getById(id);
    }

    @Override
    public void save(DistributionWithdrawProcess distributionWithdrawProcess) {
        distributionWithdrawProcessMapper.save(distributionWithdrawProcess);
    }

    @Override
    public void update(DistributionWithdrawProcess distributionWithdrawProcess) {
        distributionWithdrawProcessMapper.update(distributionWithdrawProcess);
    }

    @Override
    public void deleteById(Long id) {
        distributionWithdrawProcessMapper.deleteById(id);
    }

    @Override
    public String importUserExcel(MultipartFile file, String name) {
        try {
            DistributionWithdrawProcess process = new DistributionWithdrawProcess();
            process.setBatchName(name);
            process.setExecuteStatus(0);
            AtomicReference<Integer> success = new AtomicReference<>(0);
            AtomicReference<Integer> fail = new AtomicReference<>(0);
            process.setImportDate(new Date());
            process.setSuccessQuantity(success.get());
            process.setFailQuantity(fail.get());
            distributionWithdrawProcessMapper.save(process);
            Map<Integer, Integer> errorMap = new HashMap<>(4);
            WithdrawProcessExcelListener withdrawProcessExcelListener = new WithdrawProcessExcelListener(withdrawProcessExcelService, errorMap, process.getId());
            EasyExcel.read(file.getInputStream(), WithdrawProcessExcelDTO.class, withdrawProcessExcelListener).sheet().doRead();
            return getProcessExportInfo(errorMap, process);
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(Long id) {
        DistributionWithdrawProcess process = distributionWithdrawProcessMapper.getById(id);
        if (null == process) {
            throw new LuckException("处理信息不存在");
        }
        if (process.getExecuteStatus() == 1) {
            throw new LuckException("该次导入已处理");
        }
        List<DistributionWithdrawProcessLog> logList = distributionWithdrawProcessLogMapper.listByProcess(id);
        if (CollectionUtils.isEmpty(logList) || CollectionUtils.isEmpty(logList.stream().filter(log -> log.getStatus() == 0).collect(Collectors.toList()))) {
            throw new LuckException("导入成功信息为空");
        }
        logList.stream().filter(log -> log.getStatus() == 0).forEach(log -> {
            DistributionWithdrawRecordDTO dto = new DistributionWithdrawRecordDTO();
            dto.setWithdrawOrderNo(log.getWithdrawOrderNo());
            dto.setProcessType(log.getProcessType());
            dto.setRejectReason(log.getRejectReason());
            try {
                distributionWithdrawRecordService.processWithdraw(dto);
            } catch (Exception e) {
                LOG.error("提现批量处理失败 log:{}", log, e);
            }
        });
        process.setExecuteStatus(1);
        distributionWithdrawProcessMapper.update(process);
    }

    private String getProcessExportInfo(Map<Integer, Integer> errorMap, DistributionWithdrawProcess process) {
        Integer success = errorMap.get(WithdrawProcessExportError.SUCCESS_QUANTITY.value());
        Integer fail = errorMap.get(WithdrawProcessExportError.FAIL_QUANTITY.value());
        process.setSuccessQuantity(success);
        process.setFailQuantity(fail);
        distributionWithdrawProcessMapper.update(process);
        return WithdrawProcessExportError.SUCCESS_QUANTITY.errorInfo() + ":" + success + "\n" +
                WithdrawProcessExportError.FAIL_QUANTITY.errorInfo() + ":" + fail + "\n";
    }
}
