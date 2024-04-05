package com.mall4j.cloud.product.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销标签活动表
 *
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
@Data
public class TagActivity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
	private  Long id;
	/**
	 * 活动名称
	 */
	private  String activityName;
	/**
	 * 标签名称
	 */
	private  String tagName;
	/**
	 * 活动开始时间
	 */
	private Date startTime;
	/**
	 * 活动结束时间
	 */
	private  Date endTime;
	/**
	 * 是否全店 0 否 1是
	 */
	private  Integer isAllShop;
	/**
	 * 角标类型 1固定角标
	 */
	private  Integer tagType;
	/**
	 * 角标方位  1 左上 2 左下 3 右上 4 右下 5 全覆盖
	 */
	private  Integer tagPosition;
	/**
	 * 图标url
	 */
	private  String tagUrl;
	/**
	 * 权重
	 */
	private  Integer weight;
	/**
	 * 活动状态 0 未启动 1 未开始 2 进行中 3已结束
	 */
	private Integer status;

 ;
	/**
	 * 创建人
	 */
	private  Long createBy;
	/**
	 * 创建人名称
	 */
	private  String createName;
	/**
	 * 更新人
	 */
	private  Long updateBy;
}