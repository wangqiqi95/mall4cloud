package com.mall4j.cloud.biz.manager;

import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.vo.cp.CpWxMediaUploadResult;
import com.mall4j.cloud.biz.wx.cp.utils.QiWeiQRUtil;
import com.mall4j.cloud.biz.wx.wx.util.QrCodeUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 引流页面: 自动拉群、群活码、标签建群
 */
@Slf4j
@RefreshScope
@Component
public class DrainageUrlManager {

    @Value("${scrm.biz.drainageUrl}")
    private String DRAINAGEURL;

    @Value("${scrm.biz.appendDomain}")
    private boolean appendDomain=false;

    @Autowired
    private DomainConfig domainConfig;

    /**
     *
     * @param state 渠道唯一参数
     * @return
     */
    public  ServerResponseEntity<CpWxMediaUploadResult> getDrainageUrlQrCode(String state,String logo){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName="tmpImg_";
            if(StrUtil.isNotEmpty(logo) && appendDomain){
                logo=domainConfig.getDomain()+logo;
            }
            String drainageurl=DRAINAGEURL.replace("STATE",state);
            log.info("---getDrainageUrlQrCode: {} LOGO：{}",drainageurl,logo);
            File file = File.createTempFile(fileName+dateFormat.format(new Date()),".png");
            ImageIO.write(QrCodeUtil.createQrCode(drainageurl, logo), QrCodeUtil.FORMAT, file);
            String drainageUrl = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().uploadImg(file);

            WxMediaUploadResult wxMediaUploadResult= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                    .getMediaService().upload(WxMaConstants.MediaType.IMAGE,file);

            CpWxMediaUploadResult result=new CpWxMediaUploadResult();
            result.setDrainageUrl(drainageUrl);
            result.setDrainagePath(drainageurl);
//            result.setUrl(wxMediaUploadResult.getUrl());
            result.setUrl(drainageUrl);
            result.setMediaId(wxMediaUploadResult.getMediaId());
            result.setThumbMediaId(wxMediaUploadResult.getThumbMediaId());
            result.setType(wxMediaUploadResult.getType());
            log.info("--生成引流二维码成功：{}", JSON.toJSONString(result));
            // 删除临时文件
            if (file.exists()) {
                file.delete();
            }
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.info("生成引流二维码失败 {}",e);
            throw new LuckException("生成引流二维码失败");
        }
    }

    public ServerResponseEntity<String> insertQrCodeLogo(String qrCode, String logo){
        try {
            if(StrUtil.isNotEmpty(logo) && appendDomain){
                logo=domainConfig.getDomain()+logo;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            BufferedImage bufferedImage= QiWeiQRUtil.insertImageLogo(qrCode,logo);
            String fileName="tmpImg_";
            File file = File.createTempFile(fileName+dateFormat.format(new Date()),".png");
            ImageIO.write(bufferedImage, QrCodeUtil.FORMAT, file);
            String qrCodeUrl = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().uploadImg(file);
            return ServerResponseEntity.success(qrCodeUrl);
        }catch (Exception e){
            log.error("--downLoadImageLogo--{}",e);
            throw new LuckException("生成引流二维码失败");
        }
    }



}
