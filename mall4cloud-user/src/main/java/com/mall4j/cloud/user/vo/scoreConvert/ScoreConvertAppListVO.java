package com.mall4j.cloud.user.vo.scoreConvert;

import com.mall4j.cloud.user.model.scoreConvert.ScoreConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "小程序积分活动返回数据")
public class ScoreConvertAppListVO {

    @ApiModelProperty("指定积分活动")
    ScoreConvert appointConvert;

    @ApiModelProperty("用户可兑换积分活动列表")
    List<ScoreConvertListVO> isExchangeList;

    @ApiModelProperty("用户不可兑换积分活动列表")
    List<ScoreConvertListVO> isNotExchangeList;

}
