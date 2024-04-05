package com.mall4j.cloud.platform.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.api.platform.dto.ImportTzStoreLogVO;
import com.mall4j.cloud.api.platform.dto.StoreAddDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.platform.dto.ImportTzStoreVO;
import com.mall4j.cloud.platform.dto.OrganizationDTO;
import com.mall4j.cloud.platform.listener.StoreExcelListener;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.service.TzStoreService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2022年3月8日, 0008 15:10
 * @Created by eury
 */
@Slf4j
@Component
public class StoreExcelManager {

    @Autowired
    private TzStoreService tzStoreService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private AreaFeignClient areaFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;


    public String importStoreExcel(MultipartFile file,Long orgId) {
        try {
//            Map<String, List<ImportTzStoreLogVO>> errorMap = new HashMap<>(16);
            Map<String,ImportTzStoreLogVO> errorMap = new HashMap();
            StoreExcelListener storeExcelListener = new StoreExcelListener(this, errorMap,orgId);
            EasyExcel.read(file.getInputStream(), ImportTzStoreVO.class, storeExcelListener).sheet().doRead();
            String info = this.sendLogThreeLower(errorMap);
//            String info = getUserExportInfo(errorMap);
            return info;
        } catch (Exception e) {
            log.info("StoreExcelManager--门店导入失败 {} {}",e,e.getMessage());
            throw new LuckException("导入失败，请检查模板是否正确");
        }
    }

    /**
     * 处理导入的需要响应的信息
     *
     * @param errorMap 响应信息的集合
     * @return 响应信息
     */
//    private String getUserExportInfo(Map<String, List<ImportTzStoreLogVO>> errorMap) {
//        StringBuffer info = new StringBuffer();
//        List<String> importTotal = errorMap.get(StaffExcelListener.IMPORT_ROWS);
//        BigDecimal total = new BigDecimal("0");
//        if (CollUtil.isNotEmpty(importTotal)) {
//            for (int i = 0; i < importTotal.size(); i++) {
//                String item = importTotal.get(i);
//                if (StrUtil.isNotBlank(item)) {
//                    total = total.add(new BigDecimal(item));
//                }
//            }
//        }
//        info.append("共有： " + total.intValue() + "条数据成功导入" + StrUtil.LF);
//        // 错误信息
//        List<String> errorRows = errorMap.get(StaffExcelListener.ERROR_ROWS);
//        if (CollUtil.isNotEmpty(errorRows)) {
//            info.append("门店信息错误行数： " + errorRows.toArray() + StrUtil.LF);
//        }
//        List<String> errors = errorMap.get(StaffExcelListener.OTHER);
//        if (CollUtil.isNotEmpty(errors)) {
//            for (String error : errors) {
//                info.append(error);
//            }
//        }
//        return info.toString();
//    }

    public void importExcel(List<ImportTzStoreVO> list, Map<String,ImportTzStoreLogVO> errorMap,Long orgId) {
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("解析到0条数据");
        }
        int size = list.size();

        //门店列表
        List<TzStore> tzStores=new ArrayList<>();

        // 集合去重复
//        list = list.stream().collect(
//                Collectors.collectingAndThen(
//                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ImportTzStoreVO::getStoreCode))), ArrayList::new));

        this.loadErrorData(list, errorMap, size);
        ServerResponseEntity<List<AreaVO>> provinceAreaRes = areaFeignClient.listProvinceArea();
        Map<String, AreaVO> areaMap = provinceAreaRes.getData().stream().collect(Collectors.toMap(AreaVO::getAreaName, areaVO -> areaVO));

        //门店所属组织节点
        StringBuilder shopGroups = new StringBuilder();
        StringBuilder shopAres = new StringBuilder();
        if(Objects.nonNull(orgId)){
            Organization organization=getOrganizationById(orgId);
            shopAres.append(organization.getOrgName());
        }

        // 处理数据
        for (ImportTzStoreVO storeVO : list) {

            if (StrUtil.isBlank(storeVO.getStoreCode())) {
//                errorRows.add(row);
                this.loadErrorDataMap(storeVO,"门店编码不能为空",errorMap);
                continue;
            }
            if (StrUtil.isBlank(storeVO.getStationName())) {
//                errorRows.add(row);
                this.loadErrorDataMap(storeVO,"门店名称不能为空",errorMap);
                continue;
            }
            if (StrUtil.isBlank(storeVO.getShortName())) {
//                errorRows.add(row);
                this.loadErrorDataMap(storeVO,"门店简称不能为空",errorMap);
                continue;
            }
            if (StrUtil.isBlank(storeVO.getStatus())) {
                this.loadErrorDataMap(storeVO,"门店状态不能为空",errorMap);
//                errorRows.add(row);
                continue;
            }
            if (StrUtil.isBlank(storeVO.getStorenature())) {
                this.loadErrorDataMap(storeVO,"门店类型不能为空",errorMap);
//                errorRows.add(row);
                continue;
            }
            if (StrUtil.isBlank(storeVO.getStoreInvite())) {
                this.loadErrorDataMap(storeVO,"虚拟门店不能为空",errorMap);
//                errorRows.add(row);
                continue;
            }
            //验证编码格式（字母和数字）
            if(!PrincipalUtil.isMatching(PrincipalUtil.WITHOUT_CHINESE,storeVO.getStoreCode())){
                this.loadErrorDataMap(storeVO,"门店编码格式不正确(仅限字母和数字)",errorMap);
//                errorRows.add(row);
                continue;
            }

            TzStore tzStore= mapperFacade.map(storeVO, TzStore.class);

            if(StrUtil.isNotBlank(storeVO.getStatus())){
                tzStore.setStatus(Integer.parseInt(storeVO.getStatus()));
            }
            if(StrUtil.isNotBlank(storeVO.getStorenature())){
                tzStore.setStorenature(storeVO.getStorenature());
            }

            tzStore.setStoreInviteType(0);
            if(StrUtil.isNotBlank(storeVO.getStoreInvite())){//是否虚拟门店：0否 1是
                if(storeVO.getStoreInvite().trim().equals("是")){
                    tzStore.setStoreInviteType(1);
                }else if(storeVO.getStoreInvite().trim().equals("否")){
                    tzStore.setStoreInviteType(0);
                }
            }
            //门店所属组织节点
            tzStore.setOrgId(orgId);
            tzStore.setShopAres(StrUtil.isNotBlank(shopAres.toString())?shopAres.toString():null);
            tzStore.setShopGroups(StrUtil.isNotBlank(shopGroups.toString())?shopGroups.toString():null);

            if(StrUtil.isNotBlank(storeVO.getLocationAddr())){//门店位置 省市区需要分隔符：北京市|市辖区|北京市
                String[] locs=storeVO.getLocationAddr().split("\\|");
                if(locs!=null && locs.length>0){
                    tzStore.setProvince(locs[0]);
                    AreaVO areaProvince=areaMap.get(tzStore.getProvince());
                    if(areaProvince!=null){//省份
                        tzStore.setProvinceId(areaProvince.getAreaId());
                        if(locs.length>1) {
                            tzStore.setCity(locs[1]);
                            AreaVO areaCity=getAreaVO(areaProvince,tzStore.getCity());
                            if(areaCity!=null){//市
                                tzStore.setCityId(areaCity.getAreaId());
                                if(locs.length>=2) {//区
                                    tzStore.setArea(locs[2]);
                                    AreaVO area=getAreaVO(areaCity,tzStore.getArea());
                                    if(area!=null){
                                        tzStore.setAreaId(area.getAreaId());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            tzStore.setIsShow(0);
            tzStore.setCreateTime(new Date());
            tzStores.add(tzStore);
        }

        // 批量导入
        if(CollectionUtil.isNotEmpty(tzStores)){
            try {

                save(tzStores);

                //导入日志
//                if (CollUtil.isNotEmpty(errorMap)) {
//                    sendLogThreeLower(errorMap);
//                }
            }catch (Exception e){
                log.info(""+e.getMessage());
            }
        }

    }

    private void save(List<TzStore> tzStores) {
        List<StoreAddDTO> storeAddDtoList = tzStores.stream().map(tzStore -> {
            StoreAddDTO storeAddDTO = mapperFacade.map(tzStore,StoreAddDTO.class);
            storeAddDTO.setSecondOrgName(tzStore.getShopAres());
            storeAddDTO.setThirdOrgName(tzStore.getShopGroups());
            return storeAddDTO;
        }).collect(Collectors.toList());
        // TODO 待提供保存接口
        storeFeignClient.sync(storeAddDtoList);

        //删除组织节点缓存
        RedisUtil.del(CacheNames.PLATFORM_ORG_ALL+"::all");
    }

    private AreaVO getAreaVO(AreaVO parentAreaVo,String key){
        Map<String, AreaVO> areas = parentAreaVo.getAreas().stream().collect(Collectors.toMap(AreaVO::getAreaName, area -> area));
        return areas.get(key);
    }

    /**
     * 过滤门店编号已存在
     * @param list 用户数据列表
     * @param size 数据数量
     */
    private void loadErrorData(List<ImportTzStoreVO> list, Map<String,ImportTzStoreLogVO> errorData , int size) {
        List<String> storeCodes = new ArrayList<>();
        for (ImportTzStoreVO userExcelDTO : list) {
            storeCodes.add(userExcelDTO.getStoreCode());
        }
        if(CollectionUtil.isEmpty(storeCodes)){
            return;
        }
        //
        List<TzStore> tzStores = tzStoreService.listByStoreCode(storeCodes);
        storeCodes = tzStores.stream().map(TzStore::getStoreCode).collect(Collectors.toList());
        Iterator<ImportTzStoreVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            ImportTzStoreVO importTzStoreVO = iterator.next();
            boolean storeCode = storeCodes.contains(importTzStoreVO.getStoreCode());
            if (!storeCode) {
                storeCodes.add(importTzStoreVO.getStoreCode());
                continue;
            }
            if (storeCode) {
                loadErrorDataMap(importTzStoreVO,"门店编码已存在",errorData);
            } else {
                storeCodes.add(importTzStoreVO.getStoreCode());
            }
            iterator.remove();
        }
    }

    private void loadErrorDataMap(ImportTzStoreVO storeVO, String importRemarks,Map<String,ImportTzStoreLogVO> errorData) {
        if(Objects.nonNull(storeVO)){
            if(StrUtil.isBlank(storeVO.getStoreCode())){
                ImportTzStoreLogVO importTzStoreLogVO=mapperFacade.map(storeVO,ImportTzStoreLogVO.class);
                importTzStoreLogVO.setImportStatus("失败");
                importTzStoreLogVO.setImportRemarks("门店编码不能为空");
                errorData.put(storeVO.getStoreCode()+System.currentTimeMillis(),importTzStoreLogVO);
                return;
            }
            if(!errorData.containsKey(storeVO.getStoreCode())){
                ImportTzStoreLogVO importTzStoreLogVO=mapperFacade.map(storeVO,ImportTzStoreLogVO.class);
                importTzStoreLogVO.setImportStatus("失败");
                importTzStoreLogVO.setImportRemarks(importRemarks);
                errorData.put(storeVO.getStoreCode(),importTzStoreLogVO);
            }else{
                ImportTzStoreLogVO importTzStoreLogVO=mapperFacade.map(storeVO,ImportTzStoreLogVO.class);
                importTzStoreLogVO.setImportStatus("失败");
                importTzStoreLogVO.setImportRemarks("门店编码重复");
                errorData.put(storeVO.getStoreCode(),importTzStoreLogVO);
            }
        }
    }

    private Organization getOrganization(String orgName ,Integer type){
        OrganizationDTO organizationDTO=new OrganizationDTO();
        organizationDTO.setOrgName(orgName);
        organizationDTO.setType(type);
        return organizationService.getOrganizationByParam(organizationDTO);
    }

    private Organization getOrganizationById(Long orgId){
        OrganizationDTO organizationDTO=new OrganizationDTO();
        return organizationService.getByOrgId(orgId);
    }

    private String sendLogThreeLower(Map<String,ImportTzStoreLogVO> logVOSMap){
        if(CollectionUtil.isEmpty(logVOSMap)){
            return null;
        }
        //下载中心记录
        File file=null;
        try {
            List<ImportTzStoreLogVO> list=new ArrayList<>(logVOSMap.values());
            String pathExport= SystemUtils.getExcelFilePath()+"/"+ SystemUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, ImportTzStoreLogVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(list);
            String contentType = "application/vnd.ms-excel";
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess()) {
                log.info("门店导入日志 {}",responseEntity.getData());
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
                return responseEntity.getData();
            }
        }catch (Exception e){
            log.info("上传文件失败 {} {}",e,e.getMessage());
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
        return null;
    }
}
