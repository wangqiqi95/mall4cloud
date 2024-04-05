package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VO
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
@Data
public class UserStaffCpRelationVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("会员Id")
    private Long userId;

    @ApiModelProperty("会员企微id")
    private String qiWeiUserId;

    @ApiModelProperty("员工id")
    private Long staffId;

	@ApiModelProperty("员工Name")
	private String staffName;

	@ApiModelProperty("员工职务")
	private String staffPosition;

    @ApiModelProperty("员工企微id")
    private String qiWeiStaffId;

    @ApiModelProperty("绑定关系 1-绑定 2- 解绑")
    private Integer status;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("更新时间")
	private Date updateTime;

	@ApiModelProperty("最近联系时间")
	private Date lastContactTime;

    @ApiModelProperty("添加方式")
    private Integer cpAddWay;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("外部联系人性别 0-未知 1-男性 2-女性")
    private Integer gender;



}
