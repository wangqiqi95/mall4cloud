package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "积分活动列表的搜索参数")
public class ScoreConvertListDTO implements Serializable {
    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;
    @ApiModelProperty(value = "活动名称")
    private String convertTitle;
    @ApiModelProperty(value = "活动状态")
    private Integer convertStatus;
    @ApiModelProperty(value = "活动类型")
    private Integer type;
    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;
    @ApiModelProperty(value = "活动id")
    private Long convertId;
    @ApiModelProperty(value = "banner图活动类型: {1：积分商城首页，2：完善信息，3：社区头部，4：社区活动}")
    private Integer bannerType;
}
