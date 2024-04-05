package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryCrmIdsDTO {

    @ApiModelProperty(value = "优惠券图片标识（0：默认/1：礼品券/2：生日礼/3:员工券/4:升级礼/5：保级礼/6:服务券/7:满减券/8:折扣券/9:入会券/10:免邮券）")
    private List<Integer> couponPictures;

    @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private List<Integer> kinds;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

}
