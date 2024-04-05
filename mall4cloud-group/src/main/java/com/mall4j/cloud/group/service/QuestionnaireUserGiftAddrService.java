package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftAddr;

/**
 * 用户奖品配送地址
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:11:00
 */
public interface QuestionnaireUserGiftAddrService {

	/**
	 * 分页获取用户奖品配送地址列表
	 * @param pageDTO 分页参数
	 * @return 用户奖品配送地址列表分页数据
	 */
	PageVO<QuestionnaireUserGiftAddr> page(PageDTO pageDTO);

	/**
	 * 根据用户奖品配送地址id获取用户奖品配送地址
	 *
	 * @param id 用户奖品配送地址id
	 * @return 用户奖品配送地址
	 */
	QuestionnaireUserGiftAddr getById(Long id);

	/**
	 * 保存用户奖品配送地址
	 * @param questionnaireUserGiftAddr 用户奖品配送地址
	 */
	void save(QuestionnaireUserGiftAddr questionnaireUserGiftAddr);

	/**
	 * 更新用户奖品配送地址
	 * @param questionnaireUserGiftAddr 用户奖品配送地址
	 */
	void update(QuestionnaireUserGiftAddr questionnaireUserGiftAddr);

	/**
	 * 根据用户奖品配送地址id删除用户奖品配送地址
	 * @param id 用户奖品配送地址id
	 */
	void deleteById(Long id);
}
