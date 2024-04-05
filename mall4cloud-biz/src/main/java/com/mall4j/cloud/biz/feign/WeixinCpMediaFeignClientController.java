package com.mall4j.cloud.biz.feign;



import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.feign.WeixinCpMediaFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.biz.manager.WeixinCpMediaManager;
import com.mall4j.cloud.biz.service.WechatUploadFileService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeixinCpMediaFeignClientController implements WeixinCpMediaFeignClient {

    @Autowired
    private WeixinCpMediaManager weixinCpMediaManager;

    @Autowired
    private WechatUploadFileService wechatUploadFileService;

    @Override
    public ServerResponseEntity<WeixinUploadMediaResultVO> uploadByUrlFile(UploadUrlMediaDTO uploadUrlMediaDTO) {

        try {
            log.info("UPLOAD URL FILE PARAM IS:{}", JSONObject.toJSONString(uploadUrlMediaDTO));
            WxMediaUploadResult wxMediaUploadResult = weixinCpMediaManager.uploadByUrlFile(uploadUrlMediaDTO.getMediaUrl(),
                    uploadUrlMediaDTO.getMediaType(), uploadUrlMediaDTO.getUrlFlag());

            WeixinUploadMediaResultVO weixinUploadMediaResultVO = new WeixinUploadMediaResultVO();
            weixinUploadMediaResultVO.setMediaId(wxMediaUploadResult.getMediaId());
            weixinUploadMediaResultVO.setMediaUrl(wxMediaUploadResult.getUrl());

            return ServerResponseEntity.success(weixinUploadMediaResultVO);
        }catch (Exception e){
           log.error("UPLOAD CP URL INPUTSTREAM MEDIA IS FAIL,MESSAGE IS {}",e);
           return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }


    public ServerResponseEntity<WeixinUploadMediaResultVO> uploadEventCodeFile(UploadUrlMediaDTO uploadUrlMediaDTO){

        log.info("UPLOAD MERGE IMAGE PARAM IS:{}", JSONObject.toJSONString(uploadUrlMediaDTO));

        return wechatUploadFileService.groupPushTaskMediaMergeAndUpload(uploadUrlMediaDTO);

    }

}
