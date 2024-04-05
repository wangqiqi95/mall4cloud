package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.constant.cp.TaskPhoneLibraryStatusEnum;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.CpPhoneTaskDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskDTO;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskMapper;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskUserMapper;
import com.mall4j.cloud.biz.model.cp.CpPhoneLibrary;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskUser;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserCountVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 引流手机号任务
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
@Slf4j
@Service
@RefreshScope
public class CpPhoneTaskServiceImpl extends ServiceImpl<CpPhoneTaskMapper,CpPhoneTask> implements CpPhoneTaskService {

    @Autowired
    private CpPhoneTaskMapper cpPhoneTaskMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CpPhoneTaskStaffService taskRelService;
    @Autowired
    private CpPhoneTaskUserMapper cpPhoneTaskUserMapper;
    @Autowired
    private CpPhoneLibraryService phoneLibraryService;
    @Autowired
    private CpPhoneTaskUserService phoneTaskUserService;
    @Autowired
    private WxCpPushService wxCpPushService;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Override
    public PageVO<CpPhoneTask> page(PageDTO pageDTO, SelectCpPhoneTaskDTO dto) {
        PageVO<CpPhoneTask> phoneTaskPageVO=PageUtil.doPage(pageDTO, () -> cpPhoneTaskMapper.list(dto));
        if(CollUtil.isNotEmpty(phoneTaskPageVO.getList())){
            List<CpPhoneTaskUserCountVO>  userCountVOS= cpPhoneTaskUserMapper.selectCountByTaskId(phoneTaskPageVO.getList()
                    .stream().map(item->item.getId()).collect(Collectors.toList()));
            if(CollUtil.isNotEmpty(userCountVOS)){
                Map<Long,CpPhoneTaskUserCountVO> userCountMap=LambdaUtils.toMap(userCountVOS,CpPhoneTaskUserCountVO::getTaskId);
                for (CpPhoneTask cpPhoneTask : phoneTaskPageVO.getList()) {
                    if(userCountMap.containsKey(cpPhoneTask.getId())){
                        cpPhoneTask.setActualTotal(userCountMap.get(cpPhoneTask.getId()).getActualTotal());
                    }
                }
            }
        }
        return phoneTaskPageVO;
    }

    @Override
    public PageVO<CpPhoneTaskVO> pageMobile(PageDTO pageDTO, SelectCpPhoneTaskDTO dto) {
        //需要过滤当前登录员工可见范围数据
        dto.setStaffs(ListUtil.toList(AuthUserContext.get().getUserId()));
        dto.setOpenStatus(1);
        PageVO<CpPhoneTask> page=PageUtil.doPage(pageDTO, () -> cpPhoneTaskMapper.mobileList(dto));
        PageVO<CpPhoneTaskVO> backPage=new PageVO<>();
        backPage.setList(mapperFacade.mapAsList(page.getList(),CpPhoneTaskVO.class));
        backPage.setPages(page.getPages());
        backPage.setTotal(page.getTotal());
        return backPage;
    }

    @Override
    public CpPhoneTask getById(Long id) {
        return cpPhoneTaskMapper.getById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAndUpdate(CpPhoneTaskDTO phoneTaskDTO) {
        CpPhoneTask phoneTask=null;
        if(Objects.isNull(phoneTaskDTO.getId())){
            phoneTask=mapperFacade.map(phoneTaskDTO,CpPhoneTask.class);
            phoneTask.setIsDelete(0);
            phoneTask.setOpenStatus(1);
            phoneTask.setStatus(0);
            phoneTask.setCreateBy(AuthUserContext.get().getUsername());
            phoneTask.setCreateTime(new Date());
            phoneTask.setAllocationTotal(getAllocationTotal(phoneTaskDTO));
            this.save(phoneTask);


        }else{
            phoneTask=this.getById(phoneTaskDTO.getId());
            if(Objects.isNull(phoneTask)){
                throw new LuckException("任务未找到");
            }
            Date now=new Date();
            //校验进行中的任务不可修改
            if(phoneTask.getStartTime().getTime()<now.getTime() && now.getTime()<phoneTask.getEndTime().getTime()){
                throw new LuckException("任务进行中不可修改");
            }
            phoneTask.setAllocationTotal(getAllocationTotal(phoneTaskDTO));
            this.updateById(phoneTask);
        }
        //保存执行员工
        taskRelService.saveStaffs(phoneTask.getId(),phoneTaskDTO.getStaffs());

        //TODO 提醒员工需要发送消息
        if(phoneTaskDTO.isSaveAndSend()){
            //发送消息提醒
            if(CollUtil.isNotEmpty(phoneTaskDTO.getStaffs())){
                StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
                staffQueryDTO.setStaffIdList(phoneTaskDTO.getStaffs().stream().map(item->item.getStaffId()).collect(Collectors.toList()));
                ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
                ServerResponseEntity.checkResponse(responseEntity);
                List<StaffVO> staffVOS=responseEntity.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(staffVOS)){
                    NotifyMsgTemplateDTO.PhoneTask phoneTaskNotify=NotifyMsgTemplateDTO.PhoneTask.builder()
                            .startTime(DateUtil.format(phoneTaskDTO.getStartTime(),"yyyy-MM-dd HH:mm:ss"))
                            .endTime(DateUtil.format(phoneTaskDTO.getEndTime(),"yyyy-MM-dd HH:mm:ss"))
                            .number(""+phoneTaskDTO.getDailyAllocationTotal())
                            .taskId(phoneTask.getId())
                            .build();
                    for (StaffVO staffVO : staffVOS) {
                        wxCpPushService.phoneTaskNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffVO.getQiWeiUserId()).phoneTask(phoneTaskNotify).build());
                    }
                }

            }
        }
    }

    private Integer getAllocationTotal(CpPhoneTaskDTO phoneTaskDTO){
        //TODO 需要计算累计分配数=执行员工*执行天数*每日数量
        Integer staffSize=phoneTaskDTO.getStaffs().size();
        Integer dailyAllocationTotal=phoneTaskDTO.getDailyAllocationTotal();
        Long day=DateUtil.between(phoneTaskDTO.getStartTime(),phoneTaskDTO.getEndTime(), DateUnit.DAY);
        return NumberUtil.mul(staffSize,dailyAllocationTotal,day).intValue();
    }

    @Override
    public void deleteById(Long id) {
        cpPhoneTaskMapper.deleteById(id);
    }

    public static void main(String[] strings){
        Date now=DateUtil.offsetDay(DateUtil.endOfDay(new Date()),4);
        System.out.println(now);
    }

    /**
     * 关闭任务
     * 业务处理：
     * 1. 更改任务状态
     * 2. 手机号回收：未添加成功的手机号
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeTask(Long id) {
        CpPhoneTask phoneTask=this.getById(id);
        if(Objects.isNull(phoneTask)){
            throw new LuckException("任务未找到");
        }
        phoneTask.setOpenStatus(0);
        phoneTask.setUpdateTime(new Date());
        phoneTask.setUpdateBy(AuthUserContext.get().getUsername()+"手动关闭任务回收");
        this.updateById(phoneTask);
        //获取未添加成功的手机号
        List<CpPhoneTaskUser>  phoneTaskUsers=cpPhoneTaskUserMapper.selectRecycleList(phoneTask.getId());
        if(CollUtil.isNotEmpty(phoneTaskUsers)){
            //获取未添加成功的手机号
            List<Long> phoneUserIds=phoneTaskUsers.stream().map(item->item.getPhoneUserId()).collect(Collectors.toList());
            LambdaQueryWrapper<CpPhoneLibrary> lambdaQueryWrapper=new LambdaQueryWrapper<CpPhoneLibrary>();
            lambdaQueryWrapper.in(CpPhoneLibrary::getId,phoneUserIds);
//            lambdaQueryWrapper.notIn(CpPhoneLibrary::getStatus,2);
            lambdaQueryWrapper.eq(CpPhoneLibrary::getStatus,1);
            List<CpPhoneLibrary> phoneLibraries=phoneLibraryService.list(lambdaQueryWrapper);
            if(CollUtil.isEmpty(phoneLibraries)){
                log.info("手动关闭任务回收，无手机号可回收");
                return;
            }
            Map<Long,CpPhoneLibrary> phoneLibraryMap= LambdaUtils.toMap(phoneLibraries,CpPhoneLibrary::getId);
            List<CpPhoneLibrary> updateLibraries=new ArrayList<>();
            phoneTaskUsers.stream().forEach(item->{
                if(phoneLibraryMap.containsKey(item.getPhoneUserId())){
                    //手机号回收
                    CpPhoneLibrary cpPhoneLibrary=new CpPhoneLibrary();
                    cpPhoneLibrary.setId(item.getPhoneUserId());
                    cpPhoneLibrary.setStatus(TaskPhoneLibraryStatusEnum.BE_DISTRIBUTED.getValue());
                    cpPhoneLibrary.setUpdateTime(new Date());
                    cpPhoneLibrary.setUpdateBy(AuthUserContext.get().getUsername()+"手动关闭任务回收");
                    updateLibraries.add(cpPhoneLibrary);
                }

            });
            phoneLibraryService.updateBatchById(updateLibraries);
            log.info("手动关闭任务回收，回收手机号条数:{}",updateLibraries.size());
        }
    }


    /**
     * 处理任务结束更新手机号库状态
     * 1. 任务结束
     * 2. 手机号未添加
     */
    @Override
    public void refeshFinishTaskStatus() {
        Date now=DateUtil.endOfDay(new Date());
        List<CpPhoneTask>  phoneTasks=cpPhoneTaskMapper.selectFinishList(now);
        if(CollUtil.isEmpty(phoneTasks)){
            log.info("处理任务结束回收手机号库状态失败，未获取到结束任务");
            return;
        }
        Long startTime=System.currentTimeMillis();
        List<CpPhoneTask> updateCpPhoneTask=new ArrayList<>();
        List<CpPhoneLibrary> updateCpPhoneLibrary=new ArrayList<>();
        List<CpPhoneTaskUser> updateCpPhoneTaskUser=new ArrayList<>();
        for (CpPhoneTask phoneTask : phoneTasks) {
            CpPhoneTask cpPhoneTask=new CpPhoneTask();
            cpPhoneTask.setStatus(3);//更新任务状态：已结束
            cpPhoneTask.setId(phoneTask.getId());
            cpPhoneTask.setUpdateTime(new Date());
            cpPhoneTask.setUpdateBy("任务结束回收");
            updateCpPhoneTask.add(cpPhoneTask);
            /**
             * 状态：0待分配/1任务中/2添加成功
             */
            List<CpPhoneTaskUser>  phoneTaskUsers=cpPhoneTaskUserMapper.selectRecycleList(phoneTask.getId());
            if(CollUtil.isEmpty(phoneTaskUsers)){
                log.info("处理任务结束回收手机号库状态失败，根据任务id【{}】未获取到需要回收的手机号",phoneTask.getId());
                continue;
            }
            for (CpPhoneTaskUser phoneTaskUser : phoneTaskUsers) {
                //任务记录：添加失败
                CpPhoneTaskUser cpPhoneTaskUser=new CpPhoneTaskUser();
                cpPhoneTaskUser.setId(phoneTaskUser.getId());
                cpPhoneTaskUser.setStatus(2);//状态：0未添加/1添加成功/2添加失败
                cpPhoneTaskUser.setUpdateTime(new Date());
                cpPhoneTaskUser.setUpdateBy("任务结束回收");
                updateCpPhoneTaskUser.add(cpPhoneTaskUser);

                //手机号回收
                CpPhoneLibrary cpPhoneLibrary=new CpPhoneLibrary();
                cpPhoneLibrary.setId(phoneTaskUser.getPhoneUserId());
                cpPhoneLibrary.setStatus(TaskPhoneLibraryStatusEnum.BE_DISTRIBUTED.getValue());
                cpPhoneLibrary.setUpdateTime(new Date());
                cpPhoneLibrary.setUpdateBy("任务结束回收");
                updateCpPhoneLibrary.add(cpPhoneLibrary);
            }
        }
        if(CollUtil.isNotEmpty(updateCpPhoneTask)){
            this.updateBatchById(updateCpPhoneTask);
            log.info("处理任务结束回收手机号库状态成功，处理任务结束条数:{}",updateCpPhoneTask.size());
        }
        if(CollUtil.isNotEmpty(updateCpPhoneLibrary)){
            phoneLibraryService.updateBatchById(updateCpPhoneLibrary);
            log.info("处理任务结束回收手机号库状态成功，回收条数:{}",updateCpPhoneLibrary.size());
        }
        if(CollUtil.isNotEmpty(updateCpPhoneTaskUser)){
            phoneTaskUserService.updateBatchById(updateCpPhoneTaskUser);
            log.info("处理任务结束回收手机号库状态成功，处理任务记录条数:{}",updateCpPhoneTaskUser.size());
        }
        log.info("处理任务结束回收手机号库状态 执行结束，耗时:{}s",System.currentTimeMillis() - startTime);
    }

}
