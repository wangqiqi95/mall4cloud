package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinMenuContentPutDTO;
import com.mall4j.cloud.biz.dto.WeixinMenuPutDTO;
import com.mall4j.cloud.biz.dto.WeixinMenuUpdateOrdersDto;
import com.mall4j.cloud.biz.vo.WeixinMenuContentVo;
import com.mall4j.cloud.biz.vo.WeixinMenuTreeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMenu;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

/**
 * 微信菜单表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
public interface WeixinMenuService {

	/**
	 * 分页获取微信菜单表列表
	 * @param pageDTO 分页参数
	 * @return 微信菜单表列表分页数据
	 */
	PageVO<WeixinMenu> page(PageDTO pageDTO);

	/**
	 * 菜单树形结构
	 * @param appId
	 * @return
	 */
	List<WeixinMenuTreeVO> menuTrees(String appId);

	/**
	 * 根据微信菜单表id获取微信菜单表
	 *
	 * @param id 微信菜单表id
	 * @return 微信菜单表
	 */
	WeixinMenu getById(String id);

	/**
	 * 保存微信菜单表
	 * @param weixinMenu 微信菜单表
	 */
	void save(WeixinMenu weixinMenu);

	/**
	 * 更新微信菜单表
	 * @param weixinMenu 微信菜单表
	 */
	void update(WeixinMenu weixinMenu);

	/**
	 * 根据微信菜单表id删除微信菜单表
	 * @param id 微信菜单表id
	 */
	void deleteById(String id);

	ServerResponseEntity<Void> updateMenuOrders(WeixinMenuUpdateOrdersDto ordersDto);

	ServerResponseEntity<Void> saveWeixinMenu(WeixinMenuPutDTO menuPutDTO);
	ServerResponseEntity<Void> updateWeixinMenu(WeixinMenuPutDTO menuPutDTO);

	ServerResponseEntity<Void> saveWeixinMenuContent(WeixinMenuContentPutDTO contentPutDTO);

	ServerResponseEntity<WeixinMenuContentVo> getWeixinMenuContent(String id);

	ServerResponseEntity<Void> doSyncMenu(String appId);
}
