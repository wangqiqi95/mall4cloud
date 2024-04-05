package com.mall4j.cloud.api.biz.dto.cp;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class CpUserGroupDTO extends PageDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户企微Id",required = false)
    private String qiWeiUserId;

}
