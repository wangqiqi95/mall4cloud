package com.mall4j.cloud.api.platform.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lhd
 * @date 2020/9/2
 */
@Data
public class SysUserVO {

	/**
	 * sysUserId
	 */
	@ApiModelProperty("平台用户id")
	private Long sysUserId;

	/**
	 * 昵称
	 */
	@ApiModelProperty("员工姓名")
	private String nickName;

	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("密码")
	private String password;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	@JsonSerialize(using = ImgJsonSerializer.class)
	private String avatar;

	/**
	 * 员工编号
	 */
	@ApiModelProperty("员工编号")
	private String code;

	/**
	 * 联系方式
	 */
	@ApiModelProperty("联系方式")
	private String phoneNum;

	@ApiModelProperty("节点id")
	private String orgId;
	@ApiModelProperty("节点名称")
	private List<String> orgs;

	@ApiModelProperty("角色id列表")
	private List<Long> roleIds;
	@ApiModelProperty("角色名称列表")
	private List<String> roles;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("状态: 0正常/1注销/2-离职")
	private Integer status;

	@ApiModelProperty("是否开通导购：0否/1是")
	private Integer openPlatform=0;

	private Integer hasAccount;
}
