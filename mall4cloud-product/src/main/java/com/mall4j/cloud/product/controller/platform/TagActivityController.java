package com.mall4j.cloud.product.controller.platform;


import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.constant.TagActivityStatus;
import com.mall4j.cloud.product.dto.*;
import com.mall4j.cloud.product.model.TagActRelationProd;
import com.mall4j.cloud.product.model.TagActRelationStore;
import com.mall4j.cloud.product.model.TagActivity;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.TagActivityDetailVO;
import com.mall4j.cloud.product.vo.TagActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 营销标签活动表
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
@RequiredArgsConstructor
@RestController("platformTagActivityController")
@RequestMapping("/p/tag_activity")
@Api(tags = "platform-营销标签活动表")
public class TagActivityController {
    private final TagActivityService activityService;
    private final TagActRelationProdService prodService;
    private final TagActRelationStoreService storeService;
    private final MapperFacade mapperFacade;
	@GetMapping("/page")
	@ApiOperation(value = "获取营销标签活动表列表", notes = "分页获取营销标签活动表列表")
	public ServerResponseEntity<PageVO<TagActivityVO>> page(@Valid PageDTO pageDTO, TagActivityPageDTO request) {
		PageVO<TagActivityVO> activityVOPageVO = activityService.page(pageDTO,request);
		return ServerResponseEntity.success(activityVOPageVO);
	}

    @GetMapping
    @ApiOperation(value = "获取营销标签活动表详情", notes = "根据id获取营销标签活动表详情")
    public ServerResponseEntity<TagActivityDetailVO> getById(@RequestParam Long id) {
        TagActivity tagActivity = activityService.getById(id);
        List<TagActRelationStore> stores = storeService.listByActId(id);
        List<TagActRelationProd> prods = prodService.listByActId(id);
        return ServerResponseEntity.success(new TagActivityDetailVO(tagActivity,stores,prods));
    }

    @PostMapping
    @ApiOperation(value = "保存营销标签活动表", notes = "保存营销标签活动表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TagActivityDTO request) {
        TagActivity tagActivity = mapperFacade.map(request, TagActivity.class);
        tagActivity.setCreateBy(AuthUserContext.get().getUserId());
        tagActivity.setStatus(TagActivityStatus.CREATE.value());
        if(request.getSaveBtnType()==1) {
            tagActivity.setStatus(TagActivityStatus.NOT_START.value());
        }
        tagActivity.setCreateName(AuthUserContext.get().getUsername());
        tagActivity.setCreateTime(new Date());
        tagActivity.setUpdateBy(tagActivity.getCreateBy());
        tagActivity.setUpdateTime(tagActivity.getCreateTime());
        activityService.createOrUpdateTagActivity(tagActivity,request.getStores(),request.getSpuList());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新营销标签活动表", notes = "更新营销标签活动表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TagActivityDTO request) {
        TagActivity tagActivity = mapperFacade.map(request, TagActivity.class);
        if(request.getSaveBtnType()==1) {
            tagActivity.setStatus(TagActivityStatus.NOT_START.value());
        }
        tagActivity.setUpdateBy(AuthUserContext.get().getUserId());
        tagActivity.setUpdateTime(new Date());
        activityService.createOrUpdateTagActivity(tagActivity,request.getStores(),request.getSpuList());
        return ServerResponseEntity.success();
    }

    @PostMapping("/enable")
    @ApiOperation(value = "禁用/启用营销标签活动", notes = "禁用/启用营销标签活动")
    public ServerResponseEntity<Void> enable(@Valid @RequestBody EnableTagActivityDTO request) {
        TagActivity tagActivity = new TagActivity();
        tagActivity.setId(request.getId());
        if(request.getEnabled()==1){
            Date date = new Date();
            TagActivity oldTag =  activityService.getById(request.getId());
            Date startDate  = oldTag.getStartTime();
            Date endDate  = oldTag.getEndTime();
            //开始时间比当前时间早
            if(startDate.before(date)&&date.before(endDate)){
                tagActivity.setStatus(TagActivityStatus.PROGRESS.value());
            }
            if(endDate.before(date)){
                tagActivity.setStatus(TagActivityStatus.END.value());
            }
            if(date.before(startDate)){
                tagActivity.setStatus(TagActivityStatus.NOT_START.value());
            }
        }else{
            tagActivity.setStatus(TagActivityStatus.CREATE.value());
        }
        activityService.update(tagActivity);
        return ServerResponseEntity.success();
    }


    @PostMapping("/addShops")
    @ApiOperation(value = "添加商店", notes = "添加商店")
    public ServerResponseEntity<Void> addShops(@Valid @RequestBody AddTagActivityShopsDTO request) {
        activityService.addShops(request.getId(),request.getShops());
        return ServerResponseEntity.success();
    }


    @PostMapping("/deleteShop")
    @ApiOperation(value = "添加商店", notes = "添加商店")
    public ServerResponseEntity<Void> deleteShops(@Valid @RequestBody AddTagActivityShopsDTO request) {
        storeService.deleteById(request.getId(),request.getShops().get(0));
        activityService.removeCache(request.getId());
        return ServerResponseEntity.success();
    }
}
