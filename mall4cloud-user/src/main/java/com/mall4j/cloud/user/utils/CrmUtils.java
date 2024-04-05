package com.mall4j.cloud.user.utils;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.io.Charsets;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CrmUtils {

    public static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    public static void main(String[] args) {
//        Map<String, String> stringStringMap = generateHeader("http://123.dddd.com/service/v1/method","2","3");
//        System.out.println(1);
//    }

    public static Map<String, String> generateHeader(String url, String callerService, String secret) {
        int index = url.indexOf("//");
        String uriPath = url;
        StringBuffer requestPath = new StringBuffer();
        if (index > 0) { //若包含http或https,则排除http:// or https://
            uriPath = url.substring(index + 2);
        }
        //去除url中之后的参数
        index = uriPath.indexOf("?");
        if (index > 0) {
            uriPath = uriPath.substring(0, index);
        }
        String[] paths = uriPath.split("/");
        String serviceName = paths[1];
        String version = paths[2];
        for (int i = 3; i < paths.length; i++) {
            requestPath.append("/").append(paths[i]);
        }
        String timestamp = DTF.format(LocalDateTime.now());
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("X-Caller-Service", callerService);
        headerMap.put("X-Caller-Timestamp", timestamp);
        headerMap.put("X-Caller-Sign", generateSign(callerService, serviceName, version, timestamp, secret, requestPath.toString()));
        return headerMap;
    }

    @SneakyThrows
    public static String generateSign(String callerService, String contextPath, String version, String timestamp, String serviceSecret, String requestPath) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("callerService", callerService);
        map.put("contextPath", contextPath);
        if (requestPath != null) {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(requestPath.split("/")).forEach(it -> sb.append("/").append(it));
            map.put("requestPath", sb.toString().substring(1));
            map.put("timestamp", timestamp);
            map.put("v", version);
        }
        return generateMD5Sign(serviceSecret, map);
    }

    private static String generateMD5Sign(String secret, Map<String, String> parameters) {
        return DigestUtils.md5DigestAsHex(generateConcatSign(secret, parameters).getBytes(Charsets.UTF_8)).toUpperCase();
    }

    private static String generateConcatSign(String secret, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder().append(secret);
        Set<String> keys = parameters.keySet();
        keys.stream().forEach(it -> sb.append(it).append(parameters.get(it)));
        return sb.append(secret).toString();
    }
}
