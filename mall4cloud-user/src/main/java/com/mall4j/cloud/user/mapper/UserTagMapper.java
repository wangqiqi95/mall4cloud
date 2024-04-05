package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.user.dto.UserTagDTO;
import com.mall4j.cloud.user.model.UserTag;
import com.mall4j.cloud.user.vo.UserTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客户标签
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserTagMapper {

	/**
	 * 获取客户标签列表
	 * @param  userTag 条件参数
	 * @return 客户标签列表
	 */
	List<UserTagVO> list(@Param("userTag") UserTag userTag);

	/**
	 * 根据客户标签id获取客户标签
	 *
	 * @param userTagId 客户标签id
	 * @return 客户标签
	 */
	UserTag getByUserTagId(@Param("userTagId") Long userTagId);

	/**
	 * 保存客户标签
	 * @param userTag 客户标签
	 */
	Long save(@Param("userTag") UserTag userTag);

	/**
	 * 更新客户标签
	 * @param userTag 客户标签
	 */
	void update(@Param("userTag") UserTag userTag);

	/**
	 * 根据客户标签id删除客户标签
	 * @param userTagId 客户标签id
	 */
	void deleteById(@Param("userTagId") Long userTagId);

	/**
	 * 统计已存在的数量
	 * @param userTag 条件参数
	 * @return 数量
	 */
    int count(@Param("userTag") UserTag userTag);

	/**
	 * 批量修改标签人数
	 * @param tagUser 标签人数map
	 * @return 修改行数
	 */
	int updateBatchTagUserNum(@Param("tagUser") Map<Long, Integer> tagUser);

	/**
	 * 通过标签id集合获取标签集合
	 * @param tagIds 标签id
	 * @return 标签列表
	 */
	List<UserTagApiVO> getByUserTagIds(@Param("tagIds") List<Long> tagIds);

	List<UserTagApiVO> getStaffUserTagList(@Param("tagName") String tagName, @Param("tagIds") List<Long> tagIds);

    List<UserTagApiVO> listUserTagByType(@Param("tagType")Integer tagType);
}
