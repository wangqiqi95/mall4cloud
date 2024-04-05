package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
public class WeixinShortlinkRecordDTO extends PageDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("短链路径")
    private String shortUrl;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
