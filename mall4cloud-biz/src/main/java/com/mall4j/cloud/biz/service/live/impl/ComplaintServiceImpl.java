package com.mall4j.cloud.biz.service.live.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintListRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintListResponse;
import com.mall4j.cloud.biz.wx.wx.api.live.ComplaintApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.service.live.ComplaintService;
import com.mall4j.cloud.common.exception.Assert;
import com.qcloud.cos.utils.Jackson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Autowired
    ComplaintApi complaintApi;
    @Autowired
    WxConfig wxConfig;

    @Override
    public ComplaintListResponse list(ComplaintListRequest request) {
        log.info("查询视频号纠纷单列表，参数对象：{}", Jackson.toJsonString(request));
        String url = "https://api.weixin.qq.com/shop/complaint/get_list?access_token={}";
//        String token = "62_yt3pMtIWMUxmt4sbsNUszGOb787QWTvJth6gVv3egvg9ADjLoFqmzcpkPapi2lGtXIxMwmy-d7vlozjDx3M5I3OZwWwJTu2D9cohQ4VQBtRxbI4Wwk8yoWGRsMfyO1WTcopEiH3HxSpRb8ykGFPaAJAQPZ";
//        url = StrUtil.format(url,token);
        url = StrUtil.format(url,wxConfig.getWxMaToken());
        String result = HttpUtil.post(url,JSONObject.toJSONString(request));
        ComplaintListResponse complaintListResponse = null;
        if(StrUtil.isNotEmpty(result)){
            complaintListResponse = JSONObject.parseObject(result,ComplaintListResponse.class);
        }else{
            Assert.faild("查询视频号纠纷单列表返回数据异常。");
        }
        log.info("查询视频号纠纷单列表结束，参数对象：{}，返回对象{}", Jackson.toJsonString(request), Jackson.toJsonString(complaintListResponse));
        return complaintListResponse;
    }

    @Override
    public ComplaintDetailResponse detail(ComplaintDetailRequest request) {
        log.info("查询视频号纠纷单详情，参数对象：{}", Jackson.toJsonString(request));
//        String token = "62_yt3pMtIWMUxmt4sbsNUszGOb787QWTvJth6gVv3egvg9ADjLoFqmzcpkPapi2lGtXIxMwmy-d7vlozjDx3M5I3OZwWwJTu2D9cohQ4VQBtRxbI4Wwk8yoWGRsMfyO1WTcopEiH3HxSpRb8ykGFPaAJAQPZ";
//        ComplaintDetailResponse response = complaintApi.get(token,request);
        ComplaintDetailResponse response = complaintApi.get(wxConfig.getWxMaToken(),request);
        log.info("查询视频号纠纷单详情结束，参数对象：{}，返回对象{}", Jackson.toJsonString(request),Jackson.toJsonString(response));
        return response;
    }

    @Override
    public BaseResponse uploadMaterial(ComplaintUploadMaterialRequest request) {
        log.info("视频号上传纠纷凭证，参数对象：{}", Jackson.toJsonString(request));
        log.info("视频号上传纠纷凭证2，参数对象：{}", JSONObject.toJSONString(request));
//        BaseResponse response = complaintApi.uploadMaterial(wxConfig.getWxMaToken(),request);
        String url = "https://api.weixin.qq.com/shop/complaint/upload_material?access_token={}";
//        String token = "62_yt3pMtIWMUxmt4sbsNUszGOb787QWTvJth6gVv3egvg9ADjLoFqmzcpkPapi2lGtXIxMwmy-d7vlozjDx3M5I3OZwWwJTu2D9cohQ4VQBtRxbI4Wwk8yoWGRsMfyO1WTcopEiH3HxSpRb8ykGFPaAJAQPZ";
//        url = StrUtil.format(url,token);
        url = StrUtil.format(url,wxConfig.getWxMaToken());
        String result = HttpUtil.post(url,Jackson.toJsonString(request));
        BaseResponse response = null;
        if(StrUtil.isNotEmpty(result)){
            response = JSONObject.parseObject(result,BaseResponse.class);
        }else{
            Assert.faild("视频号上传纠纷凭证结束数据异常。");
        }
        log.info("视频号上传纠纷凭证结束，参数对象：{}，返回对象{}", Jackson.toJsonString(request),Jackson.toJsonString(response));
        return response;
    }


}
