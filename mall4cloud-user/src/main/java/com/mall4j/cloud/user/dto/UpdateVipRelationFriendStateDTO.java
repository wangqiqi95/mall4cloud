package com.mall4j.cloud.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateVipRelationFriendStateDTO {

    @NotNull(message = "导购ID不允许为空")
    private Long staffId;

    @NotNull(message = "用户ID不允许为空")
    private Long vipUserId;

    @NotBlank(message = "用户企微ID不允许为空")
    private String vipCpUserId;

}
