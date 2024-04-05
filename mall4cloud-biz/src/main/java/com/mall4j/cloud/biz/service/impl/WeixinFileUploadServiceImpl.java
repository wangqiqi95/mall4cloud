package com.mall4j.cloud.biz.service.impl;

import cn.hutool.http.HttpUtil;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinFileUploadService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 微信素材文件上传
 *
 * @author gmq
 * @date 2022-2-11 10:21:40
 */
@Slf4j
@Service
public class WeixinFileUploadServiceImpl implements WeixinFileUploadService {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private WeixinWebAppService weixinWebAppService;

    @Override
    public WxMpMaterialUploadResult uploadImg(MultipartFile file, String appId) {
        try {
            File uploadFile=multipartFileToFile(file);
            if(uploadFile!=null){
                WxMpMaterial wxMpMaterial=new WxMpMaterial();
                wxMpMaterial.setFile(uploadFile);
                wxMpMaterial.setName(file.getOriginalFilename());
                WxMpMaterialUploadResult wxMpMaterialUploadResult=getWxMpService(appId).getMaterialService().materialFileUpload("image",wxMpMaterial);

                // 会在本地产生临时文件，用完后需要删除
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }
                return wxMpMaterialUploadResult;
            }
            return null;
        } catch (Exception e) {
            log.error("文件上传异常", e);
            throw new LuckException("文件上传失败");
        } finally {
            log.info("文件上传请求结束");
        }
    }

    @Override
    public WxMpMaterialUploadResult uploadImgUrl(String url, String appId) {
        try {
            String downPath=downloadImage(url);
            if(StringUtils.isEmpty(downPath)){
                return null;
            }
            File uploadFile=new File(downPath);
            if(uploadFile!=null){
                WxMpMaterial wxMpMaterial=new WxMpMaterial();
                wxMpMaterial.setFile(uploadFile);
                wxMpMaterial.setName(uploadFile.getName());
                WxMpMaterialUploadResult wxMpMaterialUploadResult=getWxMpService(appId).getMaterialService().materialFileUpload("image",wxMpMaterial);
                // 会在本地产生临时文件，用完后需要删除
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }

                return wxMpMaterialUploadResult;
            }
            return null;
        } catch (Exception e) {
            log.error("文件上传异常", e);
            throw new LuckException("文件上传失败");
        } finally {
            log.info("文件上传请求结束");
        }
    }


    private WxMpService getWxMpService(String appId){
        WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(appId);
        WxMp wxMp=new WxMp();
        wxMp.setAppId(weixinWebApp.getWeixinAppId());
        wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
        return wxConfig.getWxMpService(wxMp);
    }

    public static String downloadImage(String fileUrl ) {
        long l = 0L;
        String path = null;
        String staticAndMksDir = null;
        if (fileUrl != null) {
            //下载时文件名称
            String fileName = System.currentTimeMillis()+".jpg";
            try {
                String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                staticAndMksDir = "/opt/skechers/temp/wxaterial/img"+ File.separator+time;
                new File(staticAndMksDir).mkdir();
                path = staticAndMksDir+File.separator +fileName;
                HttpUtil.downloadFile(fileUrl, path);
            } catch (Exception e) {
                log.info(""+e.getMessage());
            } finally {

            }
        }
        log.info("--->path："+path);
        return path;
    }

    /**
     * MultipartFile 文件转换 微信上传文件需要的 File
     * @param file
     * @return
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
