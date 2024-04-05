package com.mall4j.cloud.biz.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.WeixinMaSubscriptTmessageDTO;
import com.mall4j.cloud.biz.dto.WeixinMaSubscriptTmessageValueDTO;
import com.mall4j.cloud.biz.dto.WeixinSubTmessageStatusDTO;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageValueMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptUserRecordMapper;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessage;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageValue;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserRecord;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageService;
import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageTypeVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信小程序订阅模版消息service实现类
 *
 * @luzhengxiang
 * @create 2022-03-05 4:46 PM
 **/
@Slf4j
@Service
public class WeixinMaSubscriptTmessageServiceImpl implements WeixinMaSubscriptTmessageService {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    WeixinMaSubscriptTmessageMapper weixinMaSubscriptTmessageMapper;
    @Autowired
    WeixinMaSubscriptTmessageValueMapper weixinMaSubscriptTmessageValueMapper;
    @Autowired
    WeixinMaSubscriptUserRecordMapper weixinMaSubscriptUserRecordMapper;

    @Override
    public List<WeixinMaSubscriptTmessageTypeVO> getList(String appId, Integer businessType, Integer sendType) {
        List<WeixinMaSubscriptTmessageTypeVO> subscriptTmessageTypeVOS=weixinMaSubscriptTmessageMapper.getList(appId,businessType,sendType);

        subscriptTmessageTypeVOS.stream().peek(item->{
            item.setContentValues(weixinMaSubscriptTmessageValueMapper.getByTmessageId(item.getId()));
        }).collect(Collectors.toList());

        return subscriptTmessageTypeVOS;
    }

    @Override
    public ServerResponseEntity<Void> saveTo(WeixinMaSubscriptTmessageDTO subscriptTmessageDTO) {
        WeixinMaSubscriptTmessageVO tmessageVO = weixinMaSubscriptTmessageMapper.getBySendType(subscriptTmessageDTO.getSendType());
        if(tmessageVO!=null){
            return ServerResponseEntity.showFailMsg("模板已存在："+tmessageVO.getTitle());
        }
        if(CollectionUtil.isEmpty(subscriptTmessageDTO.getContentValues())){
            return ServerResponseEntity.showFailMsg("模板content内容不能为空");
        }

        WeixinMaSubscriptTmessage subscribeMessage=mapperFacade.map(subscriptTmessageDTO,WeixinMaSubscriptTmessage.class);
        subscribeMessage.setId(RandomUtil.getUniqueNumStr());
        subscribeMessage.setStatus(0);
        subscribeMessage.setCreateTime(new Date());
        subscribeMessage.setDelFlag(0);
        weixinMaSubscriptTmessageMapper.save(subscribeMessage);
        String templateId=subscribeMessage.getId();

        List<WeixinMaSubscriptTmessageValue> tmessageValues=new ArrayList<>();
        for(WeixinMaSubscriptTmessageValueDTO tmessageValueDTO:subscriptTmessageDTO.getContentValues()){
            WeixinMaSubscriptTmessageValue tmessageValue=mapperFacade.map(tmessageValueDTO,WeixinMaSubscriptTmessageValue.class);
            tmessageValue.setTemplateId(templateId);
            tmessageValue.setCreateTime(new Date());
            tmessageValues.add(tmessageValue);
        }
        weixinMaSubscriptTmessageValueMapper.saveBatch(tmessageValues);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateTo(WeixinMaSubscriptTmessageDTO subscriptTmessageDTO) {
        if(weixinMaSubscriptTmessageMapper.getById(subscriptTmessageDTO.getId())==null){
            return ServerResponseEntity.showFailMsg("模板未找到");
        }
        if(CollectionUtil.isEmpty(subscriptTmessageDTO.getContentValues())){
            return ServerResponseEntity.showFailMsg("模板content内容不能为空");
        }

        WeixinMaSubscriptTmessage subscribeMessage=mapperFacade.map(subscriptTmessageDTO,WeixinMaSubscriptTmessage.class);
        subscribeMessage.setUpdateTime(new Date());
        weixinMaSubscriptTmessageMapper.update(subscribeMessage);
        String templateId=subscribeMessage.getId();

        if(CollectionUtil.isNotEmpty(subscriptTmessageDTO.getContentValues())){

            weixinMaSubscriptTmessageValueMapper.deleteByTemplateId(templateId);

            List<WeixinMaSubscriptTmessageValue> tmessageValues=new ArrayList<>();
            for(WeixinMaSubscriptTmessageValueDTO tmessageValueDTO:subscriptTmessageDTO.getContentValues()){
                WeixinMaSubscriptTmessageValue tmessageValue=mapperFacade.map(tmessageValueDTO,WeixinMaSubscriptTmessageValue.class);
                tmessageValue.setTemplateId(templateId);
                tmessageValue.setCreateTime(new Date());
                tmessageValues.add(tmessageValue);
            }
            weixinMaSubscriptTmessageValueMapper.saveBatch(tmessageValues);
        }

        return ServerResponseEntity.success();
    }

    @Override
    public void updateStatus(WeixinSubTmessageStatusDTO statusDTO) {
        if(weixinMaSubscriptTmessageMapper.getById(statusDTO.getId())==null){
            throw new LuckException("模板未找到");
        }
        WeixinMaSubscriptTmessage subscribeMessage=new WeixinMaSubscriptTmessage();
        subscribeMessage.setId(statusDTO.getId());
        subscribeMessage.setStatus(statusDTO.getStatus());
        subscribeMessage.setUpdateTime(new Date());
        weixinMaSubscriptTmessageMapper.update(subscribeMessage);
    }

    @Override
    public void deleteById(String id) {
        weixinMaSubscriptTmessageMapper.deleteById(id);
    }

    @Override
    public void sendMessageToUser(List<WeixinMaSubscriptTmessageSendVO> sendVOList) {

        for (WeixinMaSubscriptTmessageSendVO sendVO : sendVOList) {
            if (sendVO != null) {
                WeixinMaSubscriptUserRecord userRecord = weixinMaSubscriptUserRecordMapper.getById(sendVO.getUserSubscriptRecordId());
                WeixinMaSubscriptTmessage tmessage = weixinMaSubscriptTmessageMapper.getById(userRecord.getTemplateId());
                if(userRecord==null || userRecord.getSendStatus()==1){
                    log.error("WeixinMaSubscriptUserRecord id:{} ,已经发送或者id不存在。",sendVO.getUserSubscriptRecordId());
                    continue;
                }


                WxMaSubscribeMessage wxMaSubscribeMessage = mapperFacade.map(userRecord,WxMaSubscribeMessage.class);

                List<WxMaSubscribeMessage.MsgData> msgDataList =new ArrayList<>();
                if(CollUtil.isNotEmpty(sendVO.getData())){
                    msgDataList =  mapperFacade.mapAsList(sendVO.getData(),WxMaSubscribeMessage.MsgData.class);
                }
                wxMaSubscribeMessage.setData(msgDataList);
                wxMaSubscribeMessage.setToUser(userRecord.getOpenId());
                wxMaSubscribeMessage.setTemplateId(tmessage.getTemplateCode());
                if (StrUtil.isNotBlank(sendVO.getPage())) {
                    wxMaSubscribeMessage.setPage(sendVO.getPage());
                }
                sendMessage(wxMaSubscribeMessage);

                //更新订阅记录发送状态
                weixinMaSubscriptUserRecordMapper.sendMessage(userRecord.getId());
            }
        }

    }

    /**
     * 获取发送模板内容
     * @param sendType
     * @return
     */
    @Override
    public ServerResponseEntity<WeixinMaSubscriptTmessageVO> getSubscriptTmessage(Integer sendType) {
        WeixinMaSubscriptTmessageVO tmessageVO = weixinMaSubscriptTmessageMapper.getBySendType(sendType);
        tmessageVO.setTmessageValues(weixinMaSubscriptTmessageValueMapper.getByTmessageId(tmessageVO.getId()));
        return ServerResponseEntity.success(tmessageVO);
    }

    /**
     * 执行消息发送
     * @param message
     */
    private void sendMessage(WxMaSubscribeMessage message){
        if(StrUtil.isEmpty(message.getToUser())){
            log.info("小程序订阅消息接收者不允许为空。");
            return;
        }
        if(StrUtil.isEmpty(message.getTemplateId())){
            log.info("小程序订阅消息模版id不允许为空。");
            return;
        }
        try {
            wxConfig.getWxMaService().getSubscribeService().sendSubscribeMsg(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        //TODO 发送成功后是否需要写日志表
    }
}
