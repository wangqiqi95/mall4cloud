
package com.mall4j.cloud.delivery.controller.staff;

import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.service.DeliveryOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author
 */
@RestController("staffDeliveryController")
@RequestMapping("/s/delivery")
@Api(tags = "app-我的物流接口")
public class DeliveryController {

    @Autowired
    private DeliveryOrderService deliveryOrderService;
    /**
     * 物流查询接口
     */
    @GetMapping("/order_info/{orderId}")
    @ApiOperation(value = "物流查询接口", notes = "根据orderNumber查询订单所有包裹信息")
    public ServerResponseEntity<List<DeliveryOrderFeignVO>> info(@PathVariable("orderId") String orderId) {
        try {
            List<DeliveryOrderFeignVO> deliveryOrders = deliveryOrderService.getByDeliveryByOrderId(Long.parseLong(orderId));
            return ServerResponseEntity.success(deliveryOrders);
        } catch (Exception e) {
            e.printStackTrace();
            // 查询出错
            throw new LuckException("查询出错");
        }
    }


}
