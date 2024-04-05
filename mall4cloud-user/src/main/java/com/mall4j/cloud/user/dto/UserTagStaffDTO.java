package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 导购标签关系信息DTO
 *
 * @author ZengFanChang
 * @date 2022-02-13 22:25:55
 */
@Data
public class UserTagStaffDTO {

//    @ApiModelProperty("导购标签ID")
//    private Long staffTagId;

    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("用户标签ID")
    private Long userTagId;

    @ApiModelProperty("用户标签名称")
    private String tagName;

    @ApiModelProperty("用户标签类型 2系统 3导购自定义")
    private Integer tagType;

    @ApiModelProperty("用户ID集合")
    private List<Long> userIds;

    @ApiModelProperty("用户标签ID集合")
    private List<Long> userTagIds;

}
