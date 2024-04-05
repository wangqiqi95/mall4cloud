package com.mall4j.cloud.delivery.controller.platform;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.model.DeliveryCompany;
import com.mall4j.cloud.delivery.service.DeliveryCompanyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

/**
 * @Author lth
 * @Date 2021/4/27 10:17
 */
@RestController("platformDeliveryCompanyController")
@RequestMapping("/p/delivery_company")
@Api(tags = "物流公司")
public class DeliveryCompanyController {

    @Autowired
    private DeliveryCompanyService deliveryCompanyService;

    @Autowired
    private MapperFacade mapperFacade;

    @DeleteMapping
    @ApiOperation(value = "根据id删除物流公司信息", notes = "根据id删除物流公司信息")
    public ServerResponseEntity<Void> deleteById(@RequestParam(value = "deliveryCompanyId") Long deliveryCompanyId) {
        deliveryCompanyService.deleteById(deliveryCompanyId);
        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "新增物流公司信息", notes = "新增物流公司信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DeliveryCompanyDTO deliveryCompanyDTO) {
        DeliveryCompany deliveryCompany = mapperFacade.map(deliveryCompanyDTO, DeliveryCompany.class);
        deliveryCompanyService.save(deliveryCompany);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新物流信息", notes = "更新物流信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DeliveryCompanyDTO deliveryCompanyDTO) {
        DeliveryCompany deliveryCompany = mapperFacade.map(deliveryCompanyDTO, DeliveryCompany.class);
        deliveryCompanyService.update(deliveryCompany);
        return ServerResponseEntity.success();
    }

}
