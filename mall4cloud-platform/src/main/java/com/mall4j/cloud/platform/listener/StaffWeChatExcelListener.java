package com.mall4j.cloud.platform.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.platform.dto.ImportStaffWeChatDto;
import com.mall4j.cloud.platform.manager.StaffExcelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 导入员工微信
 * @Author axin
 * @Date 2023-02-24 10:18
 **/
public class StaffWeChatExcelListener extends AnalysisEventListener<ImportStaffWeChatDto> {

    private StaffExcelManager staffExcelManager;

    private Map<String, List<String>> errorMap;
    private List<ImportStaffWeChatDto> list;

    public static String OTHER = "other";
    public static String ERROR_ROWS = "error_rows";
    public static String IMPORT_ROWS = "import_rows";
    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;


    public StaffWeChatExcelListener(StaffExcelManager staffExcelManager, Map<String, List<String>> errorMap) {
        this.staffExcelManager = staffExcelManager;
        errorMap.put(OTHER, new ArrayList());
        errorMap.put(ERROR_ROWS, new ArrayList());
        errorMap.put(IMPORT_ROWS, new ArrayList());
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
    }

    @Override
    public void invoke(ImportStaffWeChatDto data, AnalysisContext context) {
        boolean isSave = list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        staffExcelManager.importStaffWeChatExcel(list, errorMap);
        list.clear();
    }
}
