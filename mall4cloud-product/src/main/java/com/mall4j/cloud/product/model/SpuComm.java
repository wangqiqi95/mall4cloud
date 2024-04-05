package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品评论
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpuComm extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

	/**
	 * 匿名
	 */
	public static Integer anonymous = 1;

    /**
     * ID
     */
    private Long spuCommId;

    /**
     * 商品ID
     */
    private Long spuId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 订单项ID
     */
    private Long orderItemId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 掌柜回复
     */
    private String replyContent;

    /**
     * 回复时间
     */
    private Date replyTime;

    /**
     * 是否回复 0:未回复  1:已回复
     */
    private Integer replySts;

    /**
     * IP来源
     */
    private String postip;

    /**
     * 商品得分，1-5分
     */
    private Integer score;

    /**
     * 店铺评分 1-5分
     */
    private Integer storeScore;

    /**
     * 物流评分 1-5分
     */
    private Integer logisticsScore;

    /**
     * 有用的计数
     */
    private Integer usefulCounts;

    /**
     * 晒图的json字符串
     */
    private String pics;

    /**
     * 是否匿名(1:是  0:否)
     */
    private Integer isAnonymous;

    /**
     * 是否显示  -1:删除 0:不显示 1:显示 2 审核不通过 3.待审核
     */
    private Integer status;

    /**
     * 评价(0好评 1中评 2差评)
     */
    private Integer evaluate;

	//------------ 追加评论--------------------------------
	/**
	 * 追加评论内容
	 */
	private String additContent;

	/**
	 * 追加商家回复
	 */
	private String additReplyContent;

	/**
	 * 追加回复时间
	 */
	private Date additReplyTime;
	/**
	 * 追加时间
	 */
	private Date additCreateTime;

	/**
	 * 是否有图片 0 没有 1 有
	 */
	private Integer hasImages;

	/**
	 * 客户姓名
	 */
	private String userName;

	/**
	 * 追加晒图的json字符串
	 */
	private String additPics;
    /**
     * 追加评论是否回复 0:未回复  1:已回复
     */
    private Integer additReplySts;
    /**
     * 客户手机号码
     */
    private String mobile;
    /**
     * 订单号
     */
    private Long orderId;

    private String orderNumber;
    /**
     * 回复人信息
     */
    private String replyStaffName;

    /**
     * 追加回复人信息
     */
    private String additReplyStaffName;


}
