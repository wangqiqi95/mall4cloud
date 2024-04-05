package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintListRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintListResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintOrder;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.dto.platform.request.ComplaintOrderPageRequest;
import com.mall4j.cloud.order.dto.platform.response.ComplaintDetailSKXResponse;
import com.mall4j.cloud.order.dto.platform.response.ComplaintOrderSKX;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ComplaintOrderService {

    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRefundService orderRefundService;


    /**
     * 纠纷单分页查询
     * @param request
     * @return
     */
    public PageVO<ComplaintOrderSKX> pageVO(ComplaintOrderPageRequest request){
        /**
         * 组装查询视频号纠纷单查询对象
         */
        ComplaintListRequest complaintListRequest = new ComplaintListRequest();
        complaintListRequest.setLimit(request.getLimit());
        complaintListRequest.setOffset(request.getOffset());
        complaintListRequest.setStatus(request.getStatus());
        complaintListRequest.setType(request.getType());
        //如果传了订单编号，转换为视频号的订单编号
        if(StrUtil.isNotEmpty(request.getOrderNumber())){
            EsOrderBO esOrderBO =orderService.getEsOrderByOrderNumber(request.getOrderNumber());
            if(esOrderBO == null || esOrderBO.getOrderScore()!=2 ||esOrderBO.getWechatOrderId() == null){
                Assert.faild("当前订单号非视频号订单。");
            }
            complaintListRequest.setOrder_id(esOrderBO.getWechatOrderId());
        }
        //如果传了退单编号，转换为视频号的退单编号
        if(StrUtil.isNotEmpty(request.getRefundNumber())){
            OrderRefundVO orderRefundVO = orderRefundService.getByRefundNumber(request.getRefundNumber());
            if(orderRefundVO==null || orderRefundVO.getAftersaleId()==null){
                Assert.faild("当前退单号非视频号订单。");
            }
            complaintListRequest.setAfter_sale_order_id(orderRefundVO.getAftersaleId());
        }
        if(StrUtil.isNotEmpty(request.getCreteTime())){
            complaintListRequest.setBegin_create_time(DateUtil.parse(request.getCreteTime()).getTime());
        }
        if(StrUtil.isNotEmpty(request.getEndTime())){
            complaintListRequest.setBegin_create_time(DateUtil.parse(request.getEndTime()).getTime());
        }

        ServerResponseEntity<ComplaintListResponse> complaintListResponse = liveStoreClient.complaintList(complaintListRequest);
        if(complaintListResponse==null || complaintListResponse.isFail() || complaintListResponse.getData()==null){
            Assert.faild("查询纠纷单失败。");
        }


        /**
         * 转换查询结果对象
         */
        PageVO<ComplaintOrderSKX> pageVO = new PageVO<>();
        ComplaintListResponse complaintList = complaintListResponse.getData();
        if(complaintList.getTotal()<=0){
            pageVO.setTotal(0l);
            return pageVO;
        }

        if(CollUtil.isEmpty(complaintList.getOrders())){
            pageVO.setTotal(complaintList.getTotal());
            pageVO.setList(new ArrayList<ComplaintOrderSKX>());
            return pageVO;
        }
        List<ComplaintOrder> weChatOrders  = complaintList.getOrders();
        List<Long> aftersaleIdList = weChatOrders.stream().map(ComplaintOrder::getAfter_sale_order_id).collect(Collectors.toList());

        List<OrderRefundVO> refundVOS = orderRefundService.getByAftersaleIds(aftersaleIdList);
        Map<Long,OrderRefundVO> orderRefundVOMap = refundVOS.stream().collect(Collectors.toMap(OrderRefundVO::getAftersaleId, Function.identity(), (key1, key2) -> key2));

        List<Long> orderIdList = refundVOS.stream().map(OrderRefundVO::getOrderId).collect(Collectors.toList());
        List<EsOrderBO> esOrderBOS = orderService.getEsOrderByOrderIds(orderIdList);
        Map<Long,EsOrderBO> esOrderBOMap = esOrderBOS.stream().collect(Collectors.toMap(EsOrderBO::getOrderId, Function.identity(), (key1, key2) -> key2));

        List<ComplaintOrderSKX> complaintOrderSKXList = BeanUtil.copyToList(weChatOrders,ComplaintOrderSKX.class);

        for (ComplaintOrderSKX complaintOrderSKX : complaintOrderSKXList) {
            complaintOrderSKX.setRefundNumber(orderRefundVOMap.get(complaintOrderSKX.getAfter_sale_order_id()).getRefundNumber());
            complaintOrderSKX.setSkxOrderId(orderRefundVOMap.get(complaintOrderSKX.getAfter_sale_order_id()).getOrderId());
            complaintOrderSKX.setOrderNumber(esOrderBOMap.get(complaintOrderSKX.getSkxOrderId()).getOrderNumber());

            complaintOrderSKX.setComplaintUserIsRead(orderRefundVOMap.get(complaintOrderSKX.getAfter_sale_order_id()).getComplaintUserIsRead());
            complaintOrderSKX.setComplaintPlatformIsRead(orderRefundVOMap.get(complaintOrderSKX.getAfter_sale_order_id()).getComplaintPlatformIsRead());
        }

        pageVO.setTotal(complaintList.getTotal());
        pageVO.setList(complaintOrderSKXList);
        return pageVO;
    }

    /**
     * 查询纠纷单详情
     * @param complaintOrderId
     */
    public ComplaintDetailSKXResponse detail(Long complaintOrderId){

        ComplaintDetailRequest request = new ComplaintDetailRequest();
        request.setComplaint_order_id(complaintOrderId);
        ServerResponseEntity<ComplaintDetailResponse> complaintDetailResponse = liveStoreClient.complaintDetail(request);
        if(complaintDetailResponse==null || complaintDetailResponse.isFail() || complaintDetailResponse.getData()==null){
            Assert.faild("查询纠纷单详情失败。");
        }

        ComplaintDetailResponse complaintDetail = complaintDetailResponse.getData();

        ComplaintDetailSKXResponse skxResponse = new ComplaintDetailSKXResponse();
        BeanUtil.copyProperties(complaintDetail,skxResponse);

        OrderRefundVO orderRefundVO = orderRefundService.getByAftersaleId(complaintDetail.getOrder().getAfter_sale_order_id());
        EsOrderBO esOrderBO = orderService.getEsOrder(orderRefundVO.getOrderId());
        skxResponse.setRefundNumber(orderRefundVO.getRefundNumber());
        skxResponse.setOrderNumber(esOrderBO.getOrderNumber());
        return skxResponse;
    }

    /**
     * 上传纠纷凭证
     *
     * @param request
     */
    public void uploadMaterial(ComplaintUploadMaterialRequest request){
        liveStoreClient.uploadMaterial(request);
    }


}
