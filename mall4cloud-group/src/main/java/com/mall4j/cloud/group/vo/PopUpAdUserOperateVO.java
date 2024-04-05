package com.mall4j.cloud.group.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class PopUpAdUserOperateVO {

    @ApiModelProperty(value = "主键")
    private Long popUpAdUserOperateId;

    @ApiModelProperty(value = "开屏广告主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "开屏广告名称")
    private String popUpAdName;

    @ApiModelProperty(value = "用户ID")
    private Long vipUserId;

    @ApiModelProperty(value = "用户卡号")
    private String vipCode;

    @ApiModelProperty(value = "用户昵称")
    private String vipNickName;

    @ApiModelProperty(value = "微信union_id")
    private String unionId;

    @ApiModelProperty(value = "1浏览，2点击")
    private Integer operate;

    @ApiModelProperty(value = "1浏览，2点击")
    private String operateStr;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "门店code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作入口页面")
    private String entrance;
}
