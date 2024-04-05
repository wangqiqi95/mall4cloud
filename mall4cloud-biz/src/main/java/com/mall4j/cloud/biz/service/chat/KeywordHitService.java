package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.chat.KeywordHitRecomdDTO;
import com.mall4j.cloud.biz.vo.chat.KeywordHitRecomdVO;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 会话关键词service
 */
public interface KeywordHitService {

	/**
	 * 分页获取会话关键词列表
	 * @param pageDTO 分页参数
	 * @param dto
	 * @return 命中关键词列表分页数据
	 */
	PageVO<KeywordHitVO> page(PageDTO pageDTO, KeywordHitDTO dto);

	List<KeywordHitVO> soldExcel(KeywordHitDTO dto);

	/**
	 * 导购推荐内容
	 * @param recomdDTO
	 * @return
	 */
	PageVO<KeywordHitRecomdVO> appRecomdPage(KeywordHitRecomdDTO recomdDTO);

	List<KeywordHitVO> getTop(KeywordHitDTO dto);


}
