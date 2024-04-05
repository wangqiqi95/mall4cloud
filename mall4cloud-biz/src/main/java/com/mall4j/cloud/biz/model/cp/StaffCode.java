package com.mall4j.cloud.biz.model.cp;

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
public class StaffCode extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    private Long id;

    /**
     * 活动标题
     */
    private String codeName;

    /**
     * 活码类型
     */
    private Integer codeType;

    /**
     * 0 自动通过， 1 手动通过
     */
    private Integer authType;

    /**
     * 欢迎语
     */
    private String slogan;

    /**
     * 标签
     */
    private String tags;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人
     */
    private String createName;


    /**
     * 活码的配置id
     */
    private String configId;

    /**
     * 活码唯一吗
     */
    private String state;
    /**
     * 二维码链接
     */
    private  String qrCode;

    /**
     * 状态   0 无效  1  有效
     */
    private Integer status;

    /**
     *   1 已删除 0 未删除
     */
    private Integer flag;

    /**
     * 来源 0 默认 后台  1 小程序
     */
    private  Integer  origin;


}
