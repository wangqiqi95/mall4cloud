package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
public class UserScoreLockDTO {

    @NotNull(message = "orderId不能为空")
    @ApiModelProperty(value = "orderId",required=true)
    private Long orderId;

    @NotNull(message = "积分数量不能为空")
    @Min(value = 1,message = "积分数量不能为空")
    @ApiModelProperty(value = "积分数量",required=true)
    private Long score;


    private Integer orderType;

    public UserScoreLockDTO() {
    }

    public UserScoreLockDTO(Long orderId, Long score, Integer orderType) {
        this.orderId = orderId;
        this.score = score;
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "UserScoreLockDTO{" +
                "orderId=" + orderId +
                ", score=" + score +
                ", orderType=" + orderType +
                '}';
    }
}
