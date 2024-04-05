package com.mall4j.cloud.group.constant;

import com.mall4j.cloud.group.strategy.ad.attachment.enums.AttachmentHandlerEnum;
import com.mall4j.cloud.group.strategy.ad.rule.enums.PushRuleHandlerEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PopUpAdPushRuleTypeEnum {

    TIME(
            Arrays.asList(PushRuleHandlerEnum.EVERY_WEEK.getRuleType(),
                    PushRuleHandlerEnum.EVERY_DAY.getRuleType(),
                    PushRuleHandlerEnum.EVERY_MONTH.getRuleType())
    ),
    OPERATE(
            Arrays.asList(AttachmentHandlerEnum.COUPON.getContentType(), AttachmentHandlerEnum.QUESTIONNAIRE.getContentType())
    )
    ;

    private List<String> rule;

    PopUpAdPushRuleTypeEnum(List<String> rule){
        this.rule = rule;
    }

}
