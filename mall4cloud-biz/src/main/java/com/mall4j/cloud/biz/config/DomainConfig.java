package com.mall4j.cloud.biz.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@RefreshScope
@Component
public class DomainConfig {

    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    @Value("${aws.openaws}")
    private Boolean openaws=false;

    @Value("${aws.endpoint}")
    private String endpoint;

    @Value("${scrm.biz.appendDomain}")
    private boolean appendDomain=false;

    public String parseUrl(String url){
        if(StrUtil.isEmpty(url)){
            return url;
        }
        if(!url.startsWith("http:") || !url.startsWith("https:") || !url.startsWith("HTTP:") || !url.startsWith("HTTPS:")){
            return getDomain()+""+url;
        }
        return url;
    }

    public String getDomain(){
        if(!appendDomain){//亚马逊无需拼接
            return "";
        }
        if(openaws){
            return endpoint;
        }
        return imgDomain;
    }

    public String getAwsS3Domain(){
        return endpoint;
    }

}
