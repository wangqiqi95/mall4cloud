package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * @Description 修改推广商品
 * @Author axin
 * @Date 2023-02-20 17:07
 **/
@Data
public class UpdItemReqDto {
    @ApiModelProperty(value = "商品id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "推广计划类型 1普通推广 2定向推广 3专属推广")
    @NotNull(message = "推广计划类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "计划开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "计划结束时间")
    private Date endTime;

    @ApiModelProperty(value = "是否永久推广 0否 1是")
    private Boolean isForerver;

    @ApiModelProperty(value = "商品分佣比例 0-90")
    @Min(value = 0,message = "商品分佣比例0-90范围")
    @Max(value = 90,message = "商品分佣比例0-90范围")
    private Integer ratio;

    @ApiModelProperty(value = "添加达人列表")
    @Size(max = 30,message = "超出长度限制")
    private List<Long> addFinderIds;

    @ApiModelProperty(value = "删除达人列表")
    @Size(max = 30,message = "超出长度限制")
    private List<Long> delFinderIds;


}
