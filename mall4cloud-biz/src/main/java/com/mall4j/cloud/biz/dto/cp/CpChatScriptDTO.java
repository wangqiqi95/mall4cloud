package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 话术表DTO
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Data
public class CpChatScriptDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("名称")
    private String scriptName;

    @ApiModelProperty("话术分类id")
    private Integer scriptMenuId;

    @ApiModelProperty("话术内容")
    private String scriptContent;

    @ApiModelProperty("话术答案")
    private String scriptAnswer;

    @ApiModelProperty("话术类型 0普通话术 1问答话术")
    private Integer type;

    @ApiModelProperty("开始时间(话术有效期)")
    private Date validityCreateTime;

    @ApiModelProperty("截止时间(话术有效期)")
    private Date validityEndTime;

    @ApiModelProperty("创建人")
    private Integer createBy;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("是否删除 0否1是")
    private Integer delFlag;

    @ApiModelProperty("是否全部部门：0否/1是")
    private Integer isAllShop;

    @ApiModelProperty("")
    private String createName;

    @ApiModelProperty("话术标签")
    private String scriptLabal;

	@ApiModelProperty("话术适用部门列表")
	private List<Long> storeIds;

}
