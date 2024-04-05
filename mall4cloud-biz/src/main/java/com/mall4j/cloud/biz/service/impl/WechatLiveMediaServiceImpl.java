package com.mall4j.cloud.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.wx.wx.api.live.LiveApi;
import com.mall4j.cloud.biz.wx.wx.api.live.SpuApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.live.LiveMediaResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.MediaResponse;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.common.exception.LuckException;
import io.seata.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
@Slf4j
public class WechatLiveMediaServiceImpl implements WechatLiveMediaService {

    @Autowired
    private SpuApi spuApi;

    @Autowired
    private LiveApi liveApi;

    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    @Autowired
    private WxConfig wxConfig;

    /**
     * 上传图片
     *
     * @param imgUrl
     * @return 图片的临时url
     */
    @Override
    public String uploadImage(String imgUrl) {
        if (StringUtils.isBlank(imgUrl)) {
            return null;
        }

        if (imgUrl.startsWith("/")) {
            imgUrl = imgDomain + imgUrl;
        }
        MediaResponse mediaResponse = spuApi.imgUpload(wxConfig.getWxMaToken(), 1, 1, imgUrl);
        if (mediaResponse != null && mediaResponse.getErrcode() == 0) {
            return mediaResponse.getImgInfo().getTempImgUrl();
        }
        log.error("上传图片失败, rep={}, res={}", imgUrl, JSON.toJSONString(mediaResponse));
        throw new LuckException("上传图片失败");
    }

    /**
     * 新增临时素材
     *
     * @param imgUrl 地址
     * @return 图片的临时url mediaId
     */
    @Override
    public String getImageMediaId(String imgUrl) {
        if (StringUtils.isBlank(imgUrl)) {
            return null;
        }

        if (imgUrl.startsWith("/")) {
            imgUrl = imgDomain + imgUrl;
        }
        log.info("imgUrl = {}", imgUrl);
        byte[] bytes = null;
        String imageType = "png";
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imgUrl));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (imgUrl.matches(".+\\.\\w{3,4}$")) {
                imageType = imgUrl.replaceAll(".+\\.(\\w{3,4})$", "$1");
            }
            ImageIO.write(bufferedImage, imageType, baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            throw new LuckException("图片丢失，请重新上传图片");
        }


        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), bytes);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("media", "d." + imageType, requestBody);

        LiveMediaResponse mediaResponse = liveApi.upload(wxConfig.getWxMaToken(), "image", fileToUpload);
        if (mediaResponse != null && mediaResponse.getErrcode() == null) {
            return mediaResponse.getMediaId();
        }

        log.error("新增临时素材失败, rep={}, res={}", imgUrl, JSON.toJSONString(mediaResponse));
        throw new LuckException("新增临时素材失败");
    }
}
