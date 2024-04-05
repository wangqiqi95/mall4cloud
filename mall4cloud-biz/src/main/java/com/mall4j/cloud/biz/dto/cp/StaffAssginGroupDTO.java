package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class StaffAssginGroupDTO {
    @ApiModelProperty("原员工id")
    @NotNull(message = "原员工id不能为空")
    private List<Long> addBys;

    @ApiModelProperty("替换的员工id")
    @NotNull(message = "replaceStaffId不能为空")
    private Long replaceBy;

    @ApiModelProperty("分配类型：0:在职分配-客户/1:离职分配-客户/2:离职分配-客群/3:在职分配-客群")
    @NotNull(message = "分配类型不能为空")
    private Integer assignType;

    @ApiModelProperty("替换的客户企业微信群id")
    private List<String> custIds;

}
