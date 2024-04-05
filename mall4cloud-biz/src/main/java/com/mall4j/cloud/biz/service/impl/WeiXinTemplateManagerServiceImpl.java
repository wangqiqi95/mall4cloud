package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoMsgType;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.mapper.WeixinNewsItemMapper;
import com.mall4j.cloud.biz.model.*;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.*;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信图片模板公共使用
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
@Slf4j
@Service
public class WeiXinTemplateManagerServiceImpl implements WeiXinTemplateManagerService {

    @Autowired
    private WeixinTextTemplateService textTemplateService;

    @Autowired
    private WeixinImgTemplateService imgTemplateService;

    @Autowired
    private WeixinMaTemplateService maTemplateService;

    @Autowired
    private WeixinNewsTemplateService newsTemplateService;

    @Autowired
    private WeixinLinksucaiService linksucaiService;

    @Resource
    private WeixinNewsItemMapper weixinNewsItemMapper;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Override
    public WeixinTemplageManagerVO getTemplate(String templateId, String msgType) {
        WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
        if(msgType.equals(WxAutoMsgType.TEXT.value())){//文字
            WeixinTextTemplate textTemplate=textTemplateService.getById(templateId);
            if(textTemplate!=null){
                WeixinTextTemplateVO textTemplateVO=new WeixinTextTemplateVO();
                BeanUtils.copyProperties(textTemplate,textTemplateVO);
                managerVO.setTextTemplate(textTemplateVO);
            }
        }

        if(msgType.equals(WxAutoMsgType.IMAGE.value())){//图片
            WeixinImgTemplate imgTemplate=imgTemplateService.getById(templateId);
            if(imgTemplate!=null){
                WeixinImgTemplateVO imgTemplateVO=new WeixinImgTemplateVO();
                BeanUtils.copyProperties(imgTemplate,imgTemplateVO);
                managerVO.setImgTemplate(imgTemplateVO);
            }
        }

        if(msgType.equals(WxAutoMsgType.WXMA.value())){//小程序
            WeixinMaTemplate maTemplate=maTemplateService.getById(templateId);
            if(maTemplate!=null){
                WeixinMaTemplateVO maTemplateVO=new WeixinMaTemplateVO();
                BeanUtils.copyProperties(maTemplate,maTemplateVO);
                managerVO.setMaTemplate(maTemplateVO);
            }
        }

        if(msgType.equals(WxAutoMsgType.SHOP_MALL.value())){//小程序-商城功能
            WeixinMaTemplate maTemplate=maTemplateService.getById(templateId);
            if(maTemplate!=null){
                WeixinShopMallTemplateVo shopMallTemplateVo=new WeixinShopMallTemplateVo();
                BeanUtils.copyProperties(maTemplate,shopMallTemplateVo);
                managerVO.setShopMallTemplate(shopMallTemplateVo);
            }
        }

        if(msgType.equals(WxAutoMsgType.EXTERNAL_LINK.value())){//小程序
            WeixinLinksucai linksucai=linksucaiService.getById(templateId);
            if(linksucai!=null){
                WeixinLinksucaiVO linksucaiVO=new WeixinLinksucaiVO();
                BeanUtils.copyProperties(linksucai,linksucaiVO);
                managerVO.setLinksucai(linksucaiVO);
            }
        }

        if(msgType.equals(WxAutoMsgType.NEWS.value())){//图文(图文模板从-微信自动回复选取使用，此处无需新增保存)
            WeixinNewsTemplate newsTemplate=newsTemplateService.getById(templateId);//素材模板
            if(newsTemplate!=null){
                WeixinNewsTemplateContentVO newsTemplateContentVO=new WeixinNewsTemplateContentVO();
                BeanUtils.copyProperties(newsTemplate,newsTemplateContentVO);
                //素材内容
                List<WeixinNewsItemVO> items= weixinNewsItemMapper.getWeixinNewsItemList(newsTemplate.getId());
                newsTemplateContentVO.setNewsItemList(items);

                managerVO.setNewsTemplate(newsTemplateContentVO);
            }
        }
        log.info("获取公众号配置内容：入参templateId:{} msgType:{} 结果:{}",templateId,msgType,JSON.toJSONString(managerVO));
        return managerVO;
    }

    /**
     * 保存微信素材：文字、图片、小程序、图文、链接、商城功能
     * @param templateManagerDTO
     * @return
     */
    @Override
    public WeixinTemplageManagerVO saveTemplate(WeixinTemplateManagerDTO templateManagerDTO) {

        Long userId=AuthUserContext.get().getUserId();//当前登录管理员账户
        String msgType=templateManagerDTO.getMsgType();
        String appId=templateManagerDTO.getAppId();

        String templageName=msgType,templateId=null;

        WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();

        if(msgType.equals(WxAutoMsgType.TEXT.value()) && templateManagerDTO.getTextTemplate()!=null){//文字
            WeixinTextTemplate textTemplate=new WeixinTextTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getTextTemplate(),textTemplate);
            textTemplate.setAppId(appId);
            textTemplate.setTemplateName(msgType);
            if(StringUtils.isNotEmpty(textTemplate.getId())){
                textTemplate.setUpdateTime(new Date());
                textTemplate.setUpdateBy(String.valueOf(userId));
                textTemplateService.update(textTemplate);
            }else{
                textTemplate.setCreateTime(new Date());
                textTemplate.setCreateBy(String.valueOf(userId));
                textTemplate.setDelFlag(0);
                textTemplate.setId(RandomUtil.getUniqueNumStr());
                textTemplateService.save(textTemplate);
            }
            templateId=textTemplate.getId();

        }

        if(msgType.equals(WxAutoMsgType.IMAGE.value()) && templateManagerDTO.getImgTemplate()!=null){//图片
            WeixinImgTemplate imgTemplate=new WeixinImgTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getImgTemplate(),imgTemplate);
            imgTemplate.setAppId(appId);
            if(StringUtils.isNotEmpty(imgTemplate.getId())){
                imgTemplate.setUpdateTime(new Date());
                imgTemplate.setUpdateBy(String.valueOf(userId));
                imgTemplateService.update(imgTemplate);
            }else{
                imgTemplate.setCreateTime(new Date());
                imgTemplate.setCreateBy(String.valueOf(userId));
                imgTemplate.setDelFlag(0);
                imgTemplate.setId(RandomUtil.getUniqueNumStr());
                imgTemplateService.save(imgTemplate);
            }
            templateId=imgTemplate.getId();
        }

        if(msgType.equals(WxAutoMsgType.WXMA.value()) && templateManagerDTO.getMaTemplate()!=null){//小程序
            WeixinMaTemplate maTemplate=new WeixinMaTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getMaTemplate(),maTemplate);
            maTemplate.setMpAppId(appId);
            if(StringUtils.isNotEmpty(maTemplate.getId())){
                maTemplate.setUpdateTime(new Date());
                maTemplate.setUpdateBy(String.valueOf(userId));
                maTemplateService.update(maTemplate);
            }else{
                maTemplate.setDelFlag(0);
                maTemplate.setCreateTime(new Date());
                maTemplate.setCreateBy(String.valueOf(userId));
                maTemplate.setId(RandomUtil.getUniqueNumStr());
                maTemplateService.save(maTemplate);
            }
            templateId=maTemplate.getId();
        }

        if(msgType.equals(WxAutoMsgType.SHOP_MALL.value()) && templateManagerDTO.getShopMallTemplate()!=null){//小程序-商城功能
            WeixinMaTemplate maTemplate=new WeixinMaTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getShopMallTemplate(),maTemplate);
            maTemplate.setDelFlag(0);
            maTemplate.setCreateTime(new Date());
            maTemplate.setCreateBy(String.valueOf(userId));
            maTemplate.setId(RandomUtil.getUniqueNumStr());
            maTemplateService.save(maTemplate);
            templateId=maTemplate.getId();
        }

        if(msgType.equals(WxAutoMsgType.EXTERNAL_LINK.value()) && templateManagerDTO.getLinksucai()!=null){//外部链接
            WeixinLinksucai linksucai=new WeixinLinksucai();
            BeanUtils.copyProperties(templateManagerDTO.getLinksucai(),linksucai);
            linksucai.setAppId(appId);
            if(StringUtils.isNotEmpty(linksucai.getId())){
                linksucai.setUpdateTime(new Date());
                linksucai.setUpdateBy(String.valueOf(userId));
                linksucaiService.update(linksucai);
            }else{
                linksucai.setDelFlag(0);
                linksucai.setCreateTime(new Date());
                linksucai.setCreateBy(String.valueOf(userId));
                linksucai.setId(RandomUtil.getUniqueNumStr());
                linksucaiService.save(linksucai);
            }
            templateId=linksucai.getId();
        }

        if(msgType.equals(WxAutoMsgType.NEWS.value()) && templateManagerDTO.getNewsTemplate()!=null){//图文(图文模板从-微信自动回复选取使用，此处无需新增保存)
            templateId=templateManagerDTO.getNewsTemplate().getId();
        }
        managerVO.setTemplateId(templateId);
        managerVO.setTemplateName(templageName);
        managerVO.setMsgType(msgType);
        return managerVO;
    }

    @Override
    public List<WeixinTemplageManagerVO> saveTemplatesAndAutoResponses(WeixinTemplateManagerDTO templateManagerDTO) {
        List<WeixinTemplageManagerVO> managerVOS=new ArrayList<>();

        //删除关联内容
        autoResponseService.deleteByDataId(templateManagerDTO.getDataId());

        Long userId=AuthUserContext.get().getUserId();//当前登录管理员账户
        String appId=templateManagerDTO.getAppId();

        if(templateManagerDTO.getTextTemplate()!=null){//文字
            WeixinTextTemplate textTemplate=new WeixinTextTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getTextTemplate(),textTemplate);
            textTemplate.setAppId(appId);
            textTemplate.setTemplateName(WxAutoMsgType.TEXT.value());
            if(StringUtils.isNotEmpty(textTemplate.getId())){
                textTemplate.setUpdateTime(new Date());
                textTemplate.setUpdateBy(String.valueOf(userId));
                textTemplateService.update(textTemplate);
            }else{
                textTemplate.setCreateTime(new Date());
                textTemplate.setCreateBy(String.valueOf(userId));
                textTemplate.setDelFlag(0);
                textTemplate.setId(RandomUtil.getUniqueNumStr());
                textTemplateService.save(textTemplate);
            }
            WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
            managerVO.setTemplateId(textTemplate.getId());
            managerVO.setTemplateName(WxAutoMsgType.TEXT.value());
            managerVO.setMsgType(WxAutoMsgType.TEXT.value());
            managerVO.setDataId(templateManagerDTO.getDataId());
            managerVO.setDataSrc(templateManagerDTO.getDataSrc());
            managerVOS.add(managerVO);
        }

        if(templateManagerDTO.getImgTemplate()!=null){//图片
            WeixinImgTemplate imgTemplate=new WeixinImgTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getImgTemplate(),imgTemplate);
            imgTemplate.setAppId(appId);
            if(StringUtils.isNotEmpty(imgTemplate.getId())){
                imgTemplate.setUpdateTime(new Date());
                imgTemplate.setUpdateBy(String.valueOf(userId));
                imgTemplateService.update(imgTemplate);
            }else{
                imgTemplate.setCreateTime(new Date());
                imgTemplate.setCreateBy(String.valueOf(userId));
                imgTemplate.setDelFlag(0);
                imgTemplate.setId(RandomUtil.getUniqueNumStr());
                imgTemplateService.save(imgTemplate);
            }
            WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
            managerVO.setTemplateId(imgTemplate.getId());
            managerVO.setTemplateName(WxAutoMsgType.IMAGE.value());
            managerVO.setMsgType(WxAutoMsgType.IMAGE.value());
            managerVO.setDataId(templateManagerDTO.getDataId());
            managerVO.setDataSrc(templateManagerDTO.getDataSrc());
            managerVOS.add(managerVO);
        }

        if( templateManagerDTO.getMaTemplate()!=null){//小程序
            WeixinMaTemplate maTemplate=new WeixinMaTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getMaTemplate(),maTemplate);
            maTemplate.setMpAppId(appId);
            if(StringUtils.isNotEmpty(maTemplate.getId())){
                maTemplate.setUpdateTime(new Date());
                maTemplate.setUpdateBy(String.valueOf(userId));
                maTemplateService.update(maTemplate);
            }else{
                maTemplate.setDelFlag(0);
                maTemplate.setCreateTime(new Date());
                maTemplate.setCreateBy(String.valueOf(userId));
                maTemplate.setId(RandomUtil.getUniqueNumStr());
                maTemplateService.save(maTemplate);
            }
            WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
            managerVO.setTemplateId(maTemplate.getId());
            managerVO.setTemplateName(WxAutoMsgType.WXMA.value());
            managerVO.setMsgType(WxAutoMsgType.WXMA.value());
            managerVO.setDataId(templateManagerDTO.getDataId());
            managerVO.setDataSrc(templateManagerDTO.getDataSrc());
            managerVOS.add(managerVO);
        }

        if(templateManagerDTO.getShopMallTemplate()!=null){//小程序-商城功能
            WeixinMaTemplate maTemplate=new WeixinMaTemplate();
            BeanUtils.copyProperties(templateManagerDTO.getShopMallTemplate(),maTemplate);
            maTemplate.setDelFlag(0);
            maTemplate.setCreateTime(new Date());
            maTemplate.setCreateBy(String.valueOf(userId));
            maTemplate.setId(RandomUtil.getUniqueNumStr());
            maTemplateService.save(maTemplate);

            WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
            managerVO.setTemplateId(maTemplate.getId());
            managerVO.setTemplateName(WxAutoMsgType.SHOP_MALL.value());
            managerVO.setMsgType(WxAutoMsgType.SHOP_MALL.value());
            managerVO.setDataId(templateManagerDTO.getDataId());
            managerVO.setDataSrc(templateManagerDTO.getDataSrc());
            managerVOS.add(managerVO);
        }

        if(templateManagerDTO.getLinksucai()!=null){//外部链接
            WeixinLinksucai linksucai=new WeixinLinksucai();
            BeanUtils.copyProperties(templateManagerDTO.getLinksucai(),linksucai);
            linksucai.setAppId(appId);
            if(StringUtils.isNotEmpty(linksucai.getId())){
                linksucai.setUpdateTime(new Date());
                linksucai.setUpdateBy(String.valueOf(userId));
                linksucaiService.update(linksucai);
            }else{
                linksucai.setDelFlag(0);
                linksucai.setCreateTime(new Date());
                linksucai.setCreateBy(String.valueOf(userId));
                linksucai.setId(RandomUtil.getUniqueNumStr());
                linksucaiService.save(linksucai);
            }
            WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
            managerVO.setTemplateId(linksucai.getId());
            managerVO.setTemplateName(WxAutoMsgType.EXTERNAL_LINK.value());
            managerVO.setMsgType(WxAutoMsgType.EXTERNAL_LINK.value());
            managerVO.setDataId(templateManagerDTO.getDataId());
            managerVO.setDataSrc(templateManagerDTO.getDataSrc());
            managerVOS.add(managerVO);
        }

        if( templateManagerDTO.getNewsTemplate()!=null){//图文(图文模板从-微信自动回复选取使用，此处无需新增保存)

            //图文内容
            WeixinNewsTemplateDTO newsTemplate=templateManagerDTO.getNewsTemplate();


            WeixinTemplageManagerVO managerVO=new WeixinTemplageManagerVO();
            managerVO.setTemplateId(newsTemplate.getId());
            managerVO.setTemplateName(WxAutoMsgType.NEWS.value());
            managerVO.setMsgType(WxAutoMsgType.NEWS.value());
            managerVO.setDataId(templateManagerDTO.getDataId());
            managerVO.setDataSrc(templateManagerDTO.getDataSrc());
            managerVOS.add(managerVO);
        }

        if(CollectionUtil.isNotEmpty(managerVOS)){
            log.info("保存关联内容：--->>>>"+ JSON.toJSONString(managerVOS));
            for(WeixinTemplageManagerVO managerVO1:managerVOS){
                //保存或更新-微信公共回复内容
                String dataId=managerVO1.getDataId();
                Integer dataSrc=managerVO1.getDataSrc();
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO1);
            }
        }
        return managerVOS;
    }
}
