package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 离职客户分配表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpCustAssginDetail extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long custId;

    /**
     * 客户名称
     */
    private String custName;

    /**
     * 离职操作ID
     */
    private Long resignId;

    /**
     * 分配状态
     */
    private Integer staus;

    /**
     * 删除标识
     */
    private Integer flag;

	@Override
	public String toString() {
		return "CustAssginDetail{" +
				"id=" + id +
				",custId=" + custId +
				",custName=" + custName +
				",resignId=" + resignId +
				",staus=" + staus +
				",flag=" + flag +
				'}';
	}
}
