package com.mall4j.cloud.biz.event;

import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import lombok.Data;

/**
 * @Date 2022年4月27日, 0027 14:19
 * @Created by eury
 */
@Data
public class TentacleQrcodeEvent {

    private WeixinQrcodeTentacleDTO tentacleDTO;

    private WeixinQrcodeTentacle weixinQrcodeTentacle;
}
