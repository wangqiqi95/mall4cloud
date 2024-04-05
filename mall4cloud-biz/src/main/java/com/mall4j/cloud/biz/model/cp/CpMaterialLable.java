package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 素材 互动雷达标签
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
@Data
public class CpMaterialLable extends BaseModel implements Serializable{
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
     * 浏览多少秒开始统计
     */
    private Integer interactionSecond;

    /**
     * 互动达标记录标签id
     */
    private String radarLabalId;

    /**
     * 互动达标记录标签name
     */
    private String radarLabalName;

	/**
	 * 标签值 多值的话用逗号隔开
	 */
	private String radarLableValue;

}
