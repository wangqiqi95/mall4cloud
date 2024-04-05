package com.mall4j.cloud.distribution.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.distribution.dto.WithdrawProcessExcelDTO;
import com.mall4j.cloud.distribution.service.WithdrawProcessExcelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * excel导入监听
 * <p>
 * 有个很重要的点 SpuExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 */

public class WithdrawProcessExcelListener extends AnalysisEventListener<WithdrawProcessExcelDTO> {

    private Long processId;
    private WithdrawProcessExcelService withdrawProcessExcelService;
    private Map<Integer, Integer> errorMap;
    private List<WithdrawProcessExcelDTO> list;
    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;

    public WithdrawProcessExcelListener() {
    }

    public WithdrawProcessExcelListener(WithdrawProcessExcelService withdrawProcessExcelService, Map<Integer, Integer> errorMap, Long processId) {
        this.withdrawProcessExcelService = withdrawProcessExcelService;
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
        this.processId = processId;
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(WithdrawProcessExcelDTO withdrawProcessExcelDTO, AnalysisContext analysisContext) {
        boolean isSave = list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }
        list.add(withdrawProcessExcelDTO);
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //确保所有数据都能入库
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        withdrawProcessExcelService.exportWithdrawProcessExcel(list, errorMap, processId);
        list.clear();
    }

}
