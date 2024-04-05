package com.mall4j.cloud.biz.dto.channels.league;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 优选联盟商品推广
 * @Author axin
 * @Date 2023-02-20 15:56
 **/
@Data
public class ItemListPageReqDto extends PageDTO {
    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    @NotNull(message = "推广类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "商品状态 0上架 1下架 3待生效 4推广中")
    private Integer status;

    @ApiModelProperty(value = "spuCodes")
    private List<String> spuCodes;

    @ApiModelProperty(value = "达人ids")
    private List<Long> finderIds;
}
