package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.biz.dto.chat.SessionAgreeDTO;
import com.mall4j.cloud.biz.vo.chat.SessionAgreeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 会话同意service
 */
public interface SessionAgreeService {

	/**
	 * 分页获取会话同意列表
	 * @param pageDTO 分页参数
	 * @param dto
	 * @return 命中关键词列表分页数据
	 */
	PageVO<SessionAgreeVO> page(PageDTO pageDTO, SessionAgreeDTO dto);

	List<SessionAgreeVO> soldExcel(SessionAgreeDTO dto);

	List<SessionAgreeVO> agreeProportion(SessionAgreeDTO dto);

	List<SessionAgreeVO> agreeMonCount(SessionAgreeDTO dto);

	SessionAgreeVO agreeSum(SessionAgreeDTO dto);
}
