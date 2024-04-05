package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("order_gift")
public class OrderGift implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String activityName;
    private Date activityBeginTime;
    private Date activityEndTime;
    private Integer isAllShop;
    private Integer giftLimit;
    private String applyCommodityIds;
    private Integer status;
    private Integer deleted;
    private Date createTime;
    private Integer createUserId;
    private String createUserName;
    private Date updateTime;
    private Integer updateUserId;
    private String updateUserName;
}
