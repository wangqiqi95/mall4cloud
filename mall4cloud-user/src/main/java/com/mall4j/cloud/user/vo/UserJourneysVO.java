package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserJourneysVO {
    @ApiModelProperty("查询数据类型  1电话 2邮件 3企微会话 4短信 5跟进记录 6美洽 7修改跟进 8行为轨迹 ")
    private Integer type;

    @ApiModelProperty("时间记录 时间戳格式")
    private Long recordTime;

    @ApiModelProperty("数据")
    private Object data;

    @ApiModelProperty("是否允许编辑：0否/1是")
    private Integer editStatus=0;

}
