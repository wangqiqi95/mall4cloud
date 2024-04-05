package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 素材 会员浏览记录DTO
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
@Data
public class CpMaterialBrowseRecordDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("userid")
    private Long userId;

    @ApiModelProperty("union_id")
    private String unionId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("浏览时长")
    private Integer browseDuration;

    @ApiModelProperty("浏览次数")
    private Integer browseCount;

    @ApiModelProperty("标签id")
    private String labalId;

    @ApiModelProperty("标签name")
    private String labalName;

}
