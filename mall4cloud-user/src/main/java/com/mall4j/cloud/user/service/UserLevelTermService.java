package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserLevelTermDTO;
import com.mall4j.cloud.user.model.UserLevelTerm;
import com.mall4j.cloud.user.vo.UserLevelTermVO;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public interface UserLevelTermService {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<UserLevelTerm> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param levelTermId id
	 * @return 等级期数
	 */
	UserLevelTerm getByLevelTermId(Long levelTermId);

	/**
	 * 批量更新会员等级-期数关联信息
	 * @param userLevelId 等级id
	 * @param userLevelTermDTO 等级期数
	 */
	void updateBatch(Long userLevelId,List<UserLevelTermDTO> userLevelTermDTO);

	/**
	 * 批量保存会员等级-期数关联信息
	 * @param userLevelId 等级id
	 * @param userLevelTerms 等级期数列表
	 */
	void saveBatch(Long userLevelId,List<UserLevelTerm> userLevelTerms);

	/**
	 * 保存
	 * @param userLevelTerm 等级期数
	 */
	void save(UserLevelTerm userLevelTerm);

	/**
	 * 更新
	 * @param userLevelTerm 等级期数
	 */
	void update(UserLevelTerm userLevelTerm);

	/**
	 * 根据id删除
	 * @param levelTermId id
	 */
	void deleteById(Long levelTermId);

	/**
	 * 根据用户等级id批量删除
	 * @param userLevelId 用户等级id
	 */
	void deleteBatch(Long userLevelId);

	/**
	 * 根据等级id获取等级期数表
	 * @param userLevelId 等级id
	 * @return 等级期数表
	 */
	List<UserLevelTerm> getListByUserLevelId(Long userLevelId);

	/**
	 * 根据用户等级id查价格表
	 * @param userLevelId 用户等级id
	 * @return 价格表
	 */
	List<UserLevelTermVO> getAmountAndTypeByUserLevelId(Long userLevelId);
}
