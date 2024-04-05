package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 话术分类表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Data
public class CpChatScriptMenu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String menuName;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 是否显示
     */
    private Integer isShow;

    /**
     * 删除标识 1 删除 0 未删除
     */
    private Integer flag;

    /**
     * 状态 0无效 1有效
     */
    private Integer status;

    /**
     * 是否置顶 0否 1是
     */
    private Integer isTop;

	@ApiModelProperty("话术类型 0普通话术 1问答话术")
	private Integer type;
}
