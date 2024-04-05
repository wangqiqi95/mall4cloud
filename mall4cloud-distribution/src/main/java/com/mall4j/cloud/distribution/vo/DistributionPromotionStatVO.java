package com.mall4j.cloud.distribution.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributionPromotionStatVO {

    @ApiModelProperty("总记录")
    private Integer total = 0;
    @ApiModelProperty("朋友圈")
    private Integer moments = 0;
    @ApiModelProperty("商品")
    private Integer product = 0;
    @ApiModelProperty("海报")
    private Integer poster = 0;
    @ApiModelProperty("专题")
    private Integer specialSubject = 0;

}
