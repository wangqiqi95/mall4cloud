package com.mall4j.cloud.platform.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.platform.dto.ImportStaffsDto;
import com.mall4j.cloud.platform.manager.StaffExcelManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 员工excel导入监听
 *
 * 有个很重要的点 SpuExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author gmq
 * @date 2022/3/8
 */

public class StaffExcelListener extends AnalysisEventListener<ImportStaffsDto> {

    private StaffExcelManager staffExcelManager;
    private Map<String, List<String>> errorMap;
    private List<ImportStaffsDto> list;
    public static String OTHER = "other";
    public static String ERROR_ROWS = "error_rows";
    public static String IMPORT_ROWS = "import_rows";

    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static String seq;

    public StaffExcelListener() {
    }

    public StaffExcelListener(StaffExcelManager staffExcelManager, Map<String, List<String>> errorMap) {
        this.staffExcelManager = staffExcelManager;
        errorMap.put(OTHER, new ArrayList());
        errorMap.put(ERROR_ROWS, new ArrayList());
        errorMap.put(IMPORT_ROWS, new ArrayList());
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(ImportStaffsDto staffsDto, AnalysisContext analysisContext) {
        boolean isSave = list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }
        //状态 0 正常 1离职
        if(StrUtil.isNotBlank(staffsDto.getStatus())){
            switch (staffsDto.getStatus()){
                case "正常":
                    staffsDto.setStatus("0");
                    break;
                case "离职":
                    staffsDto.setStatus("1");
                    break;
            }
        }else{
            staffsDto.setStatus("0");
        }
        //角色类型 1-导购 2-店长 3-店务
        if(StrUtil.isNotBlank(staffsDto.getRoleType())){
            switch (staffsDto.getRoleType()){
                case "导购":
                    staffsDto.setRoleType("1");
                    break;
                case "店长":
                    staffsDto.setRoleType("2");
                    break;
                case "店务":
                    staffsDto.setRoleType("3");
                    break;
            }
        }
        list.add(staffsDto);
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
        staffExcelManager.importExcel(list, errorMap);
        list.clear();
    }

}
