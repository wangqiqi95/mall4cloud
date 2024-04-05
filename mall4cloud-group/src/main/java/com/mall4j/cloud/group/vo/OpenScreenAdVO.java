package com.mall4j.cloud.group.vo;


import com.mall4j.cloud.group.model.OpenScreenAdShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "开屏广告详情实体")
public class OpenScreenAdVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;
    @ApiModelProperty("活动图片链接")
    private String activityPicUrl;
    @ApiModelProperty("适用粉丝等级集合")
    private String fansLevel;
    @ApiModelProperty("跳转类型0 不跳转 1跳转h5 2跳转小程序")
    private Integer redirectType;
    @ApiModelProperty("跳转链接")
    private String redirectUrl;
    @ApiModelProperty("广告频率类型 0每天一次 1每次打开出现弹窗 2累计仅一次")
    private Integer adFrequency;
    @ApiModelProperty("权重")
    private Integer weight;
    @ApiModelProperty("状态 0未启用 1已启用")
    private Integer status;
    @ApiModelProperty("适用门店列表")
    private List<OpenScreenAdShop> Shops;
}
