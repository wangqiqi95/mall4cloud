package com.mall4j.cloud.api.openapi.skq_erp.response;

import lombok.Data;

@Data
public class FriendlyWalkCheckUserExistResponse {

    private Boolean isExist;

    private String vipcode;

    private String phone;

    private String unionId;

}
