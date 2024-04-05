package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 秒杀信息VO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class SeckillAdminVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀活动id
     */
    @ApiModelProperty("秒杀活动id")
    private Long seckillId;

    @ApiModelProperty("开始日期")
    private Date startTime;

    @ApiModelProperty("具体开始时间")
    private Date dateTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("所选批次")
    private Integer selectedLot;

    @ApiModelProperty("参与商家数")
    private Integer joinShopNum;

    @ApiModelProperty("抢购商品数量")
    private Integer prodNum;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("能否参与活动1.能 0.不能")
    private Integer canJoin;

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Integer getCanJoin() {
        return canJoin;
    }

    public void setCanJoin(Integer canJoin) {
        this.canJoin = canJoin;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getSelectedLot() {
        return selectedLot;
    }

    public void setSelectedLot(Integer selectedLot) {
        this.selectedLot = selectedLot;
    }

    public Integer getJoinShopNum() {
        return joinShopNum;
    }

    public void setJoinShopNum(Integer joinShopNum) {
        this.joinShopNum = joinShopNum;
    }

    public Integer getProdNum() {
        return prodNum;
    }

    public void setProdNum(Integer prodNum) {
        this.prodNum = prodNum;
    }

    @Override
    public String toString() {
        return "SeckillAdminVO{" +
                "startTime=" + startTime +
                ", dateTime=" + dateTime +
                ", endTime=" + endTime +
                ", selectedLot=" + selectedLot +
                ", joinShopNum=" + joinShopNum +
                ", prodNum=" + prodNum +
                ", num=" + num +
                ", canJoin=" + canJoin +
                '}';
    }
}
