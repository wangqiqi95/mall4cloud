package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.biz.vo.WeixinActoinLogsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinActoinLogs;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

import java.util.List;

/**
 * 公众号事件推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:16
 */
public interface WeixinActoinLogsService extends IService<WeixinActoinLogs> {

	/**
	 * 分页获取公众号事件推送日志列表
	 * @param pageDTO 分页参数
	 * @return 公众号事件推送日志列表分页数据
	 */
	PageVO<WeixinActoinLogs> page(PageDTO pageDTO);

	/**
	 * 根据公众号事件推送日志id获取公众号事件推送日志
	 *
	 * @param id 公众号事件推送日志id
	 * @return 公众号事件推送日志
	 */
	WeixinActoinLogs getById(Long id);

	/**
	 * 保存公众号事件推送日志
	 * @param weixinActoinLogs 公众号事件推送日志
	 */
	void saveTo(WeixinActoinLogs weixinActoinLogs);

	/**
	 * 更新公众号事件推送日志
	 * @param weixinActoinLogs 公众号事件推送日志
	 */
	void updateTo(WeixinActoinLogs weixinActoinLogs);

	/**
	 * 根据公众号事件推送日志id删除公众号事件推送日志
	 * @param id 公众号事件推送日志id
	 */
	void deleteById(Long id);

	void saveWeixinActoinLogs(WeixinActoinLogsVO weixinActoinLogsVO);

	void saveWeixinActoinLogs(WxMpXmlMessage xmlMessage,String appId);

	List<UserWeixinAccountFollowDataListVo> fansDataByAppId(UserWeixinccountFollowSelectDTO dto);
}
