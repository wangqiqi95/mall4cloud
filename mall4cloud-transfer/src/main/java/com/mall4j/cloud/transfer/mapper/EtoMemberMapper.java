package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.EtoMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-26 17:08:58
 */
public interface EtoMemberMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<EtoMember> list();

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	EtoMember getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param etoMember 
	 */
	void save(@Param("etoMember") EtoMember etoMember);

	/**
	 * 更新
	 * @param etoMember 
	 */
	void update(@Param("etoMember") EtoMember etoMember);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
