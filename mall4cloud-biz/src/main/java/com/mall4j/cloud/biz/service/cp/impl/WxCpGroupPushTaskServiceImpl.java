package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgSendResultVO;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgTaskVO;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.service.cp.WxCpGroupPushTaskService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgSendResult;
import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgTaskResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-10 17:28
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxCpGroupPushTaskServiceImpl implements WxCpGroupPushTaskService {

    @Autowired
    private WeixinCpExternalManager weixinCpExternalManager;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 调用企业微信获取群发成员发送任务列表接口
     * @param msgId 群发消息的id
     * @param limit 返回的最大记录数
     * @param cursor 用于分页查询的游标
     * @return
     */
    @Override
    public ServerResponseEntity<WxCpGroupMsgTaskVO> getGroupMessageTask(String msgId, Integer limit, String cursor) {
        log.info("调用企业微信获取群发成员发送任务列表接口入参:msgId = {}, limit = {}, cursor = {}", msgId, limit, cursor);
        WxCpGroupMsgTaskVO wxCpGroupMsgTaskVO = new WxCpGroupMsgTaskVO();
        try {
            WxCpGroupMsgTaskResult groupMessageTask = weixinCpExternalManager.getGroupMessageTask(msgId, limit, cursor);
            BeanUtils.copyProperties(groupMessageTask, wxCpGroupMsgTaskVO);
            List<WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo> taskInfoList = mapperFacade.mapAsList(groupMessageTask.getTaskList(),
                    WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo.class);
            wxCpGroupMsgTaskVO.setTaskList(taskInfoList);
            log.info("调用企业微信获取群发成员发送任务列表接口出参:groupMessageTask = {}, wxCpGroupMsgTaskVO = {}", groupMessageTask, wxCpGroupMsgTaskVO);
        } catch (WxErrorException e) {
            log.info("调用企业微信API发生异常:{}", e.getMessage());
        }
        return ServerResponseEntity.success(wxCpGroupMsgTaskVO);
    }

    /**
     * 调用企业微信获取企业群发成员执行结果
     * @param msgId 群发消息的id
     * @param staffCpUserId 发送成员userid(发送者的企业微信ID)
     * @param limit 返回的最大记录数
     * @param cursor 用于分页查询的游标
     * @return
     */
    @Override
    public ServerResponseEntity<WxCpGroupMsgSendResultVO> getGroupMessageSendResult(String msgId, String staffCpUserId, Integer limit, String cursor) {
        log.info("调用企业微信获取企业群发成员执行结果接口入参:msgId = {}, staffCpUserId = {}, limit = {}, cursor = {}", msgId, staffCpUserId, limit, cursor);
        WxCpGroupMsgSendResultVO wxCpGroupMsgSendResultVO = new WxCpGroupMsgSendResultVO();
        try {
            WxCpGroupMsgSendResult groupMessageSendResult = weixinCpExternalManager.getGroupMessageSendResult(msgId, staffCpUserId, limit, cursor);
            BeanUtils.copyProperties(groupMessageSendResult, wxCpGroupMsgSendResultVO);
            List<WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> sendInfoList = mapperFacade.mapAsList(groupMessageSendResult.getSendList(),
                    WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo.class);
            wxCpGroupMsgSendResultVO.setSendList(sendInfoList);
            log.info("调用企业微信获取企业群发成员执行结果接口出参:groupMessageSendResult = {}, wxCpGroupMsgSendResultVO = {}", groupMessageSendResult, wxCpGroupMsgSendResultVO);
        } catch (WxErrorException e) {
            log.info("调用企业微信API发生异常:{}", e.getMessage());
        }
        return ServerResponseEntity.success(wxCpGroupMsgSendResultVO);
    }
}
