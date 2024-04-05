package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户微信公众号关注记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-01 15:35:00
 */
@Data
public class UserWeixinAccountFollowDataListVo implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态 1:关注 2:取消关注 3:活跃")
    private Integer status;

    @ApiModelProperty(value = "总数")
    private Integer count;

    @ApiModelProperty(value = "时间")
	private String time;

    private String appId;
}
