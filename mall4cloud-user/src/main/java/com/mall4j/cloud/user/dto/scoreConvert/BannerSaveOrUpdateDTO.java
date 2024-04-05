package com.mall4j.cloud.user.dto.scoreConvert;

import com.mall4j.cloud.user.model.scoreConvert.ScoreBanner;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBannerUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 积分Banner
 *
 * @author shijing
 * @date 2022-1-23
 */

@Data
@ApiModel(description = "新增积分Banner参数")
public class BannerSaveOrUpdateDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "banner图活动类型: {1：积分商城首页，2：完善信息，3：社区头部，4：社区活动}")
    private Integer bannerType;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "是否全部门店（适用门店）")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;

    @ApiModelProperty(value = "图片信息")
    private List<ScoreBannerUrlDTO> bannerUrlList;

    @ApiModelProperty(value = "状态 0：启用/1：停用")
    private Integer status;


}
