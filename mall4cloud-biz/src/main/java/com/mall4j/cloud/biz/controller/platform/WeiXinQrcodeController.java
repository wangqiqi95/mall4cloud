package com.mall4j.cloud.biz.controller.platform;

import cn.hutool.core.util.IdUtil;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.QrcodeTicketSpuStoreDTO;
import com.mall4j.cloud.biz.model.QrcodeTicket;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.biz.vo.QrcodeTicketVO;
import com.mall4j.cloud.common.constant.QRCodeType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
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
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author cl
 * @date 2021-08-17 16:00:13
 */
@Slf4j
@RestController("platformWeiXinQrcodeController")
@RequestMapping(value = "/p/wx/qrcode")
@Api(tags="微信二维码")
public class WeiXinQrcodeController {

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


    @GetMapping("/getBase64InsertCode")
    @ApiOperation(value="生成小程序太阳码Base64图片(图片增加字符)", notes="生成小程序太阳码Base64图片(图片增加字符)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "小程序页面路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "scene", value = "参数", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "需要增加的字符", required = true, dataType = "String"),
            @ApiImplicitParam(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "width_y", value = "二维码的宽度，单位 px，最小 280px，最大 1280px(默认430px)", required = false, dataType = "Integer")
    })
    public ServerResponseEntity<String> getBase64InsertCode(String path,String scene,Integer width,Integer width_y,String code) {
        scene = scene.replace("|", "&");
        log.info("path : {} scene : {} ", path, scene);
        return qrcodeTicketService.getBase64InsertCode(scene,path,width,width_y,code);
    }

    @PostMapping("/getWxaCodeBySpuZip")
    @ApiOperation(value="小程序码(带商品货号)压缩包", notes="小程序码(带商品货号)压缩包")
    public ServerResponseEntity<String> getWxaCodeBySpuZip(@RequestBody QrcodeTicketSpuStoreDTO dto) {
        try {
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName=time+"_商品二维码_"+dto.getStoreCode();
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(fileName);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }
            dto.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(dto);
            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出门店信息: {} {}",e,e.getMessage());
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

}
