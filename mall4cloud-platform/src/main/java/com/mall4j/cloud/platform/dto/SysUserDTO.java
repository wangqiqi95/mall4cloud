package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lhd
 * @date 2020/9/8
 */
@Data
public class SysUserDTO {

    @ApiModelProperty("平台用户id")
    private Long sysUserId;

    @NotBlank(message = "用户姓名不能为空")
    @ApiModelProperty("用户姓名")
    private String nickName;

    @NotBlank(message = "账户不能为空")
    @ApiModelProperty("账户")
    private String username;

    @ApiModelProperty("联系方式")
    private String phoneNum;

    @ApiModelProperty("角色id列表")
    private List<Long> roleIds;

    @ApiModelProperty("组织部门id")
    @NotBlank(message = "组织部门id不能为空")
    private String orgId;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("状态：0-正常/1-注销/2-离职")
    private Integer status;

    @ApiModelProperty("密码")
    private String password;

    @NotNull(message = "是否开通导购不能为空")
    @ApiModelProperty("是否开通导购：0否/1是")
    private Integer openPlatform=0;


}
