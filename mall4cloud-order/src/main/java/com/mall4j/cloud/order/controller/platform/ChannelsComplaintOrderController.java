package com.mall4j.cloud.order.controller.platform;

import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintmaterialReqeust;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddcomplaintproofRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetcomplaintorderResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.dto.platform.request.ComplaintOrderDetailRequest;
import com.mall4j.cloud.order.dto.platform.request.ComplaintOrderPageRequest;
import com.mall4j.cloud.order.dto.platform.response.ComplaintDetailSKXResponse;
import com.mall4j.cloud.order.dto.platform.response.ComplaintOrderSKX;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.service.impl.ChannlesComplaintOrderService;
import com.mall4j.cloud.order.service.impl.ComplaintOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = "视频号4.0纠纷单管理")
@RestController("channelscomplaintOrderController")
@RequestMapping("/ua/p/channels/complaint")
public class ChannelsComplaintOrderController {

    @Autowired
    ChannlesComplaintOrderService complaintOrderService;


    @ApiOperation("纠纷单详情")
    @GetMapping("/detail")
    public ServerResponseEntity<EcGetcomplaintorderResponse> detail(ComplaintOrderDetailRequest complaintOrderDetailRequest) {
        EcGetcomplaintorderResponse response = complaintOrderService.detail(complaintOrderDetailRequest.getComplaintOrderId());
        return ServerResponseEntity.success(response);
    }


    @ApiOperation("商家补充纠纷单留言")
    @PostMapping("/addcomplaintmaterial")
    public ServerResponseEntity<Void> addcomplaintmaterial(@RequestBody EcAddcomplaintmaterialReqeust reqeust) {
        complaintOrderService.addcomplaintmaterial(reqeust);
        return ServerResponseEntity.success();
    }

    @ApiOperation("商家举证")
    @PostMapping("/addcomplaintproof")
    public ServerResponseEntity<Void> addcomplaintproof(@RequestBody EcAddcomplaintproofRequest reqeust) {
        complaintOrderService.addcomplaintproof(reqeust);
        return ServerResponseEntity.success();
    }


}
