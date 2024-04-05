package com.mall4j.cloud.biz.controller.app;

import com.mall4j.cloud.biz.service.WeixinShortlinkService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("AppWechatShortlinkController")
@RequestMapping("ma/wechat/shor/link")
@Api(tags = "小程序-小程序短链工具")
public class AppWeixinShortlinkController {
    @Autowired
    private WeixinShortlinkService weixinShortlinkService;

    @PostMapping("/view/short/link")
    @ApiOperation(value = "新增短链详情记录", notes = "新增短链详情记录")
    public ServerResponseEntity<String> saveShortLinkRecordItem(@RequestParam("shortLinkRecordId") String shortLinkRecordId, @RequestParam("code") String code) throws WxErrorException {
        return weixinShortlinkService.saveShortLinkRecordItem(shortLinkRecordId, code);
    }
}
