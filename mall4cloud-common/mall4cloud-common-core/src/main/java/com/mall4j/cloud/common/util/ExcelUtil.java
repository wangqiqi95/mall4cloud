package com.mall4j.cloud.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.csvExport.hanlder.ExcelMergeHandler;
import com.mall4j.cloud.common.util.csvExport.hanlder.SheetWrite;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 *
 * 使用说明：
 *      导出：
 *          1.第一列是作为合并单元格的判断字段， 所以一定要为具有唯一标识的字符串
 *          2.列的宽度是自适应的，不需要额外定义
 *          3.WebConfig 类中的 supportedMediaTypes.add(MediaType.ALL) 不要去掉，虽然不影响使用，但是会报
 *              No converter for [class com.mall4j.cloud.wx.response.ServerResponseEntity] with preset Content-Type 'application/vnd.ms-excel;charset=utf-8
 *
 *      导入：
 *
 *
 * @author YXF
 * @date 2021/3/9
 */
public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    private static final String SHEET_NAME = "sheet";

    /*
     * 导出excel的通用方法
     *   clazz：传入excel导出的VO类
     *   data：传入需要导出的数据列表
     *   fileName：当前导出的excel文件名称
     *   response：网络响应对象
     * */
    public static void exportExcel(Class clazz, List excelData, String fileName, HttpServletResponse response) throws IOException {
        HorizontalCellStyleStrategy styleStrategy = setCellStyle();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        ExcelWriterBuilder writeWork = EasyExcelFactory.write(response.getOutputStream(), clazz);
        writeWork.registerWriteHandler(styleStrategy).sheet("sheet1").doWrite(excelData);
    }

    /**
     * 导出方法
     * @param response
     * @param excelName excel文件名
     * @param mergeRowIndex 从哪一行开始填充数据
     * @param mergeColumnIndex 需要合并的列
     * @return
     * @throws Exception
     */
    public static ExcelWriterBuilder getExcelWriterMerge(HttpServletResponse response, String excelName, int mergeRowIndex, int[] mergeColumnIndex) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());

        ExcelWriterBuilder build = EasyExcel.write(response.getOutputStream())
                // 自定义合并规则
                .registerWriteHandler(new ExcelMergeHandler(mergeRowIndex, mergeColumnIndex))
                // 宽度自适应
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
        return build;
    }

    /**
     * 导出模板
     * @param excelWriterBuilder
     * @param map 自定义下拉框 key:列的索引  value：下拉框中多选值的数组
     * @return
     */
    public static ExcelWriterBuilder getExcelModel(ExcelWriterBuilder excelWriterBuilder, Map<Integer, String[]> map, Integer firstRow) {
        if (MapUtil.isEmpty(map)) {
            return excelWriterBuilder;
        }
        SheetWrite sheetWrite = new SheetWrite(map, firstRow, firstRow);
        excelWriterBuilder.registerWriteHandler(sheetWrite);
        return excelWriterBuilder;
    }



    /**
     * 通用导出方法
     * @param response
     * @param list 导出的数据列表
     * @param excelName excel文件名
     * @param mergeRowIndex 从哪一行开始填充数据
     * @param mergeColumnIndex 需要合并的列
     * @param clazz excel对象
     */
    public static void soleExcel(HttpServletResponse response, List list, String excelName, int mergeRowIndex, int[] mergeColumnIndex, Class clazz) {
        ExcelWriter excelWriter = null;
        try {
            // 先执行合并策略
            excelWriter = getExcelWriterMerge(response, excelName, mergeRowIndex, mergeColumnIndex).build();
            // 业务代码
            if (CollUtil.isNotEmpty(list)){
                // 进行写入操作
                WriteSheet sheetWriter = EasyExcel.writerSheet(SHEET_NAME).head(clazz).build();
                excelWriter.write(list,sheetWriter);
            }
        } catch (Exception e) {
            log.error("导出excel失败", e);
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    public static ExcelWriter getExcelWriter(String fileName){
        ExcelWriter excelWriter;
        try {
            //EasyExcel.write()
            excelWriter = EasyExcel.write(fileName).build();
        } catch (Exception e) {
            throw new LuckException("导出Excel 失败");
        }

        return excelWriter;
    }

    public static WriteSheet getWriteSheet(String sheetName,Class clazz){
        return EasyExcel.writerSheet(sheetName).head(clazz).build();
    }

    public static WriteSheet getDynamicHeadWriteSheet(String sheetName, List<List<String>> headStringList){
        return EasyExcel.writerSheet(sheetName).head(headStringList).build();
    }

    public static ExcelWriter write(List data, ExcelWriter excelWriter,
                                    WriteSheet writeSheet, boolean isClose){

        excelWriter.write(data, writeSheet);
        if (isClose){
            excelWriter.finish();
        }
        return excelWriter;
    }

    public static HorizontalCellStyleStrategy setCellStyle(){
        // 设置表头的样式（背景颜色、字体、居中显示）
        WriteCellStyle headStyle = new WriteCellStyle();
        //设置表头背景颜色
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headFont = new WriteFont();
        headFont.setFontHeightInPoints((short)12);
        headFont.setBold(true);
        headStyle.setWriteFont(headFont);
        headStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 设置Excel内容策略(水平居中)
        WriteCellStyle cellStyle = new WriteCellStyle();
        cellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(headStyle, cellStyle);
    }
}
