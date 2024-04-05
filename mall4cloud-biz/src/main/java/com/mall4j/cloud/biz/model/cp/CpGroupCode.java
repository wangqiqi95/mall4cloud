package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群活码表
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpGroupCode extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String name;

    /**
     * 欢迎语
     */
    private String slogan;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人店id
     */
    private Long storeId;

    /**
     * 创建人店名称
     */
    private String storeName;

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
    private String qrCode;

    /**
     *
     */
    private String state;

    /**
     *
     */
    private String configId;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 拉群方式：0企微群活码/1自建群活码
     */
    private Integer groupType;

    /**
     * 备用员工id
     */
    private Long standbyStaffId;
    /**
     * 备用员工活码
     */
    private String standbyStaffCode;
    /**
     * 备用员工活码configId
     */
    private String standbyStaffConfigId;
    /**
     * 备用员工活码state
     */
    private String standbyStaffState;

    /**
     * 活码样式
     */
    private String style;

    /**
     *
     */
    private String updateBy;

    /**
     * 群活码源：0群活码/1自动拉群-群活码
     */
    private Integer codeFrom;

    /**
     * 引流地址
     */
    private String drainageUrl;

    /**
     * 引流页面
     */
    private String drainagePath;
}
