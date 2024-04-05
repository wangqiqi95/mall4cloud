package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.biz.vo.MaterialBrowseRecordApiVO;
import com.mall4j.cloud.biz.dto.cp.MaterialBrowsePageDTO;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialBrowseRecordMapper;
import com.mall4j.cloud.biz.model.cp.CpMaterialBrowseRecord;
import com.mall4j.cloud.biz.service.cp.CpMaterialBrowseRecordService;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseStatisticsVO;
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
 * 素材 会员浏览记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
@Service
public class CpMaterialBrowseRecordServiceImpl implements CpMaterialBrowseRecordService {

    @Autowired
    private CpMaterialBrowseRecordMapper cpMaterialBrowseRecordMapper;

    @Override
    public PageVO<CpMaterialBrowseRecord> page(PageDTO pageDTO, MaterialBrowsePageDTO request) {
        return PageUtil.doPage(pageDTO, () -> cpMaterialBrowseRecordMapper.list(request));
    }

    @Override
    public List<MaterialBrowseRecordApiVO> listByUnionId(String unionId, String startTime, String endTime) {
        return cpMaterialBrowseRecordMapper.listByUnionId(unionId,startTime,endTime);
    }

    @Override
    public CpMaterialBrowseRecord getById(Long id) {
        return cpMaterialBrowseRecordMapper.getById(id);
    }

    @Override
    public void save(CpMaterialBrowseRecord cpMaterialBrowseRecord) {
        cpMaterialBrowseRecordMapper.save(cpMaterialBrowseRecord);
    }

    @Override
    public void update(CpMaterialBrowseRecord cpMaterialBrowseRecord) {
        cpMaterialBrowseRecordMapper.update(cpMaterialBrowseRecord);
    }

    @Override
    public void deleteById(Long id) {
        cpMaterialBrowseRecordMapper.deleteById(id);
    }

    @Override
    public MaterialBrowseStatisticsVO materialBrowseStatistics(MaterialBrowsePageDTO request) {
        MaterialBrowseStatisticsVO statisticsVO = new MaterialBrowseStatisticsVO();
        //累计浏览量  totalBrowseCount
        Integer totalBrowseCount = cpMaterialBrowseRecordMapper.totalBrowseCount(request.getId(),request.getCreateTimeStart(),request.getCreateTimeEnd());
        statisticsVO.setTotalBrowseCount(totalBrowseCount);
        //访客数     totalVisitorCount
        Integer totalVisitorCount = cpMaterialBrowseRecordMapper.totalVisitorCount(request.getId(),request.getCreateTimeStart(),request.getCreateTimeEnd());
        statisticsVO.setTotalVisitorCount(totalVisitorCount);


        //今日浏览量  todayBrowseCount
        Integer todayBrowseCount = cpMaterialBrowseRecordMapper.totalBrowseCount(request.getId(),DateUtil.today(),DateUtil.tomorrow().toDateStr());
        statisticsVO.setTodayBrowseCount(todayBrowseCount);
        //今日访客数  todayVisitorCount
        Integer todayVisitorCount = cpMaterialBrowseRecordMapper.totalVisitorCount(request.getId(),DateUtil.today(),DateUtil.tomorrow().toDateStr());
        statisticsVO.setTodayVisitorCount(todayVisitorCount);

        Date startTime=DateUtil.parse(request.getCreateTimeStart(),"yyyy-MM-dd HH:mm:ss");
        Date endTime=DateUtil.parse(request.getCreateTimeEnd(),"yyyy-MM-dd HH:mm:ss");
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,endTime, DateField.DAY_OF_YEAR);//按天
        List<MaterialBrowseRecordByDayVO> browseList=new ArrayList<>();
        List<MaterialBrowseRecordByDayVO> visitorList=new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            String time=DateUtil.format(dateTime,"yyyy-MM-dd");
            MaterialBrowseRecordByDayVO dayVO=new MaterialBrowseRecordByDayVO();
            dayVO.setNum("0");
            dayVO.setDay1(time);
            browseList.add(dayVO);

            MaterialBrowseRecordByDayVO dayVO1=new MaterialBrowseRecordByDayVO();
            dayVO1.setNum("0");
            dayVO1.setDay1(time);
            visitorList.add(dayVO1);
        }
        //
        List<MaterialBrowseRecordByDayVO> browseListData =  cpMaterialBrowseRecordMapper.browseCountByDay(request.getId(),request.getCreateTimeStart(),request.getCreateTimeEnd());
        if(CollUtil.isNotEmpty(browseListData)){
            Map<String,MaterialBrowseRecordByDayVO> dayVOMap= LambdaUtils.toMap(browseListData,MaterialBrowseRecordByDayVO::getDay1);
            for (MaterialBrowseRecordByDayVO dayVO : browseList) {
                if(dayVOMap.containsKey(dayVO.getDay1())){
                    dayVO.setNum(dayVOMap.get(dayVO.getDay1()).getNum());
                }
            }
        }
        statisticsVO.setBrowseRecordByDayVOS(browseList);
        //
        List<MaterialBrowseRecordByDayVO> visitorListData =  cpMaterialBrowseRecordMapper.visitorCountByDay(request.getId(),request.getCreateTimeStart(),request.getCreateTimeEnd());
        if(CollUtil.isNotEmpty(visitorListData)){
            Map<String,MaterialBrowseRecordByDayVO> dayVOMap= LambdaUtils.toMap(visitorListData,MaterialBrowseRecordByDayVO::getDay1);
            for (MaterialBrowseRecordByDayVO dayVO : visitorList) {
                if(dayVOMap.containsKey(dayVO.getDay1())){
                    dayVO.setNum(dayVOMap.get(dayVO.getDay1()).getNum());
                }
            }
        }
        statisticsVO.setVisitorRecordByDayVOS(visitorList);
        return statisticsVO;
    }
}
