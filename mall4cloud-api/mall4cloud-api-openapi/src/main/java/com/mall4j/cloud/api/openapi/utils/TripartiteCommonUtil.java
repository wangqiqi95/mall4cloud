package com.mall4j.cloud.api.openapi.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.openapi.dto.req.TripartiteCommonReq;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Description 三方系统验签
 * @Author axin
 * @Date 2023-05-09
 **/
@Slf4j
public class TripartiteCommonUtil {

    public static boolean verifySign(TripartiteCommonReq commonReq,String appKey,String appSecret, String data) {
        long timeDifference = DateUtil.between(new Date(), DateUtil.date(commonReq.getTimestamp()*1000), DateUnit.MINUTE,false);
        if(timeDifference > 0 || timeDifference < -5){
            log.info("验签时间差过大");
            return false;
        }

        StringBuilder signStr = new StringBuilder();
        signStr.append("appKey=").append(appKey).append("&");
        signStr.append("appSecret=").append(appSecret).append("&");
        signStr.append("method=").append(commonReq.getMethod()).append("&");
        signStr.append("timestamp=").append(commonReq.getTimestamp()).append("&");
        signStr.append("version=").append(commonReq.getVersion()).append("&");
        signStr.append("data=").append(data);

        String encode = MD5Util.encode(signStr.toString()).toUpperCase();
        log.info("加签字符串：{}，传入sign:{},加签生成字符串：{}",signStr,commonReq.getSign(),encode);
        return encode.equals(commonReq.getSign());
    }

    public static void main(String[] args) {
        long time = new Date().getTime()/1000;
        System.out.println(time);

        TripartiteCommonReq req = new TripartiteCommonReq();
        req.setAppKey("pingan");
        req.setMethod("pingAnGenerateScheme");
        req.setSign("152755FCA1FB24F8B6966B35777150EF");
        req.setVersion("v1");
        req.setTimestamp(1683700691L);


        boolean b = verifySign(req, "pingan", "skx12345678", "{\"type\":1,\"query\":\"\"}");
        System.out.println(b);
    }

}
