package com.mall4j.cloud.common.util;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date 2022年4月26日, 0026 15:29
 * @Created by eury
 */
public class SkqUtils {

    public static String getExcelFilePath(){
        String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String pathExport="/opt/skechers/temp/excel/"+time;
        File filePathExport=new File(pathExport);
        filePathExport.mkdirs();
        return pathExport;
    }

    public static String getExcelName() throws Exception{
        String excelFileName = System.currentTimeMillis()+ RandomUtil.getRandomStr(4);
        return URLEncoder.encode(excelFileName, "UTF-8");
    }

}
