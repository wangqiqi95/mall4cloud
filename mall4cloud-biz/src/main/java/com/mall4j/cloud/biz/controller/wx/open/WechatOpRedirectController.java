package com.mall4j.cloud.biz.controller.wx.open;

import com.mall4j.cloud.biz.service.WechatOpService;
import com.mall4j.cloud.biz.vo.ComponentAccessTokenVo;
import com.mall4j.cloud.biz.vo.PreAuthCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Date 2021年12月30日, 0030 15:18
 * @Created by eury
 */
@Slf4j
@Controller
@RequestMapping("/ua/wechat/op")
@Api(tags = "微信开放平台服务")
public class WechatOpRedirectController {

    @Resource
    private WechatOpService wechatOpService;

    /**
     * 跳转页面，打开授权连接只能通过页面标签，直接放浏览器打开异常
     * 直接放浏览器打开异常：错误 请确认授权入口页所在域名，与授权后回调页所在域名相同，并且，此两者都必须与申请第三方平台时填写的授权发起页域名相同。授权入口页所在域名：空
     * 访问页面：
     * 通过连接访问授权连接，管理员扫码授权确认之后，授权页会自动跳转进入回调 URI，并在 URL 参数中返回授权码和过期时间(redirect_url?auth_code=xxx&expires_in=600)
     */
    @GetMapping("/redirectAuthUrl")
    @ApiOperation(value = "跳转扫码授权页面")
    public String index(Model model) {
//    public ModelAndView redirectAuthUrl(Model model) {
        log.info("【微信开放平台扫码授权跳转页面】");

        wechatOpService.getComponentAccessToken();

        wechatOpService.getPreAuthCode();

        String authUrl=wechatOpService.getScanCodeAuthUrl().getData().toString();
        model.addAttribute("authUrl",authUrl);

//        return new ModelAndView(new RedirectView(authUrl));

        return "hello";
    }


}
