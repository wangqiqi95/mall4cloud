package com.mall4j.cloud.user.vo.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;


@Data
@ApiModel(description = "积分商城首页（商城小程序）")
public class ScoreAppVO {

    @ApiModelProperty(value = "用户积分")
    private Long score;

    @ApiModelProperty(value = "banner信息")
    private List bannerList;

    @ApiModelProperty(value = "积分活动（0：积分兑礼/1：积分兑券）")
    private List<ScoreCouponAppVO> ScoreCouponList;

}
