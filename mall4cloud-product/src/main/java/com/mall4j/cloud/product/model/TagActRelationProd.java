package com.mall4j.cloud.product.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 营销标签活动关联的商品表
 *
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagActRelationProd extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    private Long actId;

    /**
     * 商品id
     */
    private Long spuId;

}
