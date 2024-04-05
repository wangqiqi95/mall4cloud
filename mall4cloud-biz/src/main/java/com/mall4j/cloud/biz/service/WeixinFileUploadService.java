package com.mall4j.cloud.biz.service;

import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上传文件记录表
 *
 * @author gmq
 * @date 2022-2-11 10:21:40
 */
public interface WeixinFileUploadService {

	WxMpMaterialUploadResult uploadImg(MultipartFile file, String appId);

	WxMpMaterialUploadResult uploadImgUrl(String url, String appId);

}
