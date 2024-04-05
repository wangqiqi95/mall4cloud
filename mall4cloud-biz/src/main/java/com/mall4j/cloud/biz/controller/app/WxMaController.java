package com.mall4j.cloud.biz.controller.app;

import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.biz.dto.ApplySProductSubscriptTMessageDTO;
import com.mall4j.cloud.biz.dto.ApplySubscriptTMessageDTO;
import com.mall4j.cloud.biz.dto.ApplySubscriptTMessageOpenIdDTO;
import com.mall4j.cloud.biz.dto.ScoreProductSubscriptRecordDTO;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageMapper;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptUserRecordService;
import com.mall4j.cloud.biz.vo.ScoreProductSubscriptRecordVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 微信小程序接口
 *
 * @luzhengxiang
 * @create 2022-03-11 1:27 PM
 **/
@RequestMapping(value = "/wx/ma")
@RestController
@Api(tags = "微信小程序端接口")
public class WxMaController {

    @Autowired
    WeixinMaSubscriptTmessageMapper weixinMaSubscriptTmessageMapper;
    @Autowired
    WeixinMaSubscriptUserRecordService weixinMaSubscriptUserRecordService;

    /**
     * 根据类型查询小程序订阅消息模版
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "根据类型查询小程序订阅消息模版 业务类型：1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核", notes = "根据类型查询小程序订阅消息模版 业务类型：1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核")
    @GetMapping("/getSubscriptTmessage")
    public ServerResponseEntity<List<WeixinMaSubscriptTmessageVO>> getSubscriptTmessage(@RequestParam(value = "type",required = true) Integer type){
        List<WeixinMaSubscriptTmessageVO> tmessageVOs = weixinMaSubscriptTmessageMapper.getByType(type);
        return ServerResponseEntity.success(tmessageVOs);
    }

    /**
     * 用户接受发送订阅申请记录
     *
     * @param applySubscriptTMessageDTO
     * @return
     */
    @ApiOperation(value = "用户接受订阅申请记录", notes = "用户接受订阅申请记录")
    @PostMapping("/applySubscriptTMessage")
    public ServerResponseEntity<Void> applySubscriptTMessage(@RequestBody List<ApplySubscriptTMessageDTO> applySubscriptTMessageDTO){
        Long userId = AuthUserContext.get().getUserId();
        for (ApplySubscriptTMessageDTO messageDTO : applySubscriptTMessageDTO) {
            messageDTO.setUserId(userId);
            weixinMaSubscriptUserRecordService.applySubscriptTMessage(messageDTO);
        }

        return ServerResponseEntity.success();
    }


    /**
     * 用户接受积分兑礼商品到货发送订阅申请记录
     * @param applySProductSubscriptTMessageDto
     * @return
     */
    @ApiOperation(value = "用户接受积分兑礼商品到货通知订阅申请记录", notes = "用户接受积分兑礼商品到货通知订阅申请记录")
    @PostMapping("/applySProductSubscriptTMessage")
    public ServerResponseEntity<Void> applySProductSubscriptTMessage(@RequestBody ApplySProductSubscriptTMessageDTO applySProductSubscriptTMessageDto){
        Long userId = AuthUserContext.get().getUserId();
        applySProductSubscriptTMessageDto.setUserId(userId);
        weixinMaSubscriptUserRecordService.applySProductSubscriptTMessage(applySProductSubscriptTMessageDto);

        return ServerResponseEntity.success();
    }

    /**
     * 查询用户是否订阅积分礼品到货通知
     */
    @ApiOperation(value = "查询用户是否订阅过积分礼品到货通知", notes = "查询用户是否订阅过积分礼品到货通知")
    @PostMapping("/queryScoreProductSubscriptRecord")
    public ServerResponseEntity<List<ScoreProductSubscriptRecordVO>> queryScoreProductSubscriptRecord(@RequestBody ScoreProductSubscriptRecordDTO scoreProductSubscriptRecordDTO){
        Long userId = AuthUserContext.get().getUserId();
        scoreProductSubscriptRecordDTO.setUserId(userId);
        List<ScoreProductSubscriptRecordVO> scoreProductSubscriptRecordVO = weixinMaSubscriptUserRecordService.queryScoreProductSubscriptRecord(scoreProductSubscriptRecordDTO);
        return ServerResponseEntity.success(scoreProductSubscriptRecordVO);
    }

    /**
     * 用户接受发送订阅申请记录
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "用户接受订阅申请记录", notes = "用户接受订阅申请记录")
    @PostMapping("/ua/applyActivitySubscriptTMessage")
    public ServerResponseEntity<String> applyActivitySubscriptTMessage(@RequestBody @Valid ApplySubscriptTMessageOpenIdDTO dto){
        return ServerResponseEntity.success(weixinMaSubscriptUserRecordService.applyActivitySubscriptTMessage(dto));
    }

    /**
     * 根据类型查询小程序订阅消息模版
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "根据类型查询小程序订阅消息模版 业务类型：1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核", notes = "根据类型查询小程序订阅消息模版 业务类型：1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核")
    @GetMapping("/ua/getSubscriptTmessage")
    public ServerResponseEntity<List<WeixinMaSubscriptTmessageVO>> getActivitySubscriptTmessage(@RequestParam(value = "type",required = true) Integer type){
        List<WeixinMaSubscriptTmessageVO> tmessageVOs = weixinMaSubscriptTmessageMapper.getByType(type);
        return ServerResponseEntity.success(tmessageVOs);
    }
}
