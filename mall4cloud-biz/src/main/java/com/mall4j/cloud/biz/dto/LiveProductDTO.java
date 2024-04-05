
package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lt
 * @date 2020/12/30
 */
@Data
@ApiModel("直播商品")
public class LiveProductDTO {

	@ApiModelProperty(value = "类目ID")
	private String categoryId;

	@ApiModelProperty(value = "类目名称")
	private String categoryName;

	@ApiModelProperty(value = "品牌ID")
	private String brandId;

	@ApiModelProperty(value = "商品ID列表")
	@NotEmpty(message = "商品列表不能为空")
	private List<String> validCode;

	@ApiModelProperty(value = "门店ID")
	private String shopCode;

	@ApiModelProperty(value = "门店名称")
	private String shopName;

	@ApiModelProperty(value = "商品使用场景,1:视频号，3:订单中")
	private List<Integer> sceneGroupList;
}
