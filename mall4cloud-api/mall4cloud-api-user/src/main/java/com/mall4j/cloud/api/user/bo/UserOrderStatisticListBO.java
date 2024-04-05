package com.mall4j.cloud.api.user.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用户相关订单统计数据
 * @author: cl
 * @date: 2021-04-14 14:04:01
 */
public class UserOrderStatisticListBO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("老会员成交数据")
    private List<UserOrderStatisticBO> oldUserOrderStatisticList;
    @ApiModelProperty("新会员成交数据")
    private List<UserOrderStatisticBO> newUserOrderStatisticList;

    public List<UserOrderStatisticBO> getOldUserOrderStatisticList() {
        return oldUserOrderStatisticList;
    }

    public void setOldUserOrderStatisticList(List<UserOrderStatisticBO> oldUserOrderStatisticList) {
        this.oldUserOrderStatisticList = oldUserOrderStatisticList;
    }

    public List<UserOrderStatisticBO> getNewUserOrderStatisticList() {
        return newUserOrderStatisticList;
    }

    public void setNewUserOrderStatisticList(List<UserOrderStatisticBO> newUserOrderStatisticList) {
        this.newUserOrderStatisticList = newUserOrderStatisticList;
    }

    @Override
    public String toString() {
        return "UserOrderStatisticListBO{" +
                "oldUserOrderStatisticList=" + oldUserOrderStatisticList +
                ", newUserOrderStatisticList=" + newUserOrderStatisticList +
                '}';
    }
}
