package com.mall4j.cloud.order.controller.platform;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.constant.RefundType;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderAddr;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.service.*;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import com.mall4j.cloud.order.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 平台订单管理
 * @author: cl
 * @date: 2021-05-06 16:15:15
 */
@Slf4j
@Api(tags = "平台订单管理")
@RestController("platformOrderController")
@RequestMapping("/p/order")
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
    StoreFeignClient storeFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    CouponFeignClient couponFeignClient;
    @Autowired
    OrderRefundService orderRefundService;
    @Autowired
    OrderItemService orderItemService;
    /**
     * 分页获取
     */
    @ApiOperation("分页")
    @GetMapping("/page")
    public ServerResponseEntity<PageVO<EsOrderVO>> page(OrderSearchDTO orderSearchDTO) {
        PageVO<EsOrderVO> orderPage = orderService.orderPage(orderSearchDTO);

        List<Long> storeidList = orderPage.getList().stream().filter(s -> s != null).map(EsOrderVO::getStoreId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listByStoreIdList(storeidList);
        Map<Long,StoreVO> storeMaps = new HashMap<>();
        if(storesResponse!=null && storesResponse.isSuccess() && storesResponse.getData().size()>0){
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId,p->p));
        }


        /**
         * 查询用户列表
         */
        List<Long> useridList = orderPage.getList().stream().map(EsOrderVO::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        for (EsOrderVO esOrderVO : orderPage.getList()) {
            StoreVO storeVO = storeMaps.get(esOrderVO.getStoreId());
            if(storeVO!=null){
                esOrderVO.setStoreName(storeVO.getName());
            }
            //用户信息
            UserApiVO user = userMaps.get(esOrderVO.getUserId());
            if (user != null) {
                esOrderVO.setUserName(user.getNickName());
                esOrderVO.setUserMobile(user.getPhone());
//                esOrderVO.setUserNo(user.getVipcode());
            }
        }



        if (BooleanUtil.isFalse(permission)){
            for (EsOrderVO esOrderVO : orderPage.getList()) {
                esOrderVO.setMobile(PhoneUtil.hideBetween(esOrderVO.getMobile()).toString());
            }
        }
        return ServerResponseEntity.success(orderPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/order_info/{orderId}")
    public ServerResponseEntity<OrderVO> info(@PathVariable("orderId") Long orderId) throws UnsupportedEncodingException {
        // 订单和订单项
        Order order = orderService.getOrderAndOrderItemData(orderId, null);
        // 详情用户收货地址
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(order.getOrderAddrId());
        if (BooleanUtil.isFalse(permission)){
            orderAddr.setMobile(PhoneUtil.hideBetween(orderAddr.getMobile()).toString());
        }
        OrderVO orderVO = mapperFacade.map(order, OrderVO.class);
        orderVO.getOrderItems().forEach(orderItem ->{
            Long skuId = orderItem.getSkuId();
            ServerResponseEntity<SkuVO> responseEntity = skuFeignClient.getById(skuId);
            if (responseEntity != null && responseEntity.isSuccess()) {
                orderItem.setSkuCode(responseEntity.getData().getSkuCode());
                orderItem.setSkuName(responseEntity.getData().getSkuName());
            }
        });
        orderVO.setOrderAddr(mapperFacade.map(orderAddr, OrderAddrVO.class));
        // 发货和确认收货才能查看物流
//        if (Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value()) || Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value())) {
            // 查询包裹信息
            ServerResponseEntity<List<DeliveryOrderFeignVO>> deliveryOrderResponse = deliveryFeignClient.getByDeliveryByOrderId(orderId);
            if (deliveryOrderResponse.isSuccess()) {
                orderVO.setDeliveryExpresses(deliveryOrderResponse.getData());
            }
//        }
        ServerResponseEntity<UserApiVO> userApiVOServerResponseEntity = userFeignClient.getUserById(order.getUserId());
        if(userApiVOServerResponseEntity.isSuccess() && userApiVOServerResponseEntity.getData()!=null ){
            UserApiVO userApiVO = userApiVOServerResponseEntity.getData();
            orderVO.setUserName(userApiVO.getNickName());
            orderVO.setUserMobile(userApiVO.getPhone());
        }

        List<Long> orderIdList = CollectionUtil.newArrayList(orderId);
        ServerResponseEntity<List<TCouponUserOrderDetailVO>> couponResponse = couponFeignClient.getCouponListBypByOrderIds(orderIdList);
        if(couponResponse.isSuccess() && couponResponse.getData()!=null){
            orderVO.setOrderCouponDetailVO(couponResponse.getData());
        }

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderRefundVO> orderRefunds = orderRefundService.getProcessingOrderRefundByOrderId(order.getOrderId());
        long alreadyRefundAmount = 0L;
        for (OrderRefundVO orderRefund : orderRefunds) {
            alreadyRefundAmount = alreadyRefundAmount + orderRefund.getRefundAmount();
            // 整单退款
            if (Objects.equals(RefundType.ALL.value(),orderRefund.getRefundType())) {
                orderVO.setCanRefund(false);
                // 统一的退款单号
                for (OrderItem orderItemDto : orderItems) {
                    orderItemDto.setFinalRefundId(orderRefund.getRefundId());
                }
                break;
            }
            // 单项退款，每个单号都不一样
            for (OrderItem orderItemDto : orderItems) {
                if (Objects.equals(orderItemDto.getOrderItemId(), orderRefund.getOrderItemId())) {
                    orderItemDto.setFinalRefundId(orderRefund.getRefundId());
                }
            }

        }
        orderVO.setCanRefundAmount(order.getActualTotal()-alreadyRefundAmount);
        if (order.getRefundStatus() != null && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
            orderVO.setFinalRefundId(orderItems.get(0).getFinalRefundId());
        }
        return ServerResponseEntity.success(orderVO);
    }

    /**
     * 修改订单的平台备注
     */
    @PutMapping("/{orderId}/platformRemark")
    @ApiOperation(value = "修改订单的平台备注", notes = "修改订单的平台备注")
    public ServerResponseEntity<Void> platformRemark(@PathVariable("orderId") Long orderId,@RequestParam("remark")String remark) {
        orderService.editPlatformRemark(orderId,remark);
        return ServerResponseEntity.success();
    }


    @GetMapping("/get_order_by_userId")
    @ApiOperation(value = "分页获取某个用户的订单数据", notes = "分页获取某个用户的订单数据")
    public ServerResponseEntity<PageVO<OrderVO>> getOrderByUserId(PageDTO pageDTO, String userId){
        PageVO<OrderVO> pages = orderService.pageByUserId(pageDTO,userId);
        return ServerResponseEntity.success(pages);
    }


    @GetMapping("/sold_excel")
    @ApiOperation(value = "导出excel", notes = "导出订单excel")
    public ServerResponseEntity orderSoldExcel(HttpServletResponse response, OrderSearchDTO orderSearchDTO){

        try {
            orderExcelService.excelOrderList(orderSearchDTO);
//        ExcelUtil.soleExcel(response, list, OrderExcelVO.EXCEL_NAME, OrderExcelVO.MERGE_ROW_INDEX, OrderExcelVO.MERGE_COLUMN_INDEX, OrderExcelVO.class);
             return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出订单信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
         }
    }
}
