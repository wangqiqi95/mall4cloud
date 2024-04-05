package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreExportDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreItemDTO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemExportVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreVO;
import com.mall4j.cloud.biz.vo.WxQrCodeFileVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacleStore;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

/**
 * 微信触点门店二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
public interface WeixinQrcodeTentacleStoreService {

	/**
	 * 分页获取微信触点门店二维码表列表
	 * @param pageDTO 分页参数
	 * @return 微信触点门店二维码表列表分页数据
	 */
	PageVO<WeixinQrcodeTentacleStore> page(PageDTO pageDTO);

	PageVO<WeixinQrcodeTentacleStoreVO> storeCodePage(PageDTO pageDTO, String tentacleId);

	PageVO<WeixinQrcodeTentacleStoreItemVO> storeCodeItemPage(WeixinQrcodeTentacleStoreItemDTO weixinQrcodeTentacleStoreItemDTO);

	/**
	 * 微信触点门店详情列表导出
	 * @param param 微信触点门店详情列表导出参数
	 * @return
	 */
	String storeCodeItemExcel(WeixinQrcodeTentacleStoreItemDTO param);

	/**
	 * 微信触点门店列表批量导出时查询门店列表
	 * @param param 微信触点门店列表批量导出时查询门店列表接口需求参数
	 * @return
	 */
	PageVO<WeixinQrcodeTentacleStoreItemExportVO> qrcodeTentacleStorePage(WeixinQrcodeTentacleStoreExportDTO param);

	/**
	 * 微信触点门店列表批量导出
	 * @param param 微信触点门店列表批量导出入参
	 * @return
	 */
	String qrcodeTentacleStoreExcel(WeixinQrcodeTentacleStoreExportDTO param);

	/**
	 * 根据微信触点门店二维码表id获取微信触点门店二维码表
	 *
	 * @param id 微信触点门店二维码表id
	 * @return 微信触点门店二维码表
	 */
	WeixinQrcodeTentacleStore getById(Long id);

	/**
	 * 保存微信触点门店二维码表
	 * @param weixinQrcodeTentacleStore 微信触点门店二维码表
	 */
	void save(WeixinQrcodeTentacleStore weixinQrcodeTentacleStore);

	/**
	 * 更新微信触点门店二维码表
	 * @param weixinQrcodeTentacleStore 微信触点门店二维码表
	 */
	void update(WeixinQrcodeTentacleStore weixinQrcodeTentacleStore);

	/**
	 * 根据微信触点门店二维码表id删除微信触点门店二维码表
	 * @param id 微信触点门店二维码表id
	 */
	void deleteById(Long id);

//	void saveTentacleStore(List<WxQrCodeFileVO> files, WeixinQrcodeTentacle qrcodeTentacle, String storeIds);

	void saveTentacleStore(WeixinQrcodeTentacle qrcodeTentacle, String storeIds,Long downHisId);

	/**
	 * 新增触点列表详情记录
	 * @param tentacleStoreId    触点列表记录表ID
	 * @param code    微信Code
	 * @return
	 */
	ServerResponseEntity<String> saveQrcodeTentacleStoreItem(String tentacleStoreId, String code) throws WxErrorException;

	/**
	 * 获取微信触点二维码信息查缓存
	 * @param tentacleStoreId 微信门店触点二维码ID
	 * @return
	 */
	ServerResponseEntity<WeixinQrcodeTentacleStoreVO> getQrcodeTentacleStoreScene(String tentacleStoreId);

}
