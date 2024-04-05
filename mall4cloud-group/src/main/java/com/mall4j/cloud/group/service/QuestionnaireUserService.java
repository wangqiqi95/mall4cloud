package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUser;

/**
 * 问卷会员名单
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireUserService {

	/**
	 * 分页获取问卷会员名单列表
	 * @param pageDTO 分页参数
	 * @return 问卷会员名单列表分页数据
	 */
	PageVO<QuestionnaireUser> page(PageDTO pageDTO);

	/**
	 * 根据问卷会员名单id获取问卷会员名单
	 *
	 * @param id 问卷会员名单id
	 * @return 问卷会员名单
	 */
	QuestionnaireUser getById(Long id);

	/**
	 * 保存问卷会员名单
	 * @param questionnaireUser 问卷会员名单
	 */
	void save(QuestionnaireUser questionnaireUser);

	/**
	 * 更新问卷会员名单
	 * @param questionnaireUser 问卷会员名单
	 */
	void update(QuestionnaireUser questionnaireUser);

	/**
	 * 根据问卷会员名单id删除问卷会员名单
	 * @param id 问卷会员名单id
	 */
	void deleteById(Long id);
}
