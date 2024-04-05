package com.mall4j.cloud.product.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品分组标签关联信息
 *
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagActRelationStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
	/**
	 * 活动id
	 */
	private Long actId;
    /**
     * 店铺id
     */
    private Long storeId;


}
