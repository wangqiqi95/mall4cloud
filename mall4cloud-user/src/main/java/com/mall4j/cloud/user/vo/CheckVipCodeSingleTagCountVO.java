package com.mall4j.cloud.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CheckVipCodeSingleTagCountVO {

    @ApiModelProperty("标签组ID")
    Long tagGroupId;


    @ApiModelProperty("标签组名")
    String groupName;


    @ApiModelProperty("用户拥有的该标签组内标签数")
    Integer markingCount;

}
