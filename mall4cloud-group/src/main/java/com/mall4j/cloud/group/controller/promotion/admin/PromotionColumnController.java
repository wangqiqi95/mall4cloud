package com.mall4j.cloud.group.controller.promotion.admin;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.service.PromotionColumnService;
import com.mall4j.cloud.group.service.TimeLimitedDiscountActivityBizService;
import com.mall4j.cloud.group.service.TimeLimitedDiscountActivityService;
import com.mall4j.cloud.group.vo.PromotionColumnPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 限时调价活动
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
@RestController("MpPromotionColumnController")
@RequestMapping("/mp/promotion/column")
@Api(tags = "促销位活动")
public class PromotionColumnController {

    @Autowired
    PromotionColumnService promotionColumnService;
    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取促销位活动列表", notes = "分页获取促销位活动列表")
	public ServerResponseEntity<PageVO<PromotionColumnPageVO>> page(@Valid PageDTO pageDTO, PromotionColumnDTO param) {
		PageVO<PromotionColumnPageVO> promotionColumnPageVO = promotionColumnService.page(pageDTO,param);
		return ServerResponseEntity.success(promotionColumnPageVO);
	}

	@GetMapping
    @ApiOperation(value = "获取促销位活动", notes = "获取促销位活动")
    public ServerResponseEntity<PromotionColumnPageVO> getById(@RequestParam Long id) {
        Assert.isNull(id,"活动id不允许为空。");
        return ServerResponseEntity.success(promotionColumnService.detail(id.intValue()));
    }


}
