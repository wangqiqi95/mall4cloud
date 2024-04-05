package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 欢迎语门店关联表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface StoreWelcomeConfigService {
	/**
	 * 保存欢迎语门店关联表
	 * @param shopWelcomeConfig 欢迎语门店关联表
	 */
	void save(ShopWelcomeConfig shopWelcomeConfig);

	/**
	 * 根据欢迎语id删除
	 * @param welId 欢迎语id
	 */
    void deleteByWelId( Long welId);

	/**
	 * 根据欢迎语id查询商店列表
	 * @param welId 欢迎语id
	 * @return List<ShopWelcomeConfig>
	 */
    List<ShopWelcomeConfig> listByWelId(Long welId);
}
