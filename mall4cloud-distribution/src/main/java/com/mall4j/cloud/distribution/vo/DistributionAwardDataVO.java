package com.mall4j.cloud.distribution.vo;

/**
 * @author cl
 * @date 2021-08-18 15:21:12
 */
public class DistributionAwardDataVO {
    /**
     * 奖励数额(分)
     */
    private Long awardNumber;

    /**
     * 上级奖励数额(分)
     */
    private Long parentAwardNumber;

    /**
     * 奖励比例(0 按比例 1 按固定数值)
     */
    private Integer awardMode;

    public Long getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(Long awardNumber) {
        this.awardNumber = awardNumber;
    }

    public Long getParentAwardNumber() {
        return parentAwardNumber;
    }

    public void setParentAwardNumber(Long parentAwardNumber) {
        this.parentAwardNumber = parentAwardNumber;
    }

    public Integer getAwardMode() {
        return awardMode;
    }

    public void setAwardMode(Integer awardMode) {
        this.awardMode = awardMode;
    }

    @Override
    public String toString() {
        return "DistributionAwardDataVO{" +
                "awardNumber=" + awardNumber +
                ", parentAwardNumber=" + parentAwardNumber +
                ", awardMode=" + awardMode +
                '}';
    }
}
