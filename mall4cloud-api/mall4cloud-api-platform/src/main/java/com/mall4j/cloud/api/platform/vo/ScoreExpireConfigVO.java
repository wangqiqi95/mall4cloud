package com.mall4j.cloud.api.platform.vo;

/**
 * @author Pineapple
 * @date 2021/5/20 14:37
 */
public class ScoreExpireConfigVO {
    /**
     * 过期年限
     */
    private Integer expireYear;

    /**
     * 积分过期开关
     */
    private Boolean scoreExpireSwitch;

    public Integer getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(Integer expireYear) {
        this.expireYear = expireYear;
    }

    public Boolean getScoreExpireSwitch() {
        return scoreExpireSwitch;
    }

    public void setScoreExpireSwitch(Boolean scoreExpireSwitch) {
        this.scoreExpireSwitch = scoreExpireSwitch;
    }

    @Override
    public String toString() {
        return "ScoreExpireConfigVO{" +
                "expireYear=" + expireYear +
                ", scoreExpireSwitch=" + scoreExpireSwitch +
                '}';
    }
}
