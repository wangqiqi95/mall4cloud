package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 话术 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 10:25:19
 */
@Data
public class CpChatScriptUseRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 话术id
     */
    private Long scriptId;

    /**
     * 导购编号
     */
    private Long staffId;

    /**
     * 导购名称
     */
    private String staffName;

}
