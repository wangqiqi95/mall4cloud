package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoMsgType;
import com.mall4j.cloud.biz.dto.WeixinAutoScanDTO;
import com.mall4j.cloud.biz.dto.WeixinTemplateManagerDTO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.model.WeixinQrcode;
import com.mall4j.cloud.biz.service.WeiXinTemplateManagerService;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.biz.service.WeixinQrcodeService;
import com.mall4j.cloud.biz.vo.WeixinAutoScanVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoScan;
import com.mall4j.cloud.biz.mapper.WeixinAutoScanMapper;
import com.mall4j.cloud.biz.service.WeixinAutoScanService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信扫码回复表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
@Service
public class WeixinAutoScanServiceImpl implements WeixinAutoScanService {

    @Autowired
    private WeixinAutoScanMapper weixinAutoScanMapper;

    @Autowired
    private WeiXinTemplateManagerService templateManagerService;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Autowired
    private WeixinQrcodeService weixinQrcodeService;

    @Override
    public PageVO<WeixinAutoScanVO> page(PageDTO pageDTO,String appId,String name,Integer type,String startTime,String endTime) {
        Date startDate= !StringUtils.isEmpty(startTime)?DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss"):null;
        Date endDate= !StringUtils.isEmpty(endTime)?DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss"):null;

        PageVO<WeixinAutoScanVO> pageVO=PageUtil.doPage(pageDTO, () -> weixinAutoScanMapper.getList(appId,name,type,startDate,endDate));

        pageVO.getList().stream().peek(item->{
            if(StringUtils.isNotEmpty(item.getQrcodeId())){
                WeixinQrcode weixinQrcode=weixinQrcodeService.getById(item.getQrcodeId());
                if(weixinQrcode!=null){
                    item.setQrcodeName(weixinQrcode.getActionInfo());
                }
            }
        }).collect(Collectors.toList());
        return pageVO;
    }

    @Override
    public WeixinAutoScan getById(String id) {
        return weixinAutoScanMapper.getById(id);
    }

    @Override
    public void save(WeixinAutoScan weixinAutoScan) {
        weixinAutoScanMapper.save(weixinAutoScan);
    }

    @Override
    public void update(WeixinAutoScan weixinAutoScan) {
        weixinAutoScanMapper.update(weixinAutoScan);
    }

    @Override
    public void deleteById(String id) {
        weixinAutoScanMapper.deleteById(id);
    }

    @Override
    public ServerResponseEntity<WeixinAutoScanVO> getWeixinAutoScanVO(String id) {
        WeixinAutoScan autoScan=getById(id);
        if(autoScan==null){
            return ServerResponseEntity.success();
        }
        List<WeixinAutoResponse> autoResponse=autoResponseService.getWeixinAutos(autoScan.getId(), WxAutoDataSrcType.QR_SCAN.value());
        if(autoResponse==null || autoResponse.isEmpty()){
            return ServerResponseEntity.success();
        }

        WeixinAutoScanVO autoScanVO=new WeixinAutoScanVO();
        BeanUtils.copyProperties(autoScan,autoScanVO);

        autoResponse.stream().peek(item->{
            if(item.getMsgType().equals(WxAutoMsgType.TEXT.value())){
                WeixinTemplageManagerVO managerVO_text=templateManagerService.getTemplate(item.getTemplateId(), WxAutoMsgType.TEXT.value());
                autoScanVO.setTextTemplate(managerVO_text.getTextTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.IMAGE.value())){
                WeixinTemplageManagerVO managerVO_image=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.IMAGE.value());
                autoScanVO.setImgTemplate(managerVO_image.getImgTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.WXMA.value())){
                WeixinTemplageManagerVO managerVO_wxma=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.WXMA.value());
                autoScanVO.setMaTemplate(managerVO_wxma.getMaTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.NEWS.value())){
                WeixinTemplageManagerVO managerVO_news=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.NEWS.value());
                autoScanVO.setNewsTemplate(managerVO_news.getNewsTemplate());
            }
        }).collect(Collectors.toList());

        return ServerResponseEntity.success(autoScanVO);
    }

    @Override
    public void saveWeixinAutoScan(WeixinAutoScanDTO autoScanDTO) {
        String appId=autoScanDTO.getAppId();

        Long userId= AuthUserContext.get().getUserId();
        WeixinAutoScan autoScan=autoScanDTO.getId()!=null?getById(autoScanDTO.getId()):null;

        String msgType=null;
        if(autoScanDTO.getTextTemplate()!=null) msgType= WxAutoMsgType.TEXT.value();
        if(autoScanDTO.getImgTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.IMAGE.value();
        if(autoScanDTO.getMaTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.WXMA.value();
        if(autoScanDTO.getNewsTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.NEWS.value();

        if(autoScan==null){//保存回复配置信息
            autoScan=new WeixinAutoScan();
            BeanUtils.copyProperties(autoScanDTO,autoScan);
            autoScan.setId(String.valueOf(RandomUtil.getUniqueNum()));
            autoScan.setAppId(appId);
            autoScan.setCreateTime(new Date());
            autoScan.setCreateBy(String.valueOf(userId));
            autoScan.setMsgType(msgType);
            autoScan.setDelFlag(0);
            this.save(autoScan);
        }else{////更新回复配置信息
            BeanUtils.copyProperties(autoScanDTO,autoScan);
            autoScan.setMsgType(msgType);
            autoScan.setUpdateTime(new Date());
            autoScan.setUpdateBy(String.valueOf(userId));
            this.update(autoScan);

            autoResponseService.deleteByDataId(autoScan.getId());
        }
        String dataId=autoScan.getId();
        Integer dataSrc= WxAutoDataSrcType.QR_SCAN.value();


        //保存-素材模板信息
        WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
        BeanUtils.copyProperties(autoScanDTO,templateManagerDTO);
        templateManagerDTO.setDataId(dataId);
        templateManagerDTO.setDataSrc(dataSrc);
        templateManagerService.saveTemplatesAndAutoResponses(templateManagerDTO);

//        if(autoScanDTO.getTextTemplate()!=null){
//            //保存-素材模板信息(文字)
//            String msgType= WxAutoMsgType.TEXT.value();
//            WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
//            BeanUtils.copyProperties(autoScanDTO,templateManagerDTO);
//            templateManagerDTO.setMsgType(msgType);
//            WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
//            //保存或更新-微信公共回复内容
//            autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
//        }
//        if(autoScanDTO.getImgTemplate()!=null){
//            //保存-素材模板信息（图片）
//            String msgType=WxAutoMsgType.IMAGE.value();
//            WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
//            BeanUtils.copyProperties(autoScanDTO,templateManagerDTO);
//            templateManagerDTO.setMsgType(msgType);
//            WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
//            //保存或更新-微信公共回复内容
//            autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
//        }
//        if(autoScanDTO.getMaTemplate()!=null){
//            //保存-素材模板信息（小程序）
//            String msgType=WxAutoMsgType.WXMA.value();
//            WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
//            BeanUtils.copyProperties(autoScanDTO,templateManagerDTO);
//            templateManagerDTO.setMsgType(msgType);
//            WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
//            //保存或更新-微信公共回复内容
//            autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
//        }
//        if(autoScanDTO.getNewsTemplate()!=null){
//            //保存-素材模板信息（图文）
//            String msgType=WxAutoMsgType.NEWS.value();
//            WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
//            BeanUtils.copyProperties(autoScanDTO,templateManagerDTO);
//            templateManagerDTO.setMsgType(msgType);
//            WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
//            //保存或更新-微信公共回复内容
//            autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
//        }
    }

    @Override
    public List<WeixinAutoScanVO> getReplyListByQrcode(String appId, String qrcodeId) {
        return weixinAutoScanMapper.getReplyListByQrcode(appId,qrcodeId);
    }
}
