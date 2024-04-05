package com.mall4j.cloud.user.vo.scoreConvert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Timestamp;
import java.util.Date;

@Data
@ApiModel(value = "积分Banner列表参数")
public class ScoreBannerListVO {

    @ApiModelProperty(value = "ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "活动状态")
    private String activityStatus;

    @ApiModelProperty(value = "活动状态Key值")
    private Integer activityStatusKey;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "banner图活动类型: {1：积分商城首页，2：完善信息，3：社区头部，4：社区活动}")
    private Integer type;

    @ApiModelProperty(value = "是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum;

    @ApiModelProperty(value = "状态 0：启用/1：停用")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "创建人")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;

}
