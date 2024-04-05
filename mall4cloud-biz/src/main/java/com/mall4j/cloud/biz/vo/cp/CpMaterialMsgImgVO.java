package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VO
 *
 * @author gmq
 * @date 2023-12-06 16:14:28
 */
@Data
public class CpMaterialMsgImgVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("素材消息id")
    private Long matMsgId;

    @ApiModelProperty("图片地址")
    private String image;

	@ApiModelProperty("排序")
	private Integer seq;

}
