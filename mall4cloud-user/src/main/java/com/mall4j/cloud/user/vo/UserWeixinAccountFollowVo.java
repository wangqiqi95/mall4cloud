package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.model.BaseModel;
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
public class UserWeixinAccountFollowVo  implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String appId;

	@ApiModelProperty(value = "公众号类型 0成人公众号 1儿童公众号 2lifestyle公众号 3sport公众号")
    private Integer type;

	@ApiModelProperty(value = "状态 1:关注 2:取消关注")
    private Integer status;

    private String openId;

	@ApiModelProperty(value = "unionid")
    private String unionId;

	@ApiModelProperty(value = "公众号名称")
	private String wxmpname;

    @ApiModelProperty(value = "关注时间")
	private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "取消关注时间")
	private Date unFollowTime;

}
