package com.mall4j.cloud.common.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date 2022年4月26日, 0026 15:29
 * @Created by eury
 */
public class SystemUtils {

    public static String getExcelFilePath(){
        String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String pathExport="/opt/system/temp/excel/"+time;
        File filePathExport=new File(pathExport);
        filePathExport.mkdirs();
        return pathExport;
    }

    public static String getExcelName() throws Exception{
        String excelFileName = System.currentTimeMillis()+ RandomUtil.getRandomStr(4);
        return URLEncoder.encode(excelFileName, "UTF-8");
    }

    public static String getCpRemarkMobile(String cpRemarkMobiles){
        cpRemarkMobiles=StrUtil.isNotBlank(cpRemarkMobiles)&&(!cpRemarkMobiles.equals("{}") || !cpRemarkMobiles.equals("[]"))?cpRemarkMobiles:null;
        if(StrUtil.isEmptyIfStr(cpRemarkMobiles)){
            return null;
        }
        return StringUtils.join(JSONArray.parseArray(cpRemarkMobiles,String.class),",");
    }

}
