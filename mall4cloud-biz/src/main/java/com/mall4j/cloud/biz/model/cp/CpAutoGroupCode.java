package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 自动拉群活码表
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpAutoGroupCode extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活码名称
     */
    private String codeName;

    /**
     * 活码类型
     */
    private Integer codeType;

    /**
     * 标签集合
     */
    private String tags;

    /**
     * 通过好友：0 自动通过， 1 手动通过
     */
    private Integer authType;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Long createBy;

    /**
     *
     */
    private String createName;

    /**
     *
     */
    private Date updateTime;

    /**
     * 状态   0 无效  1  有效
     */
    private Integer status;

    /**
     * 欢迎语
     */
    private String slogan;

    /**
     * 1 已删除 0 未删除
     */
    private Integer flag;

    /**
     *
     */
    private String configId;

    /**
     *
     */
    private String state;

    /**
     * 二维码链接
     */
    private String qrCode;

    /**
     * 自动备注：0否/1是
     */
    private Integer autoRemarkState;

    /**
     * 二维码样式
     */
    private String codeStyle;

    /**
     * 自动备注前缀
     */
    private String autoRemark;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 引流链接
     */
    private String drainageUrl;

    /**
     * 引流页面
     */
    private String drainagePath;

    /**
     * 拉群方式：0企微群活码/1自建群活码
     */
    private Integer groupType;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     *
     */
    private String updateBy;



}
