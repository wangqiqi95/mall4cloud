package com.mall4j.cloud.api.openapi.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @date 2023/5/9
 */
@Data
public class TripartiteCommonReq implements Serializable {

	private static final long serialVersionUID = -6864068678829180539L;

	@ApiModelProperty(value = "appKey不能为空")
	@NotBlank(message = "appKey不能为空")
	private String appKey;

	@ApiModelProperty(value = "方法类型")
	@NotBlank(message = "method不能为空")
	private String method;

	@ApiModelProperty(value = "加签")
	@NotBlank(message = "sign不能为空")
	private String sign;

	@ApiModelProperty(value = "版本（默认为v1）")
	@NotBlank(message = "version不能为空")
	private String version;

	@ApiModelProperty(value = "请求时的时间戳(秒级)")
	@NotNull(message = "timestamp不能为空")
	private Long timestamp;
}
