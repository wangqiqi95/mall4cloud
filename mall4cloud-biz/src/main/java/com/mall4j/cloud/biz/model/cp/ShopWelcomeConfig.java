package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 欢迎语门店关联表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class ShopWelcomeConfig extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long welId;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 1有效 0 无效
     */
    private Integer status;

    /**
     * 1删除 0 未删除
     */
    private Integer flag;

    /**
     * 0部门/1员工
     */
    private Integer type;

    @Override
	public String toString() {
		return "ShopWelcomeConfig{" +
				"id=" + id +
				",welId=" + welId +
				",shopId=" + shopId +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
