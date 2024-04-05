package com.mall4j.cloud.biz.controller.platform.wx;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.urllink.GenerateUrlLinkRequest;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.WeixinMaSubscriptTmessageDTO;
import com.mall4j.cloud.biz.dto.WeixinSubTmessageStatusDTO;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageOptionalValueService;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageService;
import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageOptionalValueVO;
import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageTypeVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.subscribemsg.CategoryData;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信订阅模版消息
 *
 * @luzhengxiang
 * @create 2022-03-05 11:51 AM
 **/
@RestController("weixinMaSubscriptTmessageController")
@RequestMapping("/p/weixin/miniapp/subcript/tmessage")
@Api(tags = "后台小程序订阅模版消息")
public class WeixinMaSubscriptTmessageController {

    @Autowired
    private WxConfig wxConfig;
    @Autowired
    WeixinMaSubscriptTmessageService weixinMaSubscriptTmessageService;
    @Autowired
    WeixinMaSubscriptTmessageOptionalValueService weixinMaSubscriptTmessageOptionalValueService;

    @GetMapping("/getList")
    @ApiOperation(value = "获取模板列表", notes = "获取模板列表")
    public ServerResponseEntity<List<WeixinMaSubscriptTmessageTypeVO>> getList(@RequestParam(value = "",required = false) String appId,
                                                                               @RequestParam(value = "",required = false) Integer businessType,
                                                                               @RequestParam(value = "",required = false) Integer sendType) {
        List<WeixinMaSubscriptTmessageTypeVO> weixinLinksucaiPage = weixinMaSubscriptTmessageService.getList(null,businessType,sendType);
        return ServerResponseEntity.success(weixinLinksucaiPage);
    }

    @GetMapping("/getTmessageValueOptions")
    @ApiOperation(value = "获取模板列表 可选参数列表", notes = "获取模板列表 可选参数列表")
    public ServerResponseEntity<List<WeixinMaSubscriptTmessageOptionalValueVO>> getTmessageValueOptions(@RequestParam(value = "",required = true) Integer typeId) {
        List<WeixinMaSubscriptTmessageOptionalValueVO> optionalValueVOS = weixinMaSubscriptTmessageOptionalValueService.getByTypeId(typeId);
        return ServerResponseEntity.success(optionalValueVOS);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存模板", notes = "保存模板")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinMaSubscriptTmessageDTO subscriptTmessageDTO) {
        weixinMaSubscriptTmessageService.saveTo(subscriptTmessageDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改模板", notes = "修改模板")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinMaSubscriptTmessageDTO subscriptTmessageDTO) {
        weixinMaSubscriptTmessageService.updateTo(subscriptTmessageDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "模板启用/禁用", notes = "模板启用/禁用")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody WeixinSubTmessageStatusDTO statusDTO) {
        weixinMaSubscriptTmessageService.updateStatus(statusDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除模板", notes = "删除模板")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinMaSubscriptTmessageService.deleteById(id);
        return ServerResponseEntity.success();
    }



    @GetMapping("/getCategory")
    @ApiOperation(value = "获取小程序账号的类目", notes = "获取小程序账号的类目")
    public ServerResponseEntity<List<CategoryData>> getCategory() {
        List<CategoryData> list = null;
        try {
            list = wxConfig.getWxMaService().getSubscribeService().getCategory();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/getTemplateList")
    @ApiOperation(value = "获取当前帐号下的个人模板列表(选取模板)", notes = "获取当前帐号下的个人模板列表(选取模板)")
    public ServerResponseEntity<List<TemplateInfo>> getTemplateList() {
        List<TemplateInfo> list = null;
        try {
            list = wxConfig.getWxMaService().getSubscribeService().getTemplateList();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/getPubTemplateTitleList")
    @ApiOperation(value = "获取帐号所属类目下的公共模板标题", notes = "获取帐号所属类目下的公共模板标题")
    public ServerResponseEntity<Void> getPubTemplateTitleList() {

        return ServerResponseEntity.success();
    }

    @GetMapping("/getUrllink")
    @ApiOperation(value = "获取可以在短信、邮件、网页、微信内等拉起小程序的url链接", notes = "获取可以在短信、邮件、网页、微信内等拉起小程序的url链接")
    public ServerResponseEntity<String> getTemplateList(@Param("url")String url) {
        String urllink = "";
        Assert.isNull(url,"url不允许为空");

        try {
            url= URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Assert.faild("url格式不正确。");
        }

        String[] urlStr = url.split("\\?");
        String path = url.split("\\?")[0];
        GenerateUrlLinkRequest linkRequest = GenerateUrlLinkRequest.builder().build();
        linkRequest.setPath(path);

        linkRequest.setExpireType(1);
        linkRequest.setExpireTime(179L);
        //如果有参数带上参数
        if(urlStr.length == 2){
            linkRequest.setQuery(url.split("\\?")[1]);
        }
        try {
            urllink = wxConfig.getWxMaService().getLinkService().generateUrlLink(linkRequest);
            urllink = StrUtil.removeAll(urllink,"\"");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        System.out.println(urllink);
        return ServerResponseEntity.success(urllink);
    }

//    public static void main(String[] args) {
//        String url = "\"https://wxaurl.cn/bkoj1c7K98e\"";
//        System.out.println(StrUtil.removeAll(url,"\""));
//    }



    /**
     * 参数demo
     * {
     *   "touser": "OPENID",
     *   "template_id": "TEMPLATE_ID",
     *   "page": "index",
     *   "miniprogram_state":"developer",
         *   "lang":"zh_CN",
     *   "data": {
     *       "number01": {
     *           "value": "339208499"
     *       },
     *       "date01": {
     *           "value": "2015年01月05日"
     *       },
     *       "site01": {
     *           "value": "TIT创意园"
     *       } ,
     *       "site02": {
     *           "value": "广州市新港中路397号"
     *       }
     *   }
     * }
     * @param message
     * @return
     */
    @PostMapping("/testSend")
    @ApiOperation(value = "测试发送微信小程序订阅消息", notes = "测试发送微信小程序订阅消息")
    public ServerResponseEntity<Void> testSend(@RequestBody WeixinMaSubscriptTmessageSendVO message) {
        ArrayList list = new ArrayList<>();
        list.add(message);
        weixinMaSubscriptTmessageService.sendMessageToUser(list);
        return ServerResponseEntity.success();
    }

    @GetMapping(value = "/getSubscriptTmessage")
    @ApiOperation(value = "获取发送模板内容", notes = "获取发送模板内容")
    public ServerResponseEntity<WeixinMaSubscriptTmessageVO> getSubscriptTmessage(@RequestParam(value = "sendType",required = true) Integer sendType){
        return weixinMaSubscriptTmessageService.getSubscriptTmessage(sendType);
    }

    @GetMapping(value = "/getWxMaOpenId")
    @ApiOperation(value = "获取小程序openId", notes = "获取小程序openId")
    public ServerResponseEntity<String> getWxMaOpenId(@RequestParam(value = "code",required = true) String code){
        try {
            WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(code);
            if (session != null) {
                String openId=session.getOpenid();
                String unionId=session.getUnionid();
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("openId",openId);
                jsonObject.put("unionId",unionId);
                return ServerResponseEntity.success(jsonObject.toJSONString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ServerResponseEntity.success();
    }
}
