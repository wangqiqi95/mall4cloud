package com.mall4j.cloud.biz.service.channels;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetcomplaintorderResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcAftersaleApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EcAftersaleService {
    @Autowired
    EcAftersaleApi ecAftersaleApi;
    @Autowired
    WxConfig wxConfig;

    /**
     * 获取售后单详情
     * @param aftersaleId
     * @return
     */
    public EcGetaftersaleorderResponse getById(Long aftersaleId){
        EcGetaftersaleorderRequest request = new EcGetaftersaleorderRequest();
        request.setAfter_sale_order_id(StrUtil.toString(aftersaleId));
        EcGetaftersaleorderResponse response = ecAftersaleApi.getaftersaleorder(wxConfig.getWxEcToken(),request);
        log.info("查询售后详情执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("视频号获取售后单详情失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }

    /**
     * 同意售后
     * @param aftersaleId
     * @param address_id
     * @return
     */
    public EcBaseResponse acceptapply(Long aftersaleId,Long address_id){
        EcAftersaleAcceptapplyRequest request = new EcAftersaleAcceptapplyRequest();
        request.setAfter_sale_order_id(aftersaleId);
        request.setAddress_id(address_id);
        EcBaseResponse response = ecAftersaleApi.acceptapply(wxConfig.getWxEcToken(),request);
        log.info("同意售后执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("视频号同意售后售后失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }

    /**
     * 拒绝售后
     * @param aftersaleId
     * @param reject_reason
     * @return
     */
    public EcBaseResponse rejectapply(Long aftersaleId,String reject_reason){
        EcAftersaleRejectapplyRequest request = new EcAftersaleRejectapplyRequest();
        request.setAfter_sale_order_id(aftersaleId);
        request.setReject_reason(reject_reason);
        EcBaseResponse response = ecAftersaleApi.rejectapply(wxConfig.getWxEcToken(),request);
        log.info("拒绝售后执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("视频号拒绝售后失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }

    /**
     * 上传退款凭证
     * @param request
     * @return
     */
    public EcBaseResponse uploadrefundcertificate(EcuploadrefundcertificateRequest request){
        EcBaseResponse response = ecAftersaleApi.uploadrefundcertificate(wxConfig.getWxEcToken(),request);
        log.info("上传退款凭证执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("视频号拒绝售后失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }



    /**
     * 商家补充纠纷单留言
     * @param request
     * @return
     */
    public EcBaseResponse addcomplaintmaterial(EcAddcomplaintmaterialReqeust request){
        EcBaseResponse response = ecAftersaleApi.addcomplaintmaterial(wxConfig.getWxEcToken(),request);
        log.info("商家补充纠纷单留言执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("商家补充纠纷单留言失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }

    /**
     * 商家举证
     * @param request
     * @return
     */
    public EcBaseResponse addcomplaintproof(EcAddcomplaintproofRequest request){
        EcBaseResponse response = ecAftersaleApi.addcomplaintproof(wxConfig.getWxEcToken(),request);
        log.info("商家举证执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("商家举证执行失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }

    /**
     * 获取纠纷单详情
     * @param complaintId
     * @return
     */
    public EcGetcomplaintorderResponse getcomplaintorder(Long complaintId){
        EcGetcomplaintorderReqeust request = new EcGetcomplaintorderReqeust();
        request.setComplaint_id(complaintId);
        EcGetcomplaintorderResponse response = ecAftersaleApi.getcomplaintorder(wxConfig.getWxEcToken(),request);
        log.info("获取纠纷单详情执行结束，执行参数:{},返回结果：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("获取纠纷单详情失败，失败原因:{}",response.getErrmsg()));
        }
        return response;
    }



}
