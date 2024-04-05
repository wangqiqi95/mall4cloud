package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 积分商城首页banner设置
 * @author shijing
 * @date 2022-01-23
 */
@Data
public class ScoreBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * banner活动名称
     */
    private String name;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 是否全部门店
     */
    private Boolean isAllShop;

    /**
     * 权重
     */
    private Integer weight;

    @ApiModelProperty(value = "banner图活动类型: {1：积分商城首页，2：完善信息，3：社区头部，4：社区活动}")
    private Integer type;

    /**
     * 0：启用/1：禁用
     */
    private int status;

    private Long createId;

    private String createName;

    private Date createTime;

    private Long updateId;

    private String updateName;

    private Date updateTime;

}
