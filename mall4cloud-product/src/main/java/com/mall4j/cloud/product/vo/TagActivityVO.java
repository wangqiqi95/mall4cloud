package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.product.model.TagActRelationStore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TagActivityVO {
    @ApiModelProperty("id")
    private  Long id;

    @ApiModelProperty("活动名称")
    private  String activityName;

    @ApiModelProperty("标签名称")
    private  String tagName;

    @ApiModelProperty("活动开始时间")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    private  Date endTime;

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

    @ApiModelProperty("活动状态 0 未启动 1 未开始 2 进行中 3已结束 ")
    private  Integer status;

    @ApiModelProperty("创建人")
    private  Long createBy;

    @ApiModelProperty("创建人名称")
    private  String createName;

    @ApiModelProperty("更新人")
    private  String updateBy;

    @ApiModelProperty("关联店铺")
    private List<TagActRelationStore> stores;

    @ApiModelProperty("商品数量")
    private  Integer prodNums;

    @ApiModelProperty("创建时间")
    protected Date createTime;

}
