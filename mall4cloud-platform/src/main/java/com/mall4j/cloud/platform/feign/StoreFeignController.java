package com.mall4j.cloud.platform.feign;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.dto.StoreAddDTO;
import com.mall4j.cloud.api.platform.dto.StoreSelectedParamFeignDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.StdStoreVO;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.cache.constant.PlatformCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.dto.StoreQueryParamDTO;
import com.mall4j.cloud.platform.dto.StoreSelectedParamDTO;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.service.TzStoreService;
import com.mall4j.cloud.platform.utils.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class StoreFeignController implements StoreFeignClient {

    @Autowired
    private TzStoreService storeService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public StoreVO findByStoreId(Long storeId) {
        log.info("---findByStoreId---" + storeId);
        StoreVO storeVO = new StoreVO();
        TzStore byStoreId = storeService.getByStoreId(storeId);
        if (byStoreId != null) {
            storeVO.setStoreId(byStoreId.getStoreId());
            storeVO.setName(byStoreId.getStationName());
            storeVO.setStoreCode(byStoreId.getStoreCode());
            storeVO.setStatus(byStoreId.getStatus());
            storeVO.setPushElStatus(byStoreId.getPushElStatus());
            storeVO.setStoreInviteType(Objects.nonNull(byStoreId.getStoreInviteType())?byStoreId.getStoreInviteType():0);
            //如果为虚拟门店，查询官店的code一起返回
            if(Objects.nonNull(byStoreId.getStoreInviteType()) && byStoreId.getStoreInviteType()==1){
                TzStore mainStore = storeService.getByStoreId(Constant.MAIN_SHOP);
                storeVO.setMainStoreCode(mainStore==null?"":mainStore.getStoreCode());
            }
        }
        return storeVO;
    }

    @Override
    public ServerResponseEntity<List<StoreVO>> listByStoreIdList(List<Long> storeIdList) {
        List<TzStore> tzStoreList = storeService.listByStoreIdList(storeIdList);
        if (CollectionUtils.isEmpty(tzStoreList)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StoreVO> storeList = new ArrayList<>();
        tzStoreList.forEach(tzStore -> {
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId(tzStore.getStoreId());
            storeVO.setName(tzStore.getStationName());
            storeVO.setStoreCode(tzStore.getStoreCode());
            storeVO.setPushElStatus(tzStore.getPushElStatus());
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    @Override
    public ServerResponseEntity<List<StoreVO>> listBypByStoreIdList(List<Long> storeIdList) {
        List<TzStore> tzStoreList = storeService.listByStoreIdList(storeIdList);
        if (CollectionUtils.isEmpty(tzStoreList)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StoreVO> storeList = new ArrayList<>();
        tzStoreList.forEach(tzStore -> {
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId(tzStore.getStoreId());
            storeVO.setName(tzStore.getStationName());
            storeVO.setStoreCode(tzStore.getStoreCode());
            storeVO.setPushElStatus(tzStore.getPushElStatus());
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    /**
     * 根据门店code 获取门店信息
     *
     * @param storeCodes@return
     */
    @Override
    public List<StoreCodeVO> findByStoreCodes(List<String> storeCodes) {
        if (CollectionUtil.isNotEmpty(storeCodes)) {
            List<StoreCodeVO> storeCodeVOS = new ArrayList<>(storeCodes.size());
            int size = storeCodes.size();
            int inSize = 1000;
            if (size > inSize) {
                int pages = size % inSize == 0 ? size / inSize : size / inSize + 1;
                for (int i = 0; i < pages; i++) {
                    int start = i * inSize;
                    int end = (i + 1) * inSize;
                    end = end > size ? size : end;
                    List<String> strings = storeCodes.subList(start, end);
                    List<TzStore> tzStores = storeService.listByStoreCode(new ArrayList<>(strings));
                    if (!CollectionUtils.isEmpty(tzStores)) {
                        for (TzStore tzStore : tzStores) {
                            storeCodeVOS.add(new StoreCodeVO(tzStore.getStoreId(), tzStore.getStoreCode()));
                        }
                    }
                }
            } else {
                List<TzStore> tzStores = storeService.listByStoreCode(storeCodes);
                if (!CollectionUtils.isEmpty(tzStores)) {
                    for (TzStore tzStore : tzStores) {
                        storeCodeVOS.add(new StoreCodeVO(tzStore.getStoreId(), tzStore.getStoreCode()));
                    }
                }
            }
            return storeCodeVOS;
        }
        return null;
    }
    @Override
    public StoreCodeVO findByStoreCode(String storeCode) {
        TzStore tzStore = storeService.getTzStoreByStoreCode(storeCode);
        if(Objects.nonNull(tzStore)){
            StoreCodeVO storeCodeVO = new StoreCodeVO();
            storeCodeVO.setStoreId(tzStore.getStoreId());
            storeCodeVO.setStoreCode(tzStore.getStoreCode());
            storeCodeVO.setStationName(tzStore.getStationName());
            return storeCodeVO;

        }
        return null;
    }

    @Override
    public List<StoreCodeVO> findListByStoreNames(List<String> storeNames) {
        List<TzStore> tzStoreByStoreNames = storeService.getTzStoreByStoreNames(storeNames);
        return mapperFacade.mapAsList(tzStoreByStoreNames,StoreCodeVO.class);
    }

    @Override
    public ServerResponseEntity<List<StoreCodeVO>> storeListByStatus(Integer status) {
        StoreQueryParamDTO storeQueryParamDTO=new StoreQueryParamDTO();
        storeQueryParamDTO.setStoreStatus(1);
        List<TzStore> tzStores=storeService.listByParam(storeQueryParamDTO);
        if (CollectionUtils.isEmpty(tzStores)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StoreCodeVO> storeList = new ArrayList<>();
        tzStores.forEach(tzStore -> {
            StoreCodeVO storeVO = new StoreCodeVO();
            storeVO.setStoreCode(tzStore.getStoreCode());
            storeVO.setStationName(tzStore.getStationName());
            storeVO.setStoreId(tzStore.getStoreId());
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    @Override
    public ServerResponseEntity<List<StoreCodeVO>> allStoreListIsShow() {
        StoreQueryParamDTO storeQueryParamDTO=new StoreQueryParamDTO();
        storeQueryParamDTO.setStoreStatus(1);
        storeQueryParamDTO.setIsShow(1);
        List<TzStore> tzStores=storeService.listByParam(storeQueryParamDTO);
        if (CollectionUtils.isEmpty(tzStores)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StoreCodeVO> storeList = new ArrayList<>();
        tzStores.forEach(tzStore -> {
            StoreCodeVO storeVO = new StoreCodeVO();
            storeVO.setStoreCode(tzStore.getStoreCode());
            storeVO.setStationName(tzStore.getStationName());
            storeVO.setStoreId(tzStore.getStoreId());
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    @Override
    public ServerResponseEntity<List<StoreVO>> listByOrgId(Long orgId) {
        List<TzStore> tzStoreList = storeService.listByOrgId(orgId);
        if (CollectionUtils.isEmpty(tzStoreList)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StoreVO> storeList = new ArrayList<>();
        tzStoreList.forEach(tzStore -> {
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId(tzStore.getStoreId());
            storeVO.setName(tzStore.getStationName());
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity sync(List<StoreAddDTO> storeAddDtoList) {
        //校验门店编码是否存在 存在不做处理
        List<String> storeCodeList = storeAddDtoList.stream().map(StoreAddDTO::getStoreCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(storeCodeList)) {
            log.error("同步门店编码为空");
            return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR, "同步门店编码为空");
        }
        List<TzStore> tzStores = storeService.listByStoreCode(storeCodeList);
        //不存在新建门店信息保存
        List<String> existedStoreCode = tzStores.stream().map(TzStore::getStoreCode).collect(Collectors.toList());
        Map<String, TzStore> storeMap = tzStores.stream().collect(Collectors.toMap(TzStore::getStoreCode, tzStore -> tzStore));
        ArrayList<TzStore> addStoreList = new ArrayList<>();
        ArrayList<TzStore> updateStoreList = new ArrayList<>();
        //所有片区节点
        List<Organization> organizationList = organizationService.lambdaQuery().in(Organization::getType, 1, 2, 3).eq(Organization::getIsDeleted, 0).list();
        Map<String, Organization> orgNameMap = organizationList.stream().collect(Collectors.toMap(Organization::getOrgName, organization -> organization));

        storeAddDtoList.stream().forEach(storeAddDTO -> {
            if (existedStoreCode.contains(storeAddDTO.getStoreCode())) {
                //门店已存在更新组织节点信息
                TzStore tzStore = storeMap.get(storeAddDTO.getStoreCode());
                tzStore.setUpdateFlag(false);

//                if (!(storeAddDTO.getType().equals(tzStore.getType()) && storeAddDTO.getStatus().equals(tzStore.getStatus()))) {
//                    tzStore.setType(storeAddDTO.getType());
//                    tzStore.setStatus(tzStore.getStatus());
//                    tzStore.setUpdateFlag(true);
//                    updateStoreList.add(tzStore);
//                }
                //状态
                if(StrUtil.isNotBlank(storeAddDTO.getSlcName())){
                    tzStore.setSlcName(storeAddDTO.getSlcName());
                    tzStore.setUpdateFlag(true);
                }
                //店仓性质
                if(StrUtil.isNotBlank(storeAddDTO.getStorenature()) && StrUtil.isBlank(tzStore.getStorenature())){
                    tzStore.setStorenature(storeAddDTO.getStorenature());
                    tzStore.setUpdateFlag(true);
                }
                //门店地址
                if(StrUtil.isNotBlank(storeAddDTO.getAddr())){
                    tzStore.setAddr(storeAddDTO.getAddr());
                    tzStore.setUpdateFlag(true);
                }
                //门店名称
                if(StrUtil.isNotBlank(storeAddDTO.getStationName())){
                    tzStore.setStationName(storeAddDTO.getStationName());
                    tzStore.setUpdateFlag(true);
                }
                //是否有更新
                if(tzStore.isUpdateFlag()){
                    if (tzStore != null && StrUtil.isNotBlank(tzStore.getAddr()) && tzStore.getLat() == null && tzStore.getLng() == null) {
                        Map<String, String> map = AddressUtil.getLatAndLngByAddr(tzStore.getAddr());
                        if (map != null && map.containsKey("lat") && map.containsKey("lng")) {
                            tzStore.setLat(new Double(map.get("lat")));
                            tzStore.setLng(new Double(map.get("lng")));
                            log.info("更新小程序经纬度：{}", JSONObject.toJSONString(tzStore));
                        }
                    }
                    updateStoreList.add(tzStore);
                }
            } else {
                //进行门店参数封装
                TzStore tzStore = buildStore(storeAddDTO, orgNameMap);
                if (tzStore != null && StrUtil.isNotBlank(tzStore.getAddr()) && tzStore.getLat() == null && tzStore.getLng() == null) {
                    Map<String, String> map = AddressUtil.getLatAndLngByAddr(tzStore.getAddr());
                    if (map != null && map.containsKey("lat") && map.containsKey("lng")) {
                        tzStore.setLat(new Double(map.get("lat")));
                        tzStore.setLng(new Double(map.get("lng")));
                        log.info("添加小程序经纬度：{}", JSONObject.toJSONString(tzStore));
                    }
                }
                addStoreList.add(tzStore);
            }
        });
        //新增节点不为空 ，进行批量保存
        if (!CollectionUtils.isEmpty(addStoreList)) {
            log.info("新增门店数据:{}",JSONObject.toJSONString(addStoreList));
            log.info("新增门店数据条数:{}",addStoreList.size());
            storeService.saveBatch(addStoreList);
        }
        if (!CollectionUtils.isEmpty(updateStoreList)) {
            log.info("更新门店数据:{}",JSONObject.toJSONString(updateStoreList));
            log.info("更新门店数据条数:{}",updateStoreList.size());
            storeService.updateBatch(updateStoreList);
        }
        RedisUtil.deleteBatchKeys(PlatformCacheNames.PLATFORM_STORE+"*");
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Long> getAreaOrgByStore(Long storeId) {
        return ServerResponseEntity.success(storeService.getAreaOrgByStore(storeId));
    }

    @Override
    public ServerResponseEntity<String> getStoreCodeByStoreId(Long storeId) {
        String storeCode = storeService.getStoreCodeByStoreId(storeId);
        return ServerResponseEntity.success(storeCode);
    }

    @Override
    public ServerResponseEntity<Boolean> isInviteStore(Long storeId) {
        StoreVO storeVO=this.findByStoreId(storeId);
        log.info("是否为虚拟门店：{}", Objects.nonNull(storeVO)?JSON.toJSONString(storeVO):"");
        if(Objects.nonNull(storeVO) && Objects.nonNull(storeVO.getStoreInviteType()) && storeVO.getStoreInviteType()==1){
            return ServerResponseEntity.success(true);
        }
        return ServerResponseEntity.success(false);
    }

    @Override
    public ServerResponseEntity<List<StoreVO>> getStoreByPushElStatus(Integer pushElStatus) {
        StoreQueryParamDTO paramDTO = new StoreQueryParamDTO();
        paramDTO.setPushElStatus(pushElStatus);
        List<TzStore> tzStoreList = storeService.listByParam(paramDTO);
        if (CollectionUtils.isEmpty(tzStoreList)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StoreVO> storeList = new ArrayList<>();
        tzStoreList.forEach(tzStore -> {
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId(tzStore.getStoreId());
            storeVO.setName(tzStore.getStationName());
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    @Override
    public ServerResponseEntity<PageVO<SelectedStoreVO>> storePage(InsiderStorePageDTO insiderStorePageDTO) {
        PageVO<SelectedStoreVO> storeVOPageVO = storeService.storePage(insiderStorePageDTO);
        return ServerResponseEntity.success(storeVOPageVO);
    }

    /**
     * 推送erp门店数据
     * @return
     */
    @Override
    public ServerResponseEntity<List<StdStoreVO>> pushStdStores() {
        StoreQueryParamDTO storeQueryParamDTO=new StoreQueryParamDTO();
        storeQueryParamDTO.setStoreStatus(1);
        List<TzStore> tzStores=storeService.listByParam(storeQueryParamDTO);
        if (CollectionUtils.isEmpty(tzStores)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<StdStoreVO> storeList = new ArrayList<>();
        tzStores.forEach(tzStore -> {
            StdStoreVO storeVO = new StdStoreVO();
            storeVO.setStoreCode(tzStore.getStoreCode());
            storeVO.setName(tzStore.getStationName());
            storeVO.setStoreId(tzStore.getStoreId());
            storeVO.setType("2");
            storeList.add(storeVO);
        });
        return ServerResponseEntity.success(storeList);
    }

    @Override
    public ServerResponseEntity<PageVO<SelectedStoreVO>> selectedPage(StoreSelectedParamFeignDTO storeSelectedParamDTO) {

        StoreSelectedParamDTO paramDTO = new StoreSelectedParamDTO();

        if (StrUtil.isNotEmpty(storeSelectedParamDTO.getKeyword())){
            paramDTO.setKeyword(storeSelectedParamDTO.getKeyword());
        }

        if (CollectionUtil.isNotEmpty(storeSelectedParamDTO.getStoreIdList())){
            paramDTO.setStoreIdList(storeSelectedParamDTO.getStoreIdList());
        }


        PageVO<SelectedStoreVO> selectedStoreVOPageVO = storeService.selectedPage(storeSelectedParamDTO, paramDTO);

        return ServerResponseEntity.success(selectedStoreVOPageVO);
    }

    private TzStore buildStore(StoreAddDTO storeAddDTO, Map<String, Organization> orgNameIdMap) {
        //先创建上级组织信息
        Organization parentStoreOrg = orgNameIdMap.get("scrm");
        //二级
        if (StrUtil.isBlank(storeAddDTO.getSecondOrgName())) {
            //放其他片区
            storeAddDTO.setSecondOrgName("其他");
        }
        Organization secondOrg = orgNameIdMap.get(storeAddDTO.getSecondOrgName());
        if (Objects.isNull(secondOrg) && StrUtil.isNotBlank(storeAddDTO.getSecondOrgName())) {
            secondOrg = createOrg(null, storeAddDTO.getSecondOrgName(), 2,storeAddDTO.getShortName());
            orgNameIdMap.put(secondOrg.getOrgName(), secondOrg);
            parentStoreOrg = secondOrg;
        } else if (secondOrg != null) {
            parentStoreOrg = secondOrg;
        }

        //三级
//        Organization thirdOrg = orgNameIdMap.get(storeAddDTO.getThirdOrgName());
//        if (Objects.isNull(thirdOrg) && StrUtil.isNotBlank(storeAddDTO.getThirdOrgName())) {
//            thirdOrg = createOrg(parentStoreOrg, storeAddDTO.getThirdOrgName(), 3);
//            orgNameIdMap.put(thirdOrg.getOrgName(), thirdOrg);
//            parentStoreOrg = thirdOrg;
//        }else if(thirdOrg!=null){
//            parentStoreOrg = thirdOrg;
//        }
        //门店
        Organization storeOrg = orgNameIdMap.get(storeAddDTO.getStationName());
        if (Objects.isNull(storeOrg) && StrUtil.isNotBlank(storeAddDTO.getStationName())) {
            storeOrg = createOrg(parentStoreOrg, storeAddDTO.getStationName(), 4,storeAddDTO.getShortName());
            orgNameIdMap.put(storeOrg.getOrgName(), storeOrg);
        }

        TzStore tzStore = new TzStore();
        BeanUtils.copyProperties(storeAddDTO, tzStore, "storeId");
        tzStore.setOrgId(storeOrg.getOrgId());
        tzStore.setCreateTime(new Date());
        tzStore.setUpdateTime(new Date());
        tzStore.setIsShow(0);//默认不展示
        if(Objects.isNull(tzStore.getStoreInviteType())){
            tzStore.setStoreInviteType(0);
        }
        return tzStore;
    }

    private Organization createOrg(Organization partentOrg, String orgName, int type,String shortName) {
        Organization organization = new Organization();
        organization.setType(type);
        if (type == 2) {
            organization.setParentId(1L);
            organization.setPath("0,1");
        } else {
            organization.setParentId(partentOrg.getOrgId());
            organization.setPath(partentOrg.getPath() + Constant.COMMA + partentOrg.getOrgId());
        }
        organization.setOrgName(orgName);
        organization.setShortName(shortName);
        organization.setIsDeleted(0);
        organizationService.save(organization);
        return organization;
    }
}
