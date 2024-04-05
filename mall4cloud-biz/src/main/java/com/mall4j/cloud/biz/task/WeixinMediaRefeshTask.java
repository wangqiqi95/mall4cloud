package com.mall4j.cloud.biz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.manager.WxMpManager;
import com.mall4j.cloud.biz.model.WeixinImgTemplate;
import com.mall4j.cloud.biz.model.WeixinMaTemplate;
import com.mall4j.cloud.biz.service.WeixinFileUploadService;
import com.mall4j.cloud.biz.service.WeixinImgTemplateService;
import com.mall4j.cloud.biz.service.WeixinMaTemplateService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 */
@RestController
@Component("WeixinMediaRefeshTask")
@Slf4j
public class WeixinMediaRefeshTask {

    @Autowired
    private WeixinImgTemplateService weixinImgTemplateService;

    @Autowired
    private WeixinMaTemplateService weixinMaTemplateService;

    @Autowired
    private WeixinFileUploadService weixinFileUploadService;
    @Autowired
    private DomainConfig domainConfig;
    @Autowired
    private WxMpManager wxMpManager;
    /**
     * 刷新公众号回复图片内容mediaId
     */
    @PostMapping("refreshWXMPMediaIdTask")
    @XxlJob("refreshWXMPMediaIdTask")
    public void refreshPicMediaIdTask()  {

        Long startTime=System.currentTimeMillis();
        log.info("--start refesh WeixinImgTemplate mediaid");
        List<WeixinImgTemplate> imgTemplates=weixinImgTemplateService.listAfterThreeDayPicMediaIds();
        log.info("WeixinImgTemplate 可刷新数据：{}",imgTemplates.size());
        imgTemplates=imgTemplates.stream()
                .filter(item -> StringUtils.isNotBlank(item.getTemplateImg())
                        && StrUtil.isNotBlank(item.getAppId())).collect(Collectors.toList());
        log.info("WeixinImgTemplate 实际可刷新数据：{}",imgTemplates.size());
        if(CollectionUtil.isNotEmpty(imgTemplates)){
            for(WeixinImgTemplate imgTemplate:imgTemplates){
                try {
                    imgTemplate.setTemplateImg(domainConfig.parseUrl(imgTemplate.getTemplateImg()));
                    WxMpMaterialUploadResult uploadResult=weixinFileUploadService.uploadImgUrl(imgTemplate.getTemplateImg(),imgTemplate.getAppId());
                    if(uploadResult!=null && StringUtils.isNotEmpty(uploadResult.getMediaId())){
                        imgTemplate.setMediaId(uploadResult.getMediaId());
                        imgTemplate.setUpdateTime(new Date());
                        imgTemplate.setUpdateBy("定时任务刷mediaId");
                        weixinImgTemplateService.updateMediaId(imgTemplate);
                    }
                }catch (Exception e){
                    log.info("error : {} {}",e,e.getMessage());
                }
            }
        }
        log.info("--end refesh WeixinImgTemplate mediaid 耗时：{}ms",System.currentTimeMillis() - startTime);


        startTime=System.currentTimeMillis();
        log.info("--start refesh WeixinMaTemplate mediaid");
        List<WeixinMaTemplate> maTemplates=weixinMaTemplateService.listAfterThreeDayPicMediaIds();
        log.info("WeixinMaTemplate 可刷新数据：{}",maTemplates.size());
        maTemplates=maTemplates.stream()
                .filter(item -> StringUtils.isNotBlank(item.getCardImg())
                        && StrUtil.isNotBlank(item.getMpAppId())).collect(Collectors.toList());
        log.info("WeixinMaTemplate 实际可刷新数据：{}",maTemplates.size());
        if(CollectionUtil.isNotEmpty(maTemplates)){
            for(WeixinMaTemplate maTemplate:maTemplates){
                try {
                    maTemplate.setCardImg(domainConfig.parseUrl(maTemplate.getCardImg()));
                    WxMpMaterialUploadResult uploadResult=weixinFileUploadService.uploadImgUrl(maTemplate.getCardImg(),maTemplate.getMpAppId());
                    if(uploadResult!=null && StringUtils.isNotEmpty(uploadResult.getMediaId())){
                        maTemplate.setThumbMediaId(uploadResult.getMediaId());
                        maTemplate.setUpdateTime(new Date());
                        maTemplate.setUpdateBy("定时任务刷mediaId");
                        weixinMaTemplateService.updateMediaId(maTemplate);
                    }
                }catch (Exception e){
                    log.info("error : {} {}",e,e.getMessage());
                }
            }
        }
        log.info("--end refesh WeixinMaTemplate mediaid 耗时：{}ms",System.currentTimeMillis() - startTime);

    }


    /**
     * 获取公众号关注用户列表
     */
    @PostMapping("getWxMpFollowDatas")
    @XxlJob("getWxMpFollowDatas")
    public void getWxMpFollowDatas()  {
        Long startTime=System.currentTimeMillis();
        log.info("--start 获取公众号关注用户列表");
        wxMpManager.getWxMpFollowDatas();
        log.info("--end 获取公众号关注用户列表 耗时：{}ms",System.currentTimeMillis() - startTime);
    }


}

