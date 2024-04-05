package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 引流手机号任务
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
@Data
public class CpPhoneTask extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行开始时间
     */
    private Date startTime;

    /**
     * 执行结束时间
     */
    private Date endTime;

    /**
     * 目标客户源：0全部/1按来源/2按创建时间
     */
    private Integer userType;

    /**
     * 每人每天分配数量
     */
    private Integer dailyAllocationTotal;

    /**
     * 累计分配数量
     */
    private Integer allocationTotal;

    /**
     * 引导语
     */
    private String slogan;

    /**
     * 开启状态：0未开启/1已开启
     */
    private Integer openStatus;

    /**
     * 状态：0未开始/1已开始/2进行中/3已结束
     */
    private Integer status;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * 目标手机号来源
     */
    private Integer importFrom;

    /**
     * 目标手机号开始时间
     */
    private Date phoneStartTime;

    /**
     * 目标手机号结束时间
     */
    private Date phoneEndTime;

    /**
     * 0正常/1删除
     */
    private Integer isDelete;

    /**
     * 已分配的日期
     */
    private String distributeTime;

    @TableField(exist = false)
    private Integer actualTotal;
}
