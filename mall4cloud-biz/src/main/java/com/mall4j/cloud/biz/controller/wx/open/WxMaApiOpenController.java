package com.mall4j.cloud.biz.controller.wx.open;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.service.WxMaApiService;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.biz.task.WeixinMediaRefeshTask;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date 2021年12月30日, 0030 15:18
 * @Created by eury
 */
@Slf4j
@RestController
@RequestMapping("/ua/wechat/op")
@Api(tags = "微信-小程序相关")
public class WxMaApiOpenController {

    @Resource
    private WxMaApiService wxMaApiService;

    @Autowired
    private WeixinMediaRefeshTask weixinMediaRefeshTask;
    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private ConfigService configService;

    @GetMapping("/getPhoneNumber")
    @ApiOperation(value = "小程授权获取手机号", notes = "小程授权获取手机号")
    public ServerResponseEntity<String> getPhoneNumber(@RequestParam(value = "code",required = true) String code,
                                                       @RequestParam(value = "encryptedData",required = false) String encryptedData,
                                                       @RequestParam(value = "ivStr",required = false) String ivStr,
                                                       @ApiParam(value = "授权方式：1:新版 2:旧版",required = true)
                                                       @RequestParam(value = "authType",required = false,defaultValue ="1") Integer authType) {
        return wxMaApiService.getPhoneNumber(code,encryptedData,ivStr,authType);
    }

    @GetMapping("/ua/refreshWXMPMediaIdTask")
    @ApiOperation(value = "refreshWXMPMediaIdTask", notes = "refreshWXMPMediaIdTask")
    public ServerResponseEntity<String> refreshPicMediaIdTask() {
        weixinMediaRefeshTask.refreshPicMediaIdTask();
        return ServerResponseEntity.success();
    }

    @GetMapping("/ua/getByQiWeiUserDetail")
    @ApiOperation(value = "换取企微客户user详情", notes = "换取企微客户user详情")
    public ServerResponseEntity<WxCpExternalContactInfo> getByQiWeiUserId(String qiWeiUserId,String staffQiWeiUserId) {
        try {
            WxCpExternalContactInfo wxCpExternalContactInfo=null;
            List<String> externalcontactIds=wxCpService.getExternalContactService().listExternalContacts(staffQiWeiUserId);
            log.info("staffQiWeiUserId获取到全部客户列表：---> {}",externalcontactIds.toString());
            List<String> externalcontactId=externalcontactIds.stream().filter(external_userid -> external_userid.equals(qiWeiUserId)).collect(Collectors.toList());
            log.info("staffQiWeiUserId获取到当前包含客户{}列表：---> {}",qiWeiUserId,externalcontactId.toString());
            if(CollectionUtil.isNotEmpty(externalcontactId)){
                String cursor="";//上次请求返回的next_cursor【非必填 企微API文档2023/05/19更新】
                wxCpExternalContactInfo=wxCpService.getExternalContactService().getContactDetail(qiWeiUserId,cursor);
                log.info("staffQiWeiUserId获取到当前包含客户{}详情：---> {}", qiWeiUserId,JSON.toJSON(wxCpExternalContactInfo));
            }

            return ServerResponseEntity.success(wxCpExternalContactInfo);
        }catch (Exception e){
            throw new LuckException("换取userId失败");
        }
    }

}
