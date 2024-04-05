package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 群活码设置群聊表VO
 *
 * @author gmq
 * @date 2023-10-27 14:27:52
 */
@Data
public class CpGroupCodeListVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    private String name;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("  1 已删除 0 未删除")
    private Integer isDelete;

    @ApiModelProperty("")
    private String qrCode;

    @ApiModelProperty("是否自动新建群: 0-否/1-是")
    private Integer autoCreateRoom;

    @ApiModelProperty("群人数上限")
    private Integer total;

    @ApiModelProperty("是否开启群名称设置：0否/1是")
    private Integer groupNameState;

    @ApiModelProperty("自动建群的群名前缀")
    private String roomBaseName;

    @ApiModelProperty("自动建群的群起始序号")
    private String roomBaseId;

    @ApiModelProperty("截止开始时间")
    private Date expireStart;

    @ApiModelProperty("截止结束时间")
    private Date expireEnd;

    @ApiModelProperty("群活码id")
    private Long codeId;

}
