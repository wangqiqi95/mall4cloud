package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_coupon_spu")
public class TCouponSpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 商品id
     */
    private Long spuId;

//    @ApiModelProperty("参与方式 0sku-code 1按条码")
    private Integer participationMode;

    @TableField(exist = false)
    private List<TCouponSku> skus;
}
