package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 客户标签配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class Tag extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 所属标签组
     */
    private Integer groupId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;


	@Override
	public String toString() {
		return "Tag{" +
				"id=" + id +
				",tagName=" + tagName +
				",groupId=" + groupId +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
