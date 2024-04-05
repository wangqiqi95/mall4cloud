package com.mall4j.cloud.order.task;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重推退款订单到中台
 *
 * @luzhengxiang
 * @create 2022-05-15 1:54 PM
 **/
@Slf4j
@Component
public class RePushStdOrderRefundTask {


    @Autowired
    OrderRefundService orderRefundService;
    /**
     * 订单结算
     */
    @XxlJob("rePushStdOrderRefund")
    public void settledOrder2(){
        log.info("rePushStdOrderRefund 退单重新推送中台开始>>>>>>>>>>>");

        String param = XxlJobHelper.getJobParam();
        log.info("接收調度中心参数...[{}]",param);
        if(StrUtil.isEmptyIfStr(param)){
            log.info("参数");
            return;
        }
        String[] methodParams = param.split(",");
        Long refundId = Long.parseLong(methodParams[0]);
        Integer status = Integer.parseInt(methodParams[1]);
        log.info("接收調度参数.解析:refund_id:{},refund_status:{}",refundId,status);
        orderRefundService.pushRefund(refundId, status);

//        orderRefundService.pushRefund(76266750L, 1);
//        orderRefundService.pushRefund(76259337L, 3);
//        orderRefundService.pushRefund(76266739L, 1);
//        orderRefundService.pushRefund(76260484L,-1);
//        orderRefundService.pushRefund(76261044L,5);
//        orderRefundService.pushRefund(76262341L,5);
//        orderRefundService.pushRefund(76266566L,-1);
//        orderRefundService.pushRefund(76267043L,-1);
//        orderRefundService.pushRefund(76268266L,-1);
//        orderRefundService.pushRefund(76268283L,5);
//        orderRefundService.pushRefund(76268289L,5);
//        orderRefundService.pushRefund(76268292L,1);
//        orderRefundService.pushRefund(76270346L,1);
//        orderRefundService.pushRefund(76270353L,1);
//        orderRefundService.pushRefund(76270364L,1);
//        orderRefundService.pushRefund(76273249L,1);
//        orderRefundService.pushRefund(76273251L,1);
//        orderRefundService.pushRefund(76283014L,5);
//        orderRefundService.pushRefund(76283019L,5);
//        orderRefundService.pushRefund(76284648L,5);
//        orderRefundService.pushRefund(76285785L,1);
//        orderRefundService.pushRefund(76285794L,1);
//        orderRefundService.pushRefund(76285801L,1);
//        orderRefundService.pushRefund(76285809L,1);

        log.info("rePushStdOrderRefund 退单重新推送中台开始>>>>>>>>>>>");
    }


}
