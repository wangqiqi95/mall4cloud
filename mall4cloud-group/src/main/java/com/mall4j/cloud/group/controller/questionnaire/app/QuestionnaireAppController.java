package com.mall4j.cloud.group.controller.questionnaire.app;

import cn.hutool.http.Header;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.questionnaire.*;
import com.mall4j.cloud.group.service.QuestionnaireService;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireDetailVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserAnswerRecordPageVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserGiftAddrVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 问卷信息表
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@RestController("appQuestionnaireController")
@RequestMapping("/questionnaireapp")
@Api(tags = "问卷小程序端接口")
@Slf4j
public class QuestionnaireAppController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @GetMapping("/ma/get")
    @ApiOperation(value = "简易活动信息", notes = "简易活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动ID", required = true, dataType = "Long", paramType = "query")
    })
    public ServerResponseEntity<QuestionnaireDetailVO> simpleInfo(@RequestParam Long id){
        return ServerResponseEntity.success(questionnaireService.simpleInfo(id));
    }

	@GetMapping("/ma/info")
    @ApiOperation(value = "获取问卷信息表 本接口会同步生成用户的问卷浏览记录", notes = "获取问卷信息表 本接口会同步生成用户的问卷浏览记录")
    public ServerResponseEntity<QuestionnaireDetailVO> appInfo(@RequestParam("id") Long id,
                                                         @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId){
        UserInfoInTokenBO info = AuthUserContext.get();
        Long userId = null;
        if (Objects.nonNull(info)) {
            userId = AuthUserContext.get().getUserId();
        }
//        Long userId = 5l;
        return ServerResponseEntity.success(questionnaireService.appInfo(id,storeId,userId));
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交问卷", notes = "提交问卷")
    public ServerResponseEntity<Void> submit(@Valid @RequestBody SubmitDTO submitDTO,
                                             @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId){
        Long userId = AuthUserContext.get().getUserId();
        submitDTO.setUserId(userId);
        submitDTO.setStoreId(storeId);
        questionnaireService.submit(submitDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/receivePrize")
    @ApiOperation(value = "问卷领奖 如果奖品类型为抽奖，抽完奖结束后调用此接口，其它类型领奖按钮点击调用此接口",
            notes = "问卷领奖 如果奖品类型为抽奖，抽完奖结束后调用此接口，其它类型领奖按钮点击调用此接口")
    public ServerResponseEntity<Void> receivePrize(@Valid @RequestBody ReceivePrizeDTO receivePrizeDTO,
                                                   @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId){
        Long userId = AuthUserContext.get().getUserId();
        receivePrizeDTO.setUserId(userId);
        receivePrizeDTO.setStoreId(storeId);
        questionnaireService.receivePrize(receivePrizeDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/award")
    @ApiOperation(value = "实物奖品填写发货地址", notes = "实物奖品填写发货地址")
    public ServerResponseEntity<Void> fillShippingAddress(@RequestBody ShippingAddressDTO shippingAddressDTO){
        Long userId = AuthUserContext.get().getUserId();
        shippingAddressDTO.setUserId(userId);
        questionnaireService.fillShippingAddress(shippingAddressDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/answerPage")
    @ApiOperation(value = "获取问卷答卷信息表列表", notes = "获取问卷答卷信息表列表")
    public ServerResponseEntity<PageVO<QuestionnaireUserAnswerRecordPageVO>> answerPage(@Valid PageDTO pageDTO, AnswerPageDTO answerPageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        answerPageDTO.setUserId(userId);
        PageVO<QuestionnaireUserAnswerRecordPageVO> questionnairePage = questionnaireService.answerPage(pageDTO,answerPageDTO);
        return ServerResponseEntity.success(questionnairePage);
    }

    @GetMapping("/gift_addr")
    @ApiOperation(value = "用户实物奖品地址信息详情", notes = "用户实物奖品地址信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID", required = true, dataType = "Long", paramType = "path")
    })
    public ServerResponseEntity<QuestionnaireUserGiftAddrVO> userGiftAddrInfo(@RequestParam Long activityId){
        Long userId = AuthUserContext.get().getUserId();
        QuestionnaireUserGiftAddrVO vo = questionnaireService.userGiftAddrInfo(activityId, userId);
        return ServerResponseEntity.success(vo);
    }

    @PostMapping("/point")
    @ApiOperation(value = "埋点，用于增加问卷浏览次数", notes = "埋点，用于增加问卷浏览次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID", required = true, dataType = "Long", paramType = "path")
    })
    public ServerResponseEntity<Void> point(@RequestParam Long activityId){
        Long userId = AuthUserContext.get().getUserId();
        questionnaireService.point(userId, activityId);
        return ServerResponseEntity.success();
    }

}
