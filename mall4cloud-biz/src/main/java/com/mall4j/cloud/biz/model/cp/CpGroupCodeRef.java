package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群活码关联群表
 *
 * @author hwy
 * @date 2022-02-16 15:17:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpGroupCodeRef extends BaseModel implements Serializable{
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
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
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
     *
     */
    private String qrCode;

    /**
     * 是否自动新建群: 0-否/1-是
     */
    private Integer autoCreateRoom;

    /**
     * 群人数上限
     */
    private Integer total;

    /**
     * 是否开启群名称设置：0否/1是
     */
    private Integer groupNameState;

    /**
     * 自动建群的群名前缀
     */
    private String roomBaseName;

    /**
     * 自动建群的群起始序号
     */
    private String roomBaseId;

    /**
     * 截止开始时间
     */
    private Date expireStart;

    /**
     * 截止结束时间
     */
    private Date expireEnd;

    /**
     * 活码id
     */
    private Long codeId;

    /**
     * 企微群id
     */
    private String chatId;

    private String configId;

    /**
     * 0群活码/1自动拉群
     */
    private Integer sourceFrom;

    private String state;

    /**
     * 扫码入群人数
     */
    private Integer scanCount=0;

    private String style;

    /**
     * 1企微群活码/2自建群活码
     */
    private Integer codeType;

    /**
     * 单个群可邀请人数
     */
    @TableField(exist = false)
    private Integer enabledCustTotal;

    /**
     * 群实际群人数
     */
    @TableField(exist = false)
    private Integer chatUserTotal=0;

    @TableField(exist = false)
    @ApiModelProperty("启用停用判断状态: 0无效 1有效")
    private Integer enableStatus;
}
