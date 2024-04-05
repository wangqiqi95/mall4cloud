package com.mall4j.cloud.user.vo.scoreConvert;

import com.mall4j.cloud.user.dto.scoreConvert.ScoreBannerUrlDTO;
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
@ApiModel(description = "积分Banner详情参数")
public class BannerDetailVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "banner图活动类型: {1：积分商城首页，2：完善信息，3：社区头部，4：社区活动}")
    private Integer type;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "是否全部门店（适用门店）")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum;

    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;

    @ApiModelProperty(value = "图片信息")
    private List<ScoreBannerUrlDTO> bannerUrlList;

    @ApiModelProperty(value = "状态  0：启用/1：禁用")
    private Integer status;

}
