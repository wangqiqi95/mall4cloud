package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireGift;

/**
 * 问卷奖品清单 实物奖品维护
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireGiftService {

	/**
	 * 分页获取问卷奖品清单 实物奖品维护列表
	 * @param pageDTO 分页参数
	 * @return 问卷奖品清单 实物奖品维护列表分页数据
	 */
	PageVO<QuestionnaireGift> page(PageDTO pageDTO);

	/**
	 * 根据问卷奖品清单 实物奖品维护id获取问卷奖品清单 实物奖品维护
	 *
	 * @param id 问卷奖品清单 实物奖品维护id
	 * @return 问卷奖品清单 实物奖品维护
	 */
	QuestionnaireGift getById(Long id);

	/**
	 * 保存问卷奖品清单 实物奖品维护
	 * @param questionnaireGift 问卷奖品清单 实物奖品维护
	 */
	void save(QuestionnaireGift questionnaireGift);

	/**
	 * 更新问卷奖品清单 实物奖品维护
	 * @param questionnaireGift 问卷奖品清单 实物奖品维护
	 */
	void update(QuestionnaireGift questionnaireGift);

	/**
	 * 根据问卷奖品清单 实物奖品维护id删除问卷奖品清单 实物奖品维护
	 * @param id 问卷奖品清单 实物奖品维护id
	 */
	void deleteById(Long id);
}
