package com.mall4j.cloud.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TagActivityDTO {
    @ApiModelProperty("id")
    private  Long id;

    @ApiModelProperty("活动名称")
    private  String activityName;

    @ApiModelProperty("标签名称")
    private  String tagName;

    @ApiModelProperty("活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("是否全店 0 否 1是")
    private  Integer isAllShop;

    @ApiModelProperty("角标类型 1固定角标")
    private  Integer tagType;

    @ApiModelProperty("角标方位  1 左上 2 左下 3 右上 4 右下 5 全覆盖")
    private  Integer tagPosition;

    @ApiModelProperty("图标url")
    private  String tagUrl;

    @ApiModelProperty("权重")
    private  Integer weight;

    @ApiModelProperty("关联店铺")
    private List<Long> stores;

    @ApiModelProperty("关联的商品")
    private List<Long> spuList;

    @ApiModelProperty("保存并启用按钮  0 保存按钮  1 保存并启用按钮")
    private Integer saveBtnType;
}
