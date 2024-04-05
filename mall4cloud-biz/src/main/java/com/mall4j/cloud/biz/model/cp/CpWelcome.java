package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 欢迎语配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpWelcome extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 欢迎语
     */
    private String slogan;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态   0 无效  1  有效
     */
    private Integer status;

    /**
     *   1 已删除 0 未删除
     */
    private Integer flag;

    /**
     *
     */
    private Integer isAllShop;

    /**
     *
     */
    private String createName;

    /**
     * 源id
     */
    private Long sourceId;

    /**
     * 源：0欢迎语/1渠道活码/2渠道活码时间段
     */
    private Integer sourceFrom;

    /**
     * 时间段：周期
     */
    private String timeCycle;

    /**
     * 时间段：开始时间
     */
    private String timeStart;

    /**
     * 时间段：结束时间
     */
    private String timeEnd;

    /**
     * 场景 0未注册，1注册
     */
    private String scene;

    @ApiModelProperty("是否分时段欢迎语：0否/1是")
    private Integer welcomeTimeState = 0;

}
