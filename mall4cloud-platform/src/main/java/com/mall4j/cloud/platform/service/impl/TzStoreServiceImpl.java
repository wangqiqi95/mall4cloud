package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.api.platform.constant.StoreStatusEnum;
import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.SoldTzStoreVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.api.product.dto.ElectronicDateMqDTO;
import com.mall4j.cloud.api.user.dto.UserChangeServiceStoreDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.CouponCacheNames;
import com.mall4j.cloud.common.cache.constant.PlatformCacheNames;
import com.mall4j.cloud.common.constant.Auth;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.StorePageQueryDTO;
import com.mall4j.cloud.platform.dto.StoreQueryParamDTO;
import com.mall4j.cloud.platform.dto.StoreSelectedParamDTO;
import com.mall4j.cloud.platform.dto.TzStoreDTO;
import com.mall4j.cloud.platform.mapper.TzStoreMapper;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.SysUserService;
import com.mall4j.cloud.platform.service.TzStoreService;
import com.mall4j.cloud.platform.service.TzTagStoreService;
import com.mall4j.cloud.platform.utils.AddressUtil;
import com.mall4j.cloud.platform.vo.CloseStoreStaffVO;
import javax.annotation.Resource;

import com.mall4j.cloud.platform.vo.TzTagSimpleVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Service
@Slf4j
public class TzStoreServiceImpl implements TzStoreService {

    @Resource
    private TzStoreMapper tzStoreMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MapperFacade mapperFacade;
//    @Autowired
//    private OnsMQTemplate soldUploadExcelTemplate;
    @Autowired
    private ExcelUploadFeignClient excelUploadFeignClient;
//    @Autowired
//    private OnsMQTemplate aliElectronicSignTemplate;
    @Resource
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private StaffService staffService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TzTagStoreService tzTagStoreService;


    @Override
    public PageVO<TzStore> page(PageDTO pageDTO, StorePageQueryDTO request) {
        return PageUtil.doPage(pageDTO, () -> tzStoreMapper.list(request));
    }

    @Override
    @Cacheable(cacheNames = PlatformCacheNames.PLATFORM_STORE, key = "#storeId")
    public TzStore getByStoreId(Long storeId) {
        log.info("test");
        return tzStoreMapper.getByStoreId(storeId);
    }

    @Override
    @CacheEvict(cacheNames = PlatformCacheNames.PLATFORM_STORE, key = "#storeId")
    public void removeCacheByStoreId(Long storeId) {
    }

    @Override
    public void save(TzStore tzStore) {
        tzStoreMapper.save(tzStore);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CloseStoreStaffVO update(TzStoreDTO tzStoreDTO) {
        log.info("更新门店信息->操作人【{}】 变更信息【{}】",AuthUserContext.get().getUsername(),JSONObject.toJSONString(tzStoreDTO));
        CloseStoreStaffVO result = new CloseStoreStaffVO();
        result.setResult(0);
        boolean flag = false;
        List<UserApiVO> userList = new ArrayList<>();
        TzStore afterStore = new TzStore();
        // 若为关闭门店
        if (tzStoreDTO.getStatus() == 0) {
            // 查询门店下是否有在职人员
            List<Staff> staff = staffService
                .list(new LambdaQueryWrapper<Staff>().eq(Staff::getStoreId, tzStoreDTO.getStoreId()).eq(Staff::getStatus, 0));
            if (!CollectionUtils.isEmpty(staff)) {
                result.setResult(1);
                return result;
            }
            // 查询门店下是否有会员
            ServerResponseEntity<List<UserApiVO>> userByStoreId = userFeignClient.getUserByStoreId(tzStoreDTO.getStoreId());
            if (userByStoreId.isSuccess()) {
                userList = userByStoreId.getData();
                if (!CollectionUtils.isEmpty(userList)) {
                    if (tzStoreDTO.getServiceStoreId() == null) {
                        result.setResult(2);
                        return result;
                    } else {
                        // 迁移会员
                        afterStore = tzStoreMapper.getByStoreId(tzStoreDTO.getServiceStoreId());
                        if (afterStore.getStatus() != 1) {
                            throw new LuckException("会员变更新服务门店状态为非营业中,请重新选择变更服务门店");
                        }
                        // 操作标识
                        flag = true;
                    }
                }
            }
        }
        Organization org = organizationService.getByOrgId(tzStoreDTO.getOrgId());
        org.setOrgId(tzStoreDTO.getOrgId());
        org.setOrgName(tzStoreDTO.getStationName());
        org.setShortName(tzStoreDTO.getShortName());
        org.setRemark(tzStoreDTO.getRemark());
        org.setOrgCode(tzStoreDTO.getStoreCode());
        org.setUpdateTime(new Date());
        org.setQiWeiCode(tzStoreDTO.getQiWeiCode());
        org.setLinkman(tzStoreDTO.getLinkman());
        org.setEmail(tzStoreDTO.getEmail());
        org.setMobile(tzStoreDTO.getPhone());
        organizationService.update(org);
//            TzStore tzStore = mapperFacade.map(tzStoreDTO, TzStore.class);

        if(org.getType() ==4){
            TzStore existStore = this.getByStoreId(tzStoreDTO.getStoreId());
            Assert.isNull(existStore, "storeId不存在，请检查数据。");

            TzStore tzStore = mapperFacade.map(tzStoreDTO, TzStore.class);
            if(Objects.nonNull(existStore.getStatus()) && Objects.nonNull(tzStoreDTO.getStatus())){
                if(existStore.getStatus()!=tzStoreDTO.getStatus()){
                    log.info("变更门店状态(编辑)status->操作人【{}】 原状态【{}】 变更后状态【{}】",AuthUserContext.get().getUsername(),existStore.getStatus(),tzStoreDTO.getStatus());
                }
            }

            tzStore.setFirstStartTime(tzStoreDTO.getFirstStartTime());
            tzStore.setFirstEndTime(tzStoreDTO.getFirstEndTime());
            tzStore.setQwCode(tzStoreDTO.getQiWeiCode());
            tzStore.setType(tzStoreDTO.getStoreType());
            tzStore.setUpdateTime(new Date());
            tzStoreMapper.update(tzStore);

            //开启电子价签同步：0否 1是
//            if(tzStore.getPushElStatus()!=null){
//                log.info("变更电子价签同步状态:{} 操作人:{}  原状态【{}】 变更后状态【{}】",AuthUserContext.get().getUsername(),existStore.getPushElStatus(),tzStore.getPushElStatus());
//                if(tzStore.getPushElStatus()==1){
//                    log.info("操作人:{} 操作人id:{}  操作开启电子价签同步",AuthUserContext.get().getUsername(),AuthUserContext.get().getUserId());
//                    //推送价签
//                    ElectronicDateMqDTO electronicDateMqDTO=new ElectronicDateMqDTO();
//                    electronicDateMqDTO.setMqType(2);
//                    List<Long> storeIds=new ArrayList<Long>();
//                    storeIds.add(tzStore.getStoreId());
//                    electronicDateMqDTO.setStoreIds(storeIds);
//                    aliElectronicSignTemplate.syncSend(electronicDateMqDTO);
//                }
//            }
        }
        if (flag) {
            // 执行会员迁移
            TzStore beforeStore = new TzStore();
            beforeStore.setStoreId(tzStoreDTO.getStoreId());
            beforeStore.setStationName(tzStoreDTO.getStationName());
            Long userId = AuthUserContext.get().getUserId();
            SysUserVO byUserId = sysUserService.getByUserId(userId);
            executeChange(beforeStore, userList, byUserId.getNickName(), afterStore, 0, 0);
        }
        this.removeCacheByStoreId(tzStoreDTO.getStoreId());
        return result;
    }

//    public static void main(String[] args) {
//        List<Integer> userList = new ArrayList<>();
//        userList.add(1);
//        userList.add(2);
//        userList.add(3);
//        userList.add(4);
//        userList.add(5);
//        userList.add(6);
//        userList.add(7);
//        userList.add(8);
//        userList.add(9);
//        userList.add(10);
//
//        int pageSize = 2;
//        int allPageSize = userList.size()%pageSize==0?userList.size()/pageSize:userList.size()/pageSize+1;
//        for(int i = 0;i< allPageSize;i++){
//            List<Integer> currentUserList = new ArrayList<>();
//
//            //最后一业,取模
//            if(i == (allPageSize - 1) && userList.size()%pageSize >0 ){
//                currentUserList = userList.subList(i * pageSize,userList.size()%pageSize + i * pageSize);
//            }else{
//                currentUserList = userList.subList(i * pageSize,pageSize + i * pageSize);
//            }
//            System.out.println("------------------------");
//            for (Integer integer : currentUserList) {
//                System.out.println(integer);
//            }
//
//        }
//    }

    @Override
    public void executeChange(TzStore before, List<UserApiVO> userList, String nick, TzStore afterStore, Integer type,
        Integer source) {

        int pageSize = 500;
        int allPageSize = userList.size()%pageSize==0?userList.size()/pageSize:userList.size()/pageSize+1;
        log.info("执行会员服务门店变更，pagesize:{} allPageSize:{},待修改的会员记录数:{}",pageSize,allPageSize,userList.size());

        for(int i = 0;i< allPageSize;i++){
            List<UserApiVO> currentUserList = new ArrayList<>();

            //最后一业,取模
            if(i == (allPageSize - 1) && userList.size()%pageSize >0 ){
                currentUserList = userList.subList(i * pageSize,userList.size()%pageSize + i * pageSize);
            }else{
                currentUserList = userList.subList(i * pageSize,pageSize + i * pageSize);
            }

            UserChangeServiceStoreDTO userChangeServiceStoreDTO = new UserChangeServiceStoreDTO();
            userChangeServiceStoreDTO.setAfterId(afterStore.getStoreId());
            userChangeServiceStoreDTO.setAfterName(afterStore.getStationName());
            userChangeServiceStoreDTO.setUserIds(currentUserList.stream().map(UserApiVO::getUserId).collect(
                    Collectors.toList()));
            userChangeServiceStoreDTO.setBeforeId(before.getStoreId());
            userChangeServiceStoreDTO.setBeforeName(before.getStationName());
            userChangeServiceStoreDTO.setType(type);
            userChangeServiceStoreDTO.setSource(source);
            userChangeServiceStoreDTO.setCreator(nick);
            log.info("调用用户变更服务门店userFeignClient.changeServiceStore i=={} param = {}",i, userChangeServiceStoreDTO);
            ServerResponseEntity<Void> responseEntity = userFeignClient.changeServiceStore(userChangeServiceStoreDTO);
            if (!responseEntity.isSuccess()) {
                log.error("调用userFeignClient.changeServiceStore失败 errorMsg = {}", responseEntity.getMsg());
                throw new LuckException("远程调用失败，请稍后再试");
            }
        }
    }

    @Override
    public void deleteById(Long storeId) {
        tzStoreMapper.deleteById(storeId);
    }

    @Override
    public PageVO<TzStore> page(PageDTO pageDTO, StoreQueryParamDTO storeQueryParamDTO) {
        //查询片区 和 点券下所有门店id 集合作为筛选项
        List<Long> limitOrgIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(storeQueryParamDTO.getGroupOrgIdList())) {
            //存在店群筛选项
            List<Long> orgIds = organizationService.listByTypeAndParentId(3, storeQueryParamDTO.getGroupOrgIdList());
            limitOrgIds.addAll(orgIds);
        }
        if (!CollectionUtils.isEmpty(storeQueryParamDTO.getDistrictOrgIdList())) {
            //存在店群筛选项
            List<Long> orgIds = organizationService.listByTypeAndParentId(2, storeQueryParamDTO.getDistrictOrgIdList());
            limitOrgIds.addAll(orgIds);
        }
        if (!CollectionUtils.isEmpty(limitOrgIds)) {
            storeQueryParamDTO.setLimitOrgIdList(limitOrgIds);
        }
        if (StringUtils.isNotEmpty(storeQueryParamDTO.getSlcName())) {
            storeQueryParamDTO.setSlcNameList(Arrays.asList(storeQueryParamDTO.getSlcName().split(",")));
        }
        PageVO<TzStore> storePageVO = PageUtil.doPage(pageDTO, () -> tzStoreMapper.listByParam(storeQueryParamDTO));
        List<TzStore> list = storePageVO.getList();
        buildOrgName(list);

        //门店标签
        list.forEach( item ->{
            List<TzTagSimpleVO> tzTagSimpleVOS=tzTagStoreService.listTagByStoreId(item.getStoreId());
            if(CollectionUtil.isNotEmpty(tzTagSimpleVOS)){
                List<String> tagNames = tzTagSimpleVOS.stream().map(TzTagSimpleVO::getTagName).collect(toList());
                item.setStoreTagStr(StrUtil.join(",",tagNames));
                item.setStoreTagCount(tzTagSimpleVOS.size());
            }
        });

        storePageVO.setList(list);
        return storePageVO;
    }

    /**
     * 根据门店code获取门店列表
     *
     * @param storeQueryParamDTO
     * @return
     */
    @Override
    public List<TzStore> storeListByCodes(StoreQueryParamDTO storeQueryParamDTO) {
        return tzStoreMapper.listByParam(storeQueryParamDTO);
    }

//    private void buildOrgName(List<TzStore> list) {
//        List<Organization> orgList = organizationService.lambdaQuery().in(Organization::getType, 2, 3, 4).list();
//        Map<Long, Organization> orgMap = orgList.stream().collect(Collectors.toMap(Organization::getOrgId, organization -> organization));
//        list.forEach(tzStore -> {
//            Organization organization = orgMap.get(tzStore.getOrgId());
//            if (Objects.nonNull(organization)) {
//                //无上级
//                if (organization.getType() == 3) {
//                    tzStore.setShopGroups(organization.getOrgName());
//                    Organization organization1 = orgMap.get(organization.getParentId());
//                    if (Objects.nonNull(organization1) && organization1.getType() == 2) {
//                        tzStore.setShopAres(organization.getOrgName());
//                    }
//                } else if (organization.getType() == 2) {
//                    tzStore.setShopAres(organization.getOrgName());
//                }
//            }
//        });
//    }


    private void buildOrgName(List<TzStore> list) {
        //查询全部的组织结构转换成map  orgid为key
        List<Organization> orgList = organizationService.lambdaQuery().in(Organization::getType, 2, 3, 4).list();
        Map<Long, Organization> orgMap = orgList.stream().collect(Collectors.toMap(Organization::getOrgId, organization -> organization));
        for (TzStore tzStore : list) {
            //获取当前门店的组织结构
            Organization organization = orgMap.get(tzStore.getOrgId());
            if (organization == null) {
                continue;
            }
            if (StrUtil.isEmpty(organization.getPath())) {
                continue;
            }
            StringBuilder shopGroups = new StringBuilder();
            StringBuilder shopAres = new StringBuilder();
            for (String s : organization.getPath().split(",")) {
                Organization parentOrg = orgMap.get(Long.parseLong(s));
                if (parentOrg != null && parentOrg.getType() == 3) {
                    shopGroups.append(parentOrg.getOrgName() + "  ");
                }
                if (parentOrg != null && parentOrg.getType() == 2) {
                    shopAres.append(parentOrg.getOrgName() + "  ");
                }
            }
            tzStore.setShopAres(shopAres.toString());
            tzStore.setShopGroups(shopGroups.toString());
        }

    }

    @Override
    public PageVO<SelectedStoreVO> selectedPage(PageDTO pageDTO, StoreSelectedParamDTO storeSelectedParamDTO) {
        if(Objects.nonNull(storeSelectedParamDTO.getCusPageSize()) && storeSelectedParamDTO.getCusPageSize()>0){
            pageDTO.setPageSize(storeSelectedParamDTO.getCusPageSize(),true);
        }
        return PageUtil.doPage(pageDTO, () -> tzStoreMapper.selectedList(storeSelectedParamDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TzStoreDTO tzStoreDTO) {

        //组织节点
        Organization parentOrg = organizationService.getByOrgId(tzStoreDTO.getParentOrgId());
        if (Objects.isNull(parentOrg)) {
            throw new LuckException("上级节点不能为空");
        }
        if (parentOrg.getType() >= tzStoreDTO.getType()) {
            throw new LuckException("不能添加上级 或 同级节点");
        }
        Organization org = mapperFacade.map(tzStoreDTO, Organization.class);
        org.setParentId(tzStoreDTO.getParentOrgId());
        org.setOrgName(tzStoreDTO.getStationName());
        org.setOrgCode(tzStoreDTO.getStoreCode());
        org.setCreateTime(new Date());
        org.setUpdateTime(new Date());
        org.setQiWeiCode(tzStoreDTO.getQiWeiCode());
        org.setLinkman(tzStoreDTO.getLinkman());
        org.setEmail(tzStoreDTO.getEmail());
        org.setMobile(tzStoreDTO.getPhone());
        org.setIsDeleted(0);
        org.setCreateTime(new Date());
        organizationService.saveOrg(org);
        if (!checkOrg(tzStoreDTO.getParentOrgId(), tzStoreDTO.getType())) {
            TzStore tzStore = mapperFacade.map(tzStoreDTO, TzStore.class);
            tzStore.setStoreId(null);
            tzStore.setOrgId(org.getOrgId());
            tzStore.setIsShow(0);
            tzStore.setStatus(0);
            tzStore.setQwCode(tzStoreDTO.getQiWeiCode());
            tzStore.setCreateTime(new Date());
            tzStore.setUpdateTime(new Date());
            tzStore.setType(tzStoreDTO.getStoreType());
            tzStoreMapper.save(tzStore);
        }

    }

    @Override
    public List<TzStore> listByParam(StoreQueryParamDTO storeQueryParamDTO) {
        return tzStoreMapper.listByParam(storeQueryParamDTO);
    }

    @Override
    public List<TzStore> listByOrgId(Long orgId) {
        Organization organization = organizationService.getByOrgId(orgId);
        if (Objects.nonNull(organization)) {
            StoreQueryParamDTO storeQueryParamDTO = new StoreQueryParamDTO();
            if (organization.getType() == 4) {
                List<Long> orgIdList = new ArrayList<>();
                orgIdList.add(organization.getOrgId());
                storeQueryParamDTO.setLimitOrgIdList(orgIdList);
                return tzStoreMapper.listByParam(storeQueryParamDTO);
            } else {
                String path = organization.getPath() + "," + organization.getOrgId();
                List<Organization> organizationList = organizationService.listByTypeAndPathLike(4, path);
                if (!CollectionUtils.isEmpty(organizationList)) {
                    storeQueryParamDTO.setLimitOrgIdList(organizationList.stream().map(Organization::getOrgId).collect(Collectors.toList()));
                    return tzStoreMapper.listByParam(storeQueryParamDTO);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<TzStore> listByOrgIds(String orgIds) {
        if(StrUtil.isEmpty(orgIds)){
            return new ArrayList<>();
        }
        List<TzStore> stores = new ArrayList<>();
        for (String orgId : orgIds.split(",")) {
            stores.addAll(listByOrgId(Long.parseLong(orgId)));
        }
        return stores;
    }

    @Override
    public void listByStoreLatLng() {
        List<TzStore> tzStores = tzStoreMapper.listByStoreLatLng();
        AtomicInteger succ = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        tzStores.forEach(tzStore -> {
            Map<String, String> map = AddressUtil.getLatAndLngByAddr(tzStore.getAddr());
            if (map != null && map.containsKey("lat") && map.containsKey("lng")) {
                tzStore.setLat(new Double(map.get("lat")));
                tzStore.setLng(new Double(map.get("lng")));
                tzStoreMapper.update(tzStore);
                succ.getAndIncrement();
            } else {
                fail.getAndIncrement();
            }
        });
        log.info("修改成功地址:{}条",succ.get());
        log.info("修改地址失败:{}条",fail.get());
    }

    @Override
    public List<TzStore> getTzStoreByStoreNames(List<String> storeNames) {
        if(CollectionUtil.isNotEmpty(storeNames)){
            return tzStoreMapper.getTzStoreByStoreNames(storeNames);
        }
        return Lists.newArrayList();
    }

    @Override
    public void updateByStoreCode() {

    }

    @Override
    public void updateBatch(List<TzStore> updateStoreList) {
         tzStoreMapper.updateBatch(updateStoreList);
    }

    @Override
    public void updateShows(List<Long> storeIds, Integer isShow) {
        if(CollectionUtil.isNotEmpty(storeIds) && isShow!=null){
            log.info("变更c端展示状态->操作人【{}】 状态【{}】 门店【{}】", AuthUserContext.get().getUsername(),isShow, JSONObject.toJSONString(storeIds));
            List<TzStore> stores=new ArrayList<>();
            storeIds.forEach(item->{
                TzStore tzStore=new TzStore();
                tzStore.setStoreId(item);
                tzStore.setIsShow(isShow);
                stores.add(tzStore);
            });

            tzStoreMapper.updateShows(stores);
        }
    }

    @Override
    public List<TzStore> listByStoreCode(List<String> storeCodeList) {
        return tzStoreMapper.listByStoreCode(storeCodeList);
    }

    @Override
    public TzStore getTzStoreByStoreCode(String storeCode) {
        return tzStoreMapper.getTzStoreByStoreCode(storeCode);
    }

    @Override
    public List<TzStore> listByStoreIdList(List<Long> storeIdList) {
        StoreQueryParamDTO storeQueryParamDTO = new StoreQueryParamDTO();
        storeQueryParamDTO.setStoreIdList(storeIdList);
        return tzStoreMapper.listByParam(storeQueryParamDTO);
    }

    @Override
    public TzStore getByOrgId(Long orgId) {
        return tzStoreMapper.getByOrgId(orgId);
    }

    @Override
    public void saveBatch(List<TzStore> addStoreList) {
        tzStoreMapper.saveBatch(addStoreList);
    }


    @Override
    public Long getAreaOrgByStore(Long storeId) {
        TzStore store = tzStoreMapper.getByStoreId(storeId);
        if (null == store) {
            return 0L;
        }
        return getParentId(store.getOrgId());
    }

    private Long getParentId(Long parentId) {
        Organization organization = organizationService.getByOrgId(parentId);
        if (null == organization) {
            return 0L;
        }
        if (organization.getType() == 2) {
            return organization.getOrgId();
        }
        if (organization.getType() == 1) {
            return 0L;
        }
        return getParentId(organization.getParentId());
    }

    @Override
    public String getStoreCodeByStoreId(Long storeId) {
        return tzStoreMapper.getStoreCodeByStoreId(storeId);
    }

    @Override
    public PageVO<SelectedStoreVO> storePage(InsiderStorePageDTO insiderStorePageDTO) {
        return PageUtil.doPage(insiderStorePageDTO, () -> tzStoreMapper.listByInsider(insiderStorePageDTO));
    }

    @Override
    public TzStore getByStoreCode(String storeName, String storeCode) {
        return tzStoreMapper.getByStoreCode(storeName, storeCode);
    }

    /**
     * 导出门店
     *
     * @param storeQueryParamDTO
     * @param response
     */
    @Override
    public List<SoldTzStoreVO> soldStore(StoreQueryParamDTO storeQueryParamDTO, HttpServletResponse response) {
        //查询片区 和 点券下所有门店id 集合作为筛选项
        List<Long> limitOrgIdStoreIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(storeQueryParamDTO.getGroupOrgIdList())) {
            //存在店群筛选项
            List<Long> orgIds = organizationService.listByTypeAndParentId(3, storeQueryParamDTO.getGroupOrgIdList());
            limitOrgIdStoreIds.addAll(orgIds);
        }
        if (!CollectionUtils.isEmpty(storeQueryParamDTO.getDistrictOrgIdList())) {
            //存在店群筛选项
            List<Long> orgIds = organizationService.listByTypeAndParentId(2, storeQueryParamDTO.getDistrictOrgIdList());
            limitOrgIdStoreIds.addAll(orgIds);
        }
        if (!CollectionUtils.isEmpty(storeQueryParamDTO.getStoreIdList())) {
            limitOrgIdStoreIds.addAll(storeQueryParamDTO.getStoreIdList());
            storeQueryParamDTO.getStoreIdList().clear();
        }
        if (!CollectionUtils.isEmpty(limitOrgIdStoreIds)) {
            storeQueryParamDTO.setLimitOrgIdStoreIdList(limitOrgIdStoreIds);
        }


//        List<TzStore> tzStores = tzStoreMapper.soldListByParam(storeQueryParamDTO);
        List<TzStore> tzStores = tzStoreMapper.listByParam(storeQueryParamDTO);
        if (CollectionUtil.isEmpty(tzStores)) {
            throw new LuckException("没有可导出的数据");
        }

        //节点信息
        buildOrgName(tzStores);

        List<SoldTzStoreVO> soldTzStoreVOS = tzStores.stream().map(item -> {

//            SoldTzStoreVO soldTzStoreVO = mapperFacade.map(item, SoldTzStoreVO.class);

            SoldTzStoreVO soldTzStoreVO=new SoldTzStoreVO();
            soldTzStoreVO.setStoreCode(item.getStoreCode());
            soldTzStoreVO.setStationName(item.getStationName());
//            soldTzStoreVO.setType(StoreTypeEnum.getDesc(item.getType()));
            soldTzStoreVO.setStorenature(item.getStorenature());
            soldTzStoreVO.setStatus(StoreStatusEnum.getDesc(item.getStatus()));
            soldTzStoreVO.setShopGroups(item.getShopGroups());
            soldTzStoreVO.setShopAres(item.getShopAres());

            String locationAddr = StrUtil.isNotBlank(item.getProvince()) ? item.getProvince() : "";
            locationAddr = StrUtil.isNotBlank(item.getCity()) ? locationAddr.concat(item.getCity()) : "";
            locationAddr = StrUtil.isNotBlank(item.getArea()) ? locationAddr.concat(item.getArea()) : "";
            soldTzStoreVO.setLocationAddr(locationAddr);
            soldTzStoreVO.setAddr(item.getAddr());

            soldTzStoreVO.setLinkman(item.getLinkman());
            soldTzStoreVO.setPhone(item.getPhone());
            soldTzStoreVO.setQwCode(item.getQwCode());
            soldTzStoreVO.setEmail(item.getEmail());
            soldTzStoreVO.setOpenDay(item.getOpenDay());

            if(StrUtil.isNotBlank(item.getFirstStartTime()) && StrUtil.isNotBlank(item.getFirstEndTime())){
                soldTzStoreVO.setBusinessTime(item.getFirstStartTime()+" 至 "+item.getFirstEndTime());
            }

//            soldTzStoreVO.setRemarks();

            //c端是否展示 0-不展示 ，1 -展示
            if (item.getIsShow() != null) {
                if (item.getIsShow() == 0) {
                    soldTzStoreVO.setIsShow("不展示");
                } else if (item.getIsShow() == 1) {
                    soldTzStoreVO.setIsShow("展示");
                }
            }
            //是否虚拟门店：0否 1是
            if(Objects.nonNull(item.getStoreInviteType())){
                if (item.getStoreInviteType() == 0) {
                    soldTzStoreVO.setStoreInviteType("否");
                } else if (item.getStoreInviteType() == 1) {
                    soldTzStoreVO.setStoreInviteType("是");
                }
            }

            return soldTzStoreVO;
        }).collect(Collectors.toList());

//        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
//        downloadRecordDTO.setDownloadTime(new Date());
//        downloadRecordDTO.setFileName(SoldTzStoreVO.EXCEL_NAME);
//        downloadRecordDTO.setCalCount(soldTzStoreVOS.size());
//        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
//        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
//        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
//        Long downLoadHisId=null;
//        if(serverResponseEntity.isSuccess()){
//            downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
//        }

//        ExcelUtil.soleExcel(response, soldTzStoreVOS,
//                SoldTzStoreVO.EXCEL_NAME,
//                SoldTzStoreVO.MERGE_ROW_INDEX,
//                SoldTzStoreVO.MERGE_COLUMN_INDEX,
//                SoldTzStoreVO.class);

//        ExcelUploadDTO excelUploadDTO=new ExcelUploadDTO(downLoadHisId,
//                soldTzStoreVOS,
//                SoldTzStoreVO.EXCEL_NAME,
//                SoldTzStoreVO.MERGE_ROW_INDEX,
//                SoldTzStoreVO.MERGE_COLUMN_INDEX,
//                SoldTzStoreVO.class);
//
//        soldUploadExcelTemplate.syncSend(excelUploadDTO);//发布使用

//        excelUploadFeignClient.soleAndUploadExcel(excelUploadDTO);//调试使用

        return soldTzStoreVOS;

    }

    @Override
    public PageVO<TzStore> storePlatPage(Long orgId, String keyword, PageDTO pageDTO) {
        Organization byOrgId = organizationService.getByOrgId(orgId);
        if (Objects.isNull(byOrgId)){
            throw new LuckException("组织节点信息为空");
        }
        return PageUtil.doPage(pageDTO, () -> tzStoreMapper.listByOrgIdAndKeyWord(orgId,keyword,byOrgId.getPath()));
    }

    private boolean checkOrg(Long parentOrgId, Integer type) {
        //品牌节点不允许直接创建
        if (Objects.isNull(parentOrgId)) {
            throw new LuckException("上级节点信息为空");
        }
        if (type == 4) {
            return false;
        } else {
            return true;
        }

    }
}
