package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微页面埋点信息更新接口
 */
@Data
public class RenovationBurialPointDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("微页面装修ID")
    private Long renovationId;

    @ApiModelProperty("待增加的浏览次数")
    private Integer incrBrowseNum;

    @ApiModelProperty("待增加的浏览人数")
    private Integer incrBrowsePeopleNum;

}
