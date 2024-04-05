package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.service.WeixinFileUploadService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaTemplate;
import com.mall4j.cloud.biz.mapper.WeixinMaTemplateMapper;
import com.mall4j.cloud.biz.service.WeixinMaTemplateService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 微信小程序模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
@Slf4j
@Service
public class WeixinMaTemplateServiceImpl implements WeixinMaTemplateService {

    @Autowired
    private WeixinMaTemplateMapper weixinMaTemplateMapper;

    @Autowired
    private WeixinFileUploadService weixinFileUploadService;

    @Override
    public PageVO<WeixinMaTemplate> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinMaTemplateMapper.list());
    }

    @Override
    public WeixinMaTemplate getById(String id) {
        return weixinMaTemplateMapper.getById(id);
    }

    @Override
    public void save(WeixinMaTemplate weixinMaTemplate) {
        if(StringUtils.isEmpty(weixinMaTemplate.getMaUrl())){
            weixinMaTemplate.setMaUrl(WechatConstants.WEIXIN_DOMAIN);
        }
        weixinMaTemplateMapper.save(weixinMaTemplate);

        updateMedia(weixinMaTemplate.getId(),weixinMaTemplate.getCardImg(),weixinMaTemplate.getMpAppId());
    }

    @Override
    public void update(WeixinMaTemplate weixinMaTemplate) {
        WeixinMaTemplate template=getById(weixinMaTemplate.getId());
        if(template!=null && StringUtils.isNotEmpty(template.getCardImg()) && !template.getCardImg().equals(weixinMaTemplate.getCardImg())){
            updateMedia(weixinMaTemplate.getId(),weixinMaTemplate.getCardImg(),weixinMaTemplate.getMpAppId());
        }
        if(StringUtils.isEmpty(weixinMaTemplate.getMaUrl())){
            weixinMaTemplate.setMaUrl(WechatConstants.WEIXIN_DOMAIN);
        }
        weixinMaTemplateMapper.update(weixinMaTemplate);
    }

    @Override
    public void updateMediaId(WeixinMaTemplate weixinMaTemplate) {
        weixinMaTemplateMapper.updateMediaId(weixinMaTemplate);
    }

    public void updateMedia(String id,String img,String appId){
        WeixinMaTemplate weixinMaTemplate=new WeixinMaTemplate();
        weixinMaTemplate.setId(id);
        weixinMaTemplate.setCardImg(img);
        weixinMaTemplate.setMpAppId(appId);
        getWxMpMaterialMediaId(weixinMaTemplate);
        update(weixinMaTemplate);
    }

    private void getWxMpMaterialMediaId(WeixinMaTemplate weixinMaTemplate){
        try {
            //图片需要上传微信素材库，获取mediaId 保存入库，用户消息发送
            if(StringUtils.isNotEmpty(weixinMaTemplate.getCardImg())){
                WxMpMaterialUploadResult uploadResult=weixinFileUploadService.uploadImgUrl(weixinMaTemplate.getCardImg(),weixinMaTemplate.getMpAppId());
                if(uploadResult!=null && StringUtils.isNotEmpty(uploadResult.getMediaId())){
                    weixinMaTemplate.setThumbMediaId(uploadResult.getMediaId());
                }
            }
        }catch (Exception e){
            log.info(""+e.getMessage());
        }

    }

    @Override
    public void deleteById(String id) {
        weixinMaTemplateMapper.deleteById(id);
    }

    @Override
    public List<WeixinMaTemplate> listAfterThreeDayPicMediaIds() {
        return weixinMaTemplateMapper.listAfterThreeDayPicMediaIds();
    }
}
