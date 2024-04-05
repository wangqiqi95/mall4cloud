package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodeDTO extends AttachMentBaseDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("活动标题")
    @NotBlank(message = "活动标题不能为空")
    private String codeName;

    @ApiModelProperty("活码类型 0 批量单人 1 单人 2 多人")
    @NotNull(message = "活码类型不能为空")
    private Integer codeType;

    @ApiModelProperty("通过好友：0 自动通过， 1 手动通过")
    @NotNull(message = "通过好友不能为空")
    private Integer authType;

    @ApiModelProperty("欢迎语")
    @NotBlank(message = "欢迎语不能为空")
    private String slogan;

    @ApiModelProperty("标签，多个用逗号隔开")
    @NotEmpty(message = "标签不能为空")
    private List<StaffCodeTagDTO> tagList;

    @ApiModelProperty("关联员工信息")
    @NotEmpty(message = "关联员工列表不能为空")
    private List<StaffCodeRefDTO> staffList;

    private Long baseWelId;

}
