package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 群活码表DTO
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Data
public class GroupCodeDTO{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    @NotBlank(message = "活动名称")
    private String name;

    @ApiModelProperty("入群指导语")
    @NotBlank(message = "入群指导语不能为空")
    private String slogan;

//    @ApiModelProperty("关联的群")
//    @NotNull(message = "关联的群不能为空")
//    private List<String> groupList;

    @NotNull(message = "分组id不能为空")
    @ApiModelProperty("分组id")
    private Long groupId;

    @NotNull(message = "拉群方式不能为空")
    @ApiModelProperty("拉群方式：0企微群活码/1自建群活码/2企微群活码基础")
    private Integer groupType;

    @NotNull(message = "备用员工id不能为空")
    @ApiModelProperty(value = "备用员工id",required = true)
    private Long standbyStaffId;

    @ApiModelProperty(value = "活码样式",required = false)
    private String style;

    @ApiModelProperty(value = "群组集合",required = false)
    private List<CpGroupCodeListDTO> codeList;

    private Integer codeFrom;

}
