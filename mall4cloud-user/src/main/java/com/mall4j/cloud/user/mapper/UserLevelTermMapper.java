package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserLevelTerm;
import com.mall4j.cloud.user.vo.UserLevelTermVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public interface UserLevelTermMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<UserLevelTerm> list();

	/**
	 * 根据id获取
	 *
	 * @param levelTermId id
	 * @return 等级期数
	 */
	UserLevelTerm getByLevelTermId(@Param("levelTermId") Long levelTermId);

	/**
	 * 保存
	 * @param userLevelTerm 等级期数
	 */
	void save(@Param("userLevelTerm") UserLevelTerm userLevelTerm);

	/**
	 * 更新
	 * @param userLevelTerm 等级期数
	 */
	void update(@Param("userLevelTerm") UserLevelTerm userLevelTerm);

	/**
	 * 批量保存
	 * @param userLevelTerms 等级期数列表
	 */
	void saveBatch(@Param("userLevelTerms") List<UserLevelTerm> userLevelTerms);

	/**
	 * 批量更新
	 * @param userLevelTerms 等级期数列表
	 */
	void updateBatch(@Param("userLevelTerms") List<UserLevelTerm> userLevelTerms);

	/**
	 * 批量删除
	 * @param userLevelTermIds 等级期数id列表
	 */
	void deleteBatch(@Param("userLevelTermIds") List<Long> userLevelTermIds);

	/**
	 * 根据id删除
	 * @param levelTermId 等级期数id
	 */
	void deleteById(@Param("levelTermId") Long levelTermId);

	/**
	 *  根据用户等级id获取等级期数表
	 * @param userLevelId 户等级id
	 * @return 等级期数表
	 */
	List<UserLevelTerm> getListByUserLevelId(@Param("userLevelId")Long userLevelId);

	/**
	 * 根据用户等级id查价格表
	 * @param userLevelId 用户等级id
	 * @return 价格表
	 */
	List<UserLevelTermVO> getAmountAndTypeByUserLevelId(@Param("userLevelId")Long userLevelId);

	/**
	 *  根据用户等级id查月价格
	 * @param userLevelId 用户等级id
	 * @return 月价格
	 */
	Integer getMonthAmount(@Param("userLevelId") Long userLevelId);
}
