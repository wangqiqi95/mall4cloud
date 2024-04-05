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
public class WeixinShortlinkRecordItemPageDTO extends PageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("短链记录列表ID")
    private Long shortLinkRecordId;

    @ApiModelProperty("会员编号")
    private String vipCode;

    @ApiModelProperty("查看开始时间")
    private Date startTime;

    @ApiModelProperty("查看结束时间")
    private Date endTime;

}
