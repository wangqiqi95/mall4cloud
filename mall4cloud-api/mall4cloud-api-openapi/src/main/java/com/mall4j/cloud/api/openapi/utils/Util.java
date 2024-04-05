package com.mall4j.cloud.api.openapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	private Util() {
	}

	/**
	 * 线程休眠（ms）
	 *
	 * @param iSleepTime 线程休眠的时间（ms）
	 */
	public static void threadSleep(long iSleepTime) {
		try {
			Thread.sleep(iSleepTime);
		} catch (InterruptedException ex) {
			Thread thread = Thread.currentThread();
			logger.error("线程ID：{}，线程：{}，睡眠中断",thread.getId(), thread.getName());
		}
	}

	public static void threadSleep(int iSleepTime) {
		try {
			Thread.sleep(iSleepTime);
		} catch (InterruptedException ex) {
			Thread thread = Thread.currentThread();
			logger.error("线程ID：{}，线程：{}，睡眠中断",thread.getId(), thread.getName());
		}
	}
}
