package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintmaterialReqeust;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintproofRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetcomplaintorderResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintListRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintListResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintOrder;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
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
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChannlesComplaintOrderService {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRefundService orderRefundService;
    @Autowired
    ChannlesFeignClient channlesFeignClient;

    /**
     * 查询纠纷单详情
     * @param complaintOrderId
     */
    public EcGetcomplaintorderResponse detail(Long complaintOrderId){

        ServerResponseEntity<EcGetcomplaintorderResponse> response = channlesFeignClient.getcomplaintorder(complaintOrderId);
        if(response == null || response.isFail() || response.getData()==null){
            Assert.faild("查询纠纷单详情失败。");
        }
        return response.getData();
    }

    /**
     * 商家举证
     *
     * @param request
     */
    public void addcomplaintproof(EcAddcomplaintproofRequest request){
        ServerResponseEntity<EcBaseResponse> response = channlesFeignClient.addcomplaintproof(request);
        if(response == null || response.isFail() || response.getData()==null){
            Assert.faild(response.getMsg());
        }
    }

    /**
     * 商家补充纠纷单留言
     *
     * @param request
     */
    public void addcomplaintmaterial(EcAddcomplaintmaterialReqeust request){
        ServerResponseEntity<EcBaseResponse> response = channlesFeignClient.addcomplaintmaterial(request);
        log.info("商家补充纠纷单留言执行结束，返回结果:{}", JSONObject.toJSONString(response));
        if(response == null || response.isFail() || response.getData()==null){
            Assert.faild(response.getMsg());
        }
    }


}
