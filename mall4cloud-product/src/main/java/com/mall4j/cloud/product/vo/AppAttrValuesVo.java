package com.mall4j.cloud.product.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import com.mall4j.cloud.product.model.AttrValue;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性信息
 */
@Data
public class AppAttrValuesVo {
    private static final long serialVersionUID = 1L;


    private Long attrId;
    private String attrName;

    private Long attrValueId;
    private String attrValueName;
    private String imgUrl;

}
