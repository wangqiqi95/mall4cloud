package com.mall4j.cloud.group.strategy.ad.rule;

import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.strategy.ad.rule.enums.PushRuleHandlerEnum;
import com.mall4j.cloud.group.vo.PopUpAdRuleVO;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class EveryMonthOrWeekHandler implements PushRuleHandler {


    @Override
    public boolean validation(PopUpAd popUpAd, List<PopUpAdRuleVO> ruleList) {



//        String pushRule = ruleList.get(0).getPopUpAdPushRule();

        List<String> tagList = Arrays.stream(popUpAd.getRuleTimeTag().split(",")).collect(Collectors.toList());

        boolean validationTime = this.validationTime(tagList,popUpAd.getPushRule());

        if (validationTime){
            LocalTime now = LocalTime.now();

            PopUpAdRuleVO openScreenAdRuleVO = ruleList.stream()
                    .filter(rule -> rule.getStartTime().isBefore(now)
                            && now.isBefore(rule.getEndTime()))
                    .findFirst()
                    .orElse(null);

            return Objects.nonNull(openScreenAdRuleVO);
        }

        return false;
    }

    @Override
    public boolean validationTime(List<String> tagList,String pushRule) {

        int tagValue;
        if (pushRule.equals(PushRuleHandlerEnum.EVERY_MONTH.getRuleType())) {
            tagValue = DateUtils.getTheDayByMonth(new Date());
        } else {
            tagValue = DateUtils.getTheDayByWeek(new Date());
        }

        for (String tag:tagList) {
            if (Integer.valueOf(tag).equals(tagValue)){
                return true;
            }
        }

        return false;
    }

//    public static void main(String[] args) {
//        String tag = "8,9";
//
//        String regex = ",";
//        String[] split = tag.split(regex);
//
//        List<String> tagList = Arrays.stream(tag.split(",")).collect(Collectors.toList());
//
//        int tagValue = DateUtils.getTheDayByMonth(new Date());
//
//
//        for (String yy:tagList) {
//            boolean flag = Integer.valueOf(yy).equals(tagValue);
//            if (flag){
//                System.out.println(flag);
//                return;
//            }
//
//        }
//
//
//
//        System.out.println(tagValue);
//    }

}
