package com.mall4j.cloud.biz.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.mall4j.cloud.biz.dto.TaskClientImportExcelDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

public class ExcelUtil {

    /**
     * 批量导出
     * @param response
     * @param objects 实际数据。
     * @param clazzs 导出数据对应的实体
     * @param fileName 文件名
     * @param sheetNames sheet名称
     * @param horizontalCellStyleStrategy excel策略可用来设置背景、文字颜色等
     */
    public static void batchExport(HttpServletResponse response, List<List> objects, List<Object> clazzs, String fileName, List<String> sheetNames, List<HorizontalCellStyleStrategy> horizontalCellStyleStrategy) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));// 设置文件名
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).autoCloseStream(Boolean.FALSE).build();


        for (int i = 0; i < objects.size(); i++) {
            WriteSheet writeSheet = null;
            if (clazzs.get(i) instanceof List) {
                writeSheet = EasyExcel.writerSheet(i, sheetNames.get(i)).head((List<List<String>>) clazzs.get(i)).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write((List<List<String>>) objects.get(i), writeSheet);
            } else {
                writeSheet = EasyExcel.writerSheet(i, sheetNames.get(i)).head((Class<?>) clazzs.get(i)).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(ObjectUtil.isEmpty(objects.get(i)) ? Collections.emptyList() : (List<Class<?>>) objects.get(i), writeSheet);
            }

        }

    }

    /**
     * 单个sheet导出
     * @param response
     * @param objects 实际数据。
     * @param clazz 导出数据对应的实体
     * @param fileName 文件名
     * @param sheetName sheet名称
     * @param horizontalCellStyleStrategy excel策略可用来设置背景、文字颜色等
     */
    public static void singleExport(HttpServletResponse response, List objects, Class clazz, String fileName, String sheetName, HorizontalCellStyleStrategy horizontalCellStyleStrategy) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz).autoCloseStream(Boolean.FALSE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0, sheetName).head(clazz).build();
        excelWriter.write(objects, writeSheet);
    }

    /**
     * 校验文件名称和文件格式
     * @param file
     */
    public static void checkFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        Assert.isTrue(StrUtil.isNotBlank(originalFilename), "文件名称不能为空");
        Assert.isTrue(originalFilename.endsWith(".xls") || originalFilename.endsWith(".xlsx"), "导入的文件格式必须为xls或者xlsx");
    }
}