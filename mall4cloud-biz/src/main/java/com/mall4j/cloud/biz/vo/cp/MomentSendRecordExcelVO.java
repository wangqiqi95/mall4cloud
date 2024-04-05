package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 朋友圈发送记录导出对象
 */
@Data
public class MomentSendRecordExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "朋友圈发送记录";
    public static final String SHEET_NAME = "朋友圈发送记录";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = "员工", index = 0)
    private String staffName;

    @ExcelProperty(value = "所属部门", index = 1)
    private String orgNames;

    @ExcelProperty(value = "是否完成推送", index = 2)
    private String statusName;

    @ExcelProperty(value = {"完成时间"}, index = 3)
    private String sendTime;

    @ExcelProperty(value = "企微朋友圈评论数量", index = 4)
    private Integer qwCommentNum;

    @ExcelProperty(value = "企微朋友圈点赞数量", index = 5)
    private Integer qwLikeNum;

}
