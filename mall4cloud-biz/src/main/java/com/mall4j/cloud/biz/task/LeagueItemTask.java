package com.mall4j.cloud.biz.task;


import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.biz.dto.channels.league.DisableProductReqDto;
import com.mall4j.cloud.biz.service.channels.LeagueItemService;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 联盟商品定时任务
 * @Author axin
 * @Date 2023-04-27 12:14
 **/
@Component
@Slf4j
public class LeagueItemTask {

    @Autowired
    private LeagueItemService leagueItemService;

    @XxlJob("handleExpiredItem")
    public void handleExpiredItem() {
        log.info("处理过期商品定时任务开始--------------");
        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUsername("定时任务");
        AuthUserContext.set(userInfoInTokenBO);
        leagueItemService.handleExpiredItem();
        AuthUserContext.clean();
        log.info("处理过期商品定时任务结束--------------");

    }
}
