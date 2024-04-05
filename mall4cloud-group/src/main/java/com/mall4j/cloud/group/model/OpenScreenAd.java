package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "open_screen_ad")
public class OpenScreenAd implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String activityName;
    private Date activityBeginTime;
    private Date activityEndTime;
    private String isAllShop;
    private String activityPicUrl;
    private String fansLevel;
    private Integer redirectType;
    private String redirectUrl;
    private Integer adFrequency;
    private Integer weight;
    private Integer status;
    private Integer deleted;
    private Date createTime;
    private Long createUserId;
    private String createUserName;
    private Date updateTime;
    private Long updateUserId;
    private String updateUserName;
}
