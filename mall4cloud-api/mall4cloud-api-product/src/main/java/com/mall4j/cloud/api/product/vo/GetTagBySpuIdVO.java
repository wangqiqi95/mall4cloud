package com.mall4j.cloud.api.product.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GetTagBySpuIdVO {
    private  Long id;
    /**
     * 活动名称
     */
    private  String activityName;
    /**
     * 标签名称
     */
    private  String tagName;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private  Date endTime;
    /**
     * 是否全店 0 否 1是
     */
    private  Integer isAllShop;
    /**
     * 角标类型 1固定角标
     */
    private  Integer tagType;
    /**
     * 角标方位  1 左上 2 左下 3 右上 4 右下 5 全覆盖
     */
    private  Integer tagPosition;
    /**
     * 图标url
     */
    private  String tagUrl;



}
