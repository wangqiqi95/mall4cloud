package com.mall4j.cloud.common.util.csvExport;

import com.alibaba.fastjson.JSONArray;

public interface CsvExportDataHandler {
    
    JSONArray  getData(CsvExportModel model);
    
    default boolean isOver(CsvExportModel model) {
        return model.getTmpData() == null;
    }
    
}
