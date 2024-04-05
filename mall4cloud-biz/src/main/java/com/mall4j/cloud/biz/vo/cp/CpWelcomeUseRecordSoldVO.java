package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 欢迎语 使用记录VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
@Data
@ColumnWidth(20)
public class CpWelcomeUseRecordSoldVO{
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "员工姓名")
    private String staffName;

    @ExcelProperty(value = "时间", converter = ExcelDateConverter.class)
    private Date createTime;

    @ExcelProperty(value = "客户昵称")
    private String nickName;

    @ExcelProperty(value = "客户手机号")
    private String phone;


}
