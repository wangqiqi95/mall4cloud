package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 渠道活码人员时间段
 *
 * @author gmq
 * @date 2023-10-25 16:39:38
 */
@Data
public class CpStaffCodeTime extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    private String createBy;

    private String updateBy;

    /**
     * 状态   0 无效  1  有效
     */
    private Integer status;

    /**
     *   1 已删除 0 未删除
     */
    private Integer isDelete;

    /**
     * 源id
     */
    private Long sourceId;

    /**
     * 源：0欢迎语/1渠道活码
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

}
