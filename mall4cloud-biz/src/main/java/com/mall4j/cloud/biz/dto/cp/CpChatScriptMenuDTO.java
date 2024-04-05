package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 话术分类表DTO
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Data
public class CpChatScriptMenuDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("分类名称")
    private String menuName;

    @ApiModelProperty("父分类id")
    private Integer parentId;

    @ApiModelProperty("是否显示: 0隐藏/1显示")
    private Integer isShow;

    @ApiModelProperty("删除标识 1 删除 0 未删除")
    private Integer flag;

    @ApiModelProperty("状态 0无效 1有效")
    private Integer status;

	@ApiModelProperty("话术类型 0普通话术 1问答话术")
	private Integer type;

    @ApiModelProperty("是否置顶 0否 1是")
    private Integer isTop;

}
