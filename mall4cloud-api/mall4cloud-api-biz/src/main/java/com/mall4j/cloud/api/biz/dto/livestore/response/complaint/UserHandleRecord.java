package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserHandleRecord {
    @ApiModelProperty("纠纷状态")
    private Integer state;

    @ApiModelProperty("纠纷处理事件")
    private Integer event;

    @ApiModelProperty("状态描述")
    private String state_describe;

    @ApiModelProperty("处理事件描述")
    private String event_describe;

    @ApiModelProperty("处理时间")
    private Long time;

    @ApiModelProperty("处理材料信息")
    private MateriaInfo material_info;

}
