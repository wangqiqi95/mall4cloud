package com.mall4j.cloud.api.user.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 好友统计VO
 *
 */
@Data
@ColumnWidth(20)
public class ExportUserCountVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工")
    @ExcelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty("好友新增")
    @ExcelProperty(value = "好友新增数")
    private Integer addUser=0;

    @ApiModelProperty("好友流失")
    @ExcelProperty(value = "好友流失数")
    private Integer lossUser=0;

    @ApiModelProperty("好友删除")
    @ExcelProperty(value = "好友删除数")
    private Integer delUser=0;

    @ApiModelProperty("留存率")
    @ExcelProperty(value = "留存率")
    private String retained;

    @ApiModelProperty("新增客户群")
    @ExcelProperty(value = "新增客户群")
    private String addRoom;

    @ApiModelProperty("新增入群人数")
    @ExcelProperty(value = "新增入群人数")
    private String addRoomPeople;

    @ApiModelProperty("总客户群")
    @ExcelProperty(value = "总客户群")
    private String countRoom;

    @ApiModelProperty("总好友数")
    @ExcelProperty(value = "总好友数")
    private Integer countUser=0;
}
