package com.mall4j.cloud.biz.feign;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcDeliveryProductInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcFreightProductInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcProductInfo;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintmaterialReqeust;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintproofRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcDeliverySendRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcuploadrefundcertificateRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetcomplaintorderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.biz.service.WechatLiveLogisticService;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import com.mall4j.cloud.biz.service.channels.ChannelsAddressService;
import com.mall4j.cloud.biz.service.channels.EcAftersaleService;
import com.mall4j.cloud.biz.service.channels.EcOrderService;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ChannlesFeignClientController implements ChannlesFeignClient {

    @Autowired
    EcAftersaleService ecAftersaleService;
    @Autowired
    EcOrderService ecOrderService;
    @Autowired
    WechatLiveLogisticService wechatLiveLogisticService;
    @Autowired
    private ChannelsAddressService channelsAddressService;

    @Override
    public ServerResponseEntity<EcOrderResponse> getOrder(Long orderId) {
        return ServerResponseEntity.success(ecOrderService.get(StrUtil.toString(orderId)));
    }

    @Override
    public ServerResponseEntity<EcGetaftersaleorderResponse> getEcaftersale(Long refundId) {
        return ServerResponseEntity.success(ecAftersaleService.getById(refundId));
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> ecaftersaleAcceptrefund(Long refundId) {
        return ServerResponseEntity.success(ecAftersaleService.acceptapply(refundId,null));
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> ecaftersaleAcceptreturn(Long refundId, Long addressId) {
        return ServerResponseEntity.success(ecAftersaleService.acceptapply(refundId,addressId));
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> ecaftersaleReject(Long refundId, String rejectReason) {
        return ServerResponseEntity.success(ecAftersaleService.rejectapply(refundId,rejectReason));
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> addcomplaintmaterial(EcAddcomplaintmaterialReqeust reqeust) {
        return ServerResponseEntity.success(ecAftersaleService.addcomplaintmaterial(reqeust));
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> addcomplaintproof(EcAddcomplaintproofRequest request) {
        return ServerResponseEntity.success(ecAftersaleService.addcomplaintproof(request));
    }

    @Override
    public ServerResponseEntity<EcGetcomplaintorderResponse> getcomplaintorder(Long complaintId) {
        return ServerResponseEntity.success(ecAftersaleService.getcomplaintorder(complaintId));
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> uploadrefundcertificate(EcuploadrefundcertificateRequest request) {
        return ServerResponseEntity.success(ecAftersaleService.uploadrefundcertificate(request));
    }

    @Override
    public ServerResponseEntity<String> getChannelsDeliveryCodeByDeliveryId(Long deliveryId) {
        LiveLogisticsVO byDeliveryId = wechatLiveLogisticService.getByDeliveryId(deliveryId);
        //数据如果未维护传OTHER
        String companyCode = StrUtil.isEmpty(byDeliveryId.getDeliveryCompanyId())?"OTHER":byDeliveryId.getLogisticsCode();
        return ServerResponseEntity.success(companyCode);
    }

    @Override
    public ServerResponseEntity<EcBaseResponse> deliverysend(EcDeliverySendRequest request) {
        log.info("视频号40发货，sku过滤前参数：{}", JSONObject.toJSONString(request));


        EcBaseResponse response = ecOrderService.deliverysend(request);
        return ServerResponseEntity.success(response);
    }
    
    @Override
    public ServerResponseEntity<ChannelsAddressVO> getDefaultAddress() {
        return ServerResponseEntity.success(channelsAddressService.getDefaultAddress());
    }

    @Override
    public ServerResponseEntity<ChannelsAddressVO> getAddressById(Long id) {
        return ServerResponseEntity.success(channelsAddressService.getAddressById(id));
    }
}
