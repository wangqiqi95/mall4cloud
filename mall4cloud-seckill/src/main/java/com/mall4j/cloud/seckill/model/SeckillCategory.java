package com.mall4j.cloud.seckill.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 秒杀分类信息
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
public class SeckillCategory extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String name;


    /**
     * 排序
     */
    private Integer seq;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "SeckillCategory{" +
				"categoryId=" + categoryId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",name=" + name +
				",seq=" + seq +
				'}';
	}
}
