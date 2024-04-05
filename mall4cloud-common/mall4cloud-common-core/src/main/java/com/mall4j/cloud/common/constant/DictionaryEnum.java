package com.mall4j.cloud.common.constant;

import lombok.Getter;

@Getter
public enum DictionaryEnum {
    //高价值会员活动兑奖方式字典
    CHOOSE_MEMBER_EVENT_EXCHANGE_TYPE("CHOOSE_MEMBER_EVENT_EXCHANGE_TYPE"),
    //高价值会员活动兑换记录发货状态字典查询
    CHOOSE_MEMBER_EVENT_EXCHANGE_RECORD_DELIVERY_STATUS("CHOOSE_MEMBER_EVENT_EXCHANGE_RECORD_DELIVERY_STATUS"),
    //标签分组类型
    TAG_GROUP_TYPE("TAG_GROUP_TYPE");

    private String dictionaryType;

    DictionaryEnum(String dictionaryType){
        this.dictionaryType = dictionaryType;
    }

}
