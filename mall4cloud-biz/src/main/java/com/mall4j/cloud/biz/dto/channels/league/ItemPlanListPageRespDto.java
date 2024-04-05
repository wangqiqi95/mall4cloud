package com.mall4j.cloud.biz.dto.channels.league;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 优选联盟推广计划
 * @Author axin
 * @Date 2023-02-20 16:17
 **/
@Data
public class ItemPlanListPageRespDto {

    @ApiModelProperty(value = "计划id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "计划名称")
    private String name;

    @ApiModelProperty(value = "达人ID列表")
    private List<String> finderIds;

    @ApiModelProperty(value = "商品ID列表")
    private List<String> productIds;

    @ApiModelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    private Integer type;

    @ApiModelProperty(value = "推广类型名称 1普通推广 2定向推广 3专属推广")
    private String typeName;

    @ApiModelProperty(value = "计划开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "计划结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createPerson;

    @ApiModelProperty(value = "创建人姓名")
    private String createPersonName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
