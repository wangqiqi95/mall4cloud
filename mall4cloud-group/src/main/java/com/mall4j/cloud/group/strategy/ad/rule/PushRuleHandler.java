package com.mall4j.cloud.group.strategy.ad.rule;

import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.vo.PopUpAdRuleVO;

import java.util.List;

public interface PushRuleHandler {

    boolean validation(PopUpAd popUpAd, List<PopUpAdRuleVO> ruleList);

    boolean validationTime(List<String> tagList,String pushRule);



}
