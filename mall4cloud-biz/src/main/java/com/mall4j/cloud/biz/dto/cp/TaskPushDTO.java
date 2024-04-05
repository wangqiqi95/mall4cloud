package com.mall4j.cloud.biz.dto.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 群发任务表DTO
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
public class TaskPushDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("推送名称")
    private String pushName;

    @ApiModelProperty("推送开始时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("推送截止时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("推送内容")
    private String content;

    @ApiModelProperty("附件信息")
    private MaterialMsgDTO msgDTO;
}

