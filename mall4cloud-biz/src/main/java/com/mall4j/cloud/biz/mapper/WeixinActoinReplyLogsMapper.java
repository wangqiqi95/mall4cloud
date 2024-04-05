package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinActoinReplyLogs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公众号事件推送回复日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:36
 */
public interface WeixinActoinReplyLogsMapper {

	/**
	 * 获取公众号事件推送回复日志列表
	 * @return 公众号事件推送回复日志列表
	 */
	List<WeixinActoinReplyLogs> list();

	/**
	 * 根据公众号事件推送回复日志id获取公众号事件推送回复日志
	 *
	 * @param id 公众号事件推送回复日志id
	 * @return 公众号事件推送回复日志
	 */
	WeixinActoinReplyLogs getById(@Param("id") Long id);

	/**
	 * 保存公众号事件推送回复日志
	 * @param weixinActoinReplyLogs 公众号事件推送回复日志
	 */
	void save(@Param("weixinActoinReplyLogs") WeixinActoinReplyLogs weixinActoinReplyLogs);

	/**
	 * 更新公众号事件推送回复日志
	 * @param weixinActoinReplyLogs 公众号事件推送回复日志
	 */
	void update(@Param("weixinActoinReplyLogs") WeixinActoinReplyLogs weixinActoinReplyLogs);

	/**
	 * 根据公众号事件推送回复日志id删除公众号事件推送回复日志
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
