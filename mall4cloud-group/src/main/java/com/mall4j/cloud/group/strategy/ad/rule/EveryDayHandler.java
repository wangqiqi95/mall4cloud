package com.mall4j.cloud.group.strategy.ad.rule;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.constant.PopUpAdUserOperateEnum;
import com.mall4j.cloud.group.manager.PopUpAdUserOperateManager;
import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.vo.PopUpAdRuleVO;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Component
public class EveryDayHandler implements PushRuleHandler{

    @Autowired
    private PopUpAdUserOperateManager popUpAdUserOperateManager;



    @Override
    public boolean validation(PopUpAd popUpAd, List<PopUpAdRuleVO> ruleList) {

        LocalTime now = LocalTime.now();

        //校验广告的推送时段
        PopUpAdRuleVO openScreenAdRuleVO = ruleList.stream()
                .filter(rule -> rule.getStartTime().isBefore(now)
                        && now.isBefore(rule.getEndTime()))
                .findFirst()
                .orElse(null);

        return Objects.nonNull(openScreenAdRuleVO);
    }

    @Override
    public boolean validationTime(List<String> tagList,String pushRule) {
        return false;
    }


}
