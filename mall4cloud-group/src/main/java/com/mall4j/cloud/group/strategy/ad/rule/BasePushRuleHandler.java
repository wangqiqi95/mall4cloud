package com.mall4j.cloud.group.strategy.ad.rule;


import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SpringContextUtils;

import com.mall4j.cloud.group.constant.PopUpAdPushRuleTypeEnum;
import com.mall4j.cloud.group.manager.PopUpAdAttachmentManager;
import com.mall4j.cloud.group.manager.PopUpAdManager;
import com.mall4j.cloud.group.manager.PopUpAdRuleManager;
import com.mall4j.cloud.group.manager.PopUpAdUserOperateManager;
import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.service.QuestionnaireService;
import com.mall4j.cloud.group.strategy.ad.attachment.enums.AttachmentHandlerEnum;
import com.mall4j.cloud.group.strategy.ad.rule.enums.PushRuleHandlerEnum;
import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import com.mall4j.cloud.group.vo.PopUpAdRuleVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class BasePushRuleHandler {

    @Autowired
    private PopUpAdRuleManager popUpAdRuleManager;

    @Autowired
    private PopUpAdUserOperateManager popUpAdUserOperateManager;

    @Autowired
    private PopUpAdAttachmentManager popUpAdAttachmentManager;

    @Autowired
    private QuestionnaireService questionnaireService;

    public boolean validation(PopUpAd popUpAd, Long storeId, Long userId){

        //声明业务场景标识
        boolean operateFlag = true;

        boolean timeRule;


        PushRuleHandler pushRuleHandler;
        //没有规定时段，直接返回
        if (Objects.isNull(popUpAd.getPushRule())){
            timeRule = true;
        }else {
            List<PopUpAdRuleVO> ruleVOList = popUpAdRuleManager.getTheRuleByAdId(popUpAd.getPopUpAdId());
            pushRuleHandler = this.getHandler(popUpAd.getPushRule());
            //校验弹出时段是否匹配
            timeRule = pushRuleHandler.validation(popUpAd,ruleVOList);
        }

        //校验是否属于问卷，是的话需要查询是否已进行过相关操作
        if (popUpAd.getAttachmentType().equals(AttachmentHandlerEnum.QUESTIONNAIRE.getContentType())){
            PopUpAdAttachmentVO questionnaire = popUpAdAttachmentManager
                    .getPopUpAdAttachmentByAdId(popUpAd.getPopUpAdId()).get(0);
            operateFlag = questionnaireService.checkUserAuth(questionnaire.getBusinessId(), storeId, userId);
        }

        //校验是否属于优惠券场景，是的话需要查询是否已进行过相关操作
        if (AttachmentHandlerEnum.COUPON.getContentType().equals(popUpAd.getAttachmentType())){
            operateFlag = popUpAdUserOperateManager.validationOperation(popUpAd.getPopUpAdId());
        }

        return timeRule && operateFlag;
    }

    private PushRuleHandler getHandler(String popUpAdPushRule){
        Class handler = PushRuleHandlerEnum.valueOf(popUpAdPushRule).getHandler();

        PushRuleHandler pushRuleHandler = (PushRuleHandler) SpringContextUtils.getBean(handler);

        return pushRuleHandler;
    }

}
