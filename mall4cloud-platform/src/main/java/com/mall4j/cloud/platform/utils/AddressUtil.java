package com.mall4j.cloud.platform.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AddressUtil {

    public static Map<String,String> getLatAndLngByAddr(String addr) {
        try {
            Map<String,String> result = new HashMap<>();
            addr = addr.replace(" ", "").replace("#", "").replace("中国", "");
            String queryStr = "http://api.tianditu.gov.cn/geocoder?ds=%7B'keyWord':'" + addr + "'%7D&tk=" + "259ced3dd4a96c84e7a0c34bef400e14";
            String info = HttpUtil.get(queryStr);
            if (StringUtils.isNotBlank(info)) {
                JSONObject resultJson = JSONObject.parseObject(info);
                log.info("请求数据:{}",resultJson.toJSONString());
                if (!"无结果".equals(resultJson.get("msg"))) {
                    JSONObject locationObj = (JSONObject) resultJson.get("location");
                    //纬度
                    String lat = locationObj.get("lat") + "";
                    //经度
                    String lng = locationObj.get("lon") + "";
                    result.put("lat",lat);
                    result.put("lng",lng);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(getLatAndLngByAddr("SkechersHK（CN）"));
    }

}
