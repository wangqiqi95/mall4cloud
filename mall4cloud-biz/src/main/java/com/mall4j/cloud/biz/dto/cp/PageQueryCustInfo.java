package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageQueryCustInfo {
    @ApiModelProperty("进群开始时间")
    private String joinTimeStart;
    @ApiModelProperty("进群结束时间")
    private String joinTimeEnd;
    @ApiModelProperty("进群方式")
    private Integer scene;
    @ApiModelProperty("群成员名称")
    private String custName;
    @ApiModelProperty("客群Id")
    @NotBlank(message = "客群id不能为空！")
    private String groupId;
    private Long codeId;

    @ApiModelProperty("邀请人")
    private String invitorUserName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("是否管理员：0否/1是")
    private Integer isAdmin;

    @ApiModelProperty("员工企微userId")
    private List<String> invitorUserIds;

    @ApiModelProperty("员工id")
    private List<Long> staffIds;

    @ApiModelProperty("客户企userId")
    private List<String> userQiWeiUserIds=new ArrayList<>();
}
