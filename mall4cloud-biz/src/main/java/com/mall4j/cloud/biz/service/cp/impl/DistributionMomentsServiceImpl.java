package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.constant.cp.DistributionMonentsEnum;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsDTO;
import com.mall4j.cloud.biz.dto.cp.MomentsSendDTO;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.mapper.cp.DistributionMomentsMapper;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.DistributionMoments;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsStore;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsSendRecordService;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsService;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsStoreService;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;
import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpAddMomentResult;
import me.chanjar.weixin.cp.bean.external.WxCpAddMomentTask;
import me.chanjar.weixin.cp.bean.external.moment.SenderList;
import me.chanjar.weixin.cp.bean.external.moment.VisibleRange;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Slf4j
@Service
public class DistributionMomentsServiceImpl implements DistributionMomentsService {

    @Autowired
    private DistributionMomentsMapper distributionMomentsMapper;

    @Autowired
    private DistributionMomentsStoreService distributionMomentsStoreService;
    @Autowired
    WelcomeAttachmentService welcomeAttachmentService;
    @Autowired
    StaffFeignClient staffFeignClient;
    @Autowired
    DistributionMomentsSendRecordService distributionMomentsSendRecordService;
    @Autowired
    WelcomeAttachmentService attachmentService;
    @Autowired
    WeixinCpExternalManager weixinCpExternalManager;

//    @Autowired
//    private DistributionMomentsProductService distributionMomentsProductService;


    @Override
    public PageVO<DistributionMomentsVO> page(PageDTO pageDTO,DistributionMomentsDTO dto) {
//        if (null != dto.getQueryStoreId()) {
//            List<DistributionMomentsStore> storeList = distributionMomentsStoreService.listByStoreId(dto.getQueryStoreId());
//            if (CollectionUtils.isNotEmpty(storeList)){
//                dto.setIds(storeList.stream().map(DistributionMomentsStore::getMomentsId).distinct().collect(Collectors.toList()));
//            }
//        }

        PageVO<DistributionMoments> objectPageVO = PageUtil.doPage(pageDTO, () -> distributionMomentsMapper.list(dto));

        PageVO<DistributionMomentsVO> pageVO = new PageVO<>();

        List<DistributionMomentsVO> list = new ArrayList<>();
        objectPageVO.getList().forEach(distributionMoments -> {
            DistributionMomentsVO vo = new DistributionMomentsVO();
            BeanUtils.copyProperties(distributionMoments, vo);
//            vo.setStoreNum(distributionMomentsStoreService.countByMomentsId(distributionMoments.getId()));
            if(vo.getStoreType()==1){
                List<DistributionMomentsStore>  momentsStores=distributionMomentsStoreService.listByMomentsId(distributionMoments.getId());
//                List<Long> storeIds = momentsStores.stream()
//                        .map(DistributionMomentsStore::getStoreId).collect(Collectors.toList());
//                vo.setStoreIds(storeIds);
                vo.setStoreList(momentsStores);
            }

            list.add(vo);
        });
        pageVO.setList(list);
        pageVO.setTotal(objectPageVO.getTotal());
        pageVO.setPages(PageUtil.getPages(objectPageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
//    @DS("slave")
    public DistributionMomentsDTO getMomentsById(Long id) {
        DistributionMoments moments = distributionMomentsMapper.getById(id);
        if (null != moments){
            DistributionMomentsDTO dto = new DistributionMomentsDTO();
            BeanUtils.copyProperties(moments, dto);
            if (moments.getStoreType() == 1){
                dto.setStoreList(distributionMomentsStoreService.listByMomentsId(id));
            }
//            dto.setProductList(distributionMomentsProductService.listByMomentsId(id));
            return dto;
        }
        return null;
    }

    @Override
    public DistributionMoments getById(Long id) {
        return distributionMomentsMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionMomentsDTO dto) {
        DistributionMoments distributionMoments = new DistributionMoments();
        BeanUtils.copyProperties(dto, distributionMoments);
        distributionMoments.setStatus(0);
        distributionMoments.setSendStatus(DistributionMonentsEnum.DRAFT.getValue());
        distributionMomentsMapper.save(distributionMoments);


        dto.setId(distributionMoments.getId());
        saveStoreInfo(dto);

        saveDistributionMomentsSendRecord(dto);

        //附件
        if(CollUtil.isNotEmpty(dto.getAttachMentBaseDTOS())){
            List<AttachmentExtDTO> attachMent = AttachMentVO.getAttachMents(dto.getAttachMentBaseDTOS());
            if(!org.springframework.util.CollectionUtils.isEmpty(attachMent)) {
                for (AttachmentExtDTO attachmentExtDTO : attachMent) {
                    CpWelcomeAttachment welcomeAttachment = new CpWelcomeAttachment();
                    welcomeAttachment.setData(Json.toJsonString(attachmentExtDTO));
                    welcomeAttachment.setType(attachmentExtDTO.getAttachment().getMsgType());
                    welcomeAttachment.setWelId(distributionMoments.getId());
                    welcomeAttachment.setOrigin(OriginType.MOMENTS_CONFIG.getCode());
                    welcomeAttachment.setUpdateTime(new Date());
                    welcomeAttachmentService.save(welcomeAttachment);
                }
            }
        }
    }

    private void saveDistributionMomentsSendRecord(DistributionMomentsDTO dto) {
        distributionMomentsSendRecordService.deleteByMomentId(dto.getId());
        for (DistributionMomentsStore store : dto.getStoreList()) {
            if(Objects.isNull(store.getType())){
                store.setType(0);
            }
        }
        //部门
        List<Long> orgIds=dto.getStoreList().stream().filter(item->item.getType()==0).map(item->item.getStoreId()).collect(Collectors.toList());
        //员工
        List<Long> staffIds=dto.getStoreList().stream().filter(item->item.getType()==1).map(item->item.getStoreId()).collect(Collectors.toList());

        List<StaffVO> staffVOS=new ArrayList<>();
        if(dto.getStoreType()==0){//全部员工门店
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStatus(0);
            staffQueryDTO.setQiweiUserStatus(1);
            ServerResponseEntity<List<StaffVO>> serverResponse = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            staffVOS.addAll(serverResponse.getData());
        }else{
            if(CollUtil.isNotEmpty(orgIds)){
                StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
                staffQueryDTO.setStatus(0);
                staffQueryDTO.setQiweiUserStatus(1);
                staffQueryDTO.setOrgIds(orgIds);
                ServerResponseEntity<List<StaffVO>> serverResponse = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
                staffVOS.addAll(serverResponse.getData());
            }
            if(CollUtil.isNotEmpty(staffIds)){
                StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
                staffQueryDTO.setStatus(0);
                staffQueryDTO.setQiweiUserStatus(1);
                staffQueryDTO.setStaffIdList(staffIds);
                ServerResponseEntity<List<StaffVO>> serverResponse = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
                staffVOS.addAll(serverResponse.getData());
            }
        }

        if(CollUtil.isEmpty(staffVOS)){
            Assert.faild("操作失败，未获取到员工数据");
        }
        //员工信息去重复
        List<StaffVO> staffVOSList=new ArrayList<>(staffVOS.stream().collect(Collectors.toMap(StaffVO::getId, s->s, (v1, v2)->v2)).values());
        if(CollUtil.isEmpty(staffVOSList)){
            Assert.faild("操作失败，未获取到员工数据");
        }
//        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
//        staffQueryDTO.setStatus(0);
//        staffQueryDTO.setQiweiUserStatus(1);
//        staffQueryDTO.setOrgIds(dto.getStoreList().stream().map(DistributionMomentsStore::getStoreId).distinct().collect(Collectors.toList()));
//        ServerResponseEntity<List<StaffVO>> serverResponse = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
//        if(serverResponse==null || serverResponse.isFail()){
//            Assert.faild("员工查询部门失败，请稍后再试。");
//        }
//        List<StaffVO> staffVOS=serverResponse.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).collect(Collectors.toList());
//        if(CollUtil.isEmpty(staffVOS)){
//            Assert.faild("操作失败，未获取到员工数据");
//        }
        for (StaffVO staffVO : staffVOSList) {
            if(StrUtil.isEmpty(staffVO.getQiWeiUserId())){
                Assert.faild(StrUtil.format("员工【{}】的企微user_id为空，请联系管理员。",staffVO.getStaffName()));
            }
            DistributionMomentsSendRecord sendRecord = new DistributionMomentsSendRecord();
            sendRecord.setMomentsId(dto.getId());
            sendRecord.setStaffId(staffVO.getId());
            sendRecord.setStatus(0);
            sendRecord.setQwCommentNum(0);
            sendRecord.setQwLikeNum(0);
            sendRecord.setCreateTime(new Date());
            sendRecord.setQiweiUserId(staffVO.getQiWeiUserId());
            sendRecord.setQwPublishStatus(0);
            distributionMomentsSendRecordService.save(sendRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionMomentsDTO dto) {
        DistributionMoments dbdistributionMoments = this.getById(dto.getId());
        if(dbdistributionMoments==null){
            Assert.faild("朋友圈记录不存在");
        }
        if(dbdistributionMoments.getStatus()==1 || dbdistributionMoments.getEnableTime()!=null){
            Assert.faild(StrUtil.format("朋友圈[{}]已经启用过，不允许更新或者重新启用。",dbdistributionMoments.getTitle()));
        }

        DistributionMoments distributionMoments = new DistributionMoments();
        BeanUtils.copyProperties(dto, distributionMoments);
        distributionMomentsMapper.update(distributionMoments);
        saveStoreInfo(dto);

        saveDistributionMomentsSendRecord(dto);

        //附件
        welcomeAttachmentService.deleteByWelId(distributionMoments.getId(), OriginType.MOMENTS_CONFIG.getCode());
        if(CollUtil.isNotEmpty(dto.getAttachMentBaseDTOS())){
            List<AttachmentExtDTO> attachMent = AttachMentVO.getAttachMents(dto.getAttachMentBaseDTOS());
            if(!org.springframework.util.CollectionUtils.isEmpty(attachMent)) {
                for (AttachmentExtDTO attachmentExtDTO : attachMent) {
                    CpWelcomeAttachment welcomeAttachment = new CpWelcomeAttachment();
                    welcomeAttachment.setData(Json.toJsonString(attachmentExtDTO));
                    welcomeAttachment.setType(attachmentExtDTO.getAttachment().getMsgType());
                    welcomeAttachment.setWelId(distributionMoments.getId());
                    welcomeAttachment.setOrigin(OriginType.MOMENTS_CONFIG.getCode());
                    welcomeAttachment.setUpdateTime(new Date());
                    welcomeAttachmentService.save(welcomeAttachment);
                }
            }
        }


//        WxCpAddMomentTask task = new WxCpAddMomentTask();
//        try {
//            String jobId = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getExternalContactService().addMomentTask(task).getJobId();
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
    }

    private void saveStoreInfo(DistributionMomentsDTO dto) {
        if (dto.getStoreType() == 0) {
            distributionMomentsStoreService.deleteByMomentsId(dto.getId());
            return;
        }
        if (CollectionUtils.isEmpty(dto.getStoreList())) {
            throw new LuckException("作用门店为空");
        }
        distributionMomentsStoreService.deleteByMomentsIdNotInStoreIds(dto.getId(), dto.getStoreList().stream().map(DistributionMomentsStore::getStoreId).distinct().collect(Collectors.toList()));

        List<DistributionMomentsStore> storeList = distributionMomentsStoreService.listByMomentsId(dto.getId());
        Map<String, DistributionMomentsStore> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeList)){
            storeMap = storeList.stream().collect(Collectors.toMap(s -> s.getMomentsId() + "_" + s.getStoreId(), s -> s));
        }

        Map<String, DistributionMomentsStore> finalStoreMap = storeMap;
        dto.getStoreList().forEach(s -> {
            if (null == finalStoreMap.get(dto.getId() + "_" + s.getStoreId())) {
                DistributionMomentsStore store = new DistributionMomentsStore();
                store.setMomentsId(dto.getId());
                store.setStoreId(s.getStoreId());
                store.setType(Objects.nonNull(s.getType())?s.getType():0);
                distributionMomentsStoreService.save(store);
            }
        });
    }

//    private void saveProduct(DistributionMomentsDTO dto) {
//        if (CollectionUtils.isEmpty(dto.getProductList())) {
//            distributionMomentsProductService.deleteByMomentsId(dto.getId());
//            return;
//        }
//        distributionMomentsProductService.deleteByMomentsId(dto.getId());
//        for (int i = 0; i < dto.getProductList().size(); i++) {
//            DistributionMomentsProduct product = dto.getProductList().get(i);
//            product.setMomentsId(dto.getId());
//            product.setShowSort(i);
//            distributionMomentsProductService.save(product);
//        }
//    }

    @Override
    public void deleteById(Long id) {
        distributionMomentsMapper.deleteById(id);
//        distributionMomentsProductService.deleteByMomentsId(id);
        distributionMomentsStoreService.deleteByMomentsId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusBatch(List<Long> ids, Integer status) {
        if(status==0){
            disable(ids, status);
        }else{
            enable(ids, status);
        }
//        distributionMomentsMapper.updateStatusBatch(ids, status);
    }

    private void enable(List<Long> ids, Integer status) {
        for (Long id : ids) {
            DistributionMoments distributionMoments = this.getById(id);
            if(distributionMoments.getStatus()==1){
                Assert.faild(StrUtil.format("朋友圈[{}]已经为启用状态。",distributionMoments.getTitle()));
            }
            if(distributionMoments.getEnableTime()!=null){
                Assert.faild(StrUtil.format("朋友圈[{}]不允许重新启用。",distributionMoments.getTitle()));
            }
        }

        for (Long id : ids) {
            sendByMoment(id);
            distributionMomentsMapper.enable(id);
        }
    }

    private void disable(List<Long> ids, Integer status) {
        for (Long id : ids) {
            DistributionMoments distributionMoments = this.getById(id);
            if(distributionMoments.getStatus()==0){
                Assert.faild(StrUtil.format("朋友圈[{}]已经为禁用状态。",distributionMoments.getTitle()));
            }
        }

        for (Long id : ids) {
            DistributionMoments distributionMoments = this.getById(id);
            if (distributionMoments.getQwMomentsId()==null){
                Assert.faild("还未同步到企微的朋友圈id，请稍后重试禁用功能。");
            }
            try {
                WxCpBaseResp baseResp = weixinCpExternalManager.cancelMomentTask(distributionMoments.getQwMomentsId());
                log.info("停止发表企业朋友圈执行结束,参数：{},执行结果：{}",distributionMoments.getQwMomentsId(),JSONObject.toJSONString(baseResp));
            } catch (WxErrorException e) {
                e.printStackTrace();
                log.error("停止发表企业朋友圈失败。",e);
            }
            distributionMomentsMapper.disable(id);
        }
    }


    @Override
    public void momentsTop(Long id, Integer top) {
        if (top ==1 && distributionMomentsMapper.countMomentsTopNum() >= 3){
            throw new LuckException("置顶数量已上限");
        }
        distributionMomentsMapper.momentsTop(id, top);
    }

    @Override
//    @DS("slave")
    public PageVO<DistributionMomentsVO> pageEffect(PageDTO pageDTO, DistributionMomentsDTO dto) {
//        List<DistributionMomentsStore> storeList = distributionMomentsStoreService.listByStoreId(dto.getQueryStoreId());
//        if (CollectionUtils.isNotEmpty(storeList)){
//            dto.setIds(storeList.stream().map(DistributionMomentsStore::getMomentsId).distinct().collect(Collectors.toList()));
//        }
//        List<DistributionMoments>  d=distributionMomentsMapper.pageEffect(dto);
        PageVO<DistributionMomentsVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionMomentsMapper.pageEffect(dto));
//        if (Objects.nonNull(pageVO) && CollectionUtils.isNotEmpty(pageVO.getList())) {
//            List<Long> momentsIdList = pageVO.getList().stream().map(DistributionMoments :: getId).collect(Collectors.toList());
//            Map<Long, List<DistributionMomentsProduct>>  momentsProductMap = distributionMomentsProductService
//                    .listByMomentsIdList(momentsIdList).stream().collect(Collectors.groupingBy(DistributionMomentsProduct :: getMomentsId));
//            pageVO.getList().stream().forEach(m -> {
//                m.setProductList(momentsProductMap.get(m.getId()));
//            });
//        }
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(MomentsSendDTO request) {

        DistributionMomentsDTO momentsDTO = this.getMomentsById(request.getMomentsId());
        Assert.isNull(momentsDTO,"朋友圈数据为空，请检查id是否正确！");
        List<CpWelcomeAttachment> attachments = attachmentService.listByWelId(request.getMomentsId(), OriginType.MOMENTS_CONFIG.getCode());

        ServerResponseEntity<StaffVO> staffResponse = staffFeignClient.getStaffById(request.getStaffId());
        if(staffResponse==null || staffResponse.isFail() || staffResponse.getData()==null ){
            Assert.faild("员工查询异常，请稍后再试。");
        }
        StaffVO staffVO = staffResponse.getData();
        if(StrUtil.isEmpty(staffVO.getQiWeiUserId())){
            Assert.faild("员工数据异常，qiwei_user_id为空，请联系管理员。");
        }

        DistributionMomentsSendRecord momentsSendRecord = distributionMomentsSendRecordService.getByMomentIdAndStaffId(request.getMomentsId(),request.getStaffId());
        if(momentsSendRecord==null){
            Assert.faild("数据异常，朋友圈发送失败，请稍后再试。");
        }
        if(momentsSendRecord.getStatus()==1){
            Assert.faild("当前朋友圈已经发送，不允许再次发送");
        }

        WxCpAddMomentTask task = new WxCpAddMomentTask();
        Text text = new Text();
        text.setContent(momentsDTO.getDescHtml());
        task.setText(text);

        VisibleRange visibleRange = new VisibleRange();
        List<String> sendList = new ArrayList<>();
        sendList.add(staffVO.getQiWeiUserId());

        SenderList senderList = new SenderList();
        senderList.setUserList(sendList);
        visibleRange.setSenderList(senderList);
        task.setVisibleRange(visibleRange);
        WxCpAddMomentResult wxCpAddMomentResult = null;

        if(CollUtil.isNotEmpty(attachments)){
            List<Attachment> attachmentList = new ArrayList<>();
            for (CpWelcomeAttachment attachment : attachments) {
                AttachmentExtDTO attachmentExtDTO = JSONObject.parseObject(attachment.getData(),AttachmentExtDTO.class);
                attachmentList.add(attachmentExtDTO.getAttachment());
            }
            task.setAttachments(attachmentList);
        }

        try {
            wxCpAddMomentResult = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().addMomentTask(task);
            log.info("调用成功,jobid:{}",wxCpAddMomentResult.getJobId());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }finally {
            log.info("调用发送朋友圈，执行参数：{},返回结果：{}", JSONObject.toJSONString(task),JSONObject.toJSONString(wxCpAddMomentResult));
        }

        //发送成功，更新jobid到发送记录表中更新发送状态
        distributionMomentsSendRecordService.doSend(momentsSendRecord.getId(),wxCpAddMomentResult.getJobId());

        //jobid：M0JY9D_jbdptyF8v4WKGrbb81egl_LaN7lFgR5InJ57LMcQ_Zt2Y05ePaCofVc9z

//        WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getExternalContactService().getMomentTaskResult()
    }

    @Override
    public void sendByMoment(Long id) {
        DistributionMoments momentsDTO = this.getById(id);
        Assert.isNull(momentsDTO,"朋友圈数据为空，请检查id是否正确！");
        List<CpWelcomeAttachment> attachments = attachmentService.listByWelId(id, OriginType.MOMENTS_CONFIG.getCode());

        List<DistributionMomentsSendRecord> momentsSendRecords = distributionMomentsSendRecordService.getByMomentId(id);

        List<String> sendList = momentsSendRecords.stream().map(DistributionMomentsSendRecord::getQiweiUserId).collect(Collectors.toList());

//        ServerResponseEntity<StaffVO> staffResponse = staffFeignClient.getStaffById(request.getStaffId());
//        if(staffResponse==null || staffResponse.isFail() || staffResponse.getData()==null ){
//            Assert.faild("员工查询异常，请稍后再试。");
//        }
//        StaffVO staffVO = staffResponse.getData();
//        if(StrUtil.isEmpty(staffVO.getQiWeiUserId())){
//            Assert.faild("员工数据异常，qiwei_user_id为空，请联系管理员。");
//        }
//
//        DistributionMomentsSendRecord momentsSendRecord = distributionMomentsSendRecordService.getByMomentIdAndStaffId(request.getMomentsId(),request.getStaffId());
//        if(momentsSendRecord==null){
//            Assert.faild("数据异常，朋友圈发送失败，请稍后再试。");
//        }
//        if(momentsSendRecord.getStatus()==1){
//            Assert.faild("当前朋友圈已经发送，不允许再次发送");
//        }

        WxCpAddMomentTask task = new WxCpAddMomentTask();
        Text text = new Text();
        text.setContent(momentsDTO.getDescHtml());
        task.setText(text);

        VisibleRange visibleRange = new VisibleRange();
//        List<String> sendList = new ArrayList<>();
//        sendList.add(staffVO.getQiWeiUserId());

        SenderList senderList = new SenderList();
        senderList.setUserList(sendList);
        visibleRange.setSenderList(senderList);
        task.setVisibleRange(visibleRange);
        WxCpAddMomentResult wxCpAddMomentResult = null;

        if(CollUtil.isNotEmpty(attachments)){
            List<Attachment> attachmentList = new ArrayList<>();
            for (CpWelcomeAttachment attachment : attachments) {
                AttachmentExtDTO attachmentExtDTO = JSONObject.parseObject(attachment.getData(),AttachmentExtDTO.class);
                attachmentList.add(attachmentExtDTO.getAttachment());
            }
            task.setAttachments(attachmentList);
        }

        try {
            wxCpAddMomentResult = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().addMomentTask(task);
            log.info("调用成功,jobid:{}",wxCpAddMomentResult.getJobId());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }finally {
            log.info("调用发送朋友圈，执行参数：{} ,可见人员：{}, 返回结果：{}", task.toJson(),task.getVisibleRange().toString(),JSONObject.toJSONString(wxCpAddMomentResult));
        }
        distributionMomentsMapper.updateJobId(id,wxCpAddMomentResult.getJobId());
    }

    /**
     * 定时任务处理超时未发送的朋友圈
     */
    @Override
    public void taskTimeOut() {
        List<DistributionMoments> timeOuts=distributionMomentsMapper.timeOutList();
        log.info("定时任务处理超时未发送的朋友圈:{}", JSON.toJSONString(timeOuts));
        if(CollUtil.isNotEmpty(timeOuts)){
            for (DistributionMoments timeOut : timeOuts) {
                try {
                    WxCpBaseResp baseResp = weixinCpExternalManager.cancelMomentTask(timeOut.getQwMomentsId());
                    log.info("定时任务超时未发送-停止发表企业朋友圈执行结束,参数：{},执行结果：{}",timeOut.getQwMomentsId(),JSONObject.toJSONString(baseResp));
                    if(baseResp.getErrcode()== 0){
                        distributionMomentsMapper.timeOut(timeOut.getId(),DistributionMonentsEnum.TIME_OUT_TERMINATE.getValue(),"超时未发送");
                    }
                } catch (WxErrorException e) {
                    log.error("定时任务超时未发送-停止发表企业朋友圈失败: {}",e.toString());
                }
            }
        }
    }
}
