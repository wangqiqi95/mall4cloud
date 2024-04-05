package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 组织结构表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
@Data
public class Organization extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 组织节点id
     */
//    @TableId(type = IdType.AUTO)
    @TableId
    private Long orgId;

    /**
     * 上级节点id
     */
    private Long parentId;

    /**
     * 路径
     */
    private String path;

    /**
     * 组织节点名称
     */
    private String orgName;

    /**
     * 节点类型 1-品牌 ，2-片区，3-店群，4-门店
     */
    private Integer type;

    /**
     * 组织节点id
     */
    private String orgCode;

	private String shortName;

	private String qiWeiCode;

	private String remark;

	private String linkman;

	private String mobile;

	private String email;

	private Integer isDeleted;

	private String orderNum;

	private String departmentLeader;

	private String nameEn;

	private String createBy;

	private String updateBy;

}
