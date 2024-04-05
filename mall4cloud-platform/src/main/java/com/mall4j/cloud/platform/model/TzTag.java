package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 门店标签
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
@Data
public class TzTag extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Long tagId;

    private Long parentId;

    /**
     * 标签编码
     */
    private String tagCode;

    private String tagType;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 状态：0禁用 1启用
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;

}
