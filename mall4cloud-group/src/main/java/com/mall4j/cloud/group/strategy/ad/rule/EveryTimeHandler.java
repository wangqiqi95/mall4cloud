package com.mall4j.cloud.group.strategy.ad.rule;

import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.vo.PopUpAdRuleVO;

import java.util.List;


public class EveryTimeHandler implements PushRuleHandler {
    @Override
    public boolean validation(PopUpAd popUpAd, List<PopUpAdRuleVO> ruleList) {
        return true;
    }

    @Override
    public boolean validationTime(List<String> tagList, String pushRule) {
        return false;
    }

}
