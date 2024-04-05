package com.mall4j.cloud.gateway.dev;

import org.springframework.web.server.ServerWebExchange;

/**
 * @author FrozenWatermelon
 * @date 2020/7/16
 */
public class DevWebFluxContext {

	/** The request holder. */
	private static final ThreadLocal<ServerWebExchange> SERVER_WEB_EXCHANGE_HOLDER = new ThreadLocal<>();

	public static ServerWebExchange get() {
		return SERVER_WEB_EXCHANGE_HOLDER.get();
	}

	public static void set(ServerWebExchange serverWebExchange) {
		SERVER_WEB_EXCHANGE_HOLDER.set(serverWebExchange);
	}

	public static void clean() {
		if (SERVER_WEB_EXCHANGE_HOLDER.get() != null) {
			SERVER_WEB_EXCHANGE_HOLDER.remove();
		}
	}

}
