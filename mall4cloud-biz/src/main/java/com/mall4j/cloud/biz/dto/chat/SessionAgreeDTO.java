package com.mall4j.cloud.biz.dto.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会话同意DTO
 */
@Data
public class SessionAgreeDTO {

    private Long id;
    @ApiModelProperty(value = "同意状态")
    private String agreeStatus;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "员工")
    private String userId;

    @ApiModelProperty(value = "客户昵称")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String userPhone;

    private List<String> exteranalOpenIds;
}
