package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.mapper.cp.CpWelcomeUseRecordMapper;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.model.cp.CpWelcomeUseRecord;
import com.mall4j.cloud.biz.service.cp.CpWelcomeUseRecordService;
import com.mall4j.cloud.biz.vo.cp.CpMaterialUseRecordVO;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeUseRecordVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.util.LambdaUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 欢迎语 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
@Service
public class CpWelcomeUseRecordServiceImpl implements CpWelcomeUseRecordService {

    @Autowired
    private CpWelcomeUseRecordMapper cpWelcomeUseRecordMapper;

    @Override
    public PageVO<CpWelcomeUseRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpWelcomeUseRecordMapper.list());
    }

    @Override
    public CpWelcomeUseRecord getById(Long id) {
        return cpWelcomeUseRecordMapper.getById(id);
    }

    @Override
    public void save(CpWelcomeUseRecord cpWelcomeUseRecord) {
        cpWelcomeUseRecordMapper.save(cpWelcomeUseRecord);
    }

    @Override
    public void update(CpWelcomeUseRecord cpWelcomeUseRecord) {
        cpWelcomeUseRecordMapper.update(cpWelcomeUseRecord);
    }

    @Override
    public void deleteById(Long id) {
        cpWelcomeUseRecordMapper.deleteById(id);
    }

    @Override
    public PageVO<CpWelcomeUseRecordVO> page(PageDTO pageDTO, MaterialUseRecordPageDTO request) {
        return PageUtil.doPage(pageDTO, () -> cpWelcomeUseRecordMapper.page(request));
    }

    @Override
    public List<CpWelcomeUseRecordVO> soldUserRecord(MaterialUseRecordPageDTO request) {
        return cpWelcomeUseRecordMapper.page(request);
    }

    @Override
    public List<MaterialBrowseRecordByDayVO> useStatistics(MaterialUseRecordPageDTO request) {
        Date startTime= DateUtil.parse(request.getCreateTimeStart(),"yyyy-MM-dd HH:mm:ss");
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
        List<MaterialBrowseRecordByDayVO> records=cpWelcomeUseRecordMapper.useStatistics(request);
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
}
