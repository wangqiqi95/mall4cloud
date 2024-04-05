package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 素材 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 10:03:14
 */
@Data
public class CpMaterialUseRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 素材id
     */
    private Long matId;

    /**
     * 导购编号
     */
    private Long staffId;

    /**
     * 导购名称
     */
    private String staffName;

    private Date createTime;

}
