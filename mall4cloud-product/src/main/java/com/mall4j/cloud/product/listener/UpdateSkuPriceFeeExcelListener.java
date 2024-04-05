package com.mall4j.cloud.product.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.product.manager.UpdateSkuPirceStatusManager;
import com.mall4j.cloud.product.vo.SkuPriceFeeExcelVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 员工excel导入监听
 *
 * 有个很重要的点 UpdateSkuPriceFeeExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author gmq
 * @date 2022/3/8
 */

public class UpdateSkuPriceFeeExcelListener extends AnalysisEventListener<SkuPriceFeeExcelVO> {

    private UpdateSkuPirceStatusManager skuPirceStatusManager;
    private Map<String, List<String>> errorMap;
    private String logExcel;
    private Long downLoadHisId;
    private List<SkuPriceFeeExcelVO> list;
    private List<SkuPriceFeeExcelVO> backList=new ArrayList<>();
    public static String OTHER = "other";
    public static String ERROR_ROWS = "error_rows";
    public static String IMPORT_ROWS = "import_rows";

    public boolean importStatus=false;

    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static String seq;

    public UpdateSkuPriceFeeExcelListener() {
    }

    public String getLogExcel() {
        return logExcel;
    }

    public Long getDownLoadHisId() {
        return downLoadHisId;
    }

    public void setDownLoadHisId(Long downLoadHisId) {
        this.downLoadHisId = downLoadHisId;
    }

    public List<SkuPriceFeeExcelVO> getBackList() {
        return backList;
    }

    public void setBackList(List<SkuPriceFeeExcelVO> backList) {
        this.backList = backList;
    }

    public UpdateSkuPriceFeeExcelListener(UpdateSkuPirceStatusManager skuPirceStatusManager, Map<String, List<String>> errorMap, boolean importStatus) {
        this.skuPirceStatusManager = skuPirceStatusManager;
        errorMap.put(OTHER, new ArrayList());
        errorMap.put(ERROR_ROWS, new ArrayList());
        errorMap.put(IMPORT_ROWS, new ArrayList());
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
        this.importStatus=importStatus;
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(SkuPriceFeeExcelVO staffsDto, AnalysisContext analysisContext) {
//        boolean isSave = list.size() > BATCH_COUNT;
//        if (isSave) {
//            saveData();
//        }
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
        if(Objects.nonNull(skuPirceStatusManager) && importStatus){
            skuPirceStatusManager.importExcel(list, errorMap,downLoadHisId);
        }
        backList.addAll(list);
        list.clear();
    }

}
