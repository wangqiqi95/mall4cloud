package com.mall4j.cloud.docking.utils;

import com.mall4j.cloud.api.docking.skq_erp.config.DockingStdParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 类描述：中台验签工具类
 *
 * @date 2022/1/7 1:22：26
 */
@Component
public class StdSignUtils {

	@Autowired
	DockingStdParams dockingStdParams;

	/**
	 * 方法描述：生成签名
	 * @param method		方法
	 * @param data			请求json数据
	 * @param timestamp		时间戳
	 * @return java.lang.String
	 * @date 2022-01-07 18:06:41
	 */
	public String generateSign(String method, String data, long timestamp) {
		StringBuilder signStr = new StringBuilder();
		signStr.append("data=").append(data).append("&");
		signStr.append("key=").append(dockingStdParams.getUserKey()).append("&");
		signStr.append("method=").append(method).append("&");
		signStr.append("timestamp=").append(System.currentTimeMillis()).append("&");
		signStr.append("version=").append("v1");
		return MD5Util.encode(signStr.toString());
	}
}
