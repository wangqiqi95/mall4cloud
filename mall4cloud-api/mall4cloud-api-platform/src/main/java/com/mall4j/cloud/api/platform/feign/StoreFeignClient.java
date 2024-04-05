package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.dto.StoreAddDTO;
import com.mall4j.cloud.api.platform.dto.StoreSelectedParamFeignDTO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.StdStoreVO;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhangjie
 */
@FeignClient(value = "mall4cloud-platform", contextId = "store")
public interface StoreFeignClient {

    /**
     * 根据门店ID 获取门店信息
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/findByStoreId")
    StoreVO findByStoreId(@RequestParam("storeId") Long storeId);


    /**
     * 通过门店id集合查询门店列表
     *
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/listByStoreIdList")
    ServerResponseEntity<List<StoreVO>> listByStoreIdList(@RequestParam("storeIdList") List<Long> storeIdList);

    /**
     * 通过门店id集合查询门店列表
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/listBypByStoreIdList")
    ServerResponseEntity<List<StoreVO>> listBypByStoreIdList(@RequestBody List<Long> storeIdList);


    /**
     * 根据门店code 获取门店id
     *gmq 2022-04-21 改为post请求，解决大数量入参 报400
     * @param storeCodes
     * @return
     */
//    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/findByStoreCodes")
//    List<StoreCodeVO> findByStoreCodes(@RequestParam("storeCodes") List<String> storeCodes);
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/findByStoreCodes")
    List<StoreCodeVO> findByStoreCodes(@RequestBody List<String> storeCodes);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/findByStoreCode")
    StoreCodeVO findByStoreCode(@RequestParam("storeCode") String storeCode);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/findByStoreNames")
    List<StoreCodeVO> findListByStoreNames(@RequestBody List<String> storeNames);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/storeListByStatus")
    ServerResponseEntity<List<StoreCodeVO>> storeListByStatus(@RequestParam("status") Integer status);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/allStoreListIsShow")
    ServerResponseEntity<List<StoreCodeVO>> allStoreListIsShow();


    /**
     * 根据片区ID 获取门店信息
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/listByOrgId")
    ServerResponseEntity<List<StoreVO>> listByOrgId(@RequestParam("orgId") Long orgId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/sync")
    ServerResponseEntity sync(@RequestBody List<StoreAddDTO> storeAddDtoList);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/getAreaOrgByStore")
    ServerResponseEntity<Long> getAreaOrgByStore(@RequestParam("storeId") Long storeId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/listByOrgIdlistByOrg")
    ServerResponseEntity<String> getStoreCodeByStoreId(@RequestParam("storeId") Long storeId);

    //是否为虚拟门店
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/isInviteStore")
    ServerResponseEntity<Boolean> isInviteStore(@RequestParam("storeId") Long storeId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/getStoreByPushElStatus")
    ServerResponseEntity<List<StoreVO>> getStoreByPushElStatus(@RequestParam("pushElStatus") Integer pushElStatus);

    /**
     * 优惠券适用门店信息
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/page")
    ServerResponseEntity<PageVO<SelectedStoreVO>> storePage(@RequestBody InsiderStorePageDTO insiderStorePageDTO);


    /**
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/pushStdStores")
    ServerResponseEntity<List<StdStoreVO>> pushStdStores();

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/store/selected/page")
    ServerResponseEntity<PageVO<SelectedStoreVO>> selectedPage(@RequestBody StoreSelectedParamFeignDTO storeSelectedParamDTO);

}
