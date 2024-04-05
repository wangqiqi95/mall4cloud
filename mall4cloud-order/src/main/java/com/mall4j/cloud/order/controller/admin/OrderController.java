package com.mall4j.cloud.order.controller.admin;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.delivery.dto.ChangeOrderAddrDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.order.dto.OrderAddrDTO;
import com.mall4j.cloud.order.dto.multishop.OrderAdminDTO;
import com.mall4j.cloud.order.dto.multishop.OrderChangeAddrDTO;
import com.mall4j.cloud.order.listener.OrderExcelListener;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderAddr;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.service.OrderAddrService;
import com.mall4j.cloud.order.service.OrderExcelService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.task.SkuOrderPriceErrorTask;
import com.mall4j.cloud.order.vo.OrderVO;
import com.mall4j.cloud.order.vo.UnDeliveryOrderExcelVO;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon on 2018/09/15.
 */
@RestController("adminOrderController")
@RequestMapping("/mp/order")
public class OrderController {

    @Value("${mall4cloud.expose.permission:}")
    private Boolean permission;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderExcelService orderExcelService;
    @Autowired
    private OrderAddrService orderAddrService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private SkuOrderPriceErrorTask skuOrderPriceErrorTask;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    public ServerResponseEntity<PageVO<EsOrderVO>> page(OrderSearchDTO orderSearchDTO) {
        orderSearchDTO.setShopId(AuthUserContext.get().getTenantId());
        PageVO<EsOrderVO> orderPage = orderService.orderPage(orderSearchDTO);
        // 处理下发货完成时能否修改物流
        for (EsOrderVO esOrderVO : orderPage.getList()) {
            if (!Objects.equals(esOrderVO.getStatus(), OrderStatus.CONSIGNMENT.value()) ||
                    !Objects.equals(esOrderVO.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                continue;
            }
            int updateOrViewDeliveryInfo = 0;
            for (EsOrderItemVO orderItem : esOrderVO.getOrderItems()) {
                if (Objects.nonNull(orderItem.getDeliveryType()) && Objects.equals(orderItem.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                    updateOrViewDeliveryInfo = 1;
                    break;
                }
            }
            if (BooleanUtil.isFalse(permission)){
                esOrderVO.setMobile(PhoneUtil.hideBetween(esOrderVO.getMobile()).toString());
            }
            esOrderVO.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        }
        return ServerResponseEntity.success(orderPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/order_info/{orderId}")
    public ServerResponseEntity<OrderVO> info(@PathVariable("orderId") Long orderId) throws UnsupportedEncodingException {
        // 订单和订单项
        Order order = orderService.getOrderAndOrderItemData(orderId, AuthUserContext.get().getTenantId());
        System.out.println(order.toString());
        // 详情用户收货地址
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(order.getOrderAddrId());
        order.setOrderAddr(mapperFacade.map(orderAddr, OrderAddr.class));
        OrderVO orderVO = mapperFacade.map(order, OrderVO.class);
        System.out.println(orderVO.toString());

        // 发货、确认收货或者有发货时间时才能查看物流
        if (Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value()) || Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value()) || Objects.nonNull(order.getDeliveryTime())) {
            // todo 自提物流信息 见DeliveryOrderController#info()
//        if (Objects.nonNull(order.getDvyType()) && order.getDvyType().equals(DvyType.STATION.value())){
//            OrderSelfStation orderSelfStation = orderSelfStationService.getOne(new LambdaQueryWrapper<OrderSelfStation>().eq(OrderSelfStation::getOrderNumber, order.getOrderNumber()));
//            order.getUserAddrOrder().setMobile(orderSelfStation.getStationUserMobile());
//            order.getUserAddrOrder().setReceiver(orderSelfStation.getStationUserName());
//        }
            // 查询包裹信息
            ServerResponseEntity<List<DeliveryOrderFeignVO>> deliveryOrderResponse = deliveryFeignClient.getByDeliveryByOrderId(orderId);
            if (deliveryOrderResponse.isSuccess()) {
                orderVO.setDeliveryExpresses(deliveryOrderResponse.getData());
            }
        }

        return ServerResponseEntity.success(orderVO);
    }

    /**
     * 获取订单用户下单地址
     */
    @GetMapping("/order_addr/{orderAddrId}")
    public ServerResponseEntity<OrderAddrVO> getOrderAddr(@PathVariable("orderAddrId") Long orderAddrId) {
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(orderAddrId);
        return ServerResponseEntity.success(mapperFacade.map(orderAddr, OrderAddrVO.class));
    }

    /**
     * 订单项待发货数量查询
     */
    @GetMapping("/order_item_and_address/{orderId}")
    public ServerResponseEntity<OrderVO> getOrderItemAndAddress(@PathVariable("orderId") Long orderId) {
        // 订单和订单项
        Order order = orderService.getOrderAndOrderItemData(orderId, AuthUserContext.get().getTenantId());
        OrderVO orderVO = mapperFacade.map(order, OrderVO.class);
        List<OrderItemVO> orderItems = orderVO.getOrderItems();
        for (OrderItemVO orderItem : orderItems) {
            orderItem.setChangeNum(orderItem.getBeDeliveredNum() == -1 ? orderItem.getCount() : orderItem.getBeDeliveredNum());
        }
        // 用户收货地址
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(order.getOrderAddrId());
        orderVO.setOrderAddr(mapperFacade.map(orderAddr, OrderAddrVO.class));
        return ServerResponseEntity.success(orderVO);
    }

    /**
     * 发货
     */
    @PostMapping("/delivery")
    public ServerResponseEntity<Void> delivery(@Valid @RequestBody DeliveryOrderDTO deliveryOrderParam) {
        // TODO 检查发货数量
        Order order = orderService.getOrderAndOrderItemData(deliveryOrderParam.getOrderId(), AuthUserContext.get().getTenantId());
        if (Objects.isNull(order)) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        // 订单不在支付状态
        if (!Objects.equals(order.getStatus(), OrderStatus.PAYED.value())) {
            return ServerResponseEntity.showFailMsg("订单状态异常");
        }
        List<DeliveryOrderItemDTO> selectOrderItems = deliveryOrderParam.getSelectOrderItems();
        if(CollectionUtil.isEmpty(selectOrderItems)){
            return ServerResponseEntity.showFailMsg("请至少选择一个订单项进行发货操作");
        }
        Map<Long, OrderItem> orderItemMap = order.getOrderItems().stream().collect(Collectors.toMap(OrderItem::getOrderItemId, orderItem -> orderItem));
        for (DeliveryOrderItemDTO selectOrderItem : selectOrderItems) {
            if(!orderItemMap.containsKey(selectOrderItem.getOrderItemId())){
                throw new LuckException("订单项不存在");
            }
            OrderItem orderItem = orderItemMap.get(selectOrderItem.getOrderItemId());
            if(orderItem.getBeDeliveredNum() < selectOrderItem.getChangeNum()){
                throw new LuckException("订单项可发货数量不足,请刷新后重试");
            }
        }
        orderService.delivery(deliveryOrderParam);
        return ServerResponseEntity.success();
    }

    /**
     * 修改订单金额
     */
    @PutMapping("/change_amount")
    public ServerResponseEntity<Void> changeAmount(@RequestBody OrderAdminDTO orderAdminDTO) {
        Long minFreightAmount = 0L;
        if (Objects.isNull(orderAdminDTO.getFreightAmount())) {
            orderAdminDTO.setFreightAmount(minFreightAmount);
        } else if (orderAdminDTO.getFreightAmount() < minFreightAmount) {
            throw new LuckException("运费不能小于零");
        }
        orderService.changeAmount(orderAdminDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_change_amount")
    @ApiOperation(value = "查询修改订单地址后的运费")
    public ServerResponseEntity<Double> getChangeAmount(OrderChangeAddrDTO param) {
        Order dbOrder = orderService.getOrderAndOrderItemData(param.getOrderId(), AuthUserContext.get().getTenantId());
        if (dbOrder.getStatus() > OrderStatus.PAYED.value() && !Objects.equals(dbOrder.getStatus(), OrderStatus.WAIT_GROUP.value())) {
            throw new LuckException("订单状态异常，无法更改订单地址");
        }
        List<OrderItem> orderItems = orderItemService.listOrderItemsByOrderId(dbOrder.getOrderId());
        List<Long> skuIds = orderItems.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        ServerResponseEntity<List<SkuAddrVO>> responseEntity = skuFeignClient.listSpuDetailByIds(skuIds);
        if (responseEntity.isFail()) {
            throw new LuckException(responseEntity.getMsg());
        }
        List<SkuAddrVO> skuList = responseEntity.getData();
        Map<Long, SkuAddrVO> skuMap = skuList.stream().collect(Collectors.toMap(SkuAddrVO::getSkuId, s -> s));
        List<ShopCartItemVO> shopCartItems = new ArrayList<>();
        for (OrderItem orderItem : dbOrder.getOrderItems()) {
            SkuAddrVO sku = skuMap.get(orderItem.getSkuId());
            ShopCartItemVO shopCartItem = new ShopCartItemVO();
            shopCartItem.setCount(orderItem.getCount());
            shopCartItem.setTotalAmount(orderItem.getSpuTotalAmount());
            shopCartItem.setShopId(orderItem.getShopId());
            shopCartItem.setVolume(sku.getVolume());
            shopCartItem.setWeight(sku.getWeight());
            shopCartItem.setDeliveryTemplateId(sku.getDeliveryTemplateId());
            shopCartItems.add(shopCartItem);
        }
        UserAddrVO userAddrVO = new UserAddrVO();
        userAddrVO.setAreaId(param.getAreaId());
        if (Objects.equals(dbOrder.getDeliveryType(), DeliveryType.SAME_CITY.value())) {
            userAddrVO.setLng(param.getLng());
            userAddrVO.setLat(param.getLat());
        }
        ChangeOrderAddrDTO changeOrderAddrDTO = new ChangeOrderAddrDTO(shopCartItems, dbOrder.getFreightAmount(), userAddrVO, dbOrder.getDeliveryType());
        ServerResponseEntity<Double> changeResponse = deliveryFeignClient.getOrderChangeAddrAmount(changeOrderAddrDTO);
        if (changeResponse.isFail()) {
            throw new LuckException(changeResponse.getMsg());
        }
        return ServerResponseEntity.success(changeResponse.getData());
    }

    /**
     * 修改用户订单地址
     * @return 是否修改成功
     */
    @PutMapping("/change_user_addr")
    public ServerResponseEntity<Void> changeUserAddr(@RequestBody @Valid OrderAddrDTO orderAddrDTO) {
        Order orderDb = orderService.getByOrderId(orderAddrDTO.getOrderId());
        if (orderDb.getStatus() <= OrderStatus.PAYED.value() || Objects.equals(orderDb.getStatus(), OrderStatus.WAIT_GROUP.value())) {
            OrderAddr orderAddr = mapperFacade.map(orderAddrDTO, OrderAddr.class);
            orderAddrService.update(orderAddr);
        } else {
            throw new LuckException("订单状态异常，无法更改订单地址");
        }
        return ServerResponseEntity.success();
    }

    /**
     * 修改订单备注
     *
     * @param order
     * @return 是否修改成功
     */
    @PutMapping("/change_order_remark")
    public ServerResponseEntity<Void> changeOrderRemark(@RequestBody @Valid Order order) {
        Order orderDb = orderService.getByOrderId(order.getOrderId());
        orderDb.setShopRemarks(order.getShopRemarks());
        orderService.update(orderDb);
        return ServerResponseEntity.success();
    }

    @GetMapping("/sold_excel")
    @ApiOperation(value = "导出excel", notes = "导出订单excel")
    public ServerResponseEntity<Void> orderSoldExcel(HttpServletResponse response, OrderSearchDTO orderSearchDTO) {
//        orderSearchDTO.setShopId(AuthUserContext.get().getTenantId());
//        List<OrderExcelVO> list = orderExcelService.excelOrderList(orderSearchDTO);
//        ExcelUtil.soleExcel(response, list, OrderExcelVO.EXCEL_NAME, OrderExcelVO.MERGE_ROW_INDEX, OrderExcelVO.MERGE_COLUMN_INDEX, OrderExcelVO.class);
        return ServerResponseEntity.success();
    }

    @GetMapping("/un_delivery_sold_excel")
    @ApiOperation(value = "导出待发货的订单excel")
    public ServerResponseEntity<Void> unDeliveryOrderSoldExcel(HttpServletResponse response, OrderSearchDTO orderSearchDTO) {
        orderSearchDTO.setShopId(AuthUserContext.get().getTenantId());
        orderExcelService.excelUnDeliveryOrderList(response, orderSearchDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/export_order_excel")
    @ApiOperation(value = "导入订单")
    public ServerResponseEntity<String> exportOrderExcel(@RequestParam("excelFile") MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        String info = null;
        try {
            Map<Integer, List<String>> errorMap = new HashMap<>(8);
            EasyExcel.read(file.getInputStream(), UnDeliveryOrderExcelVO.class, new OrderExcelListener(orderExcelService, errorMap)).sheet().doRead();
            info = orderExcelService.orderExportError(errorMap);
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
        return ServerResponseEntity.success(info);
    }

    @GetMapping("/ua/skuOrderPriceErrorTask")
    @ApiOperation(value = "skuOrderPriceErrorTask")
    public ServerResponseEntity<Void> skuOrderPriceErrorTask(@RequestParam(name = "begin",defaultValue = "",required = false)
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date begin,
                                                             @RequestParam(name = "end",defaultValue = "",required = false)
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        skuOrderPriceErrorTask.skuOrderPriceErrorTaskTop(begin,end);
        return ServerResponseEntity.success();
    }
}
