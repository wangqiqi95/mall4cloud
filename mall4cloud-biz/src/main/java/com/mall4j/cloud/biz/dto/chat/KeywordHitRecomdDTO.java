package com.mall4j.cloud.biz.dto.chat;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeywordHitRecomdDTO extends PageDTO {

    @ApiModelProperty(value = "员工企微ID", required = false)
    private String staffUserId;

    @ApiModelProperty(value = "客户企微ID", required = false)
    private String userId;

}
