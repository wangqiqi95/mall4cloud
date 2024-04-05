package com.mall4j.cloud.api.user.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnalyzeUserStaffCpRelationCountVO {

    @ApiModelProperty("添加时间")
    private String dateKey;

    @ApiModelProperty("新增客户数")
    private Integer newCount;

    @ApiModelProperty("被客户删除数")
    private Integer delByUserCount;

    @ApiModelProperty("被员工删除数")
    private Integer delByStaffCount;

    @ApiModelProperty("净增加数")
    private Integer addCount;




}
