package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 客户_券_适用范围
 * 可以使用的门店
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public class CrmCouponGrpShops extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统编号
     */
    private Integer id;

    /**
     * 所属集团
     */
    private Integer copid;

    /**
     * 所属品牌
     */
    private Integer brandid;

    /**
     * 券库编号
     */
    private Integer coupongrpid;

    /**
     * 组织编号
     */
    private String shopcode;

    /**
     * 适用门店类型
     */
    private String shoptype;

    /**
     * 是否排除
     */
    private String isexclude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCopid() {
        return copid;
    }

    public void setCopid(Integer copid) {
        this.copid = copid;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public Integer getCoupongrpid() {
        return coupongrpid;
    }

    public void setCoupongrpid(Integer coupongrpid) {
        this.coupongrpid = coupongrpid;
    }

    public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    public String getIsexclude() {
        return isexclude;
    }

    public void setIsexclude(String isexclude) {
        this.isexclude = isexclude;
    }

    @Override
    public String toString() {
        return "CrmCouponGrpShops{" +
                "id=" + id +
                ",copid=" + copid +
                ",brandid=" + brandid +
                ",coupongrpid=" + coupongrpid +
                ",shopcode=" + shopcode +
                ",shoptype=" + shoptype +
                ",isexclude=" + isexclude +
                '}';
    }
}
