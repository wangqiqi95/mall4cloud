package com.mall4j.cloud.coupon.controller.platform;


import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.service.ChooseMemberEventService;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventStatisticsVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 指定会员活动表（提供最具价值会员活动表） 前端控制器
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Slf4j
@RestController
@RequestMapping("p/choose/member/event")
@Api(tags = {"高价值会员活动管理后台api"})
public class PlatformChooseMemberEventController {

    @Autowired
    private ChooseMemberEventService chooseMemberEventService;

    @ParameterValid
    @PostMapping("/add")
    @ApiOperation("新增高价值会员活动")
    public ServerResponseEntity add(@Valid @RequestBody AddChooseMemberEventDTO addChooseMemberEventDTO,
                                    BindingResult result){
        return chooseMemberEventService.add(addChooseMemberEventDTO);
    }

    @ParameterValid
    @GetMapping("/page")
    @ApiOperation("高价值会员活动分页查询接口")
    public ServerResponseEntity<PageVO<ChooseMemberEventVO>> eventPage(QueryChooseMemberEventPageDTO eventPageDTO){
        return chooseMemberEventService.getChooseMemberEventPage(eventPageDTO);
    }

    @PostMapping("/export/user")
    @ApiOperation("高价值会员导入")
    public ServerResponseEntity<List<String>> exportUser(MultipartFile file){
        return chooseMemberEventService.exportUser(file);
    }

    @GetMapping("/statistics")
    @ApiOperation("高价值会员活动统计")
    public ServerResponseEntity<ChooseMemberEventStatisticsVO> eventStatistics(@ApiParam(value = "eventId",required = true)@RequestParam("eventId") Long eventId){
        return chooseMemberEventService.eventStatistics(eventId);
    }

    @GetMapping("/export/statistics")
    @ApiOperation("导出高价值会员活动统计")
    public ServerResponseEntity<Void> exportEventStatistics(HttpServletResponse response,
                                                            @ApiParam(value = "eventId",required = true)@RequestParam("eventId") Long eventId){
        chooseMemberEventService.exportEventStatistics(response, eventId);
        return ServerResponseEntity.success();
    }

    @ParameterValid
    @PostMapping("/add/stock")
    @ApiOperation("高价值会员活动增加库存")
    public ServerResponseEntity addStockNum(@RequestBody UpdateChooseMemberEventStockDTO addStockDTO,
                                                            BindingResult result){
        chooseMemberEventService.addStockNum(addStockDTO);
        return ServerResponseEntity.success();
    }
    @ParameterValid
    @PostMapping("/enable/or/disable")
    @ApiOperation("启用或停用高价值会员活动")
    public ServerResponseEntity enableOrDisable(@Valid @RequestBody EnableOrDisableDTO enableOrDisableDTO,
                                                BindingResult bindingResult){
        chooseMemberEventService.enableOrDisable(enableOrDisableDTO.getEventId());
        return ServerResponseEntity.success();
    }

    @ParameterValid
    @PostMapping("/edit")
    @ApiOperation("高价值会员修改")
    public ServerResponseEntity edit(@Valid @RequestBody EditChooseMemberEventDTO editChooseMemberEventDTO, BindingResult result){
        chooseMemberEventService.edit(editChooseMemberEventDTO);
        return ServerResponseEntity.success();
    }


    @GetMapping("/detail")
    @ApiOperation("高价值会员详情")
    public ServerResponseEntity<ChooseMemberEventVO> detail(@RequestParam("eventId") Long eventId){
        return chooseMemberEventService.detail(eventId);
    }
}

