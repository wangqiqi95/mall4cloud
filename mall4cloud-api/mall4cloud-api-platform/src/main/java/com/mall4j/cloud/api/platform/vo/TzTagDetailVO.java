package com.mall4j.cloud.api.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 门店标签VO
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
@Data
public class TzTagDetailVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long tagId;

    @ApiModelProperty("标签名称")
    private String tagName;

	@ApiModelProperty("门店集合")
	private List<TzTagStoreDetailVO> stores;

}
