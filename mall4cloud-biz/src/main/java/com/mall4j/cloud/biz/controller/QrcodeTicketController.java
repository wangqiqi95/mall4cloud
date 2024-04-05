package com.mall4j.cloud.biz.controller;

import cn.hutool.core.util.IdUtil;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.QrcodeTicket;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.biz.vo.QrcodeTicketVO;
import com.mall4j.cloud.common.constant.QRCodeType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author cl
 * @date 2021-08-17 16:00:13
 */
@Slf4j
@RestController
@RequestMapping(value = "/ua/qrcode_ticket")
@Api(tags="二维码数据")
public class QrcodeTicketController {

    @Autowired
    private QrcodeTicketService qrcodeTicketService;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 小程序二维码
     */
    @GetMapping("/mini_qrcode")
    @ApiOperation(value="小程序二维码获取", notes="获取小程序二维码，返回二维码图片流，小程序跳到二维码的页面之后，" +
            "需要根据获取的scene请求获取线上保存的content，为什么要这么麻烦，以为scene的内容有限，只能在数据库保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "要保存的内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型：1. 小程序团购商品 2.小程序分销商品二维码", required = true, dataType = "Integer")
    })
    public ServerResponseEntity<FileSystemResource> save(String content, Integer type) throws WxErrorException {

        String page;
        if(Objects.equals(type, QRCodeType.GROUP.value()) || Objects.equals(type, QRCodeType.DISTRIBUTION.value())) {
            page = "pages/prod/prod";
        } else {
            // 无法获取页面信息
            throw new LuckException("无法获取页面信息");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        map.put("content", content);
        String ticket = IdUtil.simpleUUID();
        QrcodeTicket qrcodeTicket = new QrcodeTicket();
        qrcodeTicket.setContent(Json.toJsonString(map));
        qrcodeTicket.setType(type);
        qrcodeTicket.setTicket(ticket);
        qrcodeTicket.setTicketUrl(page);
        qrcodeTicketService.save(qrcodeTicket);
        File file = wxConfig.getWxMaService().getQrcodeService().createWxaCodeUnlimit(ticket, page);
        return ServerResponseEntity
                .success(new FileSystemResource(file));
    }


    /**
     * 根据Ticket获取content
     */
    @GetMapping("/get_content")
    @ApiOperation(value="根据Ticket获取保存的内容", notes="小程序里的scene就是你要的Ticket咯")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ticket", value = "小程序里的scene就是你要的Ticket咯", required = true, dataType = "String")
    })
    public ServerResponseEntity<QrcodeTicketVO> getContent(String ticket) {

        QrcodeTicketVO qrcodeTicketVO = qrcodeTicketService.getByTicket(ticket);
        if (Objects.isNull(qrcodeTicketVO)) {
            // 二维码已过期
            throw new LuckException("二维码已过期");
        }
        if (qrcodeTicketVO.getExpireTime() !=null && qrcodeTicketVO.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new LuckException("二维码已过期");
        }
        return ServerResponseEntity.success(qrcodeTicketVO);
    }

    @GetMapping("/generateUrlLink")
    @ApiOperation(value="小程序短链生成", notes="小程序短链生成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isExpire", value = "到期失效：true，永久有效：false", required = true, dataType = "boolean"),
            @ApiImplicitParam(name = "expireTime", value = "到期失效的 URL Link 的失效时间，为 Unix 时间戳", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<String> generateUrlLink(String path,boolean isExpire,String id,Integer expireTime) {

        return qrcodeTicketService.generateUrlLink(path,isExpire,id,expireTime);
    }

    @GetMapping("/generateShortLink")
    @ApiOperation(value="小程序短链生成generateShortLink", notes="小程序短链生成generateShortLink")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "参数", required = true, dataType = "String")
    })
    public ServerResponseEntity<String> generateShortLink(String path,boolean isExpire,String id,Integer expireTime) {

        return qrcodeTicketService.generateShortLink(path,id);
    }

    @GetMapping("/generateQrcodeUrlLink")
    @ApiOperation(value="小程序短链生成(二维码)", notes="小程序短链生成(二维码)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isExpire", value = "到期失效：true，永久有效：false", required = true, dataType = "boolean"),
            @ApiImplicitParam(name = "qrCodeSize", value = "二维码尺寸(默认300*300)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "expireTime", value = "到期失效的 URL Link 的失效时间，为 Unix 时间戳", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<String> generateQrcodeUrlLink(String path,boolean isExpire,String id,Integer qrCodeSize,Integer expireTime) {

        return qrcodeTicketService.generateQrcodeUrlLink(path,isExpire,id,qrCodeSize,expireTime);
    }

    @GetMapping("/getWxaCodeBaseImg")
    @ApiOperation(value="生成小程序码(太阳码)Base64图片", notes="生成小程序码(太阳码)Base64图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "scene", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<String> getWxaCodeBaseImg(String path,String scene,Integer width) {
        scene = scene.replace("|", "&");
        log.info("path : {} scene : {} ", path, scene);
        return qrcodeTicketService.getWxaCodeBaseImg(scene,path,width);
    }

    @GetMapping("/getWxaCodeFile")
    @ApiOperation(value="生成小程序码(太阳码)文件", notes="生成小程序码(太阳码)文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "scene", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<File> getWxaCodeFile(String path,String scene,Integer width) {

        return qrcodeTicketService.getWxaCodeFile(scene,path,width);
    }


    @GetMapping("/downloadWxaCode")
    @ApiOperation(value="下载小程序码", notes="下载小程序码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "scene", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<Void> downloadWxaCode(String fileName, String path, String scene, @RequestParam(required = false) Integer width,
                                HttpServletResponse response) {
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("小程序码", "UTF-8")+ ".png");
            ServerResponseEntity<File> fileResp = qrcodeTicketService.getWxaCodeFile(scene,path,width);
            if (fileResp.isSuccess()) {
                InputStream inputStream = new FileInputStream(fileResp.getData());
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = bis.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
                outputStream.close();
                outputStream.flush();
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return ServerResponseEntity.success();
    }


    @GetMapping("/getWxaCodeHttpUrl")
    @ApiOperation(value="生成小程序码(太阳码Base64图片)http链接", notes="生成小程序码(太阳码Base64图片)http链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "门店id", required = false, dataType = "Long"),
            @ApiImplicitParam(name = "scene", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<String> getWxaCodeHttpUrl(String path,String scene,Integer width,Long storeId) {
        scene = scene.replace("|", "&");
        log.info("path : {} scene : {} ", path, scene);
        return qrcodeTicketService.getWxaCodeUrl(scene,path,width,storeId);
    }
}
