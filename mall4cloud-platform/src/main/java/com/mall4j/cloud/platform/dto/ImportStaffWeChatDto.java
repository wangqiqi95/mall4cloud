package com.mall4j.cloud.platform.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.google.common.base.Objects;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 导入员工微信
 * @Author axin
 * @Date 2023-02-24 9:56
 **/
@Data
public class ImportStaffWeChatDto implements Serializable {
    @ExcelProperty(value = {"员工工号"}, index = 0)
    private String staffNo;

    @ExcelProperty(value = {"员工微信号"}, index = 1)
    private String weChatNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportStaffWeChatDto that = (ImportStaffWeChatDto) o;
        return Objects.equal(weChatNo, that.weChatNo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(weChatNo);
    }
}
