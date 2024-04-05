package com.mall4j.cloud.seckill.service;

import com.mall4j.cloud.seckill.model.SeckillCategory;

import java.util.List;

/**
 * 秒杀分类信息
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
public interface SeckillCategoryService {

	/**
	 * 获取秒杀分类信息列表
	 * @return 秒杀分类信息列表数据
	 */
	List<SeckillCategory> list();

	/**
	 * 根据秒杀分类信息id获取秒杀分类信息
	 *
	 * @param categoryId 秒杀分类信息id
	 * @return 秒杀分类信息
	 */
	SeckillCategory getByCategoryId(Long categoryId);

	/**
	 * 保存秒杀分类信息
	 * @param seckillCategory 秒杀分类信息
	 */
	void save(SeckillCategory seckillCategory);

	/**
	 * 更新秒杀分类信息
	 * @param seckillCategory 秒杀分类信息
	 */
	void update(SeckillCategory seckillCategory);

	/**
	 * 根据秒杀分类信息id删除秒杀分类信息
	 * @param categoryId 秒杀分类信息id
	 */
	void deleteById(Long categoryId);

	/**
	 * 根据名称获取分类总数
	 * @return 分类总数
     * @param name
     * @param categoryId
     */
	Integer countByName(String name, Long categoryId);

	/**
	 * 批量更新秒杀分类排序
	 * @param seckillCategoryList 秒杀分类排序
	 */
    void updateBatch(List<SeckillCategory> seckillCategoryList);
}
