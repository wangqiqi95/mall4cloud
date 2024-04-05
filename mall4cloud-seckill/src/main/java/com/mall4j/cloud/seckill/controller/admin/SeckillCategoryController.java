package com.mall4j.cloud.seckill.controller.admin;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 秒杀分类信息
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
@RestController("adminSeckillCategoryController")
@RequestMapping("/mp/seckill_category")
@Api(tags = "秒杀分类信息")
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

	@GetMapping
    @ApiOperation(value = "获取秒杀分类信息", notes = "根据categoryId获取秒杀分类信息")
    public ServerResponseEntity<SeckillCategory> getByCategoryId(@RequestParam Long categoryId) {
        return ServerResponseEntity.success(seckillCategoryService.getByCategoryId(categoryId));
    }

    @PostMapping
    @ApiOperation(value = "保存秒杀分类信息", notes = "保存秒杀分类信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SeckillCategoryDTO seckillCategoryDTO) {
        Long shopId = AuthUserContext.get().getTenantId();
        if(!Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)){
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        if(seckillCategoryService.countByName(null, null) + 1 > SeckillConstant.SECKILL_CATEGORY_MAX_NUM){
	        throw new LuckException("分类最多存在16个！");
        }
	    if(seckillCategoryService.countByName(seckillCategoryDTO.getName(), null) > 0){
	        throw new LuckException("已存在有相同的分类名称");
        }
        SeckillCategory seckillCategory = mapperFacade.map(seckillCategoryDTO, SeckillCategory.class);
        seckillCategory.setCategoryId(null);
        seckillCategoryService.save(seckillCategory);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新秒杀分类信息", notes = "更新秒杀分类信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SeckillCategoryDTO seckillCategoryDTO) {
        if(!Objects.equals(AuthUserContext.get().getTenantId(), Constant.PLATFORM_SHOP_ID)){
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        if(seckillCategoryService.countByName(seckillCategoryDTO.getName(),seckillCategoryDTO.getCategoryId()) > 0){
            throw new LuckException("已存在有相同的分类名称");
        }
        SeckillCategory seckillCategory = mapperFacade.map(seckillCategoryDTO, SeckillCategory.class);
        seckillCategoryService.update(seckillCategory);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_batch_seq")
    @ApiOperation(value = "批量更新秒杀分类排序", notes = "批量更新秒杀分类排序")
    public ServerResponseEntity<Void> updateBatchSeq(@Valid @RequestBody SeckillCategoryDTO seckillCategoryDTO) {
        if(!Objects.equals(AuthUserContext.get().getTenantId(), Constant.PLATFORM_SHOP_ID)){
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        List<Long> categoryIds = seckillCategoryDTO.getCategoryIds();
        List<SeckillCategory> seckillCategoryList = new ArrayList<>();
        int seq = 16;
        for (Long categoryId : categoryIds) {
            SeckillCategory seckillCategory = new SeckillCategory();
            seckillCategory.setCategoryId(categoryId);
            seckillCategory.setSeq(seq);
            seckillCategoryList.add(seckillCategory);
            seq--;
        }
        seckillCategoryService.updateBatch(seckillCategoryList);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除秒杀分类信息", notes = "根据秒杀分类信息id删除秒杀分类信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long categoryId) {
        if(!Objects.equals(AuthUserContext.get().getTenantId(), Constant.PLATFORM_SHOP_ID)){
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        SeckillDTO seckillDTO = new SeckillDTO();
        seckillDTO.setCategoryId(categoryId);
        if(seckillMapper.countBySeckillConditions(seckillDTO) > 0){
            throw new LuckException("已有秒杀商品关联该分类，无法删除");
        }
        seckillCategoryService.deleteById(categoryId);
        return ServerResponseEntity.success();
    }
}
