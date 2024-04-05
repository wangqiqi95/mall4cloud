package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信素材链接表VO
 *
 * @author gmq
 * @date 2022-01-26 22:53:05
 */
@Data
public class WeixinLinksucaiVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("外部链接")
    private String outerLink;
}
