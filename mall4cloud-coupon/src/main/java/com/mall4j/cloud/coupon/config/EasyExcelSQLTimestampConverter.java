//package com.mall4j.cloud.coupon.config;
//
//import cn.hutool.core.date.DateUtil;
//import com.alibaba.excel.converters.Converter;
//import com.alibaba.excel.enums.CellDataTypeEnum;
//import com.alibaba.excel.metadata.CellData;
//import com.alibaba.excel.metadata.GlobalConfiguration;
//import com.alibaba.excel.metadata.property.ExcelContentProperty;
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//
//
///**
// * @Description EasyExcelSQLTimestamp转换器
// * @Author axin
// * @Date 2022-11-04 18:08
// **/
//public class EasyExcelSQLTimestampConverter implements Converter<Timestamp> {
//    @Override
//    public Class<LocalDate> supportJavaTypeKey() {
//        return LocalDate.class;
//    }
//
//    @Override
//    public CellDataTypeEnum supportExcelTypeKey() {
//        return CellDataTypeEnum.STRING;
//    }
//
//    @Override
//    public Timestamp convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return Timestamp.valueOf(cellData.getStringValue());
//    }
//
//    @Override
//    public CellData<String> convertToExcelData(Timestamp value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        String format = DateUtil.format(value, "yyyy-MM-dd HH:mm:ss");
//        return new CellData<>(format);
//    }
//}
