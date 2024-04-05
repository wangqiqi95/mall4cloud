package com.mall4j.cloud.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 积分清零活动配置
 * @Author axin
 * @Date 2022-10-17 18:05
 **/
@Data
//@Component
//@ConfigurationProperties(prefix = "score.clearing.activity")
//@RefreshScope
public class ScoreClearingActivityConfig {
    /**
     * 新用户积分
     */
    private Integer newUserScore;

    /**
     * 旧用户积分
     */
    private Integer oldUserScore;

    /**
     * 最大助力次数
     */
    private Integer maxBigHelp;

    /**
     * 活动海报
     */
    private String activityPoster;

    /**
     * 活动开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateTime;
}
