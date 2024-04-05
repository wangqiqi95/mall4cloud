package com.mall4j.cloud.user.dto.scoreConvert;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 */

@Data
@ApiModel(description = "积分换物兑换列表参数")
public class ScoreBarterLogListDTO {

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;
    @ApiModelProperty(value = "关联活动")
    private Long convertId;
    @ApiModelProperty(value = "会员信息")
    private String userInfo;
    @ApiModelProperty(value = "导入状态")
    private Integer exportStatus;
    @ApiModelProperty(value = "发货状态")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    private Long downLoadHisId;

}
