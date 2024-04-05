package com.mall4j.cloud.biz.service.channels;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandCancelRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandListRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandOneRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandStoreRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcPageRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAllBrandResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandListResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandOneResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandStoreResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.AuditResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcBrandApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EcBrandService {
    @Autowired
    EcBrandApi ecBrandApi;
    @Autowired
    WxConfig wxConfig;

    public EcAllBrandResponse all(EcPageRequest ecPageRequest){
        EcAllBrandResponse response = ecBrandApi.allBrand(ecPageRequest);
        return response;
    }
    
    public EcBrandStoreResponse brandAll(EcBrandStoreRequest ecBrandStoreRequest){
    
        log.info("调用微信接口获取视频号4.0品牌库列表参数对象：{}", JSONObject.toJSONString(ecBrandStoreRequest));
        EcBrandStoreResponse ecBrandStoreResponse = ecBrandApi.brandAll(wxConfig.getWxEcToken(), ecBrandStoreRequest);
        log.info("调用微信接口获取视频号4.0品牌库列表执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecBrandStoreRequest),JSONObject.toJSONString(ecBrandStoreResponse));
        if (ecBrandStoreResponse != null) {
            if (ecBrandStoreResponse.getErrcode() != 0) {
                throw new LuckException("获取视频号4.0品牌库列表异常, 错误信息:" + ecBrandStoreResponse.getErrmsg());
            }
        }
        return ecBrandStoreResponse;
    }
    
    
    public AuditResponse addBrand(EcBrandRequest ecBrandRequest) {
    
        log.info("提交视频号4.0品牌审核参数对象：{}", JSONObject.toJSONString(ecBrandRequest));
        AuditResponse auditResponse = ecBrandApi.addBrand(wxConfig.getWxEcToken(), ecBrandRequest);
        log.info("提交视频号4.0品牌审核执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecBrandRequest), JSONObject.toJSONString(auditResponse));
        if (auditResponse != null) {
            if (auditResponse.getErrcode() != 0) {
                throw new LuckException("提交审核品牌失败, 错误信息:" + auditResponse.getErrmsg());
            }
        }
        return auditResponse;
    }
    
    public AuditResponse updateBrand(EcBrandRequest ecBrandRequest){
    
        log.info("视频号4.0更新品牌参数对象：{}", JSONObject.toJSONString(ecBrandRequest));
        AuditResponse auditResponse = ecBrandApi.updateBrand(wxConfig.getWxEcToken(), ecBrandRequest);
        log.info("视频号4.0更新品牌执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecBrandRequest),JSONObject.toJSONString(auditResponse));
    
        if(auditResponse != null) {
            if (auditResponse.getErrcode() != 0) {
                log.error("视频号4.0更新品牌失败, req={}, res={}", JSON.toJSONString(ecBrandRequest), JSON.toJSONString(auditResponse));
                throw new LuckException("视频号4.0更新品牌失败, 错误信息:" + auditResponse.getErrmsg());
            }
        }
        return auditResponse;
    }
    
    
    public EcBaseResponse cancelBrand(EcBrandCancelRequest ecBrandCancelRequest){

        log.info("调用微信接口撤回品牌资质审核参数对象：{}", JSONObject.toJSONString(ecBrandCancelRequest));
        EcBaseResponse baseResponse = ecBrandApi.cancelBrand(wxConfig.getWxEcToken(), ecBrandCancelRequest);
        log.info("调用微信接口撤回品牌资质审核参数 req{}, res={}", JSON.toJSONString(ecBrandCancelRequest),JSON.toJSONString(baseResponse));
        if (baseResponse != null) {
            if (baseResponse.getErrcode() != 0) {
                log.error("调用微信接口撤回品牌资质审核参数异常 req{}, res={}", JSON.toJSONString(ecBrandCancelRequest), JSON.toJSONString(baseResponse));
                throw new LuckException("调用微信接口撤回品牌资质审核参数异常, 错误信息:" + baseResponse.getErrmsg());
            }
        }
        return  baseResponse;
    }
    
    
    public EcBrandOneResponse getBrand(EcBrandOneRequest ecBrandOneRequest){
        return ecBrandApi.getBrand(wxConfig.getWxEcToken(), ecBrandOneRequest);
    }
    
    public EcBrandListResponse getBrandList(EcBrandListRequest ecBrandListRequest){
    
        log.info("调用微信接口获取视频号4.0品牌资质申请列表参数对象：{}", JSONObject.toJSONString(ecBrandListRequest));
        EcBrandListResponse brandListResponse = ecBrandApi.getBrandList(wxConfig.getWxEcToken(), ecBrandListRequest);
        log.info("调用微信接口获取视频号4.0品牌资质申请列表执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecBrandListRequest),JSONObject.toJSONString(brandListResponse));
        if (brandListResponse != null) {
            if (brandListResponse.getErrcode() != 0) {
                log.error("调用微信接口获取视频号4.0品牌资质申请列表异常 req{}, res={}", JSON.toJSONString(ecBrandListRequest), JSON.toJSONString(brandListResponse));
                throw new LuckException("调用微信接口获取视频号4.0品牌资质申请列表异常, 错误信息:" + brandListResponse.getErrmsg());
            }
        }
        return brandListResponse;
    }
}
