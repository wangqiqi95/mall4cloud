package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.biz.model.WeixinActoinLogs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公众号事件推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:16
 */
public interface WeixinActoinLogsMapper extends BaseMapper<WeixinActoinLogs> {

	/**
	 * 获取公众号事件推送日志列表
	 * @return 公众号事件推送日志列表
	 */
	List<WeixinActoinLogs> list();

	/**
	 * 根据公众号事件推送日志id获取公众号事件推送日志
	 *
	 * @param id 公众号事件推送日志id
	 * @return 公众号事件推送日志
	 */
	WeixinActoinLogs getById(@Param("id") Long id);

	/**
	 * 保存公众号事件推送日志
	 * @param weixinActoinLogs 公众号事件推送日志
	 */
//	void save(@Param("weixinActoinLogs") WeixinActoinLogs weixinActoinLogs);

	/**
	 * 更新公众号事件推送日志
	 * @param weixinActoinLogs 公众号事件推送日志
	 */
//	void update(@Param("weixinActoinLogs") WeixinActoinLogs weixinActoinLogs);

	/**
	 * 根据公众号事件推送日志id删除公众号事件推送日志
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<UserWeixinAccountFollowDataListVo> fansDataByAppId(@Param("dto") UserWeixinccountFollowSelectDTO dto);
}
