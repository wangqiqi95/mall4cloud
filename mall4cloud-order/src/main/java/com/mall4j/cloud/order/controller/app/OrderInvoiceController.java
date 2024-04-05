package com.mall4j.cloud.order.controller.app;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.constant.InvoiceHeaderType;
import com.mall4j.cloud.order.constant.OrderInvoiceState;
import com.mall4j.cloud.order.model.OrderInvoice;
import com.mall4j.cloud.order.service.OrderInvoiceService;
import com.mall4j.cloud.order.vo.OrderInvoiceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * @author Pineapple
 * @date 2021/8/2 8:59
 */
@RestController("appOrderInvoiceController")
@RequestMapping("/order_invoice")
@Api(tags = "用户端订单发票")
public class OrderInvoiceController {
    @Autowired
    private OrderInvoiceService orderInvoiceService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "获取订单发票列表", notes = "分页获取列表")
    public ServerResponseEntity<PageVO<OrderInvoiceVO>> page(@Valid PageDTO pageDTO) {
        PageVO<OrderInvoiceVO> orderInvoicePage = orderInvoiceService.pageUserInvoice(pageDTO);
        return ServerResponseEntity.success(orderInvoicePage);
    }

    @GetMapping
    @ApiOperation(value = "获取发票信息", notes = "根据orderInvoiceId获取")
    public ServerResponseEntity<OrderInvoiceVO> getByOrderInvoiceId(@RequestParam Long orderInvoiceId) {
        OrderInvoiceVO orderInvoice = orderInvoiceService.getById(orderInvoiceId);
        if (Objects.nonNull(orderInvoice.getUserId()) && !Objects.equals(orderInvoice.getUserId(), AuthUserContext.get().getUserId())) {
            //非当前用户的发票信息
            throw new LuckException("无法查看非当前用户的发票信息");
        }
        return ServerResponseEntity.success(orderInvoice);
    }

    @PostMapping
    @ApiOperation(value = "申请开票", notes = "保存申请信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        if (Objects.isNull(orderInvoiceDTO.getShopId())) {
            throw new LuckException("店铺id不能为空");
        }
        Long orderId = orderInvoiceService.getByOrderId(orderInvoiceDTO.getOrderId());
        if (Objects.nonNull(orderId)){
            throw new LuckException("该订单已经申请发票，请勿重复申请！");
        }
        OrderInvoice orderInvoice = mapperFacade.map(orderInvoiceDTO, OrderInvoice.class);
        orderInvoice.setShopId(orderInvoiceDTO.getShopId());
        if (Objects.equals(InvoiceHeaderType.PERSONAL.value(), orderInvoice.getHeaderType())) {
            orderInvoice.setInvoiceTaxNumber(null);
        }
        orderInvoice.setInvoiceState(OrderInvoiceState.APPLICATION.value());
        orderInvoice.setApplicationTime(new Date());
        orderInvoiceService.save(orderInvoice);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "申请换开", notes = "更新发票信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        OrderInvoice orderInvoice = mapperFacade.map(orderInvoiceDTO, OrderInvoice.class);
        orderInvoice.setInvoiceState(OrderInvoiceState.APPLICATION.value());
        orderInvoice.setApplicationTime(new Date());
        if (Objects.equals(InvoiceHeaderType.PERSONAL.value(), orderInvoice.getHeaderType())) {
            orderInvoice.setInvoiceTaxNumber(null);
        }
        orderInvoiceService.update(orderInvoice);
        return ServerResponseEntity.success();
    }
}
