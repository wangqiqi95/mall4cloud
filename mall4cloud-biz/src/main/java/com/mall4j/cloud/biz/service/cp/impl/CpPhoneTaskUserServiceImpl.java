package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.*;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.constant.ImportFromStatusEnum;
import com.mall4j.cloud.biz.constant.PhoneTaskUserStatusEnum;
import com.mall4j.cloud.biz.dto.cp.CpPhoneLibraryDTO;
import com.mall4j.cloud.biz.dto.cp.CpSelectPhoneTaskUserDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskRelDTO;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneLibraryMapper;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskMapper;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskStaffMapper;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskUserMapper;
import com.mall4j.cloud.biz.model.cp.CpPhoneLibrary;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskUser;
import com.mall4j.cloud.biz.service.cp.CpPhoneLibraryService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskUserService;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserExportVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskStaffVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 引流手机号任务关联客户
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Slf4j
@Service
public class CpPhoneTaskUserServiceImpl extends ServiceImpl<CpPhoneTaskUserMapper,CpPhoneTaskUser> implements CpPhoneTaskUserService {

    @Autowired
    private CpPhoneTaskUserMapper cpPhoneTaskUserMapper;
    @Autowired
    private CpPhoneTaskMapper cpPhoneTaskMapper;
    @Autowired
    private CpPhoneTaskStaffMapper taskRelMapper;
    @Autowired
    private CpPhoneLibraryMapper cpPhoneLibraryMapper;
    @Autowired
    private CpPhoneLibraryService cpPhoneLibraryService;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<CpPhoneTaskUserVO> page(PageDTO pageDTO, CpSelectPhoneTaskUserDTO dto) {
        PageVO<CpPhoneTaskUserVO> pageVO= PageUtil.doPage(pageDTO, () -> cpPhoneTaskUserMapper.list(dto));
        initDate(pageVO.getList());
        return pageVO;
    }

    @Override
    public PageVO<CpPhoneTaskUserVO> pageMobile(PageDTO pageDTO, CpSelectPhoneTaskUserDTO dto) {
        dto.setStaffs(Arrays.asList(AuthUserContext.get().getUserId()));
        PageVO<CpPhoneTaskUserVO> pageVO= PageUtil.doPage(pageDTO, () -> cpPhoneTaskUserMapper.list(dto));
        initDate(pageVO.getList());
        return pageVO;
    }

    @Override
    public List<CpPhoneTaskUserExportVO> exportList(CpSelectPhoneTaskUserDTO dto) {
        List<CpPhoneTaskUserVO> list=cpPhoneTaskUserMapper.list(dto);
        initDate(list);
        List<CpPhoneTaskUserExportVO> backs=mapperFacade.mapAsList(list,CpPhoneTaskUserExportVO.class);
        return backs;
    }

    private void initDate(List<CpPhoneTaskUserVO> list){
        //获取员工信息
        List<Long> staffIds=list.stream().map(item->item.getStaffId()).collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.getStaffByIds(staffIds);
        ServerResponseEntity.checkResponse(responseEntity);

        Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);

        for (CpPhoneTaskUserVO taskUserVO : list) {
            if(staffVOMap.containsKey(taskUserVO.getStaffId())){
                taskUserVO.setStaffName(staffVOMap.get(taskUserVO.getStaffId()).getStaffName());
            }
            //状态
            if(Objects.nonNull(taskUserVO.getStatus())){
                taskUserVO.setStatusName(PhoneTaskUserStatusEnum.instance(taskUserVO.getStatus()).desc());
            }
            //导入来源
            if(Objects.nonNull(taskUserVO.getImportFrom())){
                taskUserVO.setImportFromName(ImportFromStatusEnum.instance(taskUserVO.getImportFrom()).desc());
            }
        }
    }

    @Override
    public CpPhoneTaskUser getById(Long id) {
        return cpPhoneTaskUserMapper.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        cpPhoneTaskUserMapper.deleteById(id);
    }

    /**
     * TODO 任务员工分配客户
     * @param taskId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeTaskUser(Long taskId) {
        //1获取执行中的任务
        List<CpPhoneTask> tasks=cpPhoneTaskMapper.selectDistributeTask(taskId);
        if(CollUtil.isEmpty(tasks)) {
            log.info("无可分配手机号的任务");
            return;
        }
        for (CpPhoneTask task : tasks) {
            //校验是否有可分配的日期
            DateTime dateTime=vailTime(task);
            if(Objects.isNull(dateTime)){
                log.info("无可分配的日期，任务信息：{}", JSON.toJSONString(task));
                continue;
            }
            //1.1获取分发手机号
            CpPhoneLibraryDTO phoneLibraryDTO=new CpPhoneLibraryDTO();
            phoneLibraryDTO.setStatus(0);//待分配的手机号
            if(task.getUserType()==0){//目标客户源：0全部/1按来源/2按创建时间
            }else if(task.getUserType()==1){
                phoneLibraryDTO.setImportFrom(task.getImportFrom());
            }else if(task.getUserType()==2){
                phoneLibraryDTO.setStartTime(task.getPhoneStartTime());
                phoneLibraryDTO.setEndTime(task.getPhoneEndTime());
            }
            List<CpPhoneLibrary>  phoneLibraries= cpPhoneLibraryMapper.list(phoneLibraryDTO);
            if(CollUtil.isEmpty(phoneLibraries)) {
                log.info("无可分配的手机号，任务信息：{}", JSON.toJSONString(task));
                continue;
            }
            log.info("可分配的手机号：{}", phoneLibraries.size());
            //1.2获取任务员工
            SelectCpPhoneTaskRelDTO taskRelDTO=new SelectCpPhoneTaskRelDTO();
            taskRelDTO.setTaskId(task.getId());
            List<CpPhoneTaskStaffVO> taskRelVOS=taskRelMapper.list(taskRelDTO);
            if(CollUtil.isEmpty(taskRelVOS)) {
                log.info("无可分配的员工，任务信息：{}", JSON.toJSONString(task));
                continue;
            }
            //TODO 根据任务配置每天每天分配数量拆分手机号
            Integer dailyAllocationTotal=task.getDailyAllocationTotal();
            Map<Long,List<CpPhoneTaskUser>> taskUserMap=new HashMap<>();
            for (CpPhoneLibrary cpPhoneLibrary : phoneLibraries) {
                for (CpPhoneTaskStaffVO taskRelVO : taskRelVOS) { //批次手机号分配给员工
                    if(cpPhoneLibrary.getStatus()==1){
                        log.info("当前手机号已分配 :{}",JSON.toJSONString(cpPhoneLibrary));
                        continue;
                    }
                    CpPhoneTaskUser taskUser=new CpPhoneTaskUser();
                    taskUser.setStaffId(taskRelVO.getStaffId());
                    taskUser.setTaskId(task.getId());
                    taskUser.setPhoneUserId(cpPhoneLibrary.getId());
                    taskUser.setIsDelete(0);
                    taskUser.setCreateBy("定时任务分配");
                    taskUser.setCreateTime(new Date());
                    taskUser.setStatus(0);
                    taskUser.setDistributeTime(dateTime);
                    if(CollUtil.isNotEmpty(taskUserMap.get(taskRelVO.getStaffId())) && taskUserMap.get(taskRelVO.getStaffId()).size()>=dailyAllocationTotal){
                        log.info("员工id【{}】当前分配手机号数【{}】已达到分配上限【{}】继续下一个员工",taskRelVO.getStaffId(),
                                taskUserMap.get(taskRelVO.getStaffId()).size(),
                                dailyAllocationTotal);
                        continue;
                    }
                    if(taskUserMap.containsKey(taskRelVO.getStaffId())){
                        taskUserMap.get(taskRelVO.getStaffId()).add(taskUser);
                    }else{
                        List<CpPhoneTaskUser> users=new ArrayList<>();
                        users.add(taskUser);
                        taskUserMap.put(taskRelVO.getStaffId(),users);
                    }
                    cpPhoneLibrary.setStatus(1);//修改手机号为任务中
                }
            }
            if(CollUtil.isNotEmpty(taskUserMap)){
                List<CpPhoneTaskUser> taskUsers=new ArrayList<>();
                for (Map.Entry<Long, List<CpPhoneTaskUser>> entry : taskUserMap.entrySet()) {
                    log.info("员工id【{}】分配手机号数【{}】",entry.getKey(),
                            entry.getValue().size());
                    taskUsers.addAll(entry.getValue());
                }
                log.info("需要分配的手机号行数：{}",taskUsers.size());
                this.saveBatch(taskUsers,200);
                //修改手机号库为已分配
                cpPhoneLibraryService.updateBatchById(phoneLibraries,200);
                //修改任务已分配日期
                if(CharSequenceUtil.isEmpty(task.getDistributeTime())){
                    task.setDistributeTime(DateUtil.format(dateTime,"yyyy-MM-dd"));
                }else{
                    task.setDistributeTime(task.getDistributeTime()+","+DateUtil.format(dateTime,"yyyy-MM-dd"));
                }
                //修改任务已分发日期
                cpPhoneTaskMapper.updateById(task);
            }
        }

    }

    /**
     * 获取分发日期
     * @param task
     * @return
     */
    private static DateTime vailTime(CpPhoneTask task){
        Date startTime=task.getStartTime();
        if(CharSequenceUtil.isEmpty(task.getDistributeTime())){
            return DateUtil.parseDate(DateUtil.format(startTime,"yyyy-MM-dd"));
        }
        if(CharSequenceUtil.isNotEmpty(task.getDistributeTime())){ //已分发日期
            //根据已分发的日期获取接下来需要分发的日期
            startTime=DateUtil.parse(task.getDistributeTime().substring(task.getDistributeTime().lastIndexOf(",")+1,task.getDistributeTime().length()),"yyyy-MM-dd");
            startTime=DateUtil.offsetDay(startTime,1);
        }
        //执行分发时间
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,task.getEndTime(), DateField.DAY_OF_YEAR);
        if(CollUtil.isEmpty(dateTimes)){//无已分发日期
            return null;
        }
        Map<String,String> distributeTimes=LambdaUtils.toMap(Arrays.asList(task.getDistributeTime().split(",")),item->item);
        for (DateTime dateTime : dateTimes) {
            if(!distributeTimes.containsKey(DateUtil.format(dateTime, DatePattern.NORM_DATE_FORMAT)) && dateTime.getTime()<task.getEndTime().getTime()){
                return dateTime;
            }
        }
        return null;
    }

}
