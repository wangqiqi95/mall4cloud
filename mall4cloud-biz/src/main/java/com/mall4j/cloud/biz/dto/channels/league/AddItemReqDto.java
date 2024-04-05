package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @Description 添加推广商品
 * @Author axin
 * @Date 2023-02-20 17:07
 **/
@Data
public class AddItemReqDto {

    @ApiModelProperty(value = "推广计划类型1普通推广 2定向推广 3专属推广")
    @NotNull(message = "推广计划类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "计划开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "计划结束时间")
    private Date endTime;

    @ApiModelProperty(value = "是否永久推广0否 1是")
    private Boolean isForerver;

    @ApiModelProperty(value = "达人列表")
    @Size(max = 30,message = "超出长度限制")
    private List<Long> finderIds;

    @ApiModelProperty(value = "商品")
    @Size(max = 20,message = "超出长度限制")
    @NotNull(message = "商品不能为空")
    private List<AddItem> items;

}
