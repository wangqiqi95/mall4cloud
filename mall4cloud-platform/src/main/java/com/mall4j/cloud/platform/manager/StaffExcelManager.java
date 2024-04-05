package com.mall4j.cloud.platform.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeRefPDTO;
import com.mall4j.cloud.api.biz.feign.StaffCodeFeignClient;
//import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.IpHelper;
import com.mall4j.cloud.platform.dto.ImportStaffWeChatDto;
import com.mall4j.cloud.platform.dto.ImportStaffsDto;
import com.mall4j.cloud.platform.listener.StaffExcelListener;
import com.mall4j.cloud.platform.listener.StaffWeChatExcelListener;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.TzStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2022年3月8日, 0008 15:10
 * @Created by eury
 */
@Slf4j
@Component
public class StaffExcelManager {

    @Autowired
    private StaffService staffService;

    @Autowired
    private TzStoreService tzStoreService;

//    @Autowired
//    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private StaffCodeFeignClient staffCodeFeignClient;


    public String importStaffExcel(MultipartFile file) {
        try {
            Map<String, List<String>> errorMap = new HashMap<>(16);
            StaffExcelListener staffExcelListener = new StaffExcelListener(this, errorMap);
            EasyExcel.read(file.getInputStream(), ImportStaffsDto.class, staffExcelListener).sheet().doRead();
            String info = getUserExportInfo(errorMap);
            return info;
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
    }

    public String importStaffWeChat(MultipartFile file) {
        try {
            Map<String, List<String>> errorMap = new HashMap<>(16);
            StaffWeChatExcelListener staffWeChatExcelListener = new StaffWeChatExcelListener(this, errorMap);
            EasyExcel.read(file.getInputStream(), ImportStaffWeChatDto.class, staffWeChatExcelListener).sheet().doRead();
            return getUserExportInfo(errorMap);
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
    }

    /**
     * 处理导入的需要响应的信息
     *
     * @param errorMap 响应信息的集合
     * @return 响应信息
     */
    private String getUserExportInfo(Map<String, List<String>> errorMap) {
        StringBuffer info = new StringBuffer();
        List<String> importTotal = errorMap.get(StaffExcelListener.IMPORT_ROWS);
        BigDecimal total = new BigDecimal("0");
        if (CollUtil.isNotEmpty(importTotal)) {
            for (int i = 0; i < importTotal.size(); i++) {
                String item = importTotal.get(i);
                if (StrUtil.isNotBlank(item)) {
                    total = total.add(new BigDecimal(item));
                }
            }
        }
        info.append("共有： " + total.intValue() + "条数据成功导入" + StrUtil.LF);
        // 错误信息
        List<String> errorRows = errorMap.get(StaffExcelListener.ERROR_ROWS);
        if (CollUtil.isNotEmpty(errorRows)) {
            info.append("员工信息错误行数： " + errorRows.toArray() + StrUtil.LF);
        }
        List<String> errors = errorMap.get(StaffExcelListener.OTHER);
        if (CollUtil.isNotEmpty(errors)) {
            for (String error : errors) {
                info.append(error);
            }
        }
        return info.toString();
    }

    public void importExcel(List<ImportStaffsDto> list, Map<String, List<String>> errorMap) {
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("解析到0条数据");
        }
        int size = list.size();

        //员工列表
        List<Staff> staffList=new ArrayList<>();

        // 集合去重复
//        list = list.stream().collect(
//                Collectors.collectingAndThen(
//                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ImportStaffsDto::getMobile))), ArrayList::new));

        list = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ImportStaffsDto::getStaffNo))), ArrayList::new));

        // 第几行数据有误 的集合
        List<Integer> errorRows = new ArrayList<>();
        this.loadErrorData(list, errorMap, size);
        int row = 1;
        // 处理数据
        for (ImportStaffsDto staffsDto : list) {
            row++;
            // 手机号必须是11位 且不能为空
            String phone = staffsDto.getMobile();
            if (StrUtil.isBlank(phone) || phone.length() != 11) {
                errorRows.add(row);
                continue;
            }
            if (StrUtil.isBlank(staffsDto.getStaffName())) {
                errorRows.add(row);
                continue;
            }
            if (StrUtil.isBlank(staffsDto.getStaffNo())) {
                errorRows.add(row);
                continue;
            }

//            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.SCRM_STAFF_USER);
//            if (!segmentIdResponse.isSuccess()) {
//                throw new LuckException(segmentIdResponse.getMsg());
//            }
            Staff staff=new Staff();

            //员工编码
//            staff.setStaffCode();

//            staff.setId(segmentIdResponse.getData());
            staff.setStaffNo(staffsDto.getStaffNo());//员工工号
            staff.setStaffCode(staffsDto.getStaffNo());//员工编号
            staff.setStaffName(staffsDto.getStaffName());
            staff.setMobile(staffsDto.getMobile());

            if(StrUtil.isNotBlank(staffsDto.getStatus())){
                staff.setStatus(Integer.parseInt(staffsDto.getStatus()));
            }
            if(StrUtil.isNotBlank(staffsDto.getRoleType())){
                staff.setRoleType(Integer.parseInt(staffsDto.getRoleType()));
            }
            if(StrUtil.isNotBlank(staffsDto.getPosition())) staff.setPosition(staffsDto.getPosition());
            if(StrUtil.isNotBlank(staffsDto.getEmail())) staff.setEmail(staffsDto.getEmail());

            //门店信息
            if(StrUtil.isNotBlank(staffsDto.getStoreName())){
                log.info("导入门店编号：{} "+staffsDto.getStoreName());
                TzStore tzStore = tzStoreService.getByStoreCode(null,staffsDto.getStoreName().trim());
                log.info("查询到门店：名称【{}】  编号【{}】 ",tzStore.getStationName(),tzStore.getStoreCode());
                if (tzStore!=null) {
                    staff.setStoreId(tzStore.getStoreId());
                }

//                String[] storeInfo=staffsDto.getStoreName().split("\\|");
//                if(storeInfo.length>1){
//                    TzStore tzStore = tzStoreService.getByStoreCode(storeInfo[0],storeInfo[1]);
//                    if (Objects.nonNull(tzStore)) {
//                        staff.setStoreId(tzStore.getStoreId());
//                    }
//                }
            }
            staffList.add(staff);
        }

        // 批量导入员工
        if(CollectionUtil.isNotEmpty(staffList)){
            try {
                staffService.saveBatch(staffList);
                String rowSuccess = ""+staffList.size();
                errorMap.get(StaffExcelListener.IMPORT_ROWS).add(rowSuccess);
            }catch (Exception e){
                log.info(""+e.getMessage());
            }
        }
        if (CollUtil.isNotEmpty(errorRows)) {
            List<String> collect = errorRows.stream().map(item -> item + "").collect(Collectors.toList());
            errorMap.get(StaffExcelListener.ERROR_ROWS).addAll(collect);
        }
    }

    /**
     * 验证员工工号唯一
     * @param list
     * @param errorMap
     * @param size
     */
    private void loadErrorData(List<ImportStaffsDto> list, Map<String, List<String>> errorMap , int size) {
        List<String> errorStaffs = new ArrayList<>();
        List<String> staffs = new ArrayList<>();
        for (ImportStaffsDto userExcelDTO : list) {
            staffs.add(userExcelDTO.getStaffNo());
        }
        List<Staff> staffList = staffService.getStaffListByStaffNos(staffs);
        staffs = staffList.stream().map(Staff::getStaffNo).collect(Collectors.toList());
        Iterator<ImportStaffsDto> iterator = list.iterator();
        while (iterator.hasNext()) {
            ImportStaffsDto importStaffsDto = iterator.next();
            boolean staffNo = staffs.contains(importStaffsDto.getStaffNo());

            if (!staffNo) {
                staffs.add(importStaffsDto.getStaffNo());
                continue;
            }
            if (staffNo) {
                errorStaffs.add(importStaffsDto.getStaffNo());
            } else {
                staffs.add(importStaffsDto.getStaffNo());
            }
            iterator.remove();
        }
        List<String> errorList = errorMap.get(StaffExcelListener.OTHER);
        if (CollUtil.isNotEmpty(errorStaffs)) {
            errorList.add("员工工号：" + errorStaffs.toString() + "已存在)");
        }
    }


    /**
     * 过滤用户手机号已经存在的用户
     * @param list 用户数据列表
     * @param size 数据数量
     */
//    private void loadErrorData(List<ImportStaffsDto> list, Map<String, List<String>> errorMap , int size) {
//        List<String> errorPhones = new ArrayList<>();
//        List<String> mobiles = new ArrayList<>();
//        for (ImportStaffsDto userExcelDTO : list) {
//            mobiles.add(userExcelDTO.getMobile());
//        }
//        List<Staff> staffList = staffService.getStaffListByMobiles(mobiles);
//        mobiles = staffList.stream().map(Staff::getMobile).collect(Collectors.toList());
//        Iterator<ImportStaffsDto> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            ImportStaffsDto importStaffsDto = iterator.next();
//            boolean phone = mobiles.contains(importStaffsDto.getMobile());
//
//            if (!phone) {
//                mobiles.add(importStaffsDto.getMobile());
//                continue;
//            }
//            if (phone) {
//                errorPhones.add(importStaffsDto.getMobile());
//            } else {
//                mobiles.add(importStaffsDto.getMobile());
//            }
//            iterator.remove();
//        }
//        List<String> errorList = errorMap.get(StaffExcelListener.OTHER);
//        if (CollUtil.isNotEmpty(errorPhones)) {
//            errorList.add("手机号码：" + errorPhones.toString() + "已存在)");
//        }
//    }

    /**
     * 导入员工微信
     * @param list
     * @param errorMap
     */
    public void importStaffWeChatExcel(List<ImportStaffWeChatDto> list, Map<String, List<String>> errorMap) {
        if(CollectionUtil.isNotEmpty(list)){
            staffService.updateBatchWeChatNoByStaffNo(list);
            errorMap.get(StaffExcelListener.IMPORT_ROWS).add(String.valueOf(list.size()));
        }
    }

}
