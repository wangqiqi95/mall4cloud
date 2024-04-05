package com.mall4j.cloud.api.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CpUserCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工名称",required = true)
    private String staffName;

    @ApiModelProperty(value = "员工手机号",required = true)
    private String mobile;

    @ApiModelProperty(value = "员工id",required = true)
    private String staffId;

    @ApiModelProperty(value = "员工工号",required = true)
    private String staffNo;

    @ApiModelProperty(value = "组织id集合",required = true)
    private String orgIds;

    @ApiModelProperty("员工邮箱")
    private String email;

    @ApiModelProperty("员工职位")
    private String position;

    @ApiModelProperty("性别")
    private Integer sex;
}
