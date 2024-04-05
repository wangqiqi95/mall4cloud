package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 秒杀分类信息VO
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
public class SeckillCategoryVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀分类id")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("排序")
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
		return "SeckillCategoryVO{" +
				"categoryId=" + categoryId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",name=" + name +
				",seq=" + seq +
				'}';
	}
}
