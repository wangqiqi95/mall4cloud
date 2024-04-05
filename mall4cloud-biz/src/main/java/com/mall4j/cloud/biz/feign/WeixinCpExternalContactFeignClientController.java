package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.CpChatAddMsgTemplateDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.ExtendWxCpMsgTemplateDTO;
import com.mall4j.cloud.api.biz.feign.WeixinCpExternalContactFeignClient;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgTaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeixinCpExternalContactFeignClientController implements WeixinCpExternalContactFeignClient {

    @Autowired
    private WeixinCpExternalManager weixinCpExternalManager;

    @Override
    public ServerResponseEntity<WxCpMsgTemplateAddResult> addExternalContactMsgTemplate(String wxCpMsgTemplate) {
        return weixinCpExternalManager.addMsgTemplate(wxCpMsgTemplate);
    }

    @Override
    public ServerResponseEntity<WxCpMsgTemplateAddResult> addChatMsgTemplate(CpChatAddMsgTemplateDTO dto) {
        return weixinCpExternalManager.addChatMsgTemplate(dto);
    }

    @Override
    public ServerResponseEntity cancelExternalContactMsgTemplate(String msgId) {

        try {
            WxCpGroupMsgTaskResult wxCpGroupMsgTaskResult = weixinCpExternalManager.cancelGroupmsgSend(msgId);
            if (wxCpGroupMsgTaskResult.getErrmsg().equals("ok")){
                return ServerResponseEntity.success();
            }
            return ServerResponseEntity.showFailMsg(wxCpGroupMsgTaskResult.getErrmsg());
        }catch (Exception e){
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }
}
