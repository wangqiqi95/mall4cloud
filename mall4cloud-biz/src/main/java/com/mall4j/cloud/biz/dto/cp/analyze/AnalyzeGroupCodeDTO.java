package com.mall4j.cloud.biz.dto.cp.analyze;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnalyzeGroupCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活码id")
    private Long codeId;

    @ApiModelProperty("渠道来源")
    private Integer sourceFrom;

    private String state;

    private String groupName;

}
