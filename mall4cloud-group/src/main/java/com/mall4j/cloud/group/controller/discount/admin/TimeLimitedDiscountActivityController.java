package com.mall4j.cloud.group.controller.discount.admin;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityCheckDTO;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityPageDTO;
import com.mall4j.cloud.group.dto.TimeLimitedDiscountActivityDTO;
import com.mall4j.cloud.group.dto.TimeLimitedDiscountAddShopDTO;
import com.mall4j.cloud.group.service.TimeLimitedDiscountActivityBizService;
import com.mall4j.cloud.group.service.TimeLimitedDiscountActivityService;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityPageVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 限时调价活动
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
@RestController("multishopTimeLimitedDiscountActivityController")
@RequestMapping("/mp/timeLimited/discountActivity")
@Api(tags = "限时调价活动")
public class TimeLimitedDiscountActivityController {

    @Autowired
    private TimeLimitedDiscountActivityService timeLimitedDiscountActivityService;
    @Autowired
    TimeLimitedDiscountActivityBizService discountActivityBizService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取限时调价活动列表", notes = "分页获取限时调价活动列表")
	public ServerResponseEntity<PageVO<TimeLimitedDiscountActivityPageVO>> page(@Valid PageDTO pageDTO, TimeLimitDiscountActivityPageDTO param) {
		PageVO<TimeLimitedDiscountActivityPageVO> timeLimitedDiscountActivityPage = timeLimitedDiscountActivityService.page(pageDTO,param);
		return ServerResponseEntity.success(timeLimitedDiscountActivityPage);
	}

	@GetMapping
    @ApiOperation(value = "获取限时调价活动", notes = "根据id获取限时调价活动")
    public ServerResponseEntity<TimeLimitedDiscountActivityVO> getById(@RequestParam Long id) {
        Assert.isNull(id,"活动id不允许为空。");
        return ServerResponseEntity.success(discountActivityBizService.detail(id.intValue()));
    }

    @PostMapping
    @ApiOperation(value = "保存限时调价活动", notes = "保存限时调价活动")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TimeLimitedDiscountActivityDTO timeLimitedDiscountActivityDTO) {
        discountActivityBizService.insertActivity(timeLimitedDiscountActivityDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新限时调价活动", notes = "更新限时调价活动")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TimeLimitedDiscountActivityDTO timeLimitedDiscountActivityDTO) {
        discountActivityBizService.updateActivity(timeLimitedDiscountActivityDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
	    discountActivityBizService.enable(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        discountActivityBizService.disable(id);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除限时调价活动", notes = "根据限时调价活动id删除限时调价活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        timeLimitedDiscountActivityService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/shop")
    @ApiOperation(value = "新增调价活动关联店铺", notes = "新增调价活动关联店铺")
    public ServerResponseEntity<Void> addStore(@Valid @RequestBody TimeLimitedDiscountAddShopDTO timeLimitedDiscountShopDTO) {
        discountActivityBizService.addShop(timeLimitedDiscountShopDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delShop")
    @ApiOperation(value = "删除调价活动关联店铺", notes = "删除调价活动关联店铺")
    public ServerResponseEntity<Void> delShop(@RequestParam Integer activityId, @RequestParam Integer shopId) {
        discountActivityBizService.deleteShop(activityId,shopId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/updateCheckStatusBatch")
    @ApiOperation(value = "批量修改审核状态", notes = "批量修改审核状态")
    public ServerResponseEntity<Void> updateCheckStatusBatch(@Valid @RequestBody List<TimeLimitDiscountActivityCheckDTO> checkDTOS) {
        timeLimitedDiscountActivityService.updateCheckStatusBatch(checkDTOS);
        return ServerResponseEntity.success();
    }

    @GetMapping("/ua/checkSpuSkuPrice")
    @ApiOperation(value = "校验商品调价是否低于3折", notes = "校验商品调价是否低于3折")
    public ServerResponseEntity<String> checkSpuSkuPrice(@RequestParam Long id) {
        Assert.isNull(id,"活动id不允许为空。");
        return ServerResponseEntity.success(discountActivityBizService.checkSpuSkuPrice(id.intValue()));
    }

}
