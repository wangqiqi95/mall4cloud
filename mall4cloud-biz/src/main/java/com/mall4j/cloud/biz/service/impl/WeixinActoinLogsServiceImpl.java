package com.mall4j.cloud.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.biz.mapper.WeixinWebAppMapper;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.vo.WeixinActoinLogsVO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinActoinLogs;
import com.mall4j.cloud.biz.mapper.WeixinActoinLogsMapper;
import com.mall4j.cloud.biz.service.WeixinActoinLogsService;
import com.mall4j.cloud.common.util.RandomUtil;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 公众号事件推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:16
 */
@Service
public class WeixinActoinLogsServiceImpl extends ServiceImpl<WeixinActoinLogsMapper,WeixinActoinLogs> implements WeixinActoinLogsService {

    @Autowired
    private WeixinActoinLogsMapper weixinActoinLogsMapper;
    @Autowired
    private WeixinWebAppMapper weixinWebAppMapper;

    @Override
    public PageVO<WeixinActoinLogs> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinActoinLogsMapper.list());
    }

    @Override
    public WeixinActoinLogs getById(Long id) {
        return weixinActoinLogsMapper.getById(id);
    }

    @Override
    public void saveTo(WeixinActoinLogs weixinActoinLogs) {
        this.save(weixinActoinLogs);
    }

    @Override
    public void updateTo(WeixinActoinLogs weixinActoinLogs) {
        this.updateById(weixinActoinLogs);
    }

    @Override
    public void deleteById(Long id) {
        weixinActoinLogsMapper.deleteById(id);
    }

    @Override
    public void saveWeixinActoinLogs(WeixinActoinLogsVO weixinActoinLogsVO) {

        WeixinActoinLogs weixinActoinLogs=new WeixinActoinLogs();
        BeanUtils.copyProperties(weixinActoinLogsVO,weixinActoinLogs);

        weixinActoinLogs.setCreateTime(new Date());
        weixinActoinLogs.setDelFlag(0);
        this.save(weixinActoinLogs);
    }

    @Async
    @Override
    public void saveWeixinActoinLogs(WxMpXmlMessage xmlMessage,String appId) {
        WeixinActoinLogs weixinActoinLogs=new WeixinActoinLogs();

        weixinActoinLogs.setAppId(appId);
        weixinActoinLogs.setToUserName(xmlMessage.getToUser());
        weixinActoinLogs.setFromUserName(xmlMessage.getFromUser());
        weixinActoinLogs.setPutTime(WechatUtils.formatDate(xmlMessage.getCreateTime().toString()));
        weixinActoinLogs.setMsgType(xmlMessage.getMsgType());
        weixinActoinLogs.setContent(xmlMessage.getContent());
        weixinActoinLogs.setMsgId(xmlMessage.getMsgId()!=null?String.valueOf(xmlMessage.getMsgId()):null);
        weixinActoinLogs.setPicUrl(xmlMessage.getPicUrl());
        weixinActoinLogs.setMediaId(xmlMessage.getMediaId());
        weixinActoinLogs.setThumbmediaid(xmlMessage.getThumbMediaId());
        weixinActoinLogs.setFormat(xmlMessage.getFormat());
        weixinActoinLogs.setLocatioinX(xmlMessage.getLocationX()!=null?String.valueOf(xmlMessage.getLocationX()):null);
        weixinActoinLogs.setLocatioinY(xmlMessage.getLocationY()!=null?String.valueOf(xmlMessage.getLocationY()):null);
        weixinActoinLogs.setPrecision(xmlMessage.getPrecision()!=null?String.valueOf(xmlMessage.getPrecision()):null);
        weixinActoinLogs.setScale(xmlMessage.getScale()!=null?String.valueOf(xmlMessage.getScale()):null);
        weixinActoinLogs.setLabel(xmlMessage.getLabel());
        weixinActoinLogs.setTitle(xmlMessage.getTitle());
        weixinActoinLogs.setRecognition(xmlMessage.getRecognition());
        weixinActoinLogs.setDescription(xmlMessage.getDescription());
        weixinActoinLogs.setUrl(xmlMessage.getUrl());
        weixinActoinLogs.setEvent(xmlMessage.getEvent());
        weixinActoinLogs.setEventKey(xmlMessage.getEventKey());
        weixinActoinLogs.setTicket(xmlMessage.getTicket());

        weixinActoinLogs.setCreateTime(new Date());
        weixinActoinLogs.setDelFlag(0);
        this.save(weixinActoinLogs);
    }

    @Override
    public List<UserWeixinAccountFollowDataListVo> fansDataByAppId(UserWeixinccountFollowSelectDTO dto) {
        WeixinWebApp weixinWebApp=weixinWebAppMapper.queryByWeixinAppid(dto.getAppId());
        dto.setAppId(weixinWebApp.getAppId());
        return weixinActoinLogsMapper.fansDataByAppId(dto);
    }
}
