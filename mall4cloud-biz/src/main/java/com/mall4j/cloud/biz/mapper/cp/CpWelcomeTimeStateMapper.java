package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.CpWelcomeTimeState;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeTimeStateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人欢迎语 分时段欢迎语列表
 *
 * @author FrozenWatermelon
 * @date 2023-11-06 17:02:34
 */
public interface CpWelcomeTimeStateMapper {

	/**
	 * 获取个人欢迎语 分时段欢迎语列表列表
	 * @return 个人欢迎语 分时段欢迎语列表列表
	 */
	List<CpWelcomeTimeState> list();

	/**
	 * 根据个人欢迎语 分时段欢迎语列表id获取个人欢迎语 分时段欢迎语列表
	 *
	 * @param id 个人欢迎语 分时段欢迎语列表id
	 * @return 个人欢迎语 分时段欢迎语列表
	 */
	CpWelcomeTimeState getById(@Param("id") Long id);

	/**
	 * 保存个人欢迎语 分时段欢迎语列表
	 * @param cpWelcomeTimeState 个人欢迎语 分时段欢迎语列表
	 */
	void save(@Param("cpWelcomeTimeState") CpWelcomeTimeState cpWelcomeTimeState);

	/**
	 * 更新个人欢迎语 分时段欢迎语列表
	 * @param cpWelcomeTimeState 个人欢迎语 分时段欢迎语列表
	 */
	void update(@Param("cpWelcomeTimeState") CpWelcomeTimeState cpWelcomeTimeState);

	/**
	 * 根据个人欢迎语 分时段欢迎语列表id删除个人欢迎语 分时段欢迎语列表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<CpWelcomeTimeStateVO> listByWellId(@Param("wellId")Long wellId);

	void deleteByWelId(@Param("wellId")Long wellId);
}
