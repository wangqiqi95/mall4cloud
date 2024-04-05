package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum GarbageDataForStoreEnum {

    UNKNOWN(-1L,"Unknown","未知门店");

    private Long storeId;

    private String storeCode;

    private String storeName;

    GarbageDataForStoreEnum(Long storeId, String storeCode, String storeName){
        this.storeCode = storeCode;
        this.storeId = storeId;
        this.storeName = storeName;
    }

}
