package com.mall4j.cloud.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 导出客户信息
 */
@Data
@ColumnWidth(25)
public class SoldUserStaffRelVo {

    /**员工信息**/
    @ExcelProperty(value = {"昵称"})
    private String qiWeiNickName;

    @ExcelProperty(value = {"备注名称"})
    private String cpRemark;

    @ExcelProperty(value = {"对外部联系人描述"})
    private String cpDescription;

    @ExcelProperty(value = {"企微手机号"})
    private String cpRemarkMobiles;

    @ExcelProperty(value = {"企业"})
    private String cpRemarkCorpName;

    @ExcelProperty(value = {"分组等级"})
    private String groupName;

    @ExcelProperty(value = {"标签"})
    private String tagName;

    @ExcelProperty(value = {"添加时间"},converter = ExcelDateConverter.class)
    private Date cpCreateTime;

    @ExcelProperty(value = {"添加人"})
    private String staffName;

    @ExcelProperty(value = {"好友关系状态"})
    private String stateName;

}
