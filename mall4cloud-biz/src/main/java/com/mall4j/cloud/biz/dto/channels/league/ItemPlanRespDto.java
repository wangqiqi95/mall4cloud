package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @Description 优选联盟商品计划返回详情
 * @Author axin
 * @Date 2023-02-21 14:09
 **/
@Data
public class ItemPlanRespDto {
    @ApiModelProperty(value = "计划名称")
    private String name;

    @ApiModelProperty(value = "推广计划类型1普通推广 2定向推广 3专属推广")
    private Integer type;

    @ApiModelProperty(value = "推广计划类型1普通推广 2定向推广 3专属推广")
    private String typeName;

    @ApiModelProperty(value = "计划开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "计划结束时间")
    private Date endTime;

    @ApiModelProperty(value = "达人列表")
    private List<String> finderIds;

}
