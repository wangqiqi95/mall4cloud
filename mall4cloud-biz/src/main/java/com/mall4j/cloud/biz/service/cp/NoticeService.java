package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.NoticeDTO;
import com.mall4j.cloud.biz.model.chat.Keyword;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.model.cp.Notice;
import com.mall4j.cloud.biz.vo.cp.NoticeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 通知消息service
 */
public interface NoticeService {

	/**
	 * 分页获取通知消息列表
	 * @param pageDTO 分页参数
	 * @param dto 通知dto
	 * @return 通知消息列表分页数据
	 */
	PageVO<NoticeVO> page(PageDTO pageDTO, NoticeDTO dto);

	/**
	 * 更新通知
	 */
	void update(Notice notice);

	/**
	 * 根据通知表id删除通知
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 保存通知
	 */
	void saveNotice(Notice notice);

}
