package com.mall4j.cloud.delivery.controller.app;

import com.mall4j.cloud.delivery.service.TransfeeFreeService;

import io.swagger.annotations.Api;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 指定条件包邮项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("appTransfeeFreeController")
@RequestMapping("/transfee_free")
@Api(tags = "指定条件包邮项")
public class TransfeeFreeController {

    @Autowired
    private TransfeeFreeService transfeeFreeService;

    @Autowired
	private MapperFacade mapperFacade;

}
