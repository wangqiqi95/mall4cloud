package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 群活码设置群聊表DTO
 *
 * @author gmq
 * @date 2023-10-27 14:27:52
 */
@Data
public class CpGroupCodeListDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty(value = "名称",required = false)
    private String name;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("是否自动新建群: 0-否/1-是")
    private Integer autoCreateRoom;

    @ApiModelProperty(value = "群人数上限",required = false)
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

    @ApiModelProperty(value = "企微群id",required = false)
    private String chatId;

    @ApiModelProperty("二维码")
    private String qrCode;

    private String configId;
}
