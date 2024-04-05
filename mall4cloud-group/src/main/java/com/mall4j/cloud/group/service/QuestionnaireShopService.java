package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireShop;

/**
 * 问卷适用门店
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireShopService {

	/**
	 * 分页获取问卷适用门店列表
	 * @param pageDTO 分页参数
	 * @return 问卷适用门店列表分页数据
	 */
	PageVO<QuestionnaireShop> page(PageDTO pageDTO);

	/**
	 * 根据问卷适用门店id获取问卷适用门店
	 *
	 * @param id 问卷适用门店id
	 * @return 问卷适用门店
	 */
	QuestionnaireShop getById(Long id);

	/**
	 * 保存问卷适用门店
	 * @param questionnaireShop 问卷适用门店
	 */
	void save(QuestionnaireShop questionnaireShop);

	/**
	 * 更新问卷适用门店
	 * @param questionnaireShop 问卷适用门店
	 */
	void update(QuestionnaireShop questionnaireShop);

	/**
	 * 根据问卷适用门店id删除问卷适用门店
	 * @param id 问卷适用门店id
	 */
	void deleteById(Long id);
}
