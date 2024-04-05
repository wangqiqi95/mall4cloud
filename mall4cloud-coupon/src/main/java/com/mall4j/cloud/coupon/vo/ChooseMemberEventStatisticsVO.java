package com.mall4j.cloud.coupon.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChooseMemberEventStatisticsVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "高价值会员活动统计";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ApiModelProperty("高价值会员活动ID")
    @ExcelProperty(value = {"高价值会员活动ID"}, index = 0)
    private Long eventId;

    @ApiModelProperty("高价值会员活动标题")
    @ExcelProperty(value = {"高价值会员活动标题"}, index = 1)
    private String eventTitle;

    @ApiModelProperty("邀请人数")
    @ExcelProperty(value = {"邀请会员数"}, index = 2)
    private Integer chooseMemberCount;

    @ApiModelProperty("领取会员数")
    @ExcelProperty(value = {"领取会员数"}, index = 3)
    private Integer exchangeMemberCount;

    @ApiModelProperty("未领取会员数")
    @ExcelProperty(value = {"未领取会员数"}, index = 4)
    private Integer notExchangeMemberCount;

    @ApiModelProperty("领取率")
    @ExcelProperty(value = {"领取率"}, index = 5)
    private BigDecimal exchangeRate;
}
