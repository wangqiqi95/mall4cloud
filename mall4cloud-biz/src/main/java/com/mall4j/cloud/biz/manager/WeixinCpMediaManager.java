package com.mall4j.cloud.biz.manager;

import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class WeixinCpMediaManager  {

    @Autowired
    private MinioFileManager minioFileManager;

    private static final String MIMO_PATH = "cp/pic/";

    //代表最大为10485760个字节（10M）长度
    private final int MAX_LENGTH = 10000*1024;


    public WxMediaUploadResult uploadByUrlFile(String urlPath, String type, boolean urlFlag) throws IOException, WxErrorException {
        log.info("uploadByUrlFile->urlPath:{} type:{} urlFlag:{}",urlPath,type,urlFlag);
        urlPath = urlPath.split("[?]")[0];
        String[] bb = urlPath.split("/");
        //得到最后一个分隔符后的名字
        String fileName = bb[bb.length - 1];
        URL urlfile = new URL(urlPath);
        if (urlfile.openConnection().getContentLength() > MAX_LENGTH){
            throw new LuckException("图片大小不能超过10M");
        }

        WxMediaUploadResult wxMediaUploadResult = new WxMediaUploadResult();
        InputStream inputStream = urlfile.openStream();
        if (!urlFlag){
            wxMediaUploadResult = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                    .getMediaService()
                    .upload(type, fileName, inputStream);
        }else {

            String url = "";
            if (type.equals("image")){

                //获取url文件类型（jpg、png）
                HttpURLConnection urlConnection = (HttpURLConnection) urlfile.openConnection();
                urlConnection.connect();
                BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
                String suffix = "";
                String fileType = "";
                if (Objects.nonNull(suffix = HttpURLConnection.guessContentTypeFromStream(bis))){
                    fileType = suffix.split("/")[1];
                }else {
                    fileType = "jpeg";
                }

                String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String mimoPath = MIMO_PATH+time+"/"+fileName+"."+fileType;

                url = minioFileManager.uploadFile(inputStream, mimoPath, fileType);
            }else {
                url = wxMediaUploadResult.getUrl();
            }


            wxMediaUploadResult.setUrl(url);
        }


        inputStream.close();

        return wxMediaUploadResult;
    }



//    /**
//     * 该方法仅适合群发任务设置特殊页面跳转的图片场景使用
//     *
//     * @param urlPath 图片url地址
//     * @param type media类型
//     * @param page 需要跳转的页面链接
//     * @param remark 水印文案
//     * */
//    public WxMediaUploadResult uploadEventCodeFile(String urlPath, String type, String page, String remark) throws IOException, WxErrorException {
//
//
//
//
//        eventInputStream.close();
//        sunCodeInputStream.close();
//        inputStream.close();
//
//        return wxMediaUploadResult;
//    }


//    public static void main(String[] args) throws IOException {
//        String url = "https://xcx-sit-uat.oss-cn-shanghai.aliyuncs.com/2023/06/09/8f5be06769ac4c2183185d845432e7e2";
//
//        url = url.split("[?]")[0];
//        String[] bb = url.split("/");
//        //得到最后一个分隔符后的名字
//        String fileName = bb[bb.length - 1];
//        URL urlfile = new URL(url);
//        //获取url文件类型（jpg、png）
//        HttpURLConnection urlConnection = (HttpURLConnection) urlfile.openConnection();
//        urlConnection.connect();
//        BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
//        String fileType = HttpURLConnection.guessContentTypeFromStream(bis).split("/")[1];
//
//    }


}
