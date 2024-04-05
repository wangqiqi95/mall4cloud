package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 话术表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Data
public class CpChatScript extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String scriptName;

    /**
     * 话术分类id
     */
    private Integer scriptMenuId;

    /**
     * 话术内容
     */
    private String scriptContent;

    /**
     * 话术答案
     */
    private String scriptAnswer;

    /**
     * 话术类型 0普通话术 1问答话术
     */
    private Integer type;

    /**
     * 开始时间(话术有效期)
     */
    private Date validityCreateTime;

    /**
     * 截止时间(话术有效期)
     */
    private Date validityEndTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否删除 0否1是
     */
    private Integer delFlag;

    /**
     * 是否全部部门
     */
    private Integer isAllShop;

    /**
     * 
     */
    private String createName;

    /**
     * 话术标签
     */
    private String scriptLabal;

	@ApiModelProperty("话术适用部门列表")
	private List<Long> storeIds;

    private Integer useNum;
}
