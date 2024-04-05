package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialUseRecordMapper;
import com.mall4j.cloud.biz.mapper.cp.MaterialMapper;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.service.cp.CpMaterialUseRecordService;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 素材 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 10:03:14
 */
@Service
public class CpMaterialUseRecordServiceImpl implements CpMaterialUseRecordService {

    @Autowired
    private CpMaterialUseRecordMapper cpMaterialUseRecordMapper;
    @Autowired
    StaffFeignClient staffFeignClient;
    @Autowired
    MaterialMapper materialMapper;

    @Override
    public PageVO<CpMaterialUseRecord> page(PageDTO pageDTO, MaterialUseRecordPageDTO request) {
        return PageUtil.doPage(pageDTO, () -> cpMaterialUseRecordMapper.list(request));
    }

    @Override
    public List<CpMaterialUseRecord> exportPage(MaterialUseRecordPageDTO request) {
        return cpMaterialUseRecordMapper.list(request);
    }

    @Override
    public CpMaterialUseRecord getById(Long id) {
        return cpMaterialUseRecordMapper.getById(id);
    }

    @Override
    public void save(CpMaterialUseRecord cpMaterialUseRecord) {
        cpMaterialUseRecordMapper.save(cpMaterialUseRecord);
    }

    @Override
    public void update(CpMaterialUseRecord cpMaterialUseRecord) {
        cpMaterialUseRecordMapper.update(cpMaterialUseRecord);
    }

    @Override
    public void deleteById(Long id) {
        cpMaterialUseRecordMapper.deleteById(id);
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
        List<MaterialBrowseRecordByDayVO> records=cpMaterialUseRecordMapper.useStatistics(request);
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
    public void use(Long id) {
//        cpMaterialUseRecordMapper.use();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void use(Long matId, Long staffId) {
        ServerResponseEntity<StaffVO> staffVOServerResponse = staffFeignClient.getStaffById(staffId);
        if(staffVOServerResponse==null || staffVOServerResponse.isFail()){
            Assert.faild("查询导购信息失败，请稍后再试。");
        }
        StaffVO staffVO = staffVOServerResponse.getData();


        materialMapper.use(matId);

        CpMaterialUseRecord cpMaterialUseRecord = new CpMaterialUseRecord();
        cpMaterialUseRecord.setStaffId(staffVO.getId());
        cpMaterialUseRecord.setStaffName(staffVO.getStaffName());
        cpMaterialUseRecord.setCreateTime(new Date());
        cpMaterialUseRecord.setUpdateTime(new Date());
        cpMaterialUseRecord.setMatId(matId);
        save(cpMaterialUseRecord);
    }
}
