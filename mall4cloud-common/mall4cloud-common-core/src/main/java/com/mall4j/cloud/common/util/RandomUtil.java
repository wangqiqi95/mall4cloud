package com.mall4j.cloud.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 随机帮助
 * 
 * @author Dylan Tao
 * @date 2015-4-29 Copyright 2016 wx.com Team. All Rights Reserved.
 */
public class RandomUtil {

	private static final String DOUBLE_LINE = "//";
	private static final String DOT = ".";
	private static final String LINE = "/";
	private static final String COLON = ":";
	private static final String COMMA = ",";
	private static final String EMPTY = "";
	private static final String SPACE = " ";
	private static final String HR = "-";
	private static final String QM = "?";
	private static final String EM = "=";
	private static final String AM = "&";

	private static final Log log = LogFactory.getLog(RandomUtil.class);

	private static long lastTime = System.currentTimeMillis();
	private static short lastCount = -32768;
	private static Object mutex = new Object();

	private static final Lock LOCK = new ReentrantLock();
	private static short lastNum = 0;
	private static int count = 0;

	/**
	 * 获取唯一且有序的16位的数字(集群需要组合机器码/ip)
	 * 
	 * @return long 16位
	 */
	@SuppressWarnings("finally")
	public static long getUniqueNum() {
		LOCK.lock();
		try {
			if (lastNum == 10) {
				boolean done = false;
				while (!done) {
					long now = System.currentTimeMillis();
					if (now == lastTime) {
						try {
							Thread.currentThread();
							Thread.sleep(1);
						} catch (InterruptedException e) {
							log.error(e);
						}
						continue;
					} else {
						lastTime = now;
						lastNum = 0;
						done = true;
					}
				}
			}
			count = lastNum++;
		} catch (RuntimeException e) {
			log.error(e);
		} finally {
			LOCK.unlock();
			String ip =  "127.0.0.1";
			String[] ipArray = ip.split("\\.");  
			String lastReturnNum = lastTime + StringUtils.leftPad(ipArray[3], 3, "0").substring(1)
					+ String.format("%01d", count);
			log.info("====== Unique Number: " + lastReturnNum);
			return Long.parseLong(lastReturnNum);
		}
	}

	public static String getUniqueNumStr(){
		return String.valueOf(getUniqueNum());
	}


	/**
	 * 自制22位uuid(数字字母组合)主键且有序
	 * 
	 * @param hr
	 *            是否包含横杠
	 * @return String 22位uuid
	 */
	public static String getUniqueKey(boolean hr) {
		long l = 0L;
		short word0 = 0;
		int i = 0;
		synchronized (mutex) {
			if (lastCount == 32767) {
				for (boolean flag = false; !flag;) {
					l = System.currentTimeMillis();
					if (l < lastTime + 1000L) {
						try {
							Thread.currentThread();
							Thread.sleep(1000);
						} catch (InterruptedException interruptedexception) {
							log.error(interruptedexception);
						}
					} else {
						lastTime = l;
						lastCount = -32768;
						flag = true;
					}
				}
			} else {
				l = lastTime;
			}
			word0 = lastCount++;
			i = getHostUniqueNum();
		}
		String s = Integer.toString(i, 16) + Long.toString(l, 16)
				+ Integer.toString(word0, 16);
		if (!hr) {
			s = s.replace(HR, EMPTY);
		}
		if (s.length() > 22) {
			s = s.substring(s.length() - 22);
		}
		log.info("====== Unique Key: " + s);
		return s;
	}

	public static String randomNum(int randomLength) {
		char[] randoms = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < randomLength; i++) {
			ret.append(randoms[random.nextInt(randoms.length)]);
		}
		random = null;
		return ret.toString();
	}
	
	private static int getHostUniqueNum() {
		return (new Object()).hashCode();
	}

	public static String getRandomStr(int num){
		String randomcode = "";
		// 用字符数组的方式随机
		String model = "ABCDEFGHIJKLMN0123456789abcdefghijklOPQRSTUVWXYZmnopqrstuvwxyz";
		char[] m = model.toCharArray();
		for (int j = 0; j < num; j++) {
			char c = m[(int) (Math.random() * 36)];
			// 保证六位随机数之间没有重复的
			if (randomcode.contains(String.valueOf(c))) {
				j--;
				continue;
			}
			randomcode = randomcode + c;
		}
		return randomcode;
	}

	public static void main(String[] args) {
		System.out.println(randomNum(19));
	}

}
