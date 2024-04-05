package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 欢迎语门店关联表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShopWelcomeConfigVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    private Long welId;

    @ApiModelProperty("门店id")
    private Long shopId;

    @ApiModelProperty("1有效 0 无效")
    private Integer status;

    @ApiModelProperty("1删除 0 未删除")
    private Integer flag;


	@Override
	public String toString() {
		return "ShopWelcomeConfigVO{" +
				"id=" + id +
				",welId=" + welId +
				",shopId=" + shopId +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
