package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;

import java.io.IOException;


public interface WechatUploadFileService {


    ServerResponseEntity<WeixinUploadMediaResultVO> groupPushTaskMediaMergeAndUpload(UploadUrlMediaDTO uploadUrlMediaDTO);


}
