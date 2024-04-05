package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialBrowseRecordApiVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("素材名称")
    private String matName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("userid")
    private Long userId;

    @ApiModelProperty("昵称")
    private String userName;

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

    @ApiModelProperty("员工id")
    private Long staffId;
    @ApiModelProperty("员工name")
    private String staffName;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date updateTime;
}
