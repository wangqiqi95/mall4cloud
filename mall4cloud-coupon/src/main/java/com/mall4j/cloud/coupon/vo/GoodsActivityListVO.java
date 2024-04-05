package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 活动列表信息 *
 * @author shijing
 * @date 2022-02-27
 */
@Data
@ApiModel(description = "商详领券活动列表信息")
public class GoodsActivityListVO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动状态")
    private Integer status;

    @ApiModelProperty(value = "活动状态")
    private String activityStatus;

    @ApiModelProperty(value = "活动状态Key")
    private Integer activityStatusKey;

    @ApiModelProperty(value = "是否全部门店）")
    private Boolean isAllShop;

    @ApiModelProperty(value = "门店数量")
    private Integer shopNum;

    @ApiModelProperty(value = "门店集合")
    private List<Long> shops;

    @ApiModelProperty(value = "商品数量")
    private Integer commodityNum;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "创建人姓名")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;

}
