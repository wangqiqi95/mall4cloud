
package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lt
 * @date 2020/12/30
 */
@Data
@ApiModel("直播类目")
public class LiveCategoryDTO {

	@ApiModelProperty(value = "类目ID")
	@NotBlank(message = "类目不能为空")
	private String categoryId;

	@ApiModelProperty(value = "类目名称")
	private String categoryName;

	@ApiModelProperty(value = "营业执照或组织机构代码证，图片url")
	@NotBlank(message = "营业执照或组织机构代码证不能为空")
	private String license;

	@ApiModelProperty(value = "商品使用场景,1:视频号，3:订单中")
	@NotNull(message = "商品使用场景,1:视频号，3:订单中")
	private List<Integer> sceneGroupList;

	@ApiModelProperty(value = "资质材料，图片url")
	@NotBlank(message = "资质材料不能为空")
	private String certificate;

}
