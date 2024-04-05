package com.mall4j.cloud.api.group.feign.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPerfectDataActivityDTO implements Serializable {
    private Long userId;
    private Long shopId;
}
