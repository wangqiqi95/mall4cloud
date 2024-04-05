package com.mall4j.cloud.common.product.vo;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;
/**
 * spu活动VO
 *
 * @author YXF
 * @date 2021-03-03 09:00:09
 */
public class SpuActivityAppVO {

    @ApiModelProperty("满减活动")
    private List<SpuDiscountAppVO> discountList;

    @ApiModelProperty("满减活动")
    private List<SpuCouponAppVO> couponList;

    @ApiModelProperty("满减活动")
    private GroupActivitySpuVO groupActivity;

    @ApiModelProperty("秒杀活动活动")
    private SekillActivitySpuVO sekillActivitySpuVO;

    public SekillActivitySpuVO getSekillActivitySpuVO() {
        return sekillActivitySpuVO;
    }

    public void setSekillActivitySpuVO(SekillActivitySpuVO sekillActivitySpuVO) {
        this.sekillActivitySpuVO = sekillActivitySpuVO;
    }

    public List<SpuDiscountAppVO> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<SpuDiscountAppVO> discountList) {
        this.discountList = discountList;
    }

    public List<SpuCouponAppVO> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<SpuCouponAppVO> couponList) {
        this.couponList = couponList;
    }

    public GroupActivitySpuVO getGroupActivity() {
        return groupActivity;
    }

    public void setGroupActivity(GroupActivitySpuVO groupActivity) {
        this.groupActivity = groupActivity;
    }

    @Override
    public String toString() {
        return "SpuActivityAppVO{" +
                "discountList=" + discountList +
                ", couponList=" + couponList +
                ", groupActivity=" + groupActivity +
                '}';
    }
}
