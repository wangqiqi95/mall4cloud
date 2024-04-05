package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinMenu;
import com.mall4j.cloud.biz.vo.WeixinMenuTreeChildVO;
import com.mall4j.cloud.biz.vo.WeixinMenuTreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信菜单表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
public interface WeixinMenuMapper {

	/**
	 * 获取微信菜单表列表
	 * @return 微信菜单表列表
	 */
	List<WeixinMenu> list();

	/**
	 * 根据微信菜单表id获取微信菜单表
	 *
	 * @param id 微信菜单表id
	 * @return 微信菜单表
	 */
	WeixinMenu getById(@Param("id") String id);

	/**
	 * 保存微信菜单表
	 * @param weixinMenu 微信菜单表
	 */
	void save(@Param("weixinMenu") WeixinMenu weixinMenu);

	/**
	 * 更新微信菜单表
	 * @param weixinMenu 微信菜单表
	 */
	void update(@Param("weixinMenu") WeixinMenu weixinMenu);

	/**
	 * 根据微信菜单表id删除微信菜单表
	 * @param id
	 */
	void deleteById(@Param("id") String id);

	void deleteChildByFatherId(@Param("fatherId") String fatherId);

	WeixinMenu queryByOrders(@Param("orders") Integer orders,@Param("appId") String appId);

	WeixinMenu queryByFatherId(@Param("fatherId") String fatherId,@Param("appId") String appId);

	List<WeixinMenuTreeVO> quryMenuTreeList(@Param("appId") String appId);

	List<WeixinMenuTreeChildVO> quryMenuTreeChildList(@Param("fatherId") String fatherId, @Param("appId") String appId);

	Integer queryMaxOrders(@Param("appId") String appId);

	Integer queryMaxOrdersByFatherId(@Param("fatherId") String fatherId);
}
