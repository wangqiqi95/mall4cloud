//package com.mall4j.cloud.common.converter;
//
//import com.alibaba.excel.converters.Converter;
//import com.alibaba.excel.enums.CellDataTypeEnum;
//import com.alibaba.excel.metadata.CellData;
//import com.alibaba.excel.metadata.GlobalConfiguration;
//import com.alibaba.excel.metadata.property.ExcelContentProperty;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class LocalDateTimeConverter implements Converter<LocalDateTime> {
//    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
//
//    @Override
//    public Class<LocalDateTime> supportJavaTypeKey() {
//        return LocalDateTime.class;
//    }
//
//    @Override
//    public CellDataTypeEnum supportExcelTypeKey() {
//        return CellDataTypeEnum.STRING;
//    }
//
//    @Override
//    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
//                                           GlobalConfiguration globalConfiguration) {
//        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
//    }
//
//    @Override
//    public CellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
//                                               GlobalConfiguration globalConfiguration) {
//        return new CellData<>(value.format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN)));
//    }
//}
