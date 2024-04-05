package com.mall4j.cloud.biz.service.channels;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcStore;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddresscodeRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcUploadImgRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAddresscodeResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetStoreInfoResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcUploadResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcBasicsApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.Assert;
import io.seata.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class EcBasicsService {
    @Autowired
    EcBasicsApi ecBasicsApi;
    @Autowired
    WxConfig wxConfig;

    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    /**
     * 本地图片转换为视频号4.0 url地址
     * @param url
     * @return
     */
    public String convertImgUrlToChannelsImgUrl(String url){
        EcUploadImgRequest request = new EcUploadImgRequest();
        //TODO 这里视频地址前缀测试和生产环境不一致
        if (StringUtils.isBlank(url)) {
            return null;
        }
        /**
         * 有多重图片格式 `/` 表示需要单独拼接oss地址
         * `https` 表示不需要拼接
         */
        if (url.startsWith("//") ){
            url = "https:" + url;
        } else if (url.startsWith("/")) {
            url = imgDomain + url;
        }
        request.setImg_url(url);
        log.info("EcBasicsService.convertImgUrlToChannelsImgUrl 执行参数：{}",JSONObject.toJSONString(request));
        EcUploadResponse ecUploadResponse = ecBasicsApi.upload(wxConfig.getWxEcToken(),1,1,request);
        log.info("EcBasicsService.convertImgUrlToChannelsImgUrl 执行参数：{},返回结果：{}",JSONObject.toJSONString(request), JSONObject.toJSONString(ecUploadResponse));
        if(ecUploadResponse == null){
            Assert.faild("调用微信视频号4.0 图片地址转换失败");
        }
        if (ecUploadResponse.getErrcode() != 0){
            // 如果是图片获取原图失败就选择跳过这张图片,不进行异常处理
            if (Objects.equals(ecUploadResponse.getErrcode(), 10020057)) {
                return null;
            } else {
                Assert.faild(ecUploadResponse.getErrmsg());
            }
        }
        return ecUploadResponse.getPic_file().getImg_url();
    }

    /**
     * 获取地址编码
     * @param addr_code
     * @return
     */
    public EcAddresscodeResponse getAddresscode(String addr_code){
        EcAddresscodeRequest ecAddresscodeRequest = new EcAddresscodeRequest();
        ecAddresscodeRequest.setAddr_code(addr_code);
        log.info("EcBasicsService.getAddresscode 执行参数：{}",JSONObject.toJSONString(ecAddresscodeRequest));
        EcAddresscodeResponse response =ecBasicsApi.getAddresscode(wxConfig.getWxMaToken(),ecAddresscodeRequest);
        log.info("EcBasicsService.getAddresscode 执行参数：{},返回结果：{}",JSONObject.toJSONString(ecAddresscodeRequest), JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0 ){
            Assert.faild(response.getErrmsg());
        }
        return response;
    }

    /**
     * 获取店铺基础信息
     */
    public EcStore getBasicsInfo(){
        EcGetStoreInfoResponse response = ecBasicsApi.getBasicsInfo(wxConfig.getWxMaToken());
        log.info("EcBasicsService.getAddresscode,返回结果：{}",JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0 ){
            Assert.faild(response.getErrmsg());
        }
        return response.getInfo();
    }

    public String getImgByMediaid(String mediaid){
        return "";
    }
}
