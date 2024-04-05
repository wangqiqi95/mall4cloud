package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 种草信息
 *
 * @author cg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommend extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long recommendId;

    /**
     * 发布用户：1-B端用户 | 2-C端用户
     */
    private Integer userType;

    /**
     * 创建人uid
     */
    private Long userId;

    /**
     * 创建人nick
     */
    private String userNick;

    /**
     * 创建人头像
     */
    private String userPic;

    /**
     * 标题
     */
    private String title;

    /**
     * 种草分类id C端用户创建时为空
     */
    private Long recommendCateId;

    /**
     * 种草分类name
     */
    private String cateName;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 正文
     */
    private String content;

    /**
     * 冗余商品id，逗号隔开
     */
    private String productIds;

    /**
     * 状态：0-默认 | 1-待审核 | 2-审核通过 | 3-已驳回
     */
    private Integer status;

    /**
     * 封面
     */
    private String coverUrl;

    /**
     * 点赞数
     */
    private Long praiseCount;

    /**
     * 收藏数
     */
    private Long collectCount;

    /**
     * 分享数
     */
    private Long shareCount;

    /**
     * 分享数
     */
    private Long readCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除：0-否 |1-是
     */
    private Integer disable;

}
