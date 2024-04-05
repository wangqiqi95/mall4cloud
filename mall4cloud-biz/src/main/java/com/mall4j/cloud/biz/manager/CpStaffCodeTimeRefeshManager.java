package com.mall4j.cloud.biz.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.CpStaffCodeTimeDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeRefDTO;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeRefMapper;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeTimeMapper;
import com.mall4j.cloud.biz.model.cp.CpStaffCodePlus;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import com.mall4j.cloud.biz.service.cp.StaffCodeRefService;
import com.mall4j.cloud.biz.service.cp.StaffCodePlusService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayInfo;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 渠道活码分时段接待人员刷新
 *
 *  企微接口文档
 * https://developer.work.weixin.qq.com/document/path/92228
 *
 * 处理逻辑：
 * 定时任务更新已配置联系我方式的user字段吗
 * 即当前时间段接待员工为：HaiXiang，
 * 那通过更新配置接口更新此user字段为：HaiXiang
 * 分时段未匹配到就更新为备用人员
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CpStaffCodeTimeRefeshManager {

    private final StaffCodePlusService staffCodeService;
    private final StaffCodeRefService staffCodeRefService;
    private final CpStaffCodeTimeMapper staffCodeTimeMapper;
    private final CpStaffCodeRefMapper cpStaffCodeRefMapper;
    private final StaffFeignClient staffFeignClient;

    public void refeshTimeStaff(){
        /**
         * 1.获取分时段接待的渠道活码数据、
         * 2.分时段数据【cp_staff_code、cp_staff_code_time】
         * 2.匹配分时段逻辑
         * 3.未匹配到分时段逻辑处理未备用人员
         * 4.获取分时段接待人员数据、备用人员数据【cp_staff_code_ref type=1】
         * 5.执行更新企微配置信息【执行企微更新配置接口】
         * 6.更新当前接待人员信息接待状态【cp_staff_code_ref】
         */

        //1.获取分时段接待的渠道活码数据
        LambdaQueryWrapper<CpStaffCodePlus> lambdaQueryWrapper=new LambdaQueryWrapper<CpStaffCodePlus>();
        lambdaQueryWrapper.eq(CpStaffCodePlus::getFlag,0);
        lambdaQueryWrapper.eq(CpStaffCodePlus::getReceptionType,1);
        List<CpStaffCodePlus> codeList=staffCodeService.list(lambdaQueryWrapper);
        log.info("渠道活码分时段接待人员刷新,需要处理的渠道数据：{}",codeList.size());
        if(CollUtil.isEmpty(codeList)){
            return;
        }
        for (CpStaffCodePlus staffCode : codeList) {
            //2.获取分时段数据和人员数据
            List<CpStaffCodeTimeDTO>  codeTimes=staffCodeTimeMapper.selectStaffTimesBySourceId(staffCode.getId());
            log.info("渠道活码分时段接待人员刷新,渠道id:{} 需要处理的分时段数据：{}",staffCode.getId(),codeList.size());
            if(CollUtil.isEmpty(codeTimes)){
                continue;
            }
            //匹配分时段逻辑
            CpStaffCodeTimeDTO timeStaff=getTimeStaff(staffCode,codeTimes);
            List<String> userId= new ArrayList<>();
            List<Long> relStaffId=new ArrayList<>();
            boolean standbyStaffBy=false;
            if(Objects.nonNull(timeStaff) && CollUtil.isNotEmpty(timeStaff.getStaffList())){
                //处理分时段接待人员
                StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
                staffQueryDTO.setIsDelete(0);
                staffQueryDTO.setStatus(0);
                staffQueryDTO.setStaffIdList(timeStaff.getStaffList().stream().map(item->item.getStaffId()).collect(Collectors.toList()));
                ServerResponseEntity<List<StaffVO>>  staffResponseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
                if(staffResponseEntity.isFail() || CollUtil.isEmpty(staffResponseEntity.getData())){
                    standbyStaffBy=true;
                }
                Map<Long,StaffVO> staffVOMap=LambdaUtils.toMap(staffResponseEntity.getData(),StaffVO::getId);
                //匹配员工
                for (StaffCodeRefDTO staffCodeRefDTO : timeStaff.getStaffList()) {
                    if(staffVOMap.containsKey(staffCodeRefDTO.getStaffId())){
                        userId.add(staffCodeRefDTO.getUserId());
                        relStaffId.add(staffCodeRefDTO.getRelId());
                    }
                }
                if(CollUtil.isEmpty(userId)){
                    standbyStaffBy=true;
                }
//                userId=timeStaff.getStaffList().stream().map(item->item.getUserId()).collect(Collectors.toList());
//                relStaffId=timeStaff.getStaffList().stream().map(item->item.getRelId()).collect(Collectors.toList());
            }else{
                standbyStaffBy=true;
            }
            if(standbyStaffBy){
                //处理备用人员
                List<CpStaffCodeRef> standbyStaffs = cpStaffCodeRefMapper.listByCode(staffCode.getId(),1);
                if(CollUtil.isNotEmpty(standbyStaffs)){
                    userId=standbyStaffs.stream().map(item->item.getUserId()).collect(Collectors.toList());
                    relStaffId=standbyStaffs.stream().map(item->item.getId()).collect(Collectors.toList());
                }
            }
            log.info("渠道活码分时段接待人员刷新，需要执行更新的-> 渠道id:{} config_id:{} 接待员: {}", staffCode.getId(),staffCode.getConfigId(),JSON.toJSONString(userId));
            if(CollUtil.isEmpty(userId) && userId.size()>0){
                continue;
            }
            try {
                WxCpContactWayInfo.ContactWay contactWay =  new WxCpContactWayInfo.ContactWay();
                contactWay.setUsers(userId);
                WxCpContactWayInfo cpContactWayInfo =  new WxCpContactWayInfo();
                cpContactWayInfo.setContactWay(contactWay);
                cpContactWayInfo.getContactWay().setConfigId(staffCode.getConfigId());
                WxCpBaseResp resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().updateContactWay(cpContactWayInfo);
                log.info("渠道活码分时段接待人员刷新->渠道id:{} config_id:{} 执行结果：{}",staffCode.getId(),staffCode.getConfigId(),resp.toJson());
                if (resp.success()) {
                    List<CpStaffCodeRef> cpStaffCodeRefs=new ArrayList<>();
                    for (Long aLong : relStaffId) {
                        CpStaffCodeRef staffCodeRef=new CpStaffCodeRef();
                        staffCodeRef.setId(aLong);
                        staffCodeRef.setReceptionStatus(1);
                        staffCodeRef.setUpdateTime(new Date());
                        cpStaffCodeRefs.add(staffCodeRef);
                    }
                    //先全部处理为为接待
                    staffCodeRefService.update(new LambdaUpdateWrapper<CpStaffCodeRef>().set(CpStaffCodeRef::getReceptionStatus,0).eq(CpStaffCodeRef::getCodeId,staffCode.getId()));
                    //处理匹配成功为接待中
                    staffCodeRefService.updateBatchById(cpStaffCodeRefs);
                }
            }catch (WxErrorException e){
                log.info("渠道活码分时段接待人员刷新执行失败-> errorCode:{} errorMessage:{}",e.getError().getErrorCode(),e.getMessage());
            }
        }

    }

    /**
     * 匹配分时段接待人员
     * @param codeTimes1
     * @return
     */
    private static CpStaffCodeTimeDTO getTimeStaff(CpStaffCodePlus staffCode, List<CpStaffCodeTimeDTO> codeTimes1){
        if(CollUtil.isEmpty(codeTimes1)){
            return null;
        }
        /**
         * 周日-1/周1-2/周2-3/周3-4/周4-5/周5-6/周6-7
         */
        Date now=new Date();
        long nowTime= DateUtil.parse(DateUtil.format(now,"HH:mm"),"HH:mm").getTime();
        int dayWeek=DateUtil.dayOfWeek(now);//当前周几
        CpStaffCodeTimeDTO codeTimeDTO=null;
        for (CpStaffCodeTimeDTO codeTime : codeTimes1) {
            //匹配周条件
            Map<String,String> timesMap= LambdaUtils.toMap(Arrays.asList(codeTime.getTimeCycle().split(",")), item->item);
            if(!timesMap.containsKey(String.valueOf(dayWeek))){
                continue;
            }
            //匹配时间段条件
            Long startTime=DateUtil.parse(codeTime.getTimeStart(),"HH:mm").getTime();
            Long endTime=DateUtil.parse(codeTime.getTimeEnd(),"HH:mm").getTime();
            log.info("匹配时间段条件---codeName:{} configId:{} nowTime:{} startTime:{} endTime:{}",staffCode.getCodeName(),staffCode.getConfigId(),nowTime,startTime,endTime);
            if(nowTime<startTime || nowTime>endTime){//时间未到/时间超出
                continue;
            }
            codeTimeDTO = codeTime;
            break;
        }
        log.info("匹配成功时间段条件---codeName:{} configId:{} nowTime:{} startTime:{} endTime:{}",staffCode.getCodeName(),staffCode.getConfigId(),JSON.toJSONString(codeTimeDTO));
        return codeTimeDTO;
    }

    public static void main(String[] s ){
        CpStaffCodeTimeDTO dto1=new CpStaffCodeTimeDTO();
        dto1.setTimeCycle("2,3,4,5");
        dto1.setTimeStart("20:00");
        dto1.setTimeEnd("20:30");

        CpStaffCodeTimeDTO dto2=new CpStaffCodeTimeDTO();
        dto2.setTimeCycle("2,3,4");
        dto2.setTimeStart("20:30");
        dto2.setTimeEnd("21:30");

        List<CpStaffCodeTimeDTO> codeTimes1=new ArrayList<>();
        codeTimes1.add(dto1);
        codeTimes1.add(dto2);

        CpStaffCodePlus staffCode=new CpStaffCodePlus();
        staffCode.setCodeName("test");
        staffCode.setConfigId("test configId");
        System.out.println(JSON.toJSONString(getTimeStaff(staffCode,codeTimes1)));

        Long startTime=DateUtil.parse("20:30","HH:mm").getTime();
        Long endTime=DateUtil.parse("21:00","HH:mm").getTime();
        long nowTime= DateUtil.parse("20:30","HH:mm").getTime();
        System.out.println("startTime:"+startTime);
        System.out.println("endTime:"+endTime);
        System.out.println("nowTime:"+nowTime);
        if(nowTime<startTime || nowTime>endTime){//时间未到/时间超出
            System.out.println("continue");
        }else{
            System.out.println("break");
        }
    }

}
