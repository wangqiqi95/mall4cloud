package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 欢迎语门店关联表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface ShopWelcomeConfigMapper {
	/**
	 * 保存欢迎语门店关联表
	 * @param shopWelcomeConfig 欢迎语门店关联表
	 */
	void save(@Param("shopWelcomeConfig") ShopWelcomeConfig shopWelcomeConfig);

	/**
	 * 根据欢迎语id删除
	 * @param welId 欢迎语id
	 */
    void deleteByWelId(@Param("welId")Long welId);

	/**
	 * 据欢迎语id查询
	 * @param welId 欢迎语id
	 * @return  List<ShopWelcomeConfig>
	 */
    List<ShopWelcomeConfig> listByWelId(@Param("welId")Long welId);
}
