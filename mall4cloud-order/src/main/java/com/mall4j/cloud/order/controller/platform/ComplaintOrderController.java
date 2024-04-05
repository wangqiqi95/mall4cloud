package com.mall4j.cloud.order.controller.platform;

import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.dto.platform.request.ComplaintOrderDetailRequest;
import com.mall4j.cloud.order.dto.platform.request.ComplaintOrderPageRequest;
import com.mall4j.cloud.order.dto.platform.response.ComplaintDetailSKXResponse;
import com.mall4j.cloud.order.dto.platform.response.ComplaintOrderSKX;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.service.impl.ComplaintOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = "原视频号3。0纠纷单管理")
@RestController("complaintOrderController")
@RequestMapping("/ua/p/complaint")
public class ComplaintOrderController {

    @Autowired
    ComplaintOrderService complaintOrderService;
    @Autowired
    OrderRefundMapper orderRefundMapper;

    @ApiOperation("分页查询纠纷单列表")
    @GetMapping("/page")
    public ServerResponseEntity<PageVO<ComplaintOrderSKX>> page(ComplaintOrderPageRequest orderSearchDTO) {
        return ServerResponseEntity.success(complaintOrderService.pageVO(orderSearchDTO));
    }

    @ApiOperation("纠纷单详情")
    @GetMapping("/detail")
    public ServerResponseEntity<ComplaintDetailSKXResponse> detail(ComplaintOrderDetailRequest complaintOrderDetailRequest) {
        ComplaintDetailSKXResponse response = complaintOrderService.detail(complaintOrderDetailRequest.getComplaintOrderId());
        orderRefundMapper.complaintOrderPlatformRead(complaintOrderDetailRequest.getComplaintOrderId());
        return ServerResponseEntity.success(response);
    }



    @ApiOperation("上传纠纷单凭证" +
            "注意，这里传的图片url需要通过 biz/ua/livestore/img接口 来转换生成微信永久有效的图片地址。")
    @PostMapping("/uploadMaterial")
    public ServerResponseEntity<Void> uploadMaterial(@RequestBody  ComplaintUploadMaterialRequest complaintUploadMaterialRequest) {
        complaintOrderService.uploadMaterial(complaintUploadMaterialRequest);
        return ServerResponseEntity.success();
    }


}
