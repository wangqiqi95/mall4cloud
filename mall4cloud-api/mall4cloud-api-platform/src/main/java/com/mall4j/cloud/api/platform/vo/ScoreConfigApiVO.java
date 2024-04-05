

package com.mall4j.cloud.api.platform.vo;


import java.util.List;

/**
 * 积分配置信息
 * @author lhd
 */
public class ScoreConfigApiVO {
    /**
     * id
     */
    private Long id;

    /**
     * 参数名
     */
    private String paramKey;
    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 签到获取积分
     */
    private String signInScoreString;

    /**
     * 签到获取积分
     */
    private List<Integer> signInScore;

    /**
     * 注册获取积分
     */
    private Long registerScore;

    /**
     * 购物开关
     */
    private Boolean shoppingScoreSwitch;

    /**
     * 购物获取积分(x元获取1积分)
     */
    private Long shoppingGetScore;

    /**
     * 购物使用积分抵现(x积分抵扣1元）
     */
    private Long shoppingUseScoreRatio;

    /**
     * 平台使用积分分类上限比例
     */
    private Double useDiscount;
    /**
     * 购物积分分类获取上限比例
     */
    private Double getDiscount;

    public Double getUseDiscount() {
        return useDiscount;
    }

    public void setUseDiscount(Double useDiscount) {
        this.useDiscount = useDiscount;
    }

    public Double getGetDiscount() {
        return getDiscount;
    }

    public void setGetDiscount(Double getDiscount) {
        this.getDiscount = getDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getSignInScoreString() {
        return signInScoreString;
    }

    public void setSignInScoreString(String signInScoreString) {
        this.signInScoreString = signInScoreString;
    }

    public List<Integer> getSignInScore() {
        return signInScore;
    }

    public void setSignInScore(List<Integer> signInScore) {
        this.signInScore = signInScore;
    }

    public Long getRegisterScore() {
        return registerScore;
    }

    public void setRegisterScore(Long registerScore) {
        this.registerScore = registerScore;
    }

    public Boolean getShoppingScoreSwitch() {
        return shoppingScoreSwitch;
    }

    public void setShoppingScoreSwitch(Boolean shoppingScoreSwitch) {
        this.shoppingScoreSwitch = shoppingScoreSwitch;
    }

    public Long getShoppingGetScore() {
        return shoppingGetScore;
    }

    public void setShoppingGetScore(Long shoppingGetScore) {
        this.shoppingGetScore = shoppingGetScore;
    }

    public Long getShoppingUseScoreRatio() {
        return shoppingUseScoreRatio;
    }

    public void setShoppingUseScoreRatio(Long shoppingUseScoreRatio) {
        this.shoppingUseScoreRatio = shoppingUseScoreRatio;
    }

    @Override
    public String toString() {
        return "ScoreConfigVO{" +
                "id=" + id +
                ", paramKey='" + paramKey + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", signInScoreString='" + signInScoreString + '\'' +
                ", signInScore=" + signInScore +
                ", registerScore=" + registerScore +
                ", shoppingScoreSwitch=" + shoppingScoreSwitch +
                ", shoppingGetScore=" + shoppingGetScore +
                ", shoppingUseScoreRatio=" + shoppingUseScoreRatio +
                ", useDiscount=" + useDiscount +
                ", getDiscount=" + getDiscount +
                '}';
    }
}
