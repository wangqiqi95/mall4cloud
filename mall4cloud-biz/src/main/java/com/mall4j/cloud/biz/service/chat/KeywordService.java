package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.biz.dto.chat.KeywordDTO;
import com.mall4j.cloud.biz.vo.chat.KeywordVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 命中关键词service
 */
public interface KeywordService {

	/**
	 * 分页获取命中关键词列表
	 * @param pageDTO 分页参数
	 * @param keyword 文件名
	 * @return 会话关键词列表分页数据
	 */
	PageVO<KeywordVO> page(PageDTO pageDTO, KeywordDTO keyword);

	/**
	 * 更新会话关键词
	 * @param keyword 会话关键词表
	 */
	void update(KeywordDTO keyword);

	/**
	 * 根据会话关键词表表id删除会话关键词表
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 保存会话关键词
	 * @param keyword 会话关键词表
	 */
	void saveKeyword(KeywordDTO keyword);

	KeywordVO getDetail(Long id);
}
