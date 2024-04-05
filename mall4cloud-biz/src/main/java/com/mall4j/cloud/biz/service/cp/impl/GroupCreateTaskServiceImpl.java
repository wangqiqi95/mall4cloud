package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.constant.cp.TagGroupTaskSendScopeEnum;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.GroupCreateTaskDetailDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskDTO;
import com.mall4j.cloud.biz.dto.cp.wx.externalcontact.ExtendWxCpMsgTemplate;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpWxMediaUploadResult;
import com.mall4j.cloud.biz.vo.cp.GroupCreateTaskVO;
import com.mall4j.cloud.biz.wx.cp.constant.SendStatus;
import com.mall4j.cloud.biz.wx.cp.constant.TaskType;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskPageDTO;
import com.mall4j.cloud.biz.mapper.cp.GroupCreateTaskMapper;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.biz.manager.DrainageUrlManager;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgTaskResult;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Image;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签建群任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class GroupCreateTaskServiceImpl extends ServiceImpl<GroupCreateTaskMapper, CpGroupCreateTask> implements GroupCreateTaskService {

    private final GroupCreateTaskMapper groupCreateTaskMapper;
    private final CpTaskStaffRefService taskStaffRefService;
    private final MapperFacade mapperFacade;
    private final CpTaskUserRefService taskUserRefService;
    private final GroupCodeRefService refService;
    private final WeixinCpExternalManager weixinCpExternalManager;
    private final DrainageUrlManager drainageUrlManager;
    private final StaffFeignClient staffFeignClient;
    private final CpCodeChannelService cpCodeChannelService;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final CpMediaRefService cpMediaRefService;
    private final CpGroupCreateTaskRefService createTaskRefService;


    @Override
    public PageVO<GroupCreateTaskVO> page(PageDTO pageDTO, GroupCreateTaskPageDTO request) {
        //需要匹配好友名称列表
        if(StrUtil.isNotEmpty(request.getStaffName())){
            StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
            staffQueryDTO.setStaffName(request.getStaffName());
            ServerResponseEntity<List<StaffVO>> responseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            if(CollUtil.isEmpty(responseEntity.getData())){
                PageVO pageVO=new PageVO();
                pageVO.setPages(0);
                pageVO.setTotal(0L);
                return pageVO;
            }
            request.setStaffIds(responseEntity.getData().stream().map(item->item.getId()).collect(Collectors.toList()));
        }
        PageVO<GroupCreateTaskVO> pageVO=PageUtil.doPage(pageDTO, () -> groupCreateTaskMapper.list(request));
        return pageVO;
    }

    /**
     * 移动端任务列表
     * @param pageDTO
     * @param request
     * @return
     */
    @Override
    public PageVO<GroupCreateTaskVO> mobilePage(PageDTO pageDTO, GroupCreateTaskPageDTO request) {
        //需要过滤当前登录员工可见范围数据
        request.setStaffIds(ListUtil.toList(AuthUserContext.get().getUserId()));
        PageVO<GroupCreateTaskVO> pageVO=PageUtil.doPage(pageDTO, () -> groupCreateTaskMapper.list(request));
        return pageVO;
    }

    @Override
    public CpGroupCreateTask getById(Long id) {
        return groupCreateTaskMapper.getById(id);
    }

    @Override
    public GroupCreateTaskDetailDTO getDetailById(Long id) {
        CpGroupCreateTask task = this.getById(id);
        if(Objects.isNull(task)){
            throw new LuckException("任务未找到");
        }
        GroupCreateTaskDetailDTO detailDTO=mapperFacade.map(task,GroupCreateTaskDetailDTO.class);
        detailDTO.setTask(task);

        //员工
        List<CpTaskStaffRef> staffRefs = taskStaffRefService.listByTaskId(id, TaskType.GROUP_TAG.getCode());
        detailDTO.setStaffRefList(staffRefs);

        //好友
        List<CpTaskUserRef>  userRefs=taskUserRefService.getListByTaskId(id);
        detailDTO.setUserRefs(userRefs);

        //关联群聊信息
        List<CpGroupCodeRef>  cpGroupCodeRefs=refService.getListByCodeId(Arrays.asList(task.getId()),CodeChannelEnum.TAG_GROUP_CODE.getValue());
        detailDTO.setCodeList(cpGroupCodeRefs);

        //数据统计：已入群/已邀请/完成发送员工 未已入群/未已邀请/未完成发送员工
        detailDTO.setNoInviteCount(detailDTO.getUserCount()-detailDTO.getInviteCount());
        detailDTO.setNoSendStaffCount(detailDTO.getStaffCount()-detailDTO.getSendStaffCount());
        detailDTO.setNoJoinGroupCount(detailDTO.getUserCount()-detailDTO.getJoinGroupCount());

        List<CpGroupCreateTaskRef> tagRefs=createTaskRefService.listByGroupId(id.toString());
        if(CollUtil.isNotEmpty(tagRefs)){
            detailDTO.setRefIds(tagRefs.stream().map(item->item.getTagId()).collect(Collectors.toList()));
        }

        return detailDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        //删除群信息
        CpGroupCreateTask groupCreateTask=this.getById(id);
        if(Objects.isNull(groupCreateTask)){
            throw new LuckException("标签群未找到");
        }
        groupCreateTask.setIsDelete(1);
        groupCreateTask.setUpdateBy(AuthUserContext.get().getUsername());
        groupCreateTask.setUpdateTime(new Date());
        this.updateById(groupCreateTask);
        //删除关联员工信息
        taskStaffRefService.deleteByTaskId(id, TaskType.GROUP.getCode());
        //删除目标好友【客户】
        taskUserRefService.deleteByTaskId(id);
        //停止企微群发
        if(StrUtil.isNotEmpty(groupCreateTask.getMsgId())){
            try {
                WxCpGroupMsgTaskResult result = weixinCpExternalManager.cancelGroupmsgSend(groupCreateTask.getMsgId());
                log.info("调用企微停止企业群发接口结果 : {}", JSON.toJSONString(result));
                if (!result.getErrcode().toString().equals("0")){
                    throw new LuckException("保存失败，调用企微停止企业群发接口失败");
                }
            } catch (WxErrorException e){
            log.info("调用企微停止企业群发接口失败 : {}",e);
            throw new LuckException("保存失败，调用企微停止企业群发接口失败");
            }

        }

    }


    /**
     * 1. 设置的群聊需要与群活码逻辑一致处理
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrUpdateTask(GroupCreateTaskDTO taskDTO) {
        CpGroupCreateTask groupCreateTask=null;
        //接收方客户
        if(CollUtil.isEmpty(taskDTO.getRefIds())){
            throw new LuckException("目标好友数据不能为空");
        }
        if(Objects.isNull(taskDTO.getAllowSelect())){
            taskDTO.setAllowSelect(0);
        }
        boolean isUpdate=false;
        if(Objects.isNull(taskDTO.getId())){
            groupCreateTask = mapperFacade.map(taskDTO, CpGroupCreateTask.class);
            groupCreateTask.setCreateBy(AuthUserContext.get().getUserId());
            groupCreateTask.setCreateName(AuthUserContext.get().getUsername());
            groupCreateTask.setCreateTime(new Date());
            groupCreateTask.setUpdateTime(groupCreateTask.getCreateTime());
            groupCreateTask.setIsDelete(0);
            groupCreateTask.setIsReplay(0);
            groupCreateTask.setSendStatus(SendStatus.WAIT.getCode());
            groupCreateTask.setWarnCount(0);
            groupCreateTask.setStaffCount(taskDTO.getStaffs().size());
            groupCreateTask.setGroupCount(taskDTO.getCodeList().size());
            groupCreateTask.setUserCount(0);
            groupCreateTask.setState(String.valueOf(RandomUtil.getUniqueNum()));

            //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
            String content=groupCreateTask.getState();
            String logo="";
            ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
            ServerResponseEntity.checkResponse(responseEntity);
            CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
            groupCreateTask.setDrainageUrl(mediaUploadResult.getDrainageUrl());
            groupCreateTask.setDrainagePath(mediaUploadResult.getDrainagePath());

            this.save(groupCreateTask);

            //渠道源标识
            cpCodeChannelService.saveCpCodeChannel(CodeChannelEnum.TAG_GROUP_CODE.getValue(),groupCreateTask.getId().toString(),groupCreateTask.getId().toString(),groupCreateTask.getState());

            //保存引流页面二维码临时素材
            cpMediaRefService.saveCpMediaRef(CpMediaRef.builder()
                    .sourceId(groupCreateTask.getId().toString())
                    .sourceFrom(CodeChannelEnum.TAG_GROUP_CODE.getValue())
                    .mediaId(mediaUploadResult.getMediaId())
                    .thumbMediaId(mediaUploadResult.getThumbMediaId())
                    .url(mediaUploadResult.getUrl())
                    .type(mediaUploadResult.getType())
                    .createTime(WechatUtils.formatDate(String.valueOf(mediaUploadResult.getCreatedAt())))
                    .build());
        }else{
            groupCreateTask = this.getById(taskDTO.getId());
            if(Objects.isNull(groupCreateTask)){
                throw new LuckException("群信息未找到");
            }
            groupCreateTask = mapperFacade.map(taskDTO, CpGroupCreateTask.class);
            groupCreateTask.setUpdateTime(new Date());
            groupCreateTask.setStaffCount(taskDTO.getStaffs().size());
            groupCreateTask.setGroupCount(taskDTO.getCodeList().size());
            this.updateById(groupCreateTask);
            isUpdate=true;
        }
        //是否允许成员在待发送客户列表中重新进行选择：0否/1是
        boolean allowSelect=groupCreateTask.getAllowSelect()==1?true:false;

        //保存群活码关联群聊信息
        refService.saveTo(groupCreateTask.getId(), CodeChannelEnum.TAG_GROUP_CODE.getValue(),taskDTO.getCodeList());

        //TODO 编辑需求逻辑有问题，因为保存已经调用企微接口创建任务了，编辑会导致用户数据覆盖
        if(isUpdate){
            return;
        }

        Long groupTaskId=groupCreateTask.getId();
        //先删除员工信息
        taskStaffRefService.deleteByTaskId(groupCreateTask.getId(), TaskType.GROUP.getCode());
        //删除目标好友【客户】
        taskUserRefService.deleteByTaskId(groupCreateTask.getId());

        //发送方员工
        List<CpTaskStaffRef> taskStaffRefs=new ArrayList<>();
        List<Long> staffIds=taskDTO.getStaffs().stream().map(item->item.getStaffId()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(taskDTO.getStaffs())){
            taskDTO.getStaffs().forEach(item->{
                CpTaskStaffRef ref = mapperFacade.map(item, CpTaskStaffRef.class);
                ref.setTaskId(groupTaskId);
                ref.setStatus(0);
                ref.setType(TaskType.GROUP_TAG.getCode());
                ref.setCreateTime(new Date());
                ref.setIsDelete(0);
                taskStaffRefs.add(ref);
            });
            taskStaffRefService.saveBatch(taskStaffRefs);
        }
        Map<String,CpTaskStaffRef> staffRefMap=LambdaUtils.toMap(taskStaffRefs,CpTaskStaffRef::getUserId);

        List<CpTaskUserRef> taskUserRefs=getCpTaskUserRefByRefIds(groupTaskId,taskDTO.getSendScope(),taskDTO.getRefIds(),staffIds,staffRefMap);
        if(CollUtil.isEmpty(taskUserRefs)){
            throw new LuckException("保存失败，未获取到目标好友数据");
        }
        //关联id
        createTaskRefService.saveTo(groupTaskId.toString(),groupCreateTask.getSendScope(),taskDTO.getRefIds());

        try {
            if(Objects.isNull(taskDTO.getId()) && CollUtil.isNotEmpty(taskUserRefs)){//新建任务需要创建企微任务
                Map<String,List<CpTaskUserRef>> userRefMap=LambdaUtils.groupList(taskUserRefs,CpTaskUserRef::getSaffQiWeiUserId);
                List<CpTaskStaffRef> updateCpTaskStaffRef=new ArrayList<>();
                for (Map.Entry<String, List<CpTaskUserRef>> entry : userRefMap.entrySet()) {
                    List<String> userids=entry.getValue().stream().map(item->item.getQiWeiUserId()).collect(Collectors.toList());
                    //创建企微任务:引导语+引流页面二维码图片
                    ExtendWxCpMsgTemplate template=new ExtendWxCpMsgTemplate();
                    template.setAllow_select(allowSelect);
                    template.setExternalUserid(userids);
                    template.setSender(entry.getKey());
                    template.setChatType("single");
                    Text text = new Text();
                    text.setContent(taskDTO.getSlogan());
                    template.setText(text);
                    Image image=new Image();
                    image.setPicUrl(groupCreateTask.getDrainageUrl());
                    Attachment attachment=new Attachment();
                    attachment.setImage(image);
                    template.setAttachments(Lists.newArrayList(attachment));
                    log.info("调用企微创建企业群发任务接口入参 : {}", JSON.toJSONString(template));
                    WxCpMsgTemplateAddResult result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                            .getExternalContactService()
                            .addMsgTemplate(template);
                    log.info("调用企微创建企业群发任务接口结果 : {}", JSON.toJSONString(result));
                    if(staffRefMap.containsKey(entry.getKey())){
                        CpTaskStaffRef taskStaffRef=staffRefMap.get(entry.getKey());
                        if (result.getErrMsg().equals("ok")){
                            taskStaffRef.setMsgId(result.getMsgId());
                            taskStaffRef.setFailList(JSON.toJSONString(result.getFailList()));
                            updateCpTaskStaffRef.add(taskStaffRef);
                        }
                    }

                }
                //修改员工发送状态
                if(CollUtil.isNotEmpty(updateCpTaskStaffRef)){
                    taskStaffRefService.updateBatchById(updateCpTaskStaffRef);
                }
                groupCreateTask.setSendStatus(SendStatus.SUCCESS.getCode());
                //修改客户总人数
                groupCreateTask.setUserCount(taskUserRefs.size());
                this.updateById(groupCreateTask);

                //创建企微任务:引导语+引流页面二维码图片
//                ExtendWxCpMsgTemplate template=new ExtendWxCpMsgTemplate();
//                template.setExternalUserid(taskUserRefs.stream().map(item->item.getQiWeiUserId()).collect(Collectors.toList()));
//                template.setChatType("single");
//                Text text = new Text();
//                text.setContent(taskDTO.getSlogan());
//                template.setText(text);
//                Image image=new Image();
//                image.setPicUrl(groupCreateTask.getDrainageUrl());
//                Attachment attachment=new Attachment();
//                attachment.setImage(image);
//                template.setAttachments(Lists.newArrayList(attachment));
//                log.info("调用企微创建企业群发任务接口入参 : {}", JSON.toJSONString(template));
//                WxCpMsgTemplateAddResult result = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId())
//                        .getExternalContactService()
//                        .addMsgTemplate(template);
//                log.info("调用企微创建企业群发任务接口结果 : {}", JSON.toJSONString(result));
//                if (result.getErrMsg().equals("ok")){
//                    groupCreateTask.setSendStatus(SendStatus.SUCCESS.getCode());
//                    groupCreateTask.setMsgId(result.getMsgId());
//                    groupCreateTask.setFailList(JSON.toJSONString(result.getFailList()));
//                    //修改客户总人数
//                    groupCreateTask.setUserCount(taskUserRefs.size());
//                    this.updateById(groupCreateTask);
//                }else{
//                    throw new LuckException("保存失败，调用企微接口失败");
//                }
            }
        }catch (WxErrorException e){
            log.info("调用企微接口失败 : {}",e);
            throw new LuckException("保存失败，调用企微接口失败");
        }
    }

    /**
     * TODO 根据发送范围筛选客户
     * @param taskId 任务id
     * @param sendScope 发送范围: 0按部门员工/1按客户标签/2按客户分组
     * @param refIds 发送范围筛选id集合：部门员工id/标签id/客户分组阶段id
     * @return
     */
    private List<CpTaskUserRef> getCpTaskUserRefByRefIds(Long taskId,Integer sendScope,List<String> refIds,List<Long> staffIds,Map<String,CpTaskStaffRef> staffRefMap){
        ServerResponseEntity<List<UserStaffCpRelationListVO>>  serverResponseEntity=new ServerResponseEntity<>();
        UserStaffCpRelationSearchDTO dto=new UserStaffCpRelationSearchDTO();
        dto.initPage();
        dto.setStatus(1);
        dto.setStaffIds(staffIds);

        if(sendScope== TagGroupTaskSendScopeEnum.DEPART_STAFF.getValue()){//按部门员工
            dto.setStaffIds(refIds.stream().map(Long::valueOf).collect(Collectors.toList()));
            serverResponseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(dto);
            if(serverResponseEntity.isFail() || CollUtil.isEmpty(serverResponseEntity.getData())){
                throw new LuckException("保存失败，按发送范围：部门员工未筛选到客户数据");
            }
        }
        if(sendScope== TagGroupTaskSendScopeEnum.USER_TAG.getValue()){//1按客户标签
            dto.setTagId(refIds);
            serverResponseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(dto);
            if(serverResponseEntity.isFail() || CollUtil.isEmpty(serverResponseEntity.getData())){
                throw new LuckException("保存失败，按发送范围：按客户标签未筛选到客户数据");
            }
        }
        if(sendScope== TagGroupTaskSendScopeEnum.USER_GROUP_PHASE.getValue()){//2按客户分组
            dto.setStageIds(refIds.stream().map(Long::valueOf).collect(Collectors.toList()));
            serverResponseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(dto);
            if(serverResponseEntity.isFail() || CollUtil.isEmpty(serverResponseEntity.getData())){
                throw new LuckException("保存失败，按发送范围：按客户分组未筛选到客户数据");
            }
        }
        if(CollUtil.isEmpty(serverResponseEntity.getData())){
            throw new LuckException("保存失败，未获取到客户数据");
        }

        //筛选对应员工状态正常的客户
//        List<Long> staffIds=serverResponseEntity.getData().stream().map(item->item.getStaffId()).collect(Collectors.toList());
        StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
        staffQueryDTO.setStatus(0);
        staffQueryDTO.setIsDelete(0);
        staffQueryDTO.setStaffIdList(staffIds);
        ServerResponseEntity<List<StaffVO>> staffResponseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
        if(staffResponseEntity.isFail() || CollUtil.isEmpty(staffResponseEntity.getData())){
            throw new LuckException("保存失败，根据客户未获取员工信息");
        }
        List<UserStaffCpRelationListVO> relationListVOS=new ArrayList<>();
        Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(staffResponseEntity.getData(),StaffVO::getId);
        for (UserStaffCpRelationListVO datum : serverResponseEntity.getData()) {
            if(staffVOMap.containsKey(datum.getStaffId())){
                relationListVOS.add(datum);
            }
        }
        if(CollUtil.isEmpty(relationListVOS)){
            throw new LuckException("保存失败，根据客户未获取员工信息");
        }
        List<CpTaskUserRef> taskUserRefs=new ArrayList<>();
        for (UserStaffCpRelationListVO relation : relationListVOS) {
            CpTaskUserRef cpTaskUserRef=new CpTaskUserRef();
            cpTaskUserRef.setTaskId(taskId);
            cpTaskUserRef.init();
            cpTaskUserRef.setUserName(relation.getQiWeiNickName());
            cpTaskUserRef.setQiWeiUserId(relation.getQiWeiUserId());
            cpTaskUserRef.setStaffId(relation.getStaffId());
            cpTaskUserRef.setSaffQiWeiUserId(relation.getQiWeiStaffId());
            cpTaskUserRef.setUserId(relation.getUserId());
            cpTaskUserRef.setCreateBy(AuthUserContext.get().getUsername());
            taskUserRefs.add(cpTaskUserRef);
        }
        taskUserRefService.saveBatch(taskUserRefs);

        //修改任务用户总人数
        CpGroupCreateTask groupCreateTask=new CpGroupCreateTask();
        groupCreateTask.setId(taskId);
        groupCreateTask.setUserCount(taskUserRefs.size());
        this.updateById(groupCreateTask);

        return taskUserRefs;
    }

    /**
     * 提醒员工
     * 企业和第三方应用可调用此接口，重新触发群发通知，提醒成员完成群发任务，24小时内每个群发最多触发三次提醒。
     * @param id
     */
    @Override
    public void warnStaff(Long id) {
        CpGroupCreateTask cpGroupCreateTask=this.getById(id);
        if(Objects.isNull(cpGroupCreateTask)){
            throw new LuckException("操作失败，任务未找到");
        }
        if(Objects.nonNull(cpGroupCreateTask.getWarnCount()) && cpGroupCreateTask.getWarnCount()>=3){
            throw new LuckException("操作失败，已达到提醒上线");
        }
        //获取未完成的员工提醒
        LambdaQueryWrapper<CpTaskStaffRef> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CpTaskStaffRef::getTaskId,id);
        lambdaQueryWrapper.eq(CpTaskStaffRef::getStatus,0);
        lambdaQueryWrapper.eq(CpTaskStaffRef::getIsDelete,0);
        List<CpTaskStaffRef> wranStaffs=taskStaffRefService.list(lambdaQueryWrapper).stream()
                .filter(item->StrUtil.isNotEmpty(item.getMsgId())).collect(Collectors.toList());
        if(CollUtil.isEmpty(wranStaffs)){
            throw new LuckException("操作失败，暂无需要提醒的员工");
        }
        //TODO 是否需要校验提醒次数
        boolean warnSuccess=true;
        for (CpTaskStaffRef wranStaff : wranStaffs) {
            Integer warnCount=Objects.nonNull(wranStaff.getWarnCount())?wranStaff.getWarnCount()+1:1;
            wranStaff.setWarnCount(warnCount);
            wranStaff.setUpdateTime(new Date());
            wranStaff.setWarnTime(new Date());
            wranStaff.setUpdateBy(AuthUserContext.get().getUsername());
            taskStaffRefService.updateById(wranStaff);
            try {
                WxCpBaseResp result=weixinCpExternalManager.remindGroupmsgSend(wranStaff.getMsgId());
                if (result.getErrmsg().equals("ok")){
//                    Integer warnCount=Objects.nonNull(wranStaff.getWarnCount())?wranStaff.getWarnCount()+1:1;
//                    wranStaff.setWarnCount(warnCount);
//                    wranStaff.setUpdateTime(new Date());
//                    wranStaff.setWarnTime(new Date());
//                    wranStaff.setUpdateBy(AuthUserContext.get().getUsername());
//                    taskStaffRefService.updateById(wranStaff);
                }else {
                    warnSuccess=false;
                }
                log.error("提醒发送，任务ID：{} 任务名称：{} 员工ID：{} msgId：{} 结果:{}",
                        cpGroupCreateTask.getId(),cpGroupCreateTask.getTaskName(),wranStaff.getStaffId(),wranStaff.getMsgId(),result.toJson());
            }catch (WxErrorException e){
                warnSuccess=false;
                log.error("提醒失败，任务ID：{} 任务名称：{} 员工ID：{} msgId：{} 错误信息:{}",
                        cpGroupCreateTask.getId(),cpGroupCreateTask.getTaskName(),wranStaff.getStaffId(),wranStaff.getMsgId(),e);
            }
//            try {
//                WxCpBaseResp result=weixinCpExternalManager.remindGroupmsgSend(wranStaff.getMsgId());
//                if (result.getErrmsg().equals("ok")){
//                    Integer warnCount=Objects.nonNull(wranStaff.getWarnCount())?wranStaff.getWarnCount()+1:1;
//                    wranStaff.setWarnCount(warnCount);
//                    wranStaff.setUpdateTime(new Date());
//                    wranStaff.setWarnTime(new Date());
//                    wranStaff.setUpdateBy(AuthUserContext.get().getUsername());
//                    taskStaffRefService.updateById(wranStaff);
//                }else if(result.getErrcode().equals("41094")){
//                    throw new LuckException("操作失败，24小时内每个群发最多触发三次提醒");
//                }else{
//                    throw new LuckException("操作失败");
//                }
//            }catch (WxErrorException e){
//                if(e.getError().getErrorCode()==41094){
//                    throw new LuckException("操作失败，24小时内每个群发最多触发三次提醒");
//                }else{
//                    throw new LuckException("操作失败");
//                }
//            }
        }
//        if(!warnSuccess){
//            return;
//        }
        Integer warnCount=Objects.nonNull(cpGroupCreateTask.getWarnCount())?cpGroupCreateTask.getWarnCount()+1:1;
        cpGroupCreateTask.setWarnCount(warnCount);
        cpGroupCreateTask.setUpdateTime(new Date());
        cpGroupCreateTask.setWarnTime(new Date());
        cpGroupCreateTask.setUpdateBy(AuthUserContext.get().getUsername());
        this.updateById(cpGroupCreateTask);
    }

    @Override
    public List<TaskAttachmentVO> queryTaskList(Long id,Long staffId, String userId, Integer status) {
        return groupCreateTaskMapper.queryTaskList(id,staffId,userId,status);
    }

    @Override
    public void complete(Long id) {
        CpGroupCreateTask task = new CpGroupCreateTask();
        task.setId(id);
        task.setIsReplay(1);
        task.setUpdateTime(new Date());
        groupCreateTaskMapper.update(task);
    }
}
