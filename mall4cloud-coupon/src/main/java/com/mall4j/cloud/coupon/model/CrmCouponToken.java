package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @description crm_coupon_token
 * @author 施晶
 * @date 2022-03-17
 */
@Data
public class CrmCouponToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * crm优惠券券码
     */
    private String crmCouponCode;

    /**
     * 冻结返回的token
     */
    private String token;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 销售单号
     */
    private Long orderNo;
    /**
     * 用户id
     */
    private Long userId;

}
