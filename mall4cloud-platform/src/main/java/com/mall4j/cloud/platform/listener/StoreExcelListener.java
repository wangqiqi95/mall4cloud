package com.mall4j.cloud.platform.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.api.platform.dto.ImportTzStoreLogVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.platform.dto.ImportStaffsDto;
import com.mall4j.cloud.platform.dto.ImportTzStoreVO;
import com.mall4j.cloud.platform.manager.StaffExcelManager;
import com.mall4j.cloud.platform.manager.StoreExcelManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  门店excel导入监听
 *
 * 有个很重要的点 SpuExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author gmq
 * @date 2022/3/8
 */
@Slf4j
public class StoreExcelListener extends AnalysisEventListener<ImportTzStoreVO> {

    private StoreExcelManager storeExcelManager;
    private Map<String,ImportTzStoreLogVO> errorMap;
    private List<ImportTzStoreVO> list;
    public static String OTHER = "other";
    public static String ERROR_ROWS = "error_rows";
    public static String IMPORT_ROWS = "import_rows";

    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static String seq;

    private Long orgId;

    public StoreExcelListener() {
    }

    public StoreExcelListener(StoreExcelManager storeExcelManager, Map<String,ImportTzStoreLogVO> errorMap,Long orgId) {
        this.storeExcelManager = storeExcelManager;
//        errorMap.put(OTHER, new ArrayList());
//        errorMap.put(ERROR_ROWS, new ArrayList());
//        errorMap.put(IMPORT_ROWS, new ArrayList());
        this.errorMap = errorMap;
        this.list = new ArrayList<>();

        this.orgId=orgId;
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(ImportTzStoreVO storeVO, AnalysisContext analysisContext) {
        boolean isSave = list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }

        list.add(storeVO);
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
        try {
            storeExcelManager.importExcel(list, errorMap,orgId);
            list.clear();
        }catch (Exception e){
            log.info("StoreExcelListener--门店导入失败 {} {}",e,e.getMessage());
            throw new LuckException("导入失败，请检查模板是否正确");
        }

    }

}
