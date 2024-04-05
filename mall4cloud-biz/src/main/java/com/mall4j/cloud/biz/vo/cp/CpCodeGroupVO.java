package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.dto.cp.CpGroupCodeListDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 企微活码分组VO
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
@Data
public class CpCodeGroupVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("是否逻辑删除:默认0未删除;1已删除")
    private Integer isDeleted;

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("分组类型：0-渠道活码/1-群活码/2-自动拉群")
    private Integer type;

    @ApiModelProperty("")
    private Long parentId;

}
