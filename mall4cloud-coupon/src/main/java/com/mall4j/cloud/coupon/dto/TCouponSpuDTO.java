package com.mall4j.cloud.coupon.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.coupon.model.TCouponSku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TCouponSpuDTO {

    private Long id;

    private Long couponId;

    private Long spuId;

    @ApiModelProperty("参与方式 0sku-code 1按条码")
    private Integer participationMode;

    private List<TCouponSkuDTO> skus;
}
