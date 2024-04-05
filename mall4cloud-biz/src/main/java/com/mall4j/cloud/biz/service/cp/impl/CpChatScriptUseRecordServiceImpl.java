package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.mapper.cp.CpChatScriptUseRecordMapper;
import com.mall4j.cloud.biz.model.cp.CpChatScriptUseRecord;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.service.cp.CpChatScriptUseRecordService;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 话术 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 10:25:19
 */
@Service
public class CpChatScriptUseRecordServiceImpl implements CpChatScriptUseRecordService {

    @Autowired
    private CpChatScriptUseRecordMapper cpChatScriptUseRecordMapper;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Override
    public PageVO<CpChatScriptUseRecord> page(PageDTO pageDTO) {
//        return PageUtil.doPage(pageDTO, () -> cpChatScriptUseRecordMapper.list());s
        return null;
    }

    @Override
    public CpChatScriptUseRecord getById(Long id) {
        return cpChatScriptUseRecordMapper.getById(id);
    }

    @Override
    public void save(CpChatScriptUseRecord cpChatScriptUseRecord) {
        cpChatScriptUseRecordMapper.save(cpChatScriptUseRecord);
    }

    @Override
    public void update(CpChatScriptUseRecord cpChatScriptUseRecord) {
        cpChatScriptUseRecordMapper.update(cpChatScriptUseRecord);
    }

    @Override
    public void deleteById(Long id) {
        cpChatScriptUseRecordMapper.deleteById(id);
    }

    @Override
    public PageVO<CpChatScriptUseRecord> usePage(PageDTO pageDTO, MaterialUseRecordPageDTO request) {
        PageVO<CpChatScriptUseRecord> pageVO=PageUtil.doPage(pageDTO, () -> cpChatScriptUseRecordMapper.list(request));
        if(CollUtil.isNotEmpty(pageVO.getList())){
            StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
            staffQueryDTO.setStaffIdList(pageVO.getList().stream().map(item->item.getStaffId()).collect(Collectors.toList()));
            ServerResponseEntity<List<StaffVO>>  responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            Map<Long,StaffVO> staffMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);
            for (CpChatScriptUseRecord record : pageVO.getList()) {
                record.setStaffName(staffMap.get(record.getStaffId()).getStaffName());
            }
        }
        return pageVO;
    }

    @Override
    public List<CpChatScriptUseRecord> soldUsePage( MaterialUseRecordPageDTO request) {
        List<CpChatScriptUseRecord> records=cpChatScriptUseRecordMapper.list(request);
        if(CollUtil.isNotEmpty(records)){
            StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
            staffQueryDTO.setStaffIdList(records.stream().map(item->item.getStaffId()).collect(Collectors.toList()));
            ServerResponseEntity<List<StaffVO>>  responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            Map<Long,StaffVO> staffMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);
            for (CpChatScriptUseRecord record : records) {
                record.setStaffName(staffMap.get(record.getStaffId()).getStaffName());
            }
        }
        return records;
    }

    @Override
    public List<MaterialBrowseRecordByDayVO> useStatistics(MaterialUseRecordPageDTO request) {
        Date startTime=DateUtil.parse(request.getCreateTimeStart(),"yyyy-MM-dd HH:mm:ss");
        Date endTime=DateUtil.parse(request.getCreateTimeEnd(),"yyyy-MM-dd HH:mm:ss");
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,endTime, DateField.DAY_OF_YEAR);//按天
        List<MaterialBrowseRecordByDayVO> list=new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            String time=DateUtil.format(dateTime,"yyyy-MM-dd");
            MaterialBrowseRecordByDayVO dayVO=new MaterialBrowseRecordByDayVO();
            dayVO.setNum("0");
            dayVO.setDay1(time);
            list.add(dayVO);
        }
        List<MaterialBrowseRecordByDayVO> records=cpChatScriptUseRecordMapper.useStatistics(request);
        if(CollUtil.isNotEmpty(records)){
            Map<String,MaterialBrowseRecordByDayVO> dayVOMap= LambdaUtils.toMap(records,MaterialBrowseRecordByDayVO::getDay1);
            for (MaterialBrowseRecordByDayVO dayVO : list) {
                if(dayVOMap.containsKey(dayVO.getDay1())){
                    dayVO.setNum(dayVOMap.get(dayVO.getDay1()).getNum());
                }
            }
        }
        return list;
    }

    @Override
    public void use(Long id, Long staffId) {
        CpChatScriptUseRecord useRecord = new CpChatScriptUseRecord();
        useRecord.setScriptId(id);
        useRecord.setStaffId(staffId);
        useRecord.setCreateTime(new Date());
        useRecord.setUpdateTime(new Date());
        this.save(useRecord);
    }
}
