package com.mall4j.cloud.api.biz.feign;


import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;


/**
 * @author cl
 * @date 2021/08/13
 */
@FeignClient(value = "mall4cloud-biz",contextId = "qrcodeTicket")
public interface QrcodeFeignClient {

    /**
     * 小程序短链生成
     * @param path
     * @param isExpire
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/wxa/url/generateUrlLink")
    ServerResponseEntity<String> generateUrlLink(@ApiParam(value = "小程序页面路径", required = true)
                                               @RequestParam("path")String path,
                                               @ApiParam(value = "到期失效：true，永久有效：false", required = true)
                                               @RequestParam("isExpire")boolean isExpire,
                                                 @ApiParam(value = "携带参数", required = false)
                                                 @RequestParam("id")String id,
                                                 @ApiParam(value = "到期失效的 URL Link 的失效时间，为 Unix 时间戳", required = false)
                                                 @RequestParam("expireTime")Integer expireTime);

    /**
     * 小程序短链生成(链接转二维码，二维码图片base64编码)
     * @param path
     * @param isExpire
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/wxa/url/qrcodeUrlLink")
    ServerResponseEntity<String> generateQrcodeUrlLink(@ApiParam(value = "小程序页面路径", required = true)
                                                 @RequestParam("path")String path,
                                                 @ApiParam(value = "到期失效：true，永久有效：false", required = true)
                                                 @RequestParam("isExpire")boolean isExpire,
                                                 @ApiParam(value = "携带参数", required = false)
                                                 @RequestParam("id")String id,
                                               @ApiParam(value = "二维码尺寸(默认300*300)", required = false)
                                               @RequestParam("qrCodeSize")Integer qrCodeSize,
                                               @ApiParam(value = "到期失效的 URL Link 的失效时间，为 Unix 时间戳", required = false)
                                               @RequestParam("expireTime")Integer expireTime);


    /**
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
     * 小程序太阳码（返回base64图片）
     * @param path
     * @param scene
     * @param width
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxa/url/wxaCodeBaseImg")
    ServerResponseEntity<String> getWxaCodeBaseImg(@ApiParam(value = "参数", required = true)
                                                   @RequestParam("scene")String scene,
                                                   @ApiParam(value = "小程序页面路径", required = true)
                                                   @RequestParam("path")String path,
                                                   @ApiParam(value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false)
                                                   @RequestParam("width")Integer width);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxa/url/getWxaCodeUrl")
    ServerResponseEntity<String> getWxaCodeUrl(@ApiParam(value = "参数", required = true)
                                                   @RequestParam("scene")String scene,
                                                   @ApiParam(value = "门店id", required = false)
                                                   @RequestParam("storeId")Long storeId,
                                                   @ApiParam(value = "小程序页面路径", required = true)
                                                   @RequestParam("path")String path,
                                                   @ApiParam(value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false)
                                                   @RequestParam("width")Integer width);


    /**
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
     *小程序太阳码（本地文件路径）
     * @param path
     * @param scene
     * @param width
     * @return 返回结果为：本地文件 File路径，文件使用完建议删除避免占用磁盘空间
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxa/url/getWxaCodeFile")
    ServerResponseEntity<File> getWxaCodeFile(@ApiParam(value = "参数", required = true)
                                                   @RequestParam("scene")String scene,
                                              @ApiParam(value = "小程序页面路径", required = true)
                                                   @RequestParam("path")String path,
                                              @ApiParam(value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false)
                                                   @RequestParam("width")Integer width);
}
