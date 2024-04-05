package com.mall4j.cloud.api.biz.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * 微信二维码扫码记录表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
@Data
public class WeixinQrcodeScanRecordLogVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "扫码回复数据详情";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;

    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"序号"}, index = 0)
    private String id;

//    @ColumnWidth(25)
//    @ExcelProperty(value = {"头像"}, index = 1)
//    private String headimgurl;
//
//    @ColumnWidth(25)
//    @ExcelProperty(value = {"昵称"}, index = 2)
//    private String nickName;

    @ColumnWidth(30)
    @ExcelProperty(value = {"openid"}, index = 1)
    private String openid;

    @ColumnWidth(25)
    @ExcelProperty(value = {"消息发送时间"}, index = 2)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date scanTime;
//    private Date createTime;

}
