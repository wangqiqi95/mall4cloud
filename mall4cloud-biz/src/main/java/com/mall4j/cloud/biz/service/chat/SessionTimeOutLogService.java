package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.biz.dto.chat.TimeOutLogDTO;
import com.mall4j.cloud.biz.vo.chat.TimeOutLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 超时分析统计service
 */
public interface SessionTimeOutLogService {

	/**
	 * 分页获取超时回复列表
	 * @param pageDTO 分页参数
	 * @param dto
	 * @return 命中关键词列表分页数据
	 */
	PageVO<TimeOutLogVO> page(PageDTO pageDTO, TimeOutLogDTO dto);

	List<TimeOutLogVO> soldExcel( TimeOutLogDTO dto);

	List<TimeOutLogVO> outLogChart(TimeOutLogDTO dto);

	TimeOutLogVO outLogCount(TimeOutLogDTO dto);

	/**
	 * 超时发送消息提醒
	 */
	void sendMessage();

	TimeOutLogVO getByTimeOutId(String timeOutId);
}
