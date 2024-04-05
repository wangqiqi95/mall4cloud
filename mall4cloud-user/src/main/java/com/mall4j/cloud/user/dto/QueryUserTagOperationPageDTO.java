package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryUserTagOperationPageDTO extends PageDTO {

    @ApiModelProperty("被操作用户会员卡号")
    private String beVipCode;

}
