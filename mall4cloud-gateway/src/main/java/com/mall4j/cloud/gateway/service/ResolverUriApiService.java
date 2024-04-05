package com.mall4j.cloud.gateway.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class ResolverUriApiService {

    @Value("${mall4cloud.gateway.resolveuri:/t_coupon_user/my_coupon_list}")
    @Setter
    private String resolveuri="/t_coupon_user/my_coupon_list,/t_coupon_user/my_coupon_detail,/t_coupon_user/receive_coupon";

    @Value("${mall4cloud.gateway.hinit:活动火爆，请稍后重试 }")
    @Setter
    private String hinit="活动火爆，请稍后重试 ";


    public String getResolveuri(){
        return resolveuri;
    }

    public String getHinit() {
        return hinit;
    }

}
