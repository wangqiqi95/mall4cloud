package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.delivery.service.TransportService;

import io.swagger.annotations.Api;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 运费模板
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("appTransportController")
@RequestMapping("/transport")
@Api(tags = "运费模板")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @Autowired
	private MapperFacade mapperFacade;

}
