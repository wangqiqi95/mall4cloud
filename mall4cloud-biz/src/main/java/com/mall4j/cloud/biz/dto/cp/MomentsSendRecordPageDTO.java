package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MomentsSendRecordPageDTO {

    private List<Long> staffIds;

    @ApiModelProperty("员工名称")
    private String name;
    @ApiModelProperty("部门id")
    private List<Long> storeIds;


    @ApiModelProperty("开始时间")
    private String sendTimeBegin;
    @ApiModelProperty("结束时间")
    private String sendTimeEnd;

    @ApiModelProperty("是否发送状态 0否 1已经发送")
    private Integer status;

    @ApiModelProperty("朋友圈id")
    private Long momentsId;

}
