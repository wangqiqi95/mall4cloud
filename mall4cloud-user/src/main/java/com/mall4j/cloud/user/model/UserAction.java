package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户行为记录表
 *
 * @author gww
 * @date 2022-02-18 16:29:23
 */
@Data
@TableName("user_action")
public class UserAction implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 用户行为轨迹id
     */
	@TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 触点号
     */
    private String tentacleNo;

    /**
     * 类型 1:购买 2:加购 3:浏览 4:收藏
     */
    private Integer type;

    /**
     * 页面类型 1:海报 2:专题 3:朋友圈 4:商品 5:会员页 6:首页
     */
    private Integer pageType;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单id
     */
    private Long orderId;

	/**
	 * 创建时间
	 */
	protected Date createTime;

	/**
	 * 更新时间
	 */
	protected Date updateTime;

}
