package com.mall4j.cloud.biz.vo.channels.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Objects;

/**
 *
 * @date 2023/3/21
 */
@Data
public class ChannelsCheckPriceExcelVO {

    @ExcelProperty(value = {"商品ID"}, index = 0)
    private Long spuId;

    @ExcelProperty(value = {"货号"}, index = 1)
    private String spuCode;

    @ExcelProperty(value = {"商品名称"}, index = 2)
    private String spuName;

    @ExcelProperty(value = {"条码"}, index = 3)
    private String skuCode;

    @ExcelProperty(value = {"视频号价格"}, index = 4)
    private Long price;

    @ExcelProperty(value = {"保护价"}, index = 5)
    private Long skuProtectPrice;

    @ExcelProperty(value = {"吊牌价"}, index = 6)
    private Long marketPriceFee;

    @ExcelProperty(value = {"官店会员价"}, index = 7)
    private Long vipPriceFee;

    @ExcelProperty(value = {"会员日活动Id"}, index = 8)
    private Integer vipActivityId;

    @ExcelProperty(value = {"异常信息"}, index = 9)
    private String errorMsg;


    public void setPrice(Long price) {
        if (Objects.isNull(price)){
            return;
        }
        this.price = price / 100;
    }

    public void setSkuProtectPrice(Long skuProtectPrice) {
        if (Objects.isNull(skuProtectPrice)){
            return;
        }
        this.skuProtectPrice = skuProtectPrice / 100;
    }

    public void setMarketPriceFee(Long marketPriceFee) {
        if (Objects.isNull(marketPriceFee)){
            return;
        }
        this.marketPriceFee = marketPriceFee /100;
    }

    public void setVipPriceFee(Long vipPriceFee) {
        if (Objects.isNull(vipPriceFee)){
            return;
        }
        this.vipPriceFee = vipPriceFee /100;
    }
}
