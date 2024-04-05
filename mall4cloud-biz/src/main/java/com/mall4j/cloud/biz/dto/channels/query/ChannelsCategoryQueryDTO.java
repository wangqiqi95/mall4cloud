package com.mall4j.cloud.biz.dto.channels.query;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @date 2023/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "视频号类目查询DTO")
public class ChannelsCategoryQueryDTO extends PageDTO {

    @ApiModelProperty("类目名称匹配三级单个模糊查询")
    private String name;

    @ApiModelProperty(value = "审核状态, 1：审核中，3：审核成功，2：审核拒绝，12：主动取消申请单")
    private Integer status;
}
