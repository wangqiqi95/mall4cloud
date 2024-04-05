package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationPhoneVO;
import com.mall4j.cloud.biz.dto.cp.CpPhoneLibraryDTO;
import com.mall4j.cloud.biz.dto.cp.ImportCpPhoneLibraryDTO;
import com.mall4j.cloud.biz.dto.cp.ImportCpPhoneLibraryLogDTO;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneLibraryMapper;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskUserMapper;
import com.mall4j.cloud.biz.model.cp.CpPhoneLibrary;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskUser;
import com.mall4j.cloud.biz.service.cp.CpPhoneLibraryService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskUserService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.csvExport.ExcelListener;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 引流手机号库
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
@Slf4j
@Service
public class CpPhoneLibraryServiceImpl extends ServiceImpl<CpPhoneLibraryMapper,CpPhoneLibrary> implements CpPhoneLibraryService {

    @Autowired
    private CpPhoneLibraryMapper cpPhoneLibraryMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    @Autowired
    private CpPhoneTaskUserMapper cpPhoneTaskUserMapper;
    @Autowired
    private CpPhoneTaskUserService phoneTaskUserService;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Override
    public PageVO<CpPhoneLibrary> page(PageDTO pageDTO,CpPhoneLibraryDTO cpPhoneLibraryDTO) {
        return PageUtil.doPage(pageDTO, () -> cpPhoneLibraryMapper.list(cpPhoneLibraryDTO));
    }

    @Override
    public CpPhoneLibrary getById(Long id) {
        return cpPhoneLibraryMapper.getById(id);
    }

    @Override
    public CpPhoneLibrary getByPhone(String phone) {
        return cpPhoneLibraryMapper.getByPhone(phone);
    }

    @Override
    public void createAndUpdate(CpPhoneLibraryDTO phoneLibraryDTO) {
        CpPhoneLibrary cpPhoneLibrary=this.getByPhone(phoneLibraryDTO.getPhone());
        if(Objects.isNull(cpPhoneLibrary)){
            cpPhoneLibrary=mapperFacade.map(phoneLibraryDTO,CpPhoneLibrary.class);
            cpPhoneLibrary.setCreateBy(AuthUserContext.get().getUsername());
            cpPhoneLibrary.setIsDelete(0);
            cpPhoneLibrary.setCreateTime(new Date());
            this.save(cpPhoneLibrary);
        }else{
            cpPhoneLibrary=mapperFacade.map(phoneLibraryDTO,CpPhoneLibrary.class);
            cpPhoneLibrary.setUpdateBy(AuthUserContext.get().getUsername());
            cpPhoneLibrary.setUpdateTime(new Date());
            this.updateById(cpPhoneLibrary);
        }
    }

    @Override
    public void deleteById(Long id) {
        CpPhoneLibrary cpPhoneLibrary=this.getById(id);
        if(Objects.isNull(cpPhoneLibrary)){
            throw new LuckException("信息未找到");
        }
        //TODO 是否需要校验关联任务

        cpPhoneLibrary.setIsDelete(1);
        cpPhoneLibrary.setUpdateBy(AuthUserContext.get().getUsername());
        cpPhoneLibrary.setUpdateTime(new Date());
        this.updateById(cpPhoneLibrary);
    }

    /**
     * 导入手机号
     * @param file
     * @throws IOException
     */
    @Override
    public void importExcel(MultipartFile file) {
        ExcelListener<ImportCpPhoneLibraryDTO> excelListener = new ExcelListener<>(ImportCpPhoneLibraryDTO.class);
        try {
            EasyExcelFactory.read(file.getInputStream(), ImportCpPhoneLibraryDTO.class, excelListener).sheet().doRead();
        } catch (Exception e) {
            log.info("导入手机号失败 {} {}", e, e.getMessage());
            throw new LuckException("操作失败，请检查模板及内容是否正确或者有为空的内容");
        }
        List<ImportCpPhoneLibraryDTO> list = excelListener.getList();
        list=list.stream().filter(item-> StrUtil.isNotEmpty(item.getPhone())).collect(Collectors.toList());
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("操作失败，未解析到内容");
        }
        if (list.size() > 500) {
            throw new LuckException("操作失败，数据不可超过500行");
        }
        //
        list=cleanExcelData(list);

        //获取客户信息
        List<String> phones=list.stream().map(item->item.getPhone()).collect(Collectors.toList());
//        ServerResponseEntity<List<UserStaffCpRelationPhoneVO>> responseEntity=userStaffCpRelationFeignClient.getRelationPhone(phones);
//        if (responseEntity.isFail() || CollUtil.isEmpty(responseEntity.getData())) {
//            log.info("操作失败，根据手机号获取客户失败");
//            return;
//        }
//        Map<String,UserStaffCpRelationPhoneVO> phoneUserVOMap=LambdaUtils.toMap(responseEntity.getData(),UserStaffCpRelationPhoneVO::getPhone);

        //导入日志
        List<ImportCpPhoneLibraryLogDTO> libraryLogDTOS=new ArrayList<>();
        //获取系统已存在的手机号
        CpPhoneLibraryDTO dto=new CpPhoneLibraryDTO();
        dto.setPhones(phones);
        List<CpPhoneLibrary> phoneLibraries=cpPhoneLibraryMapper.list(dto);
        Map<String,CpPhoneLibrary> phoneLibrariesMap= LambdaUtils.toMap(phoneLibraries,CpPhoneLibrary::getPhone);

        List<CpPhoneLibrary> cpPhoneLibraries=new ArrayList<>();
        for (ImportCpPhoneLibraryDTO importCpPhoneLibraryDTO : list) {
            ImportCpPhoneLibraryLogDTO libraryLogDTO=mapperFacade.map(importCpPhoneLibraryDTO,ImportCpPhoneLibraryLogDTO.class);
            libraryLogDTOS.add(libraryLogDTO);
            //校验手机号格式
            if(!PhoneUtil.isPhone(importCpPhoneLibraryDTO.getPhone())){
                libraryLogDTO.setLog("手机号格式错误/");
                continue;
            }
            CpPhoneLibrary cpPhoneLibrary=mapperFacade.map(importCpPhoneLibraryDTO,CpPhoneLibrary.class);
            if(phoneLibrariesMap.containsKey(importCpPhoneLibraryDTO.getPhone())){//更新
                cpPhoneLibrary.setId(phoneLibrariesMap.get(importCpPhoneLibraryDTO.getPhone()).getId());
                cpPhoneLibrary.setUpdateTime(new Date());
                cpPhoneLibrary.setUpdateBy(AuthUserContext.get().getUsername());
            }else{//新增
//                if(phoneUserVOMap.containsKey(importCpPhoneLibraryDTO.getPhone())){
//                    libraryLogDTO.setLog("手机号已存在客户管理/");
//                    log.info("手机号已存在客户管理 : {}",importCpPhoneLibraryDTO.getPhone());
//                    continue;
//                }
                cpPhoneLibrary.setIsDelete(0);
                cpPhoneLibrary.setCreateTime(new Date());
                cpPhoneLibrary.setCreateBy(AuthUserContext.get().getUsername());
                cpPhoneLibrary.setImportFrom(0);
                cpPhoneLibrary.setStatus(0);
            }
            cpPhoneLibraries.add(cpPhoneLibrary);
        }
        if(CollUtil.isNotEmpty(cpPhoneLibraries)){
            this.saveOrUpdateBatch(cpPhoneLibraries,100);
        }
    }

    private List<ImportCpPhoneLibraryDTO> cleanExcelData(List<ImportCpPhoneLibraryDTO> list) {
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("请检查内容是否正确，或者有为空的内容");
        }
        // 集合去重复
        list = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ImportCpPhoneLibraryDTO::getPhone))), ArrayList::new));
        return list;
    }


    /**
     * 刷新手机号是否在企微外部联系人添加成功: 绑定状态
     */
    @Override
    public void refeshStatus() {
        CpPhoneLibraryDTO dto=new CpPhoneLibraryDTO();
        List<CpPhoneLibrary> libraries=cpPhoneLibraryMapper.list(dto);
        if(CollUtil.isEmpty(libraries)){
            log.info("刷新手机号是否在企微外部联系人添加失败，未获取到需要处理的数据");
            return;
        }
        List<String> phones=libraries.stream().map(item->item.getPhone()).collect(Collectors.toList());
        ServerResponseEntity<List<UserStaffCpRelationPhoneVO>> responseEntity=userStaffCpRelationFeignClient.getRelationPhone(phones);
        if (responseEntity.isFail() || CollUtil.isEmpty(responseEntity.getData())) {
            log.info("刷新手机号是否在企微外部联系人添加失败，根据手机号未获取到企微id信息");
            return;
        }
        List<Long> staffIds=responseEntity.getData().stream()
                .filter(item->Objects.nonNull(item.getStaffId()))
                .map(item->item.getStaffId()).collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> staffResponse=staffFeignClient.getStaffByIds(staffIds);
        if (responseEntity.isFail() || CollUtil.isEmpty(responseEntity.getData())) {
            log.info("刷新手机号根据企微外部联系人添加失败1，根据客户未获取到员工");
            return;
        }
        Map<Long,StaffVO> staffVOMap=LambdaUtils.toMap(staffResponse.getData().stream().filter(item->item.getStatus()==0&&item.getIsDelete()==0),StaffVO::getId);
        if (responseEntity.isFail() || CollUtil.isEmpty(responseEntity.getData())) {
            log.info("刷新手机号根据企微外部联系人添加失败2，根据客户未获取到员工");
            return;
        }
        Long startTime=System.currentTimeMillis();
        Map<String,UserStaffCpRelationPhoneVO> phoneVOMap=LambdaUtils.toMap(responseEntity.getData(),UserStaffCpRelationPhoneVO::getPhone);
        List<CpPhoneLibrary> updates=new ArrayList<>();
        List<CpPhoneTaskUser> taskUsers=new ArrayList<>();
        for (CpPhoneLibrary library : libraries) {
            if(phoneVOMap.containsKey(library.getPhone())){
                //TODO 校验对应员工是否离职
                StaffVO staffVO=staffVOMap.get(phoneVOMap.get(library.getPhone()).getStaffId());
                if(Objects.isNull(staffVO)){
                    log.info("刷新手机号根据企微外部联系人添加失败3，根据客户未获取到员工");
                    continue;
                }
                CpPhoneLibrary update=new CpPhoneLibrary();
                update.setExternalUserId(phoneVOMap.get(library.getPhone()).getUserId());
                update.setStatus(2);
                update.setUpdateBy("定时任务更新状态[依据外部联系人]");
                update.setUpdateTime(new Date());
                update.setId(library.getId());
                updates.add(update);
                Long staffId=phoneVOMap.get(library.getPhone()).getStaffId();
                CpPhoneTaskUser cpPhoneTaskUser=cpPhoneTaskUserMapper.selectSuccessByPhoneUserId(library.getId(),staffId);
                if(Objects.nonNull(cpPhoneTaskUser)){
                    cpPhoneTaskUser.setStatus(1);//任务记录添加成功
                    cpPhoneTaskUser.setUpdateBy("定时任务更新状态[依据外部联系人]");
                    cpPhoneTaskUser.setUpdateTime(new Date());
                    taskUsers.add(cpPhoneTaskUser);
                }
            }
        }
        if(CollUtil.isNotEmpty(updates)){
            this.updateBatchById(updates);
            log.info("刷新手机号是否在企微外部联系人添加成功，更新行数：{}",updates.size());
        }
        if(CollUtil.isNotEmpty(taskUsers)){
            phoneTaskUserService.updateBatchById(taskUsers);
            log.info("刷新手机号是否在企微外部联系人添加成功同时更新任务记录行成功，更新行数：{}",taskUsers.size());
        }
        log.info("刷新手机号是否在企微外部联系人添加成功 执行结束，耗时:{}s",System.currentTimeMillis() - startTime);
    }

}
