package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.UploadExcelVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 活动sku导入返回参数
 * @luzhengxiang
 * @create 2022-03-17 4:09 PM
 **/
@Data
public class SpuExcelImportDataVO extends UploadExcelVO {

    @ApiModelProperty(value = "商品集合")
    List<SpuExcelImportVO> parseList;

}
