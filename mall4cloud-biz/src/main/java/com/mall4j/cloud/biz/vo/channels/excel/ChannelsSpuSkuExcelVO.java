package com.mall4j.cloud.biz.vo.channels.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * @date 2023/3/18
 */
@Data
public class ChannelsSpuSkuExcelVO {

    @ExcelProperty(value = {"商品ID"}, index = 0)
    private Long spuId;

    @ExcelProperty(value = {"货号"}, index = 1)
    private String spuCode;

    @ExcelProperty(value = {"商品名称"}, index = 2)
    private String spuTitle;

    @ExcelProperty(value = {"发货方式"}, index = 3)
    private String deliverMethod;

    @ExcelProperty(value = {"商品状态"}, index = 4)
    private String spuStatus;

    @ExcelProperty(value = {"添加至橱窗"}, index = 5)
    private String isInWindow;

    @ExcelProperty(value = {"商品条码"}, index = 6)
    private String skuCode;

    @ExcelProperty(value = {"视频号售价"}, index = 7)
    private String price;

    @ExcelProperty(value = {"视频号库存"}, index = 8)
    private Integer stock;

    public void setSpuStatus(Integer spuStatus) {
        // 0:初始值 2上架审核中 5:上架 6:回收站 11:自主下架 13:违规下架/风控系统下架
        if (Objects.isNull(spuStatus)){
            return;
        }

        String str;
        switch (spuStatus){
            case 0:
                str = "初始值";
                break;
            case 2:
                str = "上架中";
                break;
            case 5:
                str = "上架";
                break;
            case 6:
                str = "回收站";
                break;
            case 11:
                str = "自主下架";
                break;
            default:
                str = "违规下架/风控系统下架";
        }

        this.spuStatus = str;
    }

    public void setIsInWindow(Integer isInWindow) {
        if (isInWindow == 1){
            this.isInWindow = "是";
        } else {
            this.isInWindow = "否";
        }
    }

    public void setDeliverMethod(Integer deliverMethod) {
        if (deliverMethod == 1){
            this.deliverMethod = "无需快递";
        } else {
            this.deliverMethod = "快递发货";
        }
    }

    public void setPrice(Long price) {
        this.price = "" + price / 100;
    }
}
