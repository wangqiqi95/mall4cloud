package com.mall4j.cloud.order.controller.multishop;

import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.constant.OrderInvoiceState;
import com.mall4j.cloud.order.model.OrderInvoice;
import com.mall4j.cloud.order.service.OrderInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * @author Pineapple
 * @date 2021/8/2 8:59
 */
@RestController
@RequestMapping("/m/order_invoice")
@Api(tags = "商家端订单发票")
public class OrderInvoiceController {
    @Autowired
    private OrderInvoiceService orderInvoiceService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "获取订单发票列表", notes = "分页获取列表")
    public ServerResponseEntity<PageVO<OrderInvoice>> page(@Valid PageDTO pageDTO, OrderInvoiceDTO orderInvoiceDTO) {
        orderInvoiceDTO.setShopId(AuthUserContext.get().getTenantId());
        PageVO<OrderInvoice> orderInvoicePage = orderInvoiceService.page(pageDTO,orderInvoiceDTO);
        return ServerResponseEntity.success(orderInvoicePage);
    }

    @GetMapping
    @ApiOperation(value = "获取", notes = "根据orderInvoiceId获取")
    public ServerResponseEntity<OrderInvoice> getByOrderInvoiceId(@RequestParam Long orderInvoiceId) {
        return ServerResponseEntity.success(orderInvoiceService.getByOrderInvoiceId(orderInvoiceId));
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        OrderInvoice orderInvoice = mapperFacade.map(orderInvoiceDTO, OrderInvoice.class);
        if (Objects.isNull(orderInvoiceDTO.getFileId())){
            //商家提交，不上传文件不给保存
            throw new LuckException("请上传文件");
        }
        orderInvoice.setInvoiceState(OrderInvoiceState.ISSUED.value());
        orderInvoice.setUploadTime(new Date());
        orderInvoiceService.update(orderInvoice);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "该订单是否已经上传发票")
    @GetMapping("/is_upload")
    public ServerResponseEntity<Boolean> isUpload(@RequestParam Long orderId) {
        return ServerResponseEntity.success(orderInvoiceService.isUpload(orderId));
    }
}
