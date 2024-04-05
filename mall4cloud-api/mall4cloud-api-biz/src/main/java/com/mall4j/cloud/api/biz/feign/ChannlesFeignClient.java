package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintmaterialReqeust;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintproofRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcDeliverySendRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcuploadrefundcertificateRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetcomplaintorderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 视频号 4。0
 */
@FeignClient(value = "mall4cloud-biz",contextId = "channels")
public interface ChannlesFeignClient {

    /**
     * 获取订单信息
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/order/get")
    ServerResponseEntity<EcOrderResponse> getOrder(@RequestParam("orderId")Long orderId);

    /**
     * 获取退单信息
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/ecaftersale/getEcaftersale")
    ServerResponseEntity<EcGetaftersaleorderResponse> getEcaftersale(@RequestParam("refundId")Long refundId);

    /**
     * 商家同意退款
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/ecaftersale/acceptrefund")
    ServerResponseEntity<EcBaseResponse> ecaftersaleAcceptrefund(@RequestParam("refundId")Long refundId);

    /**
     * 商家同意退货
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/ecaftersale/acceptreturn")
    ServerResponseEntity<EcBaseResponse> ecaftersaleAcceptreturn(@RequestParam("refundId")Long refundId,@RequestParam("addressId")Long addressId);

    /**
     * 商家拒绝售后
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/ecaftersale/reject")
    ServerResponseEntity<EcBaseResponse> ecaftersaleReject(@RequestParam("refundId")Long refundId,@RequestParam("rejectReason")String rejectReason);


    /**
     * 商家补充纠纷单留言
     * @return 成功or失败
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/aftersale/addcomplaintmaterial")
    ServerResponseEntity<EcBaseResponse> addcomplaintmaterial(@RequestBody EcAddcomplaintmaterialReqeust reqeust);

    /**
     * 商家举证
     * @return 成功or失败
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/aftersale/addcomplaintproof")
    ServerResponseEntity<EcBaseResponse> addcomplaintproof(@RequestBody EcAddcomplaintproofRequest request);

    /**
     * 获取纠纷单详情
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/aftersale/getcomplaintorder")
    ServerResponseEntity<EcGetcomplaintorderResponse> getcomplaintorder(@RequestParam("complaintId")Long complaintId);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/aftersale/uploadrefundcertificate")
    ServerResponseEntity<EcBaseResponse> uploadrefundcertificate(@RequestBody EcuploadrefundcertificateRequest request);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/order/deliverysend")
    ServerResponseEntity<EcBaseResponse> deliverysend(@RequestBody EcDeliverySendRequest request);

    /**
     * 获取视频号4。0物流公司
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/delivery/getChannelsDeliveryCodeByDeliveryId")
    ServerResponseEntity<String> getChannelsDeliveryCodeByDeliveryId(@RequestParam("deliveryId")Long deliveryId);

    /**
     * 获取视频号4.0默认退货地址
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/address/getdefaultaddress")
    ServerResponseEntity<ChannelsAddressVO> getDefaultAddress();
    /**
     * 获取视频号4.0退货地址
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/address/getAddressById")
    ServerResponseEntity<ChannelsAddressVO> getAddressById(@RequestParam("id")Long id);
}
