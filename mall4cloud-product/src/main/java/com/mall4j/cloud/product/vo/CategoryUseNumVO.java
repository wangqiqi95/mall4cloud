package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author YXF
 * @Date 2021/7/06
 */
public class CategoryUseNumVO {
    private static final Integer CATEGORY = 1;
    private static final Integer ATTR = 2;
    private static final Integer BRAND = 3;
    private static final Integer SPU = 3;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("数量")
    private Integer num;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "CategoryUseNumVO{" +
                "type=" + type +
                ", num=" + num +
                '}';
    }
}
