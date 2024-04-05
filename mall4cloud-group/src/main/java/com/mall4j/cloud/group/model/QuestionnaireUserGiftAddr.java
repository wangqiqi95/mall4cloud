package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户奖品配送地址
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:11:00
 */
@Data
public class QuestionnaireUserGiftAddr extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 地址
     */
    private String addr;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String logisticsNumber;

}
