package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 
 *
 */
@Data
public class CpStaffCodeLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    private String staffNo;

    private String staffPhone;

    /**
     * 失败原因
     */
    private String logs;

    /**
     * 微信返回失败日志
     */
    private String wxerror;

	/**
	 * 微信返回失败状态码
	 */
	private String errorCode;

    /**
     * 0失败 1成功
     */
    private Integer status;

    /**
     * 欢迎语内容
     */
    private String attachmentExt;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;
}
