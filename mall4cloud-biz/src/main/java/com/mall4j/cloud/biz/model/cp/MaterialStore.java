package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材商店表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 商店id
     */
    private Long storeId;

    /**
     * 素材id
     */
    private Long matId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标识
     */
    private Integer flag;


	@Override
	public String toString() {
		return "MaterialStore{" +
				"id=" + id +
				",storeId=" + storeId +
				",matId=" + matId +
				",status=" + status +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",flag=" + flag +
				'}';
	}
}
