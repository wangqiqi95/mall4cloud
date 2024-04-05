package com.mall4j.cloud.biz.dto.channels.league;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 可推广商品列表
 * @Author axin
 * @Date 2023-04-21 11:00
 **/
@Getter
@Setter
public class ItemAllowPromotionListPageReqDto extends PageDTO {
    @ApiModelProperty(value = "视频号商品id")
    private List<Long> outProductIds;

    @ApiModelProperty(value = "spu编码列表")
    private List<String> spuCodes;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    @NotNull(message = "推广类型不能为空")
    private Integer type;
}
