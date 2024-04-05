package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SpuCommExcelVO {

    private static final long serialVersionUID = 1L;
    public static final String EXCEL_NAME = "商品评价信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"商品名称"}, index = 0)
    private String prodName;

    @ExcelProperty(value = {"商品规格"}, index = 1)
    private String skuName;

    @ExcelProperty(value = {"订单号"}, index = 2)
    private String orderId;

    @ExcelProperty(value = {"客户姓名"}, index = 3)
    private String userName;

    @ExcelProperty(value = {"客户手机号码"}, index = 4)
    private String mobile;

    @ExcelProperty(value = {"是否匿名"}, index = 5)
    private String isAnonymous;

    @ExcelProperty(value = {"评论内容"}, index = 6)
    private String content;

    @ExcelProperty(value = {"评价"}, index = 7)
    private String evaluate;

    @ExcelProperty(value = {"是否有图片"}, index = 8)
    private String hasImages;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"评价时间"}, index = 9)
    private Date createTime;

    @ExcelProperty(value = {"商家回复"}, index = 10)
    private String replyContent;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"回复时间"}, index = 11)
    private Date replyTime;

    @ExcelProperty(value = {"状态"}, index = 12)
    private String status;
}
