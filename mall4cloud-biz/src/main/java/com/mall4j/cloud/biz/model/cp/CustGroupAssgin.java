package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 客群分配表 
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class CustGroupAssgin extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 群ID
     */
    private Long groupId;

    /**
     * 群客户数
     */
    private Integer custGroupNums;

    /**
     * 离职操作ID
     */
    private Long resignId;

    /**
     * 分配状态
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;


	@Override
	public String toString() {
		return "CustGroupAssgin{" +
				"id=" + id +
				",groupId=" + groupId +
				",custGroupNums=" + custGroupNums +
				",resignId=" + resignId +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
