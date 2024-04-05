package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 标签组配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class TagGroup extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 标签组id
     */
    private Integer id;

    /**
     * 标签组名称
     */
    private String groupName;

    /**
     * 类型 1 唯一  0 多个
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;

    /**
     *排序
     */
    private Integer sort;



	@Override
	public String toString() {
		return "TagGroup{" +
				"id=" + id +
				",groupName=" + groupName +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
