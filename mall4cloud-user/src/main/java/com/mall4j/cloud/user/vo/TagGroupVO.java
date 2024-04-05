package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Peter_Tan
 * @date 2023/02/08
 */
@Data
public class TagGroupVO {

    @ApiModelProperty("标签组ID")
    private Long tagGroupId;

    @ApiModelProperty("父级组ID")
    private Long parentId;

    @ApiModelProperty("标签组名称")
    private String groupName;

    @ApiModelProperty("标签类型，1手动标签，2导购标签")
    private Integer groupType;

    @ApiModelProperty("0多个，1单个")
    private Integer singleState;

    @ApiModelProperty("标签组启用状态:0未启用，1启用")
    private Integer enableState;

    @ApiModelProperty("组标识，1导购助手打标，2导购助手显示，3管理后台打标")
    private List<Integer> authFlagArrays;

    private String authFlag;


    @ApiModelProperty("创建用户ID")
    private Long createUser;

    @ApiModelProperty("创建用户昵称")
    private String createUserNickName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

//    @ApiModelProperty("标签组下标签列表")
//    private List<TagVO> tagVOS;
}
