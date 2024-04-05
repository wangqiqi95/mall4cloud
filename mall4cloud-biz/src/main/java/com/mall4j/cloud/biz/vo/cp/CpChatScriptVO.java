package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 话术表VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Data
public class CpChatScriptVO{
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
    private Long createBy;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("是否删除 0否1是")
    private Integer delFlag;

    @ApiModelProperty("是否全部部门")
    private Integer isAllShop;

    @ApiModelProperty("")
    private String createName;

    @ApiModelProperty("话术标签")
    private String scriptLabal;
    private Date createTime;

    private Date updateTime;

    private Integer useNum;

}
