package com.mall4j.cloud.distribution.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 门店活动DTO
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
@Data
public class DistributionStoreActivityDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @NotBlank(message = "活动名称不能为空")
    @ApiModelProperty("活动名称")
    private String name;

    @NotBlank(message = "宣传图片不能为空")
    @ApiModelProperty("宣传图片")
    private String img;

    @ApiModelProperty("组织ID")
    private Long orgId;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty("状态 0-禁用 1-启用")
    private Integer status;

    @NotNull(message = "限定报名人数不能为空")
    @ApiModelProperty("限定报名人数")
    private Integer limitApplyNum;

    @NotBlank(message = "城市编码不能为空")
    @ApiModelProperty("城市编码")
    private String provinceCode;

    @NotBlank(message = "城市名称不能为空")
    @ApiModelProperty("城市名称")
    private String provinceName;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区编码")
    private String districtCode;

    @ApiModelProperty("区名称")
    private String districtName;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;

    @ApiModelProperty("详细地址")
    private String address;

    @NotNull(message = "活动开始时间不能为空")
    @ApiModelProperty("活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date startTime;

    @NotNull(message = "活动结束时间不能为空")
    @ApiModelProperty("活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date endTime;

    @NotNull(message = "活动报名开始时间不能为空")
    @ApiModelProperty("活动报名开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date applyStartTime;

    @NotNull(message = "活动报名结束时间不能为空")
    @ApiModelProperty("活动报名结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date applyEndTime;

    @NotNull(message = "是否需要填写年龄不能为空")
    @ApiModelProperty("是否需要填写年龄 0-否 1-是")
    private Integer needAge;

    @NotNull(message = "是否需要填写证件号不能为空")
    @ApiModelProperty("是否需要填写证件号 0-否 1-是")
    private Integer needIdCard;

    @ApiModelProperty("是否需要填写衣服尺码 0-否 1-是")
    private Integer needClothes;

    @ApiModelProperty("是否需要填写鞋子尺码 0-否 1-是")
    private Integer needShoes;

    @NotBlank(message = "活动描述不能为空")
    @ApiModelProperty("活动描述")
    private String desc;

    @NotBlank(message = "活动描述图片不能为空")
    @ApiModelProperty("活动描述图片")
    private String descImg;

    @ApiModelProperty("活动开始前x小时提醒")
    private Integer startRemind;

    @ApiModelProperty("活动结束后x小时提醒")
    private Integer endRemind;

    @ApiModelProperty("活动结束后提醒是否开启 0-否 1-是")
    private Integer endRemindStatus;

    @ApiModelProperty("活动结束后提醒跳转链接")
    private String endRemindUrl;

    @ApiModelProperty("活动衣服尺码集")
    private List<String> clothesSizes;

    @ApiModelProperty("活动鞋子尺码集")
    private List<String> shoesSizes;

    @ApiModelProperty("活动上新是否发送订阅消息提醒 0否1是")
    private Integer newNotice;
    @ApiModelProperty("活动上新 温馨提示文案")
    private String newReminder;
    @ApiModelProperty("活动开始是否发送订阅消息提醒 0否1是")
    private Integer startNotice;
    @ApiModelProperty("活动开始 温馨提示文案")
    private String startReminder;
    @ApiModelProperty("活动结束是否发送订阅消息提醒 0否1是")
    private Integer endNotice;
    @ApiModelProperty("活动结束 温馨提示文案")
    private String endReminder;


}
