package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.wx.wx.constant.WxAutoCNMsgType;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoMsgType;
import com.mall4j.cloud.biz.dto.WeixinAutoKeywordPutDTO;
import com.mall4j.cloud.biz.dto.WeixinTemplateManagerDTO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.service.WeiXinTemplateManagerService;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.biz.vo.WeixinAutoKeywordVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoKeyword;
import com.mall4j.cloud.biz.mapper.WeixinAutoKeywordMapper;
import com.mall4j.cloud.biz.service.WeixinAutoKeywordService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信关键字表
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
@Service
public class WeixinAutoKeywordServiceImpl implements WeixinAutoKeywordService {

    @Autowired
    private WeixinAutoKeywordMapper weixinAutoKeywordMapper;

    @Autowired
    private WeiXinTemplateManagerService templateManagerService;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Override
    public PageVO<WeixinAutoKeywordVO> page(PageDTO pageDTO,
                                          String appId,
                                          String name,
                                          String keyword,
                                          Integer isWork) {
        PageVO<WeixinAutoKeywordVO> pageVO=PageUtil.doPage(pageDTO, () -> weixinAutoKeywordMapper.getList(appId,name,keyword,isWork));

        pageVO.getList().stream().peek(item->{
            if(StringUtils.isNotEmpty(item.getMsgType())){
                item.setMsgTypeName(WxAutoCNMsgType.value(item.getMsgType()));
            }
        }).collect(Collectors.toList());

        return pageVO;
    }

    @Override
    public WeixinAutoKeyword getById(String id) {
        return weixinAutoKeywordMapper.getById(id);
    }

    @Override
    public void save(WeixinAutoKeyword weixinAutoKeyword) {
        weixinAutoKeywordMapper.save(weixinAutoKeyword);
    }

    @Override
    public void update(WeixinAutoKeyword weixinAutoKeyword) {
        weixinAutoKeywordMapper.update(weixinAutoKeyword);
    }

    @Override
    public void deleteById(String id) {
        weixinAutoKeywordMapper.deleteById(String.valueOf(AuthUserContext.get().getUserId()),id);
    }

    /**
     * 关键词详情
     * @param id
     * @return
     */
    @Override
    public ServerResponseEntity<WeixinAutoKeywordVO> getWeixinAutoKeywordVO(String id) {
        WeixinAutoKeyword autoKeyword=getById(id);
        if(autoKeyword==null){
            return ServerResponseEntity.success();
        }
        List<WeixinAutoResponse> autoResponse=autoResponseService.getWeixinAutos(autoKeyword.getId(), WxAutoDataSrcType.KEY_WORD.value());
        if(autoResponse==null || autoResponse.isEmpty()){
            return ServerResponseEntity.success();
        }

        WeixinAutoKeywordVO autoKeywordVO=new WeixinAutoKeywordVO();
        BeanUtils.copyProperties(autoKeyword,autoKeywordVO);

        //内容包含多个
        autoKeywordVO.setTextTemplates(new ArrayList<>());
        autoKeywordVO.setImgTemplates(new ArrayList<>());
        autoKeywordVO.setMaTemplates(new ArrayList<>());
        autoKeywordVO.setNewsTemplates(new ArrayList<>());

        autoResponse.stream().peek(item->{
            if(item.getMsgType().equals(WxAutoMsgType.TEXT.value())){
                WeixinTemplageManagerVO managerVO_text=templateManagerService.getTemplate(item.getTemplateId(), WxAutoMsgType.TEXT.value());
                autoKeywordVO.getTextTemplates().add(managerVO_text.getTextTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.IMAGE.value())){
                WeixinTemplageManagerVO managerVO_image=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.IMAGE.value());
                autoKeywordVO.getImgTemplates().add(managerVO_image.getImgTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.WXMA.value())){
                WeixinTemplageManagerVO managerVO_wxma=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.WXMA.value());
                autoKeywordVO.getMaTemplates().add(managerVO_wxma.getMaTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.NEWS.value())){
                WeixinTemplageManagerVO managerVO_news=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.NEWS.value());
                autoKeywordVO.getNewsTemplates().add(managerVO_news.getNewsTemplate());
            }
        }).collect(Collectors.toList());

        return ServerResponseEntity.success(autoKeywordVO);
    }

    /**
     * 保存或者更新关键词
     * @param autoKeywordPutDTO
     */
    @Override
    public ServerResponseEntity<Void> saveWeixinAutoKeyword(WeixinAutoKeywordPutDTO autoKeywordPutDTO) {
        String appId=autoKeywordPutDTO.getAppId();
        Long userId= AuthUserContext.get().getUserId();
        WeixinAutoKeyword autoKeyword=autoKeywordPutDTO.getId()!=null?getById(autoKeywordPutDTO.getId()):null;
        int contentSize=0;

        if(autoKeywordPutDTO.getTextTemplates()!=null){
            contentSize=contentSize+autoKeywordPutDTO.getTextTemplates().size();
        }
        if(autoKeywordPutDTO.getImgTemplates()!=null){
            contentSize=contentSize+autoKeywordPutDTO.getImgTemplates().size();
        }
        if(autoKeywordPutDTO.getMaTemplates()!=null){
            contentSize=contentSize+autoKeywordPutDTO.getMaTemplates().size();
        }
        if(autoKeywordPutDTO.getNewsTemplates()!=null){
            contentSize=contentSize+autoKeywordPutDTO.getNewsTemplates().size();
        }
        if(contentSize>5){
            return ServerResponseEntity.showFailMsg("回复内容最多添加5个");
        }

        if(autoKeyword==null){//保存回复配置信息
            autoKeyword=new WeixinAutoKeyword();
            BeanUtils.copyProperties(autoKeywordPutDTO,autoKeyword);
            autoKeyword.setId(String.valueOf(RandomUtil.getUniqueNum()));
            autoKeyword.setAppId(appId);
            autoKeyword.setCreateTime(new Date());
            autoKeyword.setCreateBy(String.valueOf(userId));
            autoKeyword.setDelFlag(0);
            autoKeyword.setIsWork(0);
            this.save(autoKeyword);
        }else{////更新回复配置信息
            BeanUtils.copyProperties(autoKeywordPutDTO,autoKeyword);
            autoKeyword.setUpdateTime(new Date());
            autoKeyword.setUpdateBy(String.valueOf(userId));
            this.update(autoKeyword);
        }
        String dataId=autoKeyword.getId();
        Integer dataSrc=WxAutoDataSrcType.KEY_WORD.value();

        //删除内容
        autoResponseService.deleteByDataId(dataId);

        StringBuilder msgTypeBuf=new StringBuilder();
        if(autoKeywordPutDTO.getTextTemplates()!=null && autoKeywordPutDTO.getTextTemplates().size()>0){
            msgTypeBuf.append(WxAutoMsgType.TEXT.value()).append(",");
            //保存-素材模板信息(文字)
            autoKeywordPutDTO.getTextTemplates().stream().peek(item->{
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(WxAutoMsgType.TEXT.value());
                templateManagerDTO.setTextTemplate(item);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponseList(dataId,dataSrc,managerVO);
            }).collect(Collectors.toList());

        }
        if(autoKeywordPutDTO.getImgTemplates()!=null && autoKeywordPutDTO.getImgTemplates().size()>0){
            msgTypeBuf.append(WxAutoMsgType.IMAGE.value()).append(",");
            //保存-素材模板信息（图片）
            autoKeywordPutDTO.getImgTemplates().stream().peek(item->{
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(WxAutoMsgType.IMAGE.value());
                templateManagerDTO.setImgTemplate(item);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponseList(dataId,dataSrc,managerVO);
            }).collect(Collectors.toList());

        }
        if(autoKeywordPutDTO.getMaTemplates()!=null && autoKeywordPutDTO.getMaTemplates().size()>0){
            msgTypeBuf.append(WxAutoMsgType.WXMA.value()).append(",");
            //保存-素材模板信息（小程序）
            autoKeywordPutDTO.getMaTemplates().stream().peek(item->{
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(WxAutoMsgType.WXMA.value());
                templateManagerDTO.setMaTemplate(item);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponseList(dataId,dataSrc,managerVO);
            }).collect(Collectors.toList());
        }
        if(autoKeywordPutDTO.getNewsTemplates()!=null && autoKeywordPutDTO.getNewsTemplates().size()>0){
            msgTypeBuf.append(WxAutoMsgType.NEWS.value()).append(",");
            //保存-素材模板信息（图文）
            autoKeywordPutDTO.getNewsTemplates().stream().peek(item->{
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                templateManagerDTO.setMsgType(WxAutoMsgType.NEWS.value());
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setNewsTemplate(item);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponseList(dataId,dataSrc,managerVO);
            }).collect(Collectors.toList());
        }

        if(msgTypeBuf.length()>0){
            String msgType=msgTypeBuf.substring(0,msgTypeBuf.length()-1);
            autoKeyword=new WeixinAutoKeyword();
            autoKeyword.setId(dataId);
            autoKeyword.setMsgType(msgType);
            this.update(autoKeyword);
        }

        return ServerResponseEntity.success();
    }

    @Override
    public WeixinAutoKeywordVO getByKeyword(String appId, String keyword) {
        return weixinAutoKeywordMapper.getByKeyword(appId,keyword);
    }

}
