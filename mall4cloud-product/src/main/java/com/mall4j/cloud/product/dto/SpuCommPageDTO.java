package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SpuCommPageDTO {
    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("买家")
    private String userInfo;

    @ApiModelProperty("订单编号")
    private String orderId;

    @ApiModelProperty("订单编码")
    private String orderNumber;

    @ApiModelProperty("评分")
    private Integer evaluate;

    @ApiModelProperty("是否显示  -1:删除 0:不显示 1:显示 2 审核不通过 3.待审核")
    private String status;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("商品得分，1-5分")
    private Integer score;

    @ApiModelProperty("开始时间")
    private String createTimeStart;

    @ApiModelProperty("结束时间")
    private String createTimeEnd;

    @ApiModelProperty("评价类型  1 带图评价 2 有追评 3评价未回复 4 追加评价未回复")
    private List<Integer> type;

}
