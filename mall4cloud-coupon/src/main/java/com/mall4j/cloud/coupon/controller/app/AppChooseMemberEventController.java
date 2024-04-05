package com.mall4j.cloud.coupon.controller.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.coupon.dto.ExchangeChooseMemberEventDTO;
import com.mall4j.cloud.coupon.service.ChooseMemberEventService;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("ma/choose/member/event")
@Api(tags = {"高价值会员活动小程序端api"})
public class AppChooseMemberEventController {

    @Autowired
    private ChooseMemberEventService chooseMemberEventService;

    @ParameterValid
    @PostMapping("exchange")
    @ApiOperation("高价值会员活动兑换功能")
    public ServerResponseEntity exchange(@Valid @RequestBody ExchangeChooseMemberEventDTO exchangeChooseMemberEventDTO,
                                         BindingResult bindingResult){
        try{
            return chooseMemberEventService.exchange(exchangeChooseMemberEventDTO);
        }catch (LuckException luckException){
            return ServerResponseEntity.showFailMsg(luckException.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("小程序端高价值会员活动详情")
    public ServerResponseEntity<ChooseMemberEventVO> detail(@RequestParam("eventId") Long eventId){
        return chooseMemberEventService.detail(eventId);
    }

    @GetMapping("valid")
    @ApiOperation("高价值会员活动资格校验接口")
    public ServerResponseEntity exchange(@RequestParam("eventId") Long evenId){
        return chooseMemberEventService.eventMemberValid(evenId);
    }

    @GetMapping("/queryUserRecord")
    @ApiOperation("小程序端高价值会员兑换记录查询")
    public ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> queryUserRecord(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(value="pageSize", defaultValue = "10") Integer pageSize){
        return chooseMemberEventService.queryUserRecord(pageNo,pageSize);
    }
}
