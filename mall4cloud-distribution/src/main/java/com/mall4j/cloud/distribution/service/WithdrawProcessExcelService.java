package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.distribution.dto.WithdrawProcessExcelDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author ZengFanChang
 * @Date 2021/12/10
 */
public interface WithdrawProcessExcelService {

    void exportWithdrawProcessExcel(List<WithdrawProcessExcelDTO> list, Map<Integer, Integer> errorMap, Long processId);

}
