

package com.mall4j.cloud.seckill.vo;


import java.util.List;

/**
 * 秒杀配置信息
 * @author lhd
 */
public class SeckillConfigVO {

	/**
	 * 秒杀时段
	 */
	private List<Integer> seckillTimeList;
	/**
	 * 秒杀轮播图
	 */
	private String imgUrls;

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public List<Integer> getSeckillTimeList() {
		return seckillTimeList;
	}

	public void setSeckillTimeList(List<Integer> seckillTimeList) {
		this.seckillTimeList = seckillTimeList;
	}

	@Override
	public String toString() {
		return "SeckillConfigVO{" +
				"seckillTimeList=" + seckillTimeList +
				'}';
	}
}

