package com.mall4j.cloud.common.product.bo;

/**
 * @Author lth
 * @Date 2021/8/10 14:07
 */
public class EsDistributionInfoBO {
    /**
     * 分销商品表id
     */
    private Long distributionSpuId;

    /**
     * 状态(0:商家下架 1:商家上架 2:违规下架 3:平台审核)
     */
    private Integer state;

    /**
     * 奖励方式(0 按比例 1 按固定数值)
     */
    private Integer awardMode;

    /**
     * 上级奖励设置(0 关闭 1开启)
     */
    private Integer parentAwardSet;

    /**
     * 奖励数额(奖励方式为0时，代表百分比*100，为1时代表实际奖励金额*100）
     */
    private Long awardNumbers;

    /**
     * 上级奖励数额(奖励方式为0时，表示百分比*100，为1时代表实际奖励金额*100）
     */
    private Long parentAwardNumbers;

    public Long getDistributionSpuId() {
        return distributionSpuId;
    }

    public void setDistributionSpuId(Long distributionSpuId) {
        this.distributionSpuId = distributionSpuId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAwardMode() {
        return awardMode;
    }

    public void setAwardMode(Integer awardMode) {
        this.awardMode = awardMode;
    }

    public Integer getParentAwardSet() {
        return parentAwardSet;
    }

    public void setParentAwardSet(Integer parentAwardSet) {
        this.parentAwardSet = parentAwardSet;
    }

    public Long getAwardNumbers() {
        return awardNumbers;
    }

    public void setAwardNumbers(Long awardNumbers) {
        this.awardNumbers = awardNumbers;
    }

    public Long getParentAwardNumbers() {
        return parentAwardNumbers;
    }

    public void setParentAwardNumbers(Long parentAwardNumbers) {
        this.parentAwardNumbers = parentAwardNumbers;
    }

    @Override
    public String toString() {
        return "EsDistributionInfoBO{" +
                "distributionSpuId=" + distributionSpuId +
                ", state=" + state +
                ", awardMode=" + awardMode +
                ", parentAwardSet=" + parentAwardSet +
                ", awardNumbers=" + awardNumbers +
                ", parentAwardNumbers=" + parentAwardNumbers +
                '}';
    }
}
