package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.model.WeixinMaTemplate;
import com.mall4j.cloud.biz.service.WeixinFileUploadService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinImgTemplate;
import com.mall4j.cloud.biz.mapper.WeixinImgTemplateMapper;
import com.mall4j.cloud.biz.service.WeixinImgTemplateService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 微信图片模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
@Slf4j
@Service
public class WeixinImgTemplateServiceImpl implements WeixinImgTemplateService {

    @Autowired
    private WeixinImgTemplateMapper weixinImgTemplateMapper;

    @Autowired
    private WeixinFileUploadService weixinFileUploadService;

    @Override
    public PageVO<WeixinImgTemplate> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinImgTemplateMapper.list());
    }

    @Override
    public WeixinImgTemplate getById(String id) {
        return weixinImgTemplateMapper.getById(id);
    }

    @Override
    public void save(WeixinImgTemplate weixinImgTemplate) {

        weixinImgTemplateMapper.save(weixinImgTemplate);

        updateMedia(weixinImgTemplate.getId(),weixinImgTemplate.getTemplateImg(),weixinImgTemplate.getAppId());
    }

    @Override
    public void update(WeixinImgTemplate weixinImgTemplate) {

        WeixinImgTemplate template=getById(weixinImgTemplate.getId());
        if(template!=null && StringUtils.isNotEmpty(template.getTemplateImg()) && !template.getTemplateImg().equals(weixinImgTemplate.getTemplateImg())){
            updateMedia(weixinImgTemplate.getId(),weixinImgTemplate.getTemplateImg(),weixinImgTemplate.getAppId());
        }

        getWxMpMaterialMediaId(weixinImgTemplate);

        weixinImgTemplateMapper.update(weixinImgTemplate);
    }

    @Override
    public void updateMediaId(WeixinImgTemplate weixinImgTemplate) {
        weixinImgTemplateMapper.updateMediaId(weixinImgTemplate);
    }

    public void updateMedia(String id,String img,String appId){
        WeixinImgTemplate weixinImgTemplate=new WeixinImgTemplate();
        weixinImgTemplate.setId(id);
        weixinImgTemplate.setTemplateImg(img);
        weixinImgTemplate.setAppId(appId);
        getWxMpMaterialMediaId(weixinImgTemplate);
        update(weixinImgTemplate);
    }

    private void getWxMpMaterialMediaId(WeixinImgTemplate weixinImgTemplate){
        try {
            //图片需要上传微信素材库，获取mediaId 保存入库，用户消息发送
            if(StringUtils.isNotEmpty(weixinImgTemplate.getTemplateImg())){
                WxMpMaterialUploadResult uploadResult=weixinFileUploadService.uploadImgUrl(weixinImgTemplate.getTemplateImg(),weixinImgTemplate.getAppId());
                if(uploadResult!=null && StringUtils.isNotEmpty(uploadResult.getMediaId())){
                    weixinImgTemplate.setMediaId(uploadResult.getMediaId());
                }
            }
        }catch (Exception e){
            log.info(""+e.getMessage());
        }

    }

    @Override
    public void deleteById(String id) {
        weixinImgTemplateMapper.deleteById(id);
    }

    @Override
    public List<WeixinImgTemplate> listAfterThreeDayPicMediaIds() {
        return weixinImgTemplateMapper.listAfterThreeDayPicMediaIds();
    }
}
