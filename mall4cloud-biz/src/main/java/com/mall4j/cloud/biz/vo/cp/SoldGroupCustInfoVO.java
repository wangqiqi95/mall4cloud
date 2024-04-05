package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 命中关键词VO
 *
 */
@Data
@ColumnWidth(20)
public class SoldGroupCustInfoVO {

    @ExcelProperty(value = "成员类型")
    private String typeName;

    @ExcelProperty(value = "昵称")
    private String groupNickname;

    @ExcelProperty(value = "名字")
    private String name;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "入群方式")
    private String joinSceneName;

    @ExcelProperty("邀请人")
    private String invitorUserName;

    @ExcelProperty(value = "入群时间", converter = ExcelDateConverter.class)
    private Date joinTime;

    @ExcelProperty("管理员")
    private String isAdminStr;
}
