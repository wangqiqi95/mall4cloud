package com.mall4j.cloud.user.vo.scoreConvert;

import com.mall4j.cloud.user.dto.scoreConvert.ScoreBannerUrlDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "积分商城首页")
public class ScoreMallHomeVO implements Serializable {
    @ApiModelProperty(value = "banner信息")
    private List<ScoreBannerUrlDTO> bannerList;
    @ApiModelProperty(value = "券信息")
    private List<ScoreCouponAppVO> couponList;

}
