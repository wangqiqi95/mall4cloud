package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpStaffCodeRef extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活码id
     */
    private Long codeId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     *
     */
    private String userId;

    /**
     * 人员类型：0-接待人员/1-备用人员
     */
    private int type;

    private Long codeTimeId;

    private Integer isDelete;

    /**
     * 接待状态：0未接待/1接待中
     */
    private Integer receptionStatus;
}
