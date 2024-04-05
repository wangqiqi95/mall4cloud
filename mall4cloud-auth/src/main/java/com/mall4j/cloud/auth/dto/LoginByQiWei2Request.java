package com.mall4j.cloud.auth.dto;

import lombok.Data;

@Data
public class LoginByQiWei2Request {
    private String userid;
    private Integer qiweiStatus;
    private String sessionKey;
    private String encryptedData;
    private String ivStr;
}
