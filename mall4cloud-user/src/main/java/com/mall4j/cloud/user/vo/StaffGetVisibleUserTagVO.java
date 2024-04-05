package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Peter_Tan
 * @date 2023/02/13
 */
@Data
public class StaffGetVisibleUserTagVO {

    @ApiModelProperty("标签ID")
    private Long tagId;

    @ApiModelProperty("标签名")
    private String tagName;

    @ApiModelProperty("标签组ID")
    private Long groupId;

    @ApiModelProperty("标签组名称")
    private String groupName;

    // 组标识，1导购助手打标，2导购助手显示，3管理后台打标
    private String authFlag;

    @ApiModelProperty("是否显示删除")
    private Integer isShowDel;

    @ApiModelProperty("标签组内是否允许多选【0:允许 1:不允许】")
    private Long singleState;

    @ApiModelProperty("标签与标签组关联表ID")
    private Long groupTagRelationId;

    @ApiModelProperty("标签与用户关联表ID")
    private Long userTagRelationId;

}
