package com.mall4j.cloud.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-09
 **/
@Data
public class SpuAbbrReqDto {
    /**
     * spu编码
     */
    @ExcelProperty(value = {"商品编码"}, index = 0)
    private String spuCode;

    /**
     * spu简称
     */
    @ExcelProperty(value = {"商品简称"}, index = 1)
    private String spuAbbr;
}
