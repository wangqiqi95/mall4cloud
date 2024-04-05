package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacleStore;
import com.mall4j.cloud.biz.model.WeixinShortlinkItem;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemExportVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信触点门店二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
public interface WeixinQrcodeTentacleStoreMapper extends BaseMapper<WeixinQrcodeTentacleStore> {

	/**
	 * 获取微信触点门店二维码表列表
	 * @return 微信触点门店二维码表列表
	 */
	List<WeixinQrcodeTentacleStore> list();

	List<WeixinQrcodeTentacleStoreVO> getList(@Param("tentacleId") String tentacleId);

	List<WeixinQrcodeTentacleStoreItemExportVO> getStoreListByTentacleIds(@Param("tentacleStoreIds") List<String> tentacleStoreIds);

	/**
	 * 根据微信触点门店二维码表id获取微信触点门店二维码表
	 *
	 * @param id 微信触点门店二维码表id
	 * @return 微信触点门店二维码表
	 */
	WeixinQrcodeTentacleStore getById(@Param("id") Long id);

	/**
	 * 保存微信触点门店二维码表
	 * @param weixinQrcodeTentacleStore 微信触点门店二维码表
	 */
	void save(@Param("weixinQrcodeTentacleStore") WeixinQrcodeTentacleStore weixinQrcodeTentacleStore);

	void saveBatch(@Param("tentacleStores") List<WeixinQrcodeTentacleStore> tentacleStores);

	/**
	 * 更新微信触点门店二维码表
	 * @param weixinQrcodeTentacleStore 微信触点门店二维码表
	 */
	void update(@Param("weixinQrcodeTentacleStore") WeixinQrcodeTentacleStore weixinQrcodeTentacleStore);

	/**
	 * 根据微信触点门店二维码表id删除微信触点门店二维码表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	int selectQrcodeTentacleStoreItemOfUserId(@Param("unionId") String unionId, @Param("tentacleStoreId") String tentacleStoreId);

	void updateByTentacleStoreId(@Param("tentacleStoreId") String tentacleStoreId, @Param("checkNum") Integer checkNum);

}
