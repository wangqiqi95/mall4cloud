package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.util.PriceUtil;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息
 *
 * @author YXF
 * @date 2020-12-23 15:27:24
 */
public class UserAdminDTO {

    /**
     * 用户id列表
     */
    private List<Long> userIds;
    /**
     * 正数代表增加，负数代表减少，只能输入整数，
     * 根据修改后的成长值重新计算会员等级
     */
    @Max(value = Integer.MAX_VALUE, message = "最大值不能超过" + Integer.MAX_VALUE)
    @Min(value = Integer.MIN_VALUE, message = "最小值不能小于" + Integer.MIN_VALUE)
    private Integer growth;
    /**
     * 修改成长值，是否赠送礼物
     *  0不赠送，1赠送
     */
    private Integer isSendReward;
    /**
     * 正数代表增加，负数代表减少，只能输入整数，
     * 修改会员积分
     */
    @Max(value = Long.MAX_VALUE, message = "最大值不能超过" + Long.MAX_VALUE)
    @Min(value = Long.MIN_VALUE, message = "最小值不能小于" + Long.MIN_VALUE)
    private Long score;

    /**
     * 标签id
     */
    private List<Long> tagList;

    /**
     * 修改余额
     * 正数代表增加，负数代表减少，只能输入数字，最多两位小数
     */
    @Max(value = PriceUtil.MAX_AMOUNT, message = "最大值不能超过" + PriceUtil.MAX_AMOUNT)
    @Min(value = Long.MIN_VALUE, message = "最小值不能小于" + Long.MIN_VALUE)
    private BigDecimal balanceValue;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getIsSendReward() {
        return isSendReward;
    }

    public void setIsSendReward(Integer isSendReward) {
        this.isSendReward = isSendReward;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public List<Long> getTagList() {
        return tagList;
    }

    public void setTagList(List<Long> tagList) {
        this.tagList = tagList;
    }

    public BigDecimal getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(BigDecimal balanceValue) {
        this.balanceValue = balanceValue;
    }

    @Override
    public String toString() {
        return "UserAdminDTO{" +
                "userIds=" + userIds +
                ", growthValue=" + growth +
                ", isSendReward=" + isSendReward +
                ", scoreValue=" + score +
                ", tagList=" + tagList +
                ", balanceValue=" + balanceValue +
                '}';
    }
}
