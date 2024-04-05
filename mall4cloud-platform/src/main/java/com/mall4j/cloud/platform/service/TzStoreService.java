package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.SoldTzStoreVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.StorePageQueryDTO;
import com.mall4j.cloud.platform.dto.StoreQueryParamDTO;
import com.mall4j.cloud.platform.dto.StoreSelectedParamDTO;
import com.mall4j.cloud.platform.dto.TzStoreDTO;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.vo.CloseStoreStaffVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
public interface TzStoreService {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<TzStore> page(PageDTO pageDTO, StorePageQueryDTO request);

	/**
	 * 根据id获取
	 *
	 * @param storeId id
	 * @return 
	 */
	TzStore getByStoreId(Long storeId);

	void removeCacheByStoreId(Long storeId);

	/**
	 * 保存
	 * @param tzStore 
	 */
	void save(TzStore tzStore);

	/**
	 * 更新
	 */
	CloseStoreStaffVO update(TzStoreDTO tzStoreDTO);

	/**
	 * 变更会员服务门店
	 *
	 * @param before 变更前门店
	 * @param userList 会员
	 * @param nick 操作人nick
	 * @param afterStore 变更后门店
	 */
	void executeChange(TzStore before, List<UserApiVO> userList, String nick, TzStore afterStore, Integer type, Integer source);

	/**
	 * 根据id删除
	 * @param storeId id
	 */
	void deleteById(Long storeId);

	PageVO<TzStore> page(PageDTO pageDTO, StoreQueryParamDTO storeQueryParamDTO);

	List<TzStore> storeListByCodes(StoreQueryParamDTO storeQueryParamDTO);

	PageVO<SelectedStoreVO> selectedPage(PageDTO pageDTO, StoreSelectedParamDTO storeSelectedParamDTO);

    void save(TzStoreDTO tzStoreDTO);

	List<TzStore> listByParam(StoreQueryParamDTO storeQueryParamDTO);

    List<TzStore> listByStoreCode(List<String> storeCodeList);

	TzStore getTzStoreByStoreCode(String storeCode);

    List<TzStore> listByOrgId(Long orgId);

	List<TzStore> listByStoreIdList(List<Long> storeIdList);

	TzStore getByOrgId(Long orgId);

	void saveBatch(List<TzStore> addStoreList);

	Long getAreaOrgByStore(Long storeId);

    String getStoreCodeByStoreId(Long storeId);

	PageVO<SelectedStoreVO> storePage(InsiderStorePageDTO insiderStorePageDTO);

	TzStore getByStoreCode(String storeName,String storeCode);

	List<SoldTzStoreVO> soldStore(StoreQueryParamDTO soldStoreDTO, HttpServletResponse response);

	PageVO<TzStore> storePlatPage(Long orgId, String keyword, PageDTO pageDTO);

    List<TzStore> listByOrgIds(String orgIds);

	void updateByStoreCode();

	void updateBatch(List<TzStore> updateStoreList);

	/**
	 * 批量修改门店是否C端展示
	 * @param stores 门店集合
	 * @param isShow c端是否展示 0-不展示 ，1 -展示
	 */
	void updateShows(List<Long> stores,Integer isShow);

	void listByStoreLatLng();

	List<TzStore> getTzStoreByStoreNames(List<String> storeNames);
}
