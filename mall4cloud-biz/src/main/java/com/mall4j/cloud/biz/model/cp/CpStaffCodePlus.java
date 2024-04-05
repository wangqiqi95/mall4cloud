package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cp_staff_code")
public class CpStaffCodePlus extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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
     * 修改人
     */
    private Long updateBy;

    /**
     * 修改人
     */
    private String updateName;


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

    /**
     * 自动备注：0否/1是
     */
    private Integer autoRemarkState;

    /**
     * 自动备注前缀
     */
    private String autoRemark;

    /**
     * 二维码样式
     */
    private String codeStyle;

    /**
     * 欢迎语类型：0通用渠道/1默认员工/不发欢迎语
     */
    private Integer welcomeType;

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
     * 是否开启分时段欢迎语：0否/1是
     */
    private Integer welcomeTimeState;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 接待时间类型：0全天接待/1分时段接待
     */
    private Integer receptionType;

    /**
     * 自动描述：0否/1是
     */
    private Integer autoDescriptionState;

    /**
     * 自动描述前缀
     */
    private String autoDescription;

}
