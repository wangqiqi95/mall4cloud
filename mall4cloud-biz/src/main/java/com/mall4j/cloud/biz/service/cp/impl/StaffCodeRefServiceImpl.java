package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.cp.CpStaffCodeTimeDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodePlusDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeRefDTO;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeRefMapper;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeTimeMapper;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeTime;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import com.mall4j.cloud.biz.service.cp.CpStaffCodeTimeService;
import com.mall4j.cloud.biz.service.cp.StaffCodeRefService;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@Service
public class StaffCodeRefServiceImpl extends ServiceImpl<CpStaffCodeRefMapper, CpStaffCodeRef> implements StaffCodeRefService {
    @Autowired
    private CpStaffCodeRefMapper staffCodeRefMapper;
    @Autowired
    private  MapperFacade mapperFacade;
    @Autowired
    private CpStaffCodeTimeService cpStaffCodeTimeService;
    @Autowired
    private CpStaffCodeTimeMapper staffCodeTimeMapper;

    @Override
    public void deleteByCodeId(Long codeId) {
        staffCodeRefMapper.deleteByCodeId(codeId);
    }

    @Override
    public List<CpStaffCodeRef> listByCodeId(Long codeId) {
        return staffCodeRefMapper.listByCodeId(codeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCodeRef(Long sourceId, Integer sourceFrom, StaffCodePlusDTO request) {

        //先删除
        staffCodeRefMapper.deleteByCodeId(sourceId);
        staffCodeTimeMapper.deleteBySourceId(sourceId);

        //接待人员
        List<CpStaffCodeTimeDTO> codeTimeDTOS=request.getTimeDTOS();
        //保存时间段信息
        for (CpStaffCodeTimeDTO codeTimeDTO : codeTimeDTOS) {
            CpStaffCodeTime cpStaffCodeTimeDO=mapperFacade.map(codeTimeDTO, CpStaffCodeTime.class);
            cpStaffCodeTimeDO.setCreateTime(new Date());
            cpStaffCodeTimeDO.setCreateBy(AuthUserContext.get().getUsername());
            cpStaffCodeTimeDO.setSourceId(sourceId);
            cpStaffCodeTimeDO.setSourceFrom(sourceFrom);
            cpStaffCodeTimeService.save(cpStaffCodeTimeDO);

            //保存人员信息
            List<CpStaffCodeRef> staffCodeRefs=mapperFacade.mapAsList(codeTimeDTO.getStaffList(), CpStaffCodeRef.class);
            for (CpStaffCodeRef staffCodeRef : staffCodeRefs) {
                staffCodeRef.setType(0);
                staffCodeRef.setCodeId(sourceId);
                staffCodeRef.setCreateTime(new Date());
                staffCodeRef.setCodeTimeId(cpStaffCodeTimeDO.getId());
            }
            this.saveBatch(staffCodeRefs);
        }

        //备用人员
        List<CpStaffCodeRef> standbyStaffs=mapperFacade.mapAsList(request.getStandbyStaffs(), CpStaffCodeRef.class);
        standbyStaffs.stream().forEach(item->{
            item.setType(1);
            item.setCodeId(sourceId);
            item.setCreateTime(new Date());
        });
        this.saveBatch(standbyStaffs);
    }

    /**
     * 如果开启分时段接待员，需要取出与当前时间匹配的时间段接待员，否则是备用人员
     * @param request
     * @return
     */
    @Override
    public List<CpStaffCodeRef> getCodeStaffRefs(StaffCodePlusDTO request) {
        Map<String, CpStaffCodeRef> staffsMap=new HashMap<>();
        if(request.getReceptionType()==0){ //全天接待：备用人员无效
            for (StaffCodeRefDTO staffCodeRefDTO : request.getTimeDTOS().get(0).getStaffList()) {
                if(!staffsMap.containsKey(staffCodeRefDTO.getUserId())){
                    CpStaffCodeRef codeRef=mapperFacade.map(staffCodeRefDTO, CpStaffCodeRef.class);
                    staffsMap.put(staffCodeRefDTO.getUserId(),codeRef);
                }
            }
            return new ArrayList<>(staffsMap.values());
        }
        if(request.getReceptionType()==1){ //分时段接待员
            /**
             * 周日-1/周1-2/周2-3/周3-4/周4-5/周5-6/周6-7
             */
            Date now=new Date();
            long nowTime= DateUtil.parse(DateUtil.format(now,"HH:mm"),"HH:mm").getTime();
            int dayWeek=DateUtil.dayOfWeek(now);//当前周几
            CpStaffCodeTimeDTO codeTimeDTO=null;
            for (CpStaffCodeTimeDTO timeDTO : request.getTimeDTOS()) {
                //匹配周条件
                Map<String,String> timesMap= LambdaUtils.toMap(Arrays.asList(timeDTO.getTimeCycle().split(",")), item->item);
                if(!timesMap.containsKey(String.valueOf(dayWeek))){
                    continue;
                }
                //匹配时间段条件
                Long startTime=DateUtil.parse(timeDTO.getTimeStart(),"HH:mm").getTime();
                Long endTime=DateUtil.parse(timeDTO.getTimeEnd(),"HH:mm").getTime();
                if(nowTime<startTime || nowTime>endTime){//时间未到/时间超出
                    continue;
                }
                codeTimeDTO=timeDTO;
                log.info("匹配到分时段接待员：{}", JSON.toJSONString(codeTimeDTO));
                break;
            }
            if(Objects.nonNull(codeTimeDTO)){
                for (StaffCodeRefDTO staffCodeRefDTO : codeTimeDTO.getStaffList()) {
                    if(!staffsMap.containsKey(staffCodeRefDTO.getUserId())){
                        CpStaffCodeRef codeRef=mapperFacade.map(staffCodeRefDTO, CpStaffCodeRef.class);
                        staffsMap.put(staffCodeRefDTO.getUserId(),codeRef);
                    }
                }
            }
        }

        if(CollUtil.isEmpty(staffsMap)){//未匹配到分时段接待员，默认使用备用人员
            //备用人员
            for (StaffCodeRefDTO staffCodeRefDTO : request.getStandbyStaffs()) {
                if(!staffsMap.containsKey(staffCodeRefDTO.getUserId())){
                    CpStaffCodeRef codeRef=mapperFacade.map(staffCodeRefDTO, CpStaffCodeRef.class);
                    staffsMap.put(staffCodeRefDTO.getUserId(),codeRef);
                }
            }
        }
        return new ArrayList<>(staffsMap.values());
    }

    public static void main(String[] s ){
        StaffCodeRefServiceImpl staffCodeRefService=new StaffCodeRefServiceImpl();

        String json="{\"id\":null,\"codeName\":\"test\",\"groupId\":9,\"remarks\":\"test\",\"receptionType\":0,\"staffList\":[{\"staffId\":77,\"staffName\":\"唐亮\",\"userId\":\"YeLuoFengHang\"},{\"staffId\":36,\"staffName\":\"周洪捐\",\"userId\":\"HaiXiang\"}],\"standbyStaffs\":[{\"staffId\":78,\"staffName\":\"周佳春\",\"userId\":\"18217507893\"}],\"authType\":0,\"autoRemarkState\":1,\"autoRemark\":\"aa\",\"codeStyle\":\"/2023/12/21/6585481a5f6a429686c6dd259fa7190c\",\"welcomeType\":0,\"welcomeTimeState\":0,\"timeDTOS\":[{\"staffList\":[{\"staffId\":77,\"staffName\":\"唐亮\",\"userId\":\"YeLuoFengHang\"},{\"staffId\":36,\"staffName\":\"周洪捐\",\"userId\":\"HaiXiang\"}]}],\"attachMents\":[{\"attachMents\":[{\"msgType\":\"image\",\"materialId\":\"\",\"localUrl\":\"/2023/12/21/6585481a5f6a429686c6dd259fa7190c\",\"picUrl\":\"https://wework.qpic.cn/wwpic3az/452624_tKvLDYk9S_Sfysx_1705051292/0\"}],\"sourceFrom\":1}],\"tags\":\"[]\",\"slogan\":\"${客户昵称}${员工姓名}\",\"codeType\":0}";


        StaffCodePlusDTO staffCodeDTO= JSONObject.parseObject(json, StaffCodePlusDTO.class);
        List<CpStaffCodeRef>  cpStaffCodeRefs=staffCodeRefService.getCodeStaffRefs(staffCodeDTO);
        System.out.println(JSON.toJSONString(cpStaffCodeRefs));
    }
}
