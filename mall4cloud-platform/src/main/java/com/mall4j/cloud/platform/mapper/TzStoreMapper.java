package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.platform.dto.StorePageQueryDTO;
import com.mall4j.cloud.platform.dto.StoreQueryParamDTO;
import com.mall4j.cloud.platform.dto.StoreSelectedParamDTO;
import com.mall4j.cloud.platform.model.TzStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
public interface TzStoreMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<TzStore> list(@Param("et") StorePageQueryDTO request);

	/**
	 * 根据id获取
	 *
	 * @param storeId id
	 * @return 
	 */
	TzStore getByStoreId(@Param("storeId") Long storeId);

	TzStore getByStoreCode(@Param("storeName") String storeName,@Param("storeCode") String storeCode);

	/**
	 * 保存
	 * @param tzStore 
	 */
	void save(@Param("tzStore") TzStore tzStore);

	/**
	 * 更新
	 * @param tzStore 
	 */
	void update(@Param("tzStore") TzStore tzStore);

	/**
	 * 根据id删除
	 * @param storeId
	 */
	void deleteById(@Param("storeId") Long storeId);

	void logicDeleteById(@Param("orgId") Long orgId);

	List<TzStore>  listByParam(@Param("storeQueryParamDTO") StoreQueryParamDTO storeQueryParamDTO);

	List<TzStore>  soldListByParam(@Param("storeQueryParamDTO") StoreQueryParamDTO storeQueryParamDTO);

	List<SelectedStoreVO> selectedList(@Param("storeSelectedParamDTO") StoreSelectedParamDTO storeSelectedParamDTO);

    TzStore getByOrgId(@Param("orgId") Long orgId);

	List<TzStore> listByStoreCode(@Param("storeCodeList") List<String> storeCodeList);

	TzStore getTzStoreByStoreCode(@Param("storeCode")String storeCode);

	List<TzStore> getTzStoreByStoreNames(@Param("storeNames")List<String> storeName);

	void saveBatch(@Param("addStoreList") List<TzStore> addStoreList);

	void updateShows(@Param("stores") List<TzStore> stores);

    String getStoreCodeByStoreId(@Param("storeId") Long storeId);

	List<SelectedStoreVO> listByInsider(@Param("insiderDTO") InsiderStorePageDTO insiderStorePageDTO);

	List<TzStore> listByOrgIdAndKeyWord(@Param("orgId") Long orgId, @Param("keyword") String keyword, @Param("path") String path);

    void updateBatch(@Param("storeList") List<TzStore> storeList);

	List<TzStore> listByStoreLatLng();
}
