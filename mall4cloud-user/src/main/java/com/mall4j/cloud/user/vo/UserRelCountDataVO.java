package com.mall4j.cloud.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ColumnWidth(20)
public class UserRelCountDataVO {

    @ApiModelProperty("好友新增")
    @ExcelProperty(value = "好友新增")
    private Integer addNewCount;

    @ApiModelProperty("好友累计")
    @ExcelProperty(value = "好友累计")
    private Integer addCount;

    @ApiModelProperty("好友流失")
    @ExcelProperty(value = "好友流失")
    private Integer lossNewCount;

    @ApiModelProperty("累计流失")
    @ExcelProperty(value = "累计流失")
    private Integer lossCount;

    @ApiModelProperty("好友删除")
    @ExcelProperty(value = "好友删除")
    private Integer delNewCount;

    @ApiModelProperty("累计删除")
    @ExcelProperty(value = "累计删除")
    private Integer delCount;

}
