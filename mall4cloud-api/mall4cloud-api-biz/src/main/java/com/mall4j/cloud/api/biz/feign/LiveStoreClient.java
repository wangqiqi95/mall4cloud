package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.DeliverySendRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.UploadCertificatesRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintListRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.EcaftersaleAddResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.OrderAddResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintListResponse;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lt
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-biz",contextId = "livestore")
public interface LiveStoreClient {

    /**
     * 商品信息变更 - 信息同步
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/product/info/change")
    ServerResponseEntity<Boolean> productInfoChange(@RequestParam("productId")String productId);

    /**
     * 订单信息添加
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/info/add")
    ServerResponseEntity<OrderAddResponse> orderInfoAdd(@RequestParam("orderId")Long orderId);

    /**
     * 订单信息取消
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/info/close")
    ServerResponseEntity<BaseResponse> orderClose(@RequestParam("orderId")Long orderId);

    /**
     * 订单确认收货
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/delivery/recieve")
    ServerResponseEntity<BaseResponse> orderRecieved(@RequestParam("orderId")Long orderId);

    /**
     * 订单发货
     * @return 成功or失败
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/delivery/send")
    ServerResponseEntity<BaseResponse> orderDeliverySend(@RequestBody DeliverySendRequest deliverySendRequest);

    /**
     * 订单信息变更 - 信息同步
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/info/change")
    ServerResponseEntity<Boolean> orderInfoChange(@RequestParam("orderId")String orderId);

    /**
     * 提交售后申请
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecaftersale/add")
    ServerResponseEntity<EcaftersaleAddResponse> ecaftersaleAdd(@RequestParam("refundId")Long refundId);

    /**
     * 取消售后申请
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecaftersale/cancel")
    ServerResponseEntity<BaseResponse> ecaftersaleCancel(@RequestParam("refundId")Long refundId);

    /**
     * 商家同意退款
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecaftersale/acceptrefund")
    ServerResponseEntity<BaseResponse> ecaftersaleAcceptrefund(@RequestParam("refundId")Long refundId);

    /**
     * 商家同意退货
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecaftersale/acceptreturn")
    ServerResponseEntity<BaseResponse> ecaftersaleAcceptreturn(@RequestParam("refundId")Long refundId);

    /**
     * 商家拒绝售后
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecaftersale/reject")
    ServerResponseEntity<BaseResponse> ecaftersaleReject(@RequestParam("refundId")Long refundId);

    /**
     * 售后信息变更 - 信息同步
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/aftersale/info/change")
    ServerResponseEntity<Boolean> aftersaleInfoChange(@RequestParam("orderId")String orderId);

    /**
     * 买家上传退货物流信息
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/aftersale/upload/logistics")
    ServerResponseEntity<BaseResponse> aftersaleUploadLogistics(@RequestParam("refundId")Long refundId);

    /**
     * 买家上传退货物流信息
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/getWechatDeliveryCodeByDeliveryId")
    ServerResponseEntity<String> getWechatDeliveryCodeByDeliveryId(@RequestParam("refundId")Long deliveryId);



    /**
     * 纠纷单列表
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/complaint/list")
    ServerResponseEntity<ComplaintListResponse> complaintList(@RequestBody ComplaintListRequest request);

    /**
     * 纠纷单详情
     * @return 成功or失败
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/complaint/detail")
    ServerResponseEntity<ComplaintDetailResponse> complaintDetail(@RequestBody ComplaintDetailRequest request);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/complaint/uploadMaterial")
    ServerResponseEntity<BaseResponse> uploadMaterial(@RequestBody ComplaintUploadMaterialRequest request);

    /**
     * 线下退款， 商家上传退款凭证
     * @param request
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecaftersale/uploadCertificates")
    ServerResponseEntity<BaseResponse> uploadCertificates(@RequestBody UploadCertificatesRequest request);

}
