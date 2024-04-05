package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.util.IdUtil;
import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.manager.MinioFileManager;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.biz.service.WechatUploadFileService;
import com.mall4j.cloud.biz.util.MergeImageUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class WechatUploadFileServiceImpl implements WechatUploadFileService {

//    @Autowired
//    private WeixinCpMediaManager weixinCpMediaManager;

    @Autowired
    private QrcodeTicketService qrcodeTicketService;

    @Autowired
    private MinioFileManager minioFileManager;

    private static final String MIMO_PATH = "cp/pic/";

    private final int MAX_LENGTH = 10000*1024;

    @Override
    public ServerResponseEntity<WeixinUploadMediaResultVO> groupPushTaskMediaMergeAndUpload(UploadUrlMediaDTO uploadUrlMediaDTO) {

      try {
          // UserInfoInTokenBO tokenUser = AuthUserContext.get();


          //获取需要上传的图片的url
          String urlPath = uploadUrlMediaDTO.getMediaUrl();

          urlPath = urlPath.split("[?]")[0];
          String[] bb = urlPath.split("/");
          //得到最后一个分隔符后的名字
          String fileName = bb[bb.length - 1]+ IdUtil.simpleUUID();
          URL urlfile = new URL(urlPath);
          if (urlfile.openConnection().getContentLength() > MAX_LENGTH){
              throw new LuckException("图片大小不能超过10M");
          }

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

          //获取urlfile的输入流
          InputStream eventInputStream = urlfile.openStream();

          //将流读取成BufferedImage
          BufferedImage eventImage = ImageIO.read(eventInputStream);

          String path = uploadUrlMediaDTO.getPage().split("\\?")[0];
          String scene = uploadUrlMediaDTO.getPage().split("\\?")[1];

          //生成太阳码并获取其字节数组
          byte[] sunCodeByteArray = this.qrcodeTicketService.getWxaCodeByte(scene, path, eventImage.getWidth()/4).getData();

          //获取太阳码的输入流
          InputStream sunCodeInputStream = new ByteArrayInputStream(sunCodeByteArray);

          //将太阳码输入流读取成BufferedImage
          BufferedImage sunCodeImage = ImageIO.read(sunCodeInputStream);

          int gap = 20;

          int midGap = 20;

          //将两个图片垂直合并成一张新图片
          BufferedImage bufferedImage = MergeImageUtil.mergeImage(eventImage, sunCodeImage, false, false, true, gap,
                  Color.WHITE, true, midGap);

          //取第一个文件的宽度作为水印写入的x轴位置
          float x = bufferedImage.getWidth() * 0.025f;
          //取第一个文件的高度+太阳码图片的1/2高度作为水印写入的y轴位置
          float y = (eventImage.getHeight() + gap + midGap)+sunCodeImage.getHeight() * 0.5f;

          //添加水印
          bufferedImage = MergeImageUtil.mergeFont(bufferedImage, Color.black, new Font("思源宋体", Font.BOLD, 25), uploadUrlMediaDTO.getRemark(), x, y);

          WeixinUploadMediaResultVO weixinUploadMediaResultVO = new WeixinUploadMediaResultVO();
          //将最终的BufferedImage转成输入流
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bufferedImage, fileType, baos);
          InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

          if (!uploadUrlMediaDTO.getUrlFlag()){


              //上传图片到临时素材库
              WxMediaUploadResult wxMediaUploadResult = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                      .getMediaService()
                      .upload(uploadUrlMediaDTO.getMediaType(), fileName, inputStream);


              weixinUploadMediaResultVO.setMediaId(wxMediaUploadResult.getMediaId());


          }else {
//              File file = File.createTempFile(fileName,"jpg");
//              ImageIO.write(bufferedImage,"jpg", file);



              String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
              String mimoPath = MIMO_PATH+time+"/"+fileName+"."+fileType;

              String url = minioFileManager.uploadFile(inputStream, mimoPath, fileType);

//              String url = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId())
//                      .getMediaService()
//                      .uploadImg(file);
              weixinUploadMediaResultVO.setMediaUrl(url);
          }


          //关闭输入流
          eventInputStream.close();
          sunCodeInputStream.close();
          inputStream.close();

          return ServerResponseEntity.success(weixinUploadMediaResultVO);
      }catch (Exception e){
          log.error("MERGE IMAGE AND UPLOAD IS FAIL, MESSAGE IS :{}",e);
          return ServerResponseEntity.showFailMsg("MERGE IMAGE AND UPLOAD IS FAIL");
      }

    }
}
