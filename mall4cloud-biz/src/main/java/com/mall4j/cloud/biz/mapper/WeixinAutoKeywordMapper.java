package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinAutoKeyword;
import com.mall4j.cloud.biz.vo.WeixinAutoKeywordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信关键字表
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
public interface WeixinAutoKeywordMapper {

	/**
	 * 获取微信关键字表列表
	 * @return 微信关键字表列表
	 */
	List<WeixinAutoKeyword> list();

	List<WeixinAutoKeywordVO> getList(@Param("appId") String appId,
									  @Param("name") String name,
									  @Param("keyword") String keyword,
									  @Param("isWork") Integer isWork);

	/**
	 * 根据微信关键字表id获取微信关键字表
	 *
	 * @param id 微信关键字表id
	 * @return 微信关键字表
	 */
	WeixinAutoKeyword getById(@Param("id") String id);

	/**
	 * 保存微信关键字表
	 * @param weixinAutoKeyword 微信关键字表
	 */
	void save(@Param("weixinAutoKeyword") WeixinAutoKeyword weixinAutoKeyword);

	/**
	 * 更新微信关键字表
	 * @param weixinAutoKeyword 微信关键字表
	 */
	void update(@Param("weixinAutoKeyword") WeixinAutoKeyword weixinAutoKeyword);

	/**
	 * 根据微信关键字表id删除微信关键字表
	 * @param id
	 */
	void deleteById(@Param("updateBy") String updateBy,@Param("id") String id);

	WeixinAutoKeywordVO getByKeyword(@Param("appId") String appId,
									 @Param("keyword") String keyword);
}
