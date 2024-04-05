package com.mall4j.cloud.distribution.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DistributionStoreActivityUserDTO {

    @NotNull(message = "活动ID不能为空")
    @ApiModelProperty("活动ID")
    private Long activityId;

    @NotBlank(message = "报名用户姓名不能为空")
    @ApiModelProperty("报名用户姓名")
    private String userName;

    @NotNull(message = "报名用户性别不能为空")
    @ApiModelProperty("报名用户性别 1-男 2-女")
    private Integer userGender;

    @NotBlank(message = "报名用户手机号不能为空")
    @ApiModelProperty("报名用户手机号")
    private String userMobile;

    @ApiModelProperty("报名用户年龄")
    private Integer userAge;

    @ApiModelProperty("报名用户证件号")
    private String userIdCard;

    @ApiModelProperty("触点号")
    private String tentacleNo;

    @ApiModelProperty("衣服尺码")
    private String clothesSize;

    @ApiModelProperty("鞋子尺码")
    private String shoesSize;


}
