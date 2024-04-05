package com.mall4j.cloud.order.controller.multishop;


import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.constant.RefundApplyType;
import com.mall4j.cloud.order.constant.RefundStsType;
import com.mall4j.cloud.order.constant.ReturnProcessStatusEnum;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.dto.multishop.OrderRefundDTO;
import com.mall4j.cloud.order.model.OrderRefundAddr;
import com.mall4j.cloud.order.model.OrderSettlement;
import com.mall4j.cloud.order.service.OrderRefundAddrService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderSettlementService;
import com.mall4j.cloud.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * @author FrozenWatermelon
 */
@RestController
@RequestMapping("/m/order_refund")
public class OrderRefundController {

    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private OrderRefundAddrService orderRefundAddrService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OnsMQTransactionTemplate orderRefundTemplate;
    @Autowired
    private OrderSettlementService orderSettlementService;

    /**
     * 查看我的退款订单列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "我的退款订单列表", notes = "我的退款订单列表，显示数量时候")
    public ServerResponseEntity<PageVO<OrderRefundVO>> list(PageDTO page, OrderRefundPageDTO orderRefundPageDTO) {
        orderRefundPageDTO.setShopId(AuthUserContext.get().getTenantId());
        PageVO<OrderRefundVO> pageList = orderRefundService.page(page, orderRefundPageDTO);
        return ServerResponseEntity.success(pageList);
    }

    /**
     * 通过id查询
     *
     * @param refundId id
     * @return 查询详细信息
     */
    @GetMapping("/info/{refundId}")
    public ServerResponseEntity<OrderRefundVO> getById(@PathVariable("refundId") Long refundId) {
        OrderRefundVO orderRefund = orderRefundService.getDetailByRefundId(refundId);
        if (!Objects.equals(orderRefund.getShopId(), AuthUserContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        if (Objects.equals(orderRefund.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())&& orderRefund.getHandelTime() !=null) {
            OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(refundId);
            orderRefund.setOrderRefundAddr(mapperFacade.map(orderRefundAddr, OrderRefundAddrVO.class));
        }
        return ServerResponseEntity.success(orderRefund);
    }


    /**
     * 进入这个方法，会出现两种情况：
     * 1. 仅退款，此时商家同意买家的退款申请，执行发放退款的操作
     * 2. 退货退款操作:
     *   2.1)退货退款的第一步，商家允许买家退款的申请，商家进行设置退货地址，不执行发放退款的操作
     *   2.2)退货退款的第二步，当商家收到货之后，同意买家退款，此时需要发放退款，但不会执行这个方法，执行的是下面这个方法
     *   @see com.mall4j.cloud.order.controller.multishop.OrderRefundController#returnMoney(OrderRefundDTO)
     *
     */
    @PutMapping("/return_and_refund_audit")
    public ServerResponseEntity<Void> returnAndRefundAudit(@RequestBody OrderRefundDTO orderRefundParam) {
        // 处理退款操作


        OrderRefundVO orderRefundVo = orderRefundService.getDetailByRefundId(orderRefundParam.getRefundId());
        if (!Objects.equals(ReturnProcessStatusEnum.APPLY.value(), orderRefundVo.getReturnMoneySts())) {
            // 订单退款状态已发生改变，请勿重复操作
            return ServerResponseEntity.showFailMsg("订单退款状态已发生改变，请勿重复操作");
        }
        if (!Objects.equals(orderRefundVo.getShopId(), AuthUserContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        // 拒绝退款，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
        if (Objects.equals(orderRefundParam.getRefundSts(), RefundStsType.DISAGREE.value())) {
            orderRefundService.disagreeRefund(orderRefundParam, orderRefundVo);
            return ServerResponseEntity.success();
        }

        // 同意退货，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
        if (Objects.equals(orderRefundVo.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())) {
            orderRefundService.agreeReturns(orderRefundParam, orderRefundVo);
            return ServerResponseEntity.success();
        }

        // 同意退款，上面只是同意退货，关系到钱要看下面的
        return agreeRefund(orderRefundParam,orderRefundVo);
    }



    /**
     * 退货退款的第二步，当商家收到货之后，同意买家退款，此时需要发放退款
     */
    @PutMapping("/return_money")
    public ServerResponseEntity<Void> returnMoney(@Valid @RequestBody OrderRefundDTO orderRefundParam) {

        // 获取退款单信息
        OrderRefundVO orderRefundVo = orderRefundService.getDetailByRefundId(orderRefundParam.getRefundId());
        if (!Objects.equals(ReturnProcessStatusEnum.CONSIGNMENT.value(), orderRefundVo.getReturnMoneySts())) {
            throw new LuckException("订单退款状态已发生改变，请勿重复操作");
        }
        if (!Objects.equals(orderRefundVo.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        // 拒绝退款，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
        if (Objects.equals(orderRefundParam.getRefundSts(), RefundStsType.DISAGREE.value())) {
            orderRefundService.disagreeRefund(orderRefundParam, orderRefundVo);
            return ServerResponseEntity.success();
        }

        return agreeRefund(orderRefundParam,orderRefundVo);
    }


    private ServerResponseEntity<Void> agreeRefund(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo) {

        OrderSettlement orderSettlement = orderSettlementService.getByOrderId(orderRefundVo.getOrderId());
        if (orderSettlement == null) {
            return ServerResponseEntity.showFailMsg("没有查询到支付记录，无法申请退款");
        }

        orderRefundVo.setPayId(orderSettlement.getPayId());
        orderRefundVo.setPayType(orderSettlement.getPayType());


        // 执行退款，真正意义上的退款，需要确保
        // 发送事务消息
        SendResult sendResult = orderRefundTemplate.sendMessageInTransaction(orderRefundVo, orderRefundParam);

        if (sendResult == null || sendResult.getMessageId() == null) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        return ServerResponseEntity.success();
    }


}
