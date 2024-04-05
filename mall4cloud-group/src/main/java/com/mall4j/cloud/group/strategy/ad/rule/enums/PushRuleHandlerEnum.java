package com.mall4j.cloud.group.strategy.ad.rule.enums;

import com.mall4j.cloud.group.strategy.ad.rule.EveryDayHandler;
import com.mall4j.cloud.group.strategy.ad.rule.EveryMonthOrWeekHandler;
import lombok.Getter;

@Getter
public enum PushRuleHandlerEnum {

    EVERY_DAY("EVERY_DAY", EveryDayHandler.class),

    EVERY_WEEK("EVERY_WEEK", EveryMonthOrWeekHandler.class),
    EVERY_MONTH("EVERY_MONTH", EveryMonthOrWeekHandler.class),

//    COUPON("COUPON", OperationPushRuleHandler.class),
//
//    QUESTIONNAIRE("QUESTIONNAIRE", Object.class)
    ;

    private String ruleType;

    private Class handler;


    PushRuleHandlerEnum(String ruleType, Class handler){
        this.ruleType = ruleType;
        this.handler = handler;
    }

}
