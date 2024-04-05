package com.mall4j.cloud.group.controller.advance.admin;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.service.AdvanceActivityBizService;
import com.mall4j.cloud.group.vo.AdvanceActivityListVO;
import com.mall4j.cloud.group.vo.AdvanceActivityVO;
import com.mall4j.cloud.group.vo.MealActivityListVO;
import com.mall4j.cloud.group.vo.MealActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/mp/advance_activity")
@Api(tags = "admin-预售活动设置")
public class AdvanceActivityController {
    @Resource
    private AdvanceActivityBizService advanceActivityBizService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改套餐一口价")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid AdvanceActivityDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        Integer id = param.getId();

        //todo  缺少用户姓名记录
        if (null == id){
            param.setCreateUserId(userId);
        }else {
            param.setUpdateUserId(userId);
        }
        return advanceActivityBizService.saveOrUpdateAdvanceActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "套餐一口价详情")
    public ServerResponseEntity<AdvanceActivityVO> detail(@PathVariable Integer id){
        return advanceActivityBizService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "套餐一口价列表")
    public ServerResponseEntity<PageVO<AdvanceActivityListVO>> page(@RequestBody AdvanceActivityPageDTO param){
        return advanceActivityBizService.page(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return advanceActivityBizService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return advanceActivityBizService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return advanceActivityBizService.delete(id);
    }
}
