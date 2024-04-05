package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户微信公众号关注记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-01 15:35:00
 */
@Data
public class UserWeixinAccountFollowDataVo implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "关注人数")
    private Integer followCount;

    @ApiModelProperty(value = "取消关注人数")
    private Integer unFollowCount;

    @ApiModelProperty(value = "活跃人数")
    private Integer activeCount;

    @ApiModelProperty(value = "活跃度")
    private String activeRatio;

    @ApiModelProperty(value = "分析数据")
    private List<UserWeixinAccountFollowDataListVo> follows;
}
