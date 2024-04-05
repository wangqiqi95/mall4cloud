package com.mall4j.cloud.biz.dto.channels.league;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 优选联盟商品详情
 * @Author axin
 * @Date 2023-02-20 16:17
 **/
@Data
public class ItemListDetailRespDto {

    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "达人列表")
    private List<ItemListFinderRespDto> finder;

    @ApiModelProperty(value = "视频号商品ID")
    private String outProductId;

    @ApiModelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    private Integer type;

    @ApiModelProperty(value = "状态 0上架 1下架")
    private Integer status;

    @ApiModelProperty(value = "计划开始时间 需要计算推广状态")
    private Date beginTime;

    @ApiModelProperty(value = "计划结束时间 需要计算推广状态")
    private Date endTime;

    @ApiModelProperty(value = "是否永久推广")
    private Boolean isForerver;

    @ApiModelProperty(value = "分佣比例")
    private Integer ratio;

    @ApiModelProperty(value = "spuid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long spuId;

    @ApiModelProperty(value = "商品编码")
    private String spuCode;

    @ApiModelProperty(value = "商品图片")
    private String headImgs;

    @ApiModelProperty(value = "商品价格")
    private Long marketPriceFee;

    @ApiModelProperty(value = "创建人姓名")
    private String createPerson;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
