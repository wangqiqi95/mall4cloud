package com.mall4j.cloud.api.user.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Peter_Tan
 * @date 2023/02/08
 */
@Data
public class TagVO {

    @ApiModelProperty("标签与标签组关联ID")
    private Long groupTagRelationId;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty(value = "创建人ID")
    private Long createUser;

    @ApiModelProperty(value = "创建人昵称")
    private String createUserNickName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "启用状态，0未启用，1启用")
    private Integer enableState;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "打标用户数")
    private Integer markingVipCount;

    @ApiModelProperty(value = "组标识，1导购助手打标，2导购助手显示，3管理后台打标")
    private List<Integer> authFlagArray;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "导入时间")
    private LocalDateTime importTime;

    @ApiModelProperty(value = "导入状态：0导入中，1导入完成，2导入失败")
    private Integer importStatus;

}
