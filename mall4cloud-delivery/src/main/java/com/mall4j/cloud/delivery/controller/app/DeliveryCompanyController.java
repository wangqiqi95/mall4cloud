package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryInfoVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.service.DeliveryCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物流公司
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
@RestController("appDeliveryCompanyController")
@RequestMapping("/delivery_company")
@Api(tags = "物流公司")
@Slf4j
public class DeliveryCompanyController {

    @Autowired
    private DeliveryCompanyService deliveryCompanyService;

    @GetMapping("/list")
    @ApiOperation(value = "获取物流公司列表", notes = "获取物流公司列表")
    public ServerResponseEntity<List<DeliveryCompanyVO>> list() {
        return ServerResponseEntity.success(deliveryCompanyService.list());
    }

    @GetMapping("/getDeliveryInfo")
    @ApiOperation(value = "获取物信息", notes = "获取物流信息")
    public ServerResponseEntity<DeliveryInfoVO> getDeliveryInfo(@RequestParam String company, @RequestParam String logisticsNo, @RequestParam String phone) {
        DeliveryCompanyVO deliveryCompany = deliveryCompanyService.getByDeliveryCompanyName(company);
        if (deliveryCompany != null) {
            try {
                return ServerResponseEntity.success(deliveryCompanyService.query(deliveryCompany.getDeliveryCompanyId(), logisticsNo, phone));
            } catch (Exception e) {
                log.info("未获取到快递信息");
                return ServerResponseEntity.success();
            }
        } else {
            log.info("快递公司不存在");
            return ServerResponseEntity.success();
        }
    }
}
