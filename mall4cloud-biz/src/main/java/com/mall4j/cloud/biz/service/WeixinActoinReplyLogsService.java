package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.WeixinActoinReplyLogsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinActoinReplyLogs;

/**
 * 公众号事件推送回复日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:36
 */
public interface WeixinActoinReplyLogsService {

	/**
	 * 分页获取公众号事件推送回复日志列表
	 * @param pageDTO 分页参数
	 * @return 公众号事件推送回复日志列表分页数据
	 */
	PageVO<WeixinActoinReplyLogs> page(PageDTO pageDTO);

	/**
	 * 根据公众号事件推送回复日志id获取公众号事件推送回复日志
	 *
	 * @param id 公众号事件推送回复日志id
	 * @return 公众号事件推送回复日志
	 */
	WeixinActoinReplyLogs getById(Long id);

	/**
	 * 保存公众号事件推送回复日志
	 * @param weixinActoinReplyLogs 公众号事件推送回复日志
	 */
	void save(WeixinActoinReplyLogs weixinActoinReplyLogs);

	/**
	 * 更新公众号事件推送回复日志
	 * @param weixinActoinReplyLogs 公众号事件推送回复日志
	 */
	void update(WeixinActoinReplyLogs weixinActoinReplyLogs);

	/**
	 * 根据公众号事件推送回复日志id删除公众号事件推送回复日志
	 * @param id 公众号事件推送回复日志id
	 */
	void deleteById(Long id);

	void saveWeixinActoinReplyLogs(WeixinActoinReplyLogsVO weixinActoinReplyLogsVO);
}
