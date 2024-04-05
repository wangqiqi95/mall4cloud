package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群活码设置群聊表
 *
 * @author gmq
 * @date 2023-10-27 14:27:52
 */
@Data
public class CpGroupCodeList extends BaseModel implements Serializable{
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
     * 群活码id
     */
    private Long groupId;

}
