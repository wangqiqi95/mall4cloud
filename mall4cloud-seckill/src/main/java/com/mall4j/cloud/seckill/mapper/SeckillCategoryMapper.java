package com.mall4j.cloud.seckill.mapper;

import com.mall4j.cloud.seckill.model.SeckillCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀分类信息
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
public interface SeckillCategoryMapper {

	/**
	 * 获取秒杀分类信息列表
	 * @return 秒杀分类信息列表
	 */
	List<SeckillCategory> list();

	/**
	 * 根据秒杀分类信息id获取秒杀分类信息
	 *
	 * @param categoryId 秒杀分类信息id
	 * @return 秒杀分类信息
	 */
	SeckillCategory getByCategoryId(@Param("categoryId") Long categoryId);

	/**
	 * 保存秒杀分类信息
	 * @param seckillCategory 秒杀分类信息
	 */
	void save(@Param("seckillCategory") SeckillCategory seckillCategory);

	/**
	 * 更新秒杀分类信息
	 * @param seckillCategory 秒杀分类信息
	 */
	void update(@Param("seckillCategory") SeckillCategory seckillCategory);

	/**
	 * 根据秒杀分类信息id删除秒杀分类信息
	 * @param categoryId
	 */
	void deleteById(@Param("categoryId") Long categoryId);

	/**
	 * 根据名称获取分类总数
	 * @return 分类总数
     * @param name
     * @param categoryId
     */
    int countByName(@Param("name") String name, @Param("categoryId") Long categoryId);

	/**
	 * 批量更新秒杀分类
	 * @param seckillCategoryList 秒杀分类
	 */
	void updateBatch(@Param("seckillCategoryList") List<SeckillCategory> seckillCategoryList);
}
