package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.product.dto.SpuFilterPropertiesDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性信息
 */
@Data
public class AppAttrVo implements Serializable{
    private static final long serialVersionUID = 1L;


    private String attrName;

    private List<AppAttrValuesVo> valuesList;

    private SpuFilterPropertiesDTO spuFilterProperties;
}
