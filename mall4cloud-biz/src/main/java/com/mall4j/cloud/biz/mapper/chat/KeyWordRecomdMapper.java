package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.chat.KeyWordRecomd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会话关键词推荐回复内容
 *
 * @author gmq
 * @date 2024-01-05 15:19:52
 */
public interface KeyWordRecomdMapper extends BaseMapper<KeyWordRecomd> {

	/**
	 * 获取会话关键词推荐回复内容列表
	 * @return 会话关键词推荐回复内容列表
	 */
	List<KeyWordRecomd> list();


	/**
	 * 根据会话关键词推荐回复内容id删除会话关键词推荐回复内容
	 * @param KeyWordId
	 */
	void deleteByKeyWordId(@Param("KeyWordId") Long KeyWordId);
}
