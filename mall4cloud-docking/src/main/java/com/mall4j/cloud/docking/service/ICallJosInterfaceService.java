package com.mall4j.cloud.docking.service;

import com.mall4j.cloud.api.docking.jos.enums.JosInterfaceMethod;
import com.mall4j.cloud.api.docking.jos.service.IJosParam;
import com.mall4j.cloud.docking.utils.JosClients;

public interface ICallJosInterfaceService<T> {

	default T callJosInterface(JosInterfaceMethod josInterfaceMethod, IJosParam josParam) {
		return convert(realCallJosInterface(josInterfaceMethod, josParam));
	}

	T convert(String s);

	default String realCallJosInterface(JosInterfaceMethod josInterfaceMethod, IJosParam josParam) {
		return JosClients.clients().req(josInterfaceMethod.url(), josInterfaceMethod.method(), josParam);
	}
}
