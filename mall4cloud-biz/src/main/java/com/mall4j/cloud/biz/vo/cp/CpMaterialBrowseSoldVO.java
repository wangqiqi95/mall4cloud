package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分页获取素材浏览列表
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
@ColumnWidth(20)
public class CpMaterialBrowseSoldVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "浏览时间", converter = ExcelDateConverter.class)
    private Date createTime;

    @ExcelProperty(value = "昵称")
    private String nickName;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "浏览时长")
    private String browseDuration;

    @ExcelProperty(value = "打标签")
    private String labalName;

}
