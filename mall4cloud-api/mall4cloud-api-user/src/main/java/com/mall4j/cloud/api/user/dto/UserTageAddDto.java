package com.mall4j.cloud.api.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserTageAddDto {
    private List<Long> userTagIds;

    private String qiWeiUserId;
}
