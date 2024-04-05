package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * @description 积分banner活动图片关联表
 * @author shijing
 * @date 2022-01-23
 */
@Data
public class ScoreBannerUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 积分banner活动id
     */
    private Long bannerId;

    /**
     * 链接通用组件-类型
     */
    private Short type;

    /**
     * 链接通用组件-链接类型
     */
    private Short linkType;

    /**
     * 链接通用组件-小程序appId
     */
    private String appId;

    /**
     * 图片url
     */
    private String bannerUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 标题
     */
    private String title;

    /**
     * 链接
     */
    private String url;

    /**
     * 标题
     */
    private String linkTitle;

}
