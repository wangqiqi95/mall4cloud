package com.mall4j.cloud.api.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PerfectDataDTO implements Serializable {
    private Long userId;
    private Long shopId;
}
