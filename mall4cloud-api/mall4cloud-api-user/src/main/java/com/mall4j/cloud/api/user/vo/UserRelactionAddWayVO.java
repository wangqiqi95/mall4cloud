package com.mall4j.cloud.api.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 好友统计-来源分析
 *
 */
@Data
public class UserRelactionAddWayVO {
    private static final long serialVersionUID = 1L;

    //https://developer.work.weixin.qq.com/document/path/92114#%E6%9D%A5%E6%BA%90%E5%AE%9A%E4%B9%89
    @ApiModelProperty("来源枚举")
    private Integer cpAddWay;

    @ApiModelProperty("数量")
    private Integer count;
}
