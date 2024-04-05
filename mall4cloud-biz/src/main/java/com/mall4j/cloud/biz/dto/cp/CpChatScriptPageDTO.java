package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CpChatScriptPageDTO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("话术内容")
    private String scriptContent;

    @ApiModelProperty("话术答案")
    private String scriptAnswer;

    @ApiModelProperty("分类id")
    private Long menuId;

    @ApiModelProperty("话术类型 0普通话术 1问答话术")
    private Integer type;

    private List<Long> orgIds;

    private List<String> ids;

}
