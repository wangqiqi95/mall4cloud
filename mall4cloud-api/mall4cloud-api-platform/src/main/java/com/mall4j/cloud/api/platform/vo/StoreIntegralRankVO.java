package com.mall4j.cloud.api.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 积分抵现榜单
 * @Author axin
 * @Date 2023-02-16 13:58
 **/
@Data
public class StoreIntegralRankVO {

    @ApiModelProperty(value = "当前月榜单")
    private List<StoreIntegralRank> currentMonth;

    @ApiModelProperty(value = "上一月榜单")
    private List<StoreIntegralRank> lastMonth;

    @Data
    public static class StoreIntegralRank{
        @ApiModelProperty(value = "销售月份")
        @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
        private Date salesMonth;

        @ApiModelProperty(value = "门店编号")
        private String shopCode;

        @ApiModelProperty(value = "门店名称")
        private String storeName;

        @ApiModelProperty(value = "门店会员订单总数")
        private Integer vipOrder;

        @ApiModelProperty(value = "积分抵现单数")
        private Integer amountUsed;

        @ApiModelProperty(value = "积分抵现占比")
        private Double usageRate;

        @ApiModelProperty(value = "使用占比排名")
        private Integer rank;

        @ApiModelProperty(value = "排名编号")
        private Integer rankNo;

        @ApiModelProperty(value = "排名奖金")
        private String rankBonus;
    }

}
