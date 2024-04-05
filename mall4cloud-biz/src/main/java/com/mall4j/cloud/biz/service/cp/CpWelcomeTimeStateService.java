package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.CpWelcomeTimeStateDTO;
import com.mall4j.cloud.biz.model.cp.CpWelcomeTimeState;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeTimeStateVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 个人欢迎语 分时段欢迎语列表
 *
 * @author FrozenWatermelon
 * @date 2023-11-06 17:02:34
 */
public interface CpWelcomeTimeStateService {

	/**
	 * 分页获取个人欢迎语 分时段欢迎语列表列表
	 * @param pageDTO 分页参数
	 * @return 个人欢迎语 分时段欢迎语列表列表分页数据
	 */
	PageVO<CpWelcomeTimeState> page(PageDTO pageDTO);

	/**
	 * 根据个人欢迎语 分时段欢迎语列表id获取个人欢迎语 分时段欢迎语列表
	 *
	 * @param id 个人欢迎语 分时段欢迎语列表id
	 * @return 个人欢迎语 分时段欢迎语列表
	 */
	CpWelcomeTimeState getById(Long id);

	/**
	 * 保存个人欢迎语 分时段欢迎语列表
	 * @param cpWelcomeTimeState 个人欢迎语 分时段欢迎语列表
	 */
	void save(CpWelcomeTimeState cpWelcomeTimeState);

	/**
	 * 更新个人欢迎语 分时段欢迎语列表
	 * @param cpWelcomeTimeState 个人欢迎语 分时段欢迎语列表
	 */
	void update(CpWelcomeTimeState cpWelcomeTimeState);

	/**
	 * 根据个人欢迎语 分时段欢迎语列表id删除个人欢迎语 分时段欢迎语列表
	 * @param id 个人欢迎语 分时段欢迎语列表id
	 */
	void deleteById(Long id);

	List<CpWelcomeTimeStateVO> listByWellId(Long id);

    void deleteByWelId(Long id);

	/**
	 * 校验接待欢迎语分时段冲突
	 * @param filterAttachMents
	 */
	void checkAttachMentTime(List<CpWelcomeTimeStateDTO>  filterAttachMents);
}
