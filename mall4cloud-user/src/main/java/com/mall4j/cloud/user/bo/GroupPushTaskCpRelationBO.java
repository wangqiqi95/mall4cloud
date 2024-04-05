package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupPushTaskCpRelationBO {

    @ApiModelProperty(value = "会员企业微信ID")
    private String vipCpUserId;

}
