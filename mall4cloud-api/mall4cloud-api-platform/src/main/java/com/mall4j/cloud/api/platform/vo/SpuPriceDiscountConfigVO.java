package com.mall4j.cloud.api.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统配置信息表VO
 *
 * @author lhd
 * @date 2021-04-20 16:27:57
 */
@Data
public class SpuPriceDiscountConfigVO {
    private static final long serialVersionUID = 1L;


    private String discount;
    private String email;
    private String subject;
    private String content;

}
