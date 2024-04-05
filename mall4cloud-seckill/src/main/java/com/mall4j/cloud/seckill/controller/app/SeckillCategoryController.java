package com.mall4j.cloud.seckill.controller.app;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.seckill.constant.SeckillConstant;
import com.mall4j.cloud.seckill.dto.SeckillCategoryDTO;
import com.mall4j.cloud.seckill.dto.SeckillDTO;
import com.mall4j.cloud.seckill.mapper.SeckillMapper;
import com.mall4j.cloud.seckill.model.SeckillCategory;
import com.mall4j.cloud.seckill.service.SeckillCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 秒杀分类信息
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
@RestController("appSeckillCategoryController")
@RequestMapping("/ua/seckill_category")
@Api(tags = "app-秒杀分类信息")
public class SeckillCategoryController {

    @Autowired
    private SeckillCategoryService seckillCategoryService;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/list")
	@ApiOperation(value = "获取秒杀分类信息列表", notes = "分页获取秒杀分类信息列表")
	public ServerResponseEntity<List<SeckillCategory>> list() {
        List<SeckillCategory> seckillCategory = seckillCategoryService.list();
		return ServerResponseEntity.success(seckillCategory);
	}
}
