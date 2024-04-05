package com.mall4j.cloud.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 类描述：对外接口固定参数
 *
 * @date 2022/1/22 17:17：38
 */
@Data
@RefreshScope
@Component
public class OpenApiParams {

	@Value("${openapi.secretKey}")
	private String secretKey="gRQJ2CMqP18h60hdnbcRCYsd";

}
