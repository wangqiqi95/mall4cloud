package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 秒杀信息VO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class AppSeckillVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("最近一场秒杀活动时间，负数表示当前的秒杀活动还有多久结束")
    private Long nextTime;

    @ApiModelProperty("最近一场秒杀活动时段")
    private Integer selectedLot;

    @ApiModelProperty("秒杀活动列表")
    private List<SeckillSpuVO> seckillSpuList;

	public Integer getSelectedLot() {
		return selectedLot;
	}

	public void setSelectedLot(Integer selectedLot) {
		this.selectedLot = selectedLot;
	}

	public Long getNextTime() {
		return nextTime;
	}

	public void setNextTime(Long nextTime) {
		this.nextTime = nextTime;
	}

	public List<SeckillSpuVO> getSeckillSpuList() {
		return seckillSpuList;
	}

	public void setSeckillSpuList(List<SeckillSpuVO> seckillSpuList) {
		this.seckillSpuList = seckillSpuList;
	}

	@Override
	public String toString() {
		return "AppSeckillVO{" +
				"nextTime=" + nextTime +
				", selectedLot=" + selectedLot +
				", seckillSpuList=" + seckillSpuList +
				'}';
	}
}
