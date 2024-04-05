package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserGroupTagRelationBO {

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

}
