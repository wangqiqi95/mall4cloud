package com.mall4j.cloud.product.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.SpuSkuPriceLogDTO;
import com.mall4j.cloud.product.model.SpuSkuPriceLog;
import com.mall4j.cloud.product.service.SpuSkuPriceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-05-31 16:07:13
 */
@RestController("multishopSpuSkuPriceLogController")
@RequestMapping("/p/spu_sku_price_log")
@Api(tags = "")
public class SpuSkuPriceLogController {

    @Autowired
    private SpuSkuPriceLogService spuSkuPriceLogService;

    @Autowired
	private MapperFacade mapperFacade;

}
