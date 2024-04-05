package com.mall4j.cloud.biz.service.cp.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.wx.cp.constant.*;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.CustGroupAssignDetailPageDTO;
import com.mall4j.cloud.biz.dto.cp.ResignAssignLogQueryDTO;
import com.mall4j.cloud.biz.dto.cp.StaffAssginDTO;
import com.mall4j.cloud.biz.dto.cp.StaffAssginGroupDTO;
import com.mall4j.cloud.biz.mapper.cp.ResignAssignLogMapper;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.CustGroupAssignDetail;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import com.mall4j.cloud.biz.service.cp.CustGroupAssignDetailService;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.ResignAssignLogService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpUserTransferResultResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 离职分配日志表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ResignAssignLogServiceImpl implements ResignAssignLogService {
    private  final static String GROUP_ASSIGN_PRE = "BIZ_ASSIGN_GROUP:";
    private  final static int GROUP_ASSIGN_LIMIT = 300;

    private final  ResignAssignLogMapper resignAssignLogMapper;
    private final CustGroupAssignDetailService custGroupAssignDetailService;
    private final CustGroupService custGroupService;
//    private  final OnsMQTemplate staffSyncTemplate;
    private final StaffFeignClient staffFeignClient;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final CustGroupService groupService;
    private final WxCpPushService wxCpPushService;

    @Override
    public PageVO<ResignAssignLog> page(PageDTO pageDTO, ResignAssignLogQueryDTO request) {
        return PageUtil.doPage(pageDTO, () -> resignAssignLogMapper.list(request));
    }
    @Override
    public void update(ResignAssignLog resignAssignLog) {
        resignAssignLogMapper.update(resignAssignLog);
    }

    @Override
    public ResignAssignLog getById(Long id) {
        return resignAssignLogMapper.getById(id);
    }

    /**
     * 分配好友关系
     * @param staffVO 原添加客户
     * @param replaceByStaff 替换客户
     * @param request 分配信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignCust(StaffVO staffVO, StaffVO replaceByStaff, StaffAssginDTO request) {
        //获取员工所有客户列表
        List<UserStaffCpRelationListVO> custList = getCustByStaffId(staffVO.getId());
        log.info("执行分配好友关系======员工信息:【{}】 replaceByStaff:【{}】",(staffVO.getId()+staffVO.getStaffName()),Json.toJsonString(replaceByStaff));
        //没有客户
        if(CollectionUtils.isEmpty(custList)){

            log.info("执行分配好友关系 失败，根据员工【{}】未获取到客户数据",(staffVO.getId()+staffVO.getStaffName()));

//            ResignAssignLog resignAssignLog = new ResignAssignLog(request);
//            resignAssignLog.initStaffInfo(staffVO,replaceByStaff,0);
//            resignAssignLogMapper.save(resignAssignLog);

            return;
        }
        log.info("开始执行执行分配好友关系1======assignCust all========="+Json.toJsonString(custList));
        //如果是客户进来的则筛选出客户
        if(request.getCustIds()!=null){
            custList = custList.stream().filter(item-> request.getCustIds().stream().anyMatch(id -> id.equals(item.getQiWeiUserId()))).collect(Collectors.toList());
        }
        log.info("开始执行执行分配好友关系2=========staffId: 【{}】 replaceByStaff:【{}】 custList:【{}】",staffVO.getId(),Json.toJsonString(replaceByStaff),Json.toJsonString(custList));
        Map<Long, List<UserStaffCpRelationListVO>> replaceByInfo = Maps.newHashMap();
        for (UserStaffCpRelationListVO userStaffCpRelation : custList){
            Long staffId = null;
            //客户的导购来分配
            if(request.getType()== CustAssignType.SALE_MAN.getCode()){
                //如果当前客户的服务导购存在
                if(StringUtils.isNotEmpty(userStaffCpRelation.getServiceQiWeiStaffId())){
                    staffId = userStaffCpRelation.getStaffId();
                }
            }
            staffId = staffId==null?replaceByStaff.getId():staffId;
            //没有导购 给指定员工
            List<UserStaffCpRelationListVO> list = replaceByInfo.computeIfAbsent(staffId, k -> new ArrayList<>());
            list.add(userStaffCpRelation);
        }
        log.info("行执行分配好友关系======replaceByInfo========="+Json.toJsonString(replaceByInfo));
        Set<Map.Entry<Long,  List<UserStaffCpRelationListVO>>> set = replaceByInfo.entrySet();
        String companyName = WxCpConfigurationPlus.getCompanyName();
        for (Map.Entry<Long, List<UserStaffCpRelationListVO>> entry : set) {
            List<UserStaffCpRelationListVO> staffCustList = entry.getValue();
            ResignAssignLog resignAssignLog = new ResignAssignLog(request);
            if (entry.getKey() != replaceByStaff.getId().longValue()) {
                resignAssignLog.initStaffInfo(staffVO, null, staffCustList.size());
                UserStaffCpRelationListVO userStaffCpRelationListVO = staffCustList.get(0);
                resignAssignLog.setReplaceBy(userStaffCpRelationListVO.getServiceStaffId());
                resignAssignLog.setReplaceByUserId(userStaffCpRelationListVO.getServiceQiWeiStaffId());
                resignAssignLog.setReplaceStaffName(userStaffCpRelationListVO.getServiceStaffName());
//                resignAssignLog.setReplaceByStoreId(userStaffCpRelationListVO.getServiceStoreId());
//                resignAssignLog.setReplaceByStoreName(userStaffCpRelationListVO.getServiceStoreName());
            } else {
                resignAssignLog.initStaffInfo(staffVO, replaceByStaff, staffCustList.size());
                resignAssignLog.setReplaceByStoreId(replaceByStaff.getStoreId());
                resignAssignLog.setReplaceByStoreName(replaceByStaff.getStoreName());
            }
            resignAssignLog.setMsg(resignAssignLog.getMsg().replace("【员工姓名@公司名称】",resignAssignLog.getReplaceStaffName()+"@"+companyName));
            this.resignAssignLogMapper.save(resignAssignLog);

            //保存分配明细
            staffCustList.forEach(userStaff -> {
                CustGroupAssignDetail detail = new CustGroupAssignDetail(resignAssignLog, userStaff);
                detail.setStoreId(resignAssignLog.getReplaceByStoreId());
                detail.setStoreName(resignAssignLog.getReplaceByStoreName());
                detail.setType(AssignWayEunm.CUST.getCode());
                custGroupAssignDetailService.save(detail);
                custGroupAssignDetailService.updateUserStaffCpRelationStatus(detail.getCustGroupId(),resignAssignLog.getAddByUserId(),UserStaffCpRelationStatusEunm.ASSIGNING);
            });

        }
        log.info("结束执行分配好友关系=========success");
    }

    /**
     * 获取所有的客户信息
     */
    private  List<UserStaffCpRelationListVO>  getCustByStaffId(Long staffId){
        List<UserStaffCpRelationListVO> custList = new ArrayList<>();
        long pages =0;
        UserStaffCpRelationSearchDTO pageDTO = new UserStaffCpRelationSearchDTO();
        pageDTO.setPageNum(1);
        pageDTO.setPageSize(500);
        do {
            pageDTO.setStaffId(staffId);
            pageDTO.setStatus(1);
            ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> userStaffResponse = userStaffCpRelationFeignClient.pageWithStaff(pageDTO);
            log.info(pageDTO.getPageNum()+"-userStaffResponse---"+Json.toJsonString(userStaffResponse));
            if (userStaffResponse != null && userStaffResponse.getData() != null) {
                pages = userStaffResponse.getData().getPages();
                custList.addAll(userStaffResponse.getData().getList());
                pageDTO.setPageNum(pageDTO.getPageNum()+1);
                if(pageDTO.getPageNum()>pages){break;}
                log.info(pageDTO.getPageNum()+"-userStaffResponse pageDTO---"+Json.toJsonString(pageDTO));
            }
        }while (pages>0);
        return custList;
    }


    /**
     * 离职分配客群逻辑
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignGroup(StaffVO staffVO, StaffVO replaceByStaff, StaffAssginGroupDTO request) {
        //先获取原来员工的客群列表
        List<CpCustGroup> groups = groupService.getCustGroupListByOwnerId(staffVO.getId());
        log.info("执行离职分配客群逻辑assignGroup======groups ========="+Json.toJsonString(replaceByStaff));
        if (CollectionUtils.isEmpty(groups)) {
           /* ResignAssignLog resignAssignLog = new ResignAssignLog(request);
            resignAssignLog.initStaffInfo(staffVO,replaceByStaff,0);
            resignAssignLog.setPushStatus(1);
            resignAssignLog.setStatus(1);
            resignAssignLogMapper.save(resignAssignLog);*/

            log.info("执行离职分配客群逻辑assignGroup 失败，根据员工【{}】未获取到群数据",(staffVO.getId()+staffVO.getStaffName()));

            closeStaff(staffVO.getId(),staffVO.getQiWeiUserId(),AssignWayEunm.GROUP);

            return;
        }

        log.info("开始执行assignGroup=========staffId: 【{}】 replaceByStaff:【{}】 groups:【{}】",staffVO.getId(),Json.toJsonString(replaceByStaff),Json.toJsonString(groups));
        //筛选出制定分配群
        if(request.getCustIds()!=null){
            groups = groups.stream().filter(item-> request.getCustIds().stream().anyMatch(id -> id.equals(item.getId()))).collect(Collectors.toList());
        }
        //存在客群的员工
        //保存分配记录
        ResignAssignLog resignAssignLog = new ResignAssignLog(request);
        resignAssignLog.initStaffInfo(staffVO,replaceByStaff,groups.size());
        resignAssignLogMapper.save(resignAssignLog);
        groups.forEach(group ->{
            CustGroupAssignDetail detail = new CustGroupAssignDetail(resignAssignLog,null);
            detail.setNums(group.getTotalCust().intValue());
            detail.setCustGroupId(group.getId());
            detail.setName(group.getGroupName());
            detail.setType(AssignWayEunm.GROUP.getCode());
            custGroupAssignDetailService.save(detail);
        });
        groupService.updateBatchStatusToAssigning(groups.stream().map(CpCustGroup::getId).collect(Collectors.toList()));

        log.info("结束执行离职分配客群逻辑=========success");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void custExtends(ResignAssignLog assignLog) throws WxErrorException {
        PageDTO page = new PageDTO();
        page.setPageSize(500);
        page.setPageNum(1);
        CustGroupAssignDetailPageDTO request = new CustGroupAssignDetailPageDTO();
        request.setResignId(assignLog.getId());
        request.setStatus(0);
        request.setType(AssignWayEunm.CUST.getCode());
        int pages;
        do {
            PageVO<CustGroupAssignDetail> pageData = custGroupAssignDetailService.page(page, request);
            log.info(Json.toJsonString(page)+"========custExtends pageData==========="+Json.toJsonString(pageData));
            pages = pageData.getPages();
            if(!CollectionUtils.isEmpty(pageData.getList())) {
                custGroupAssignDetailService.syncCust(assignLog, pageData.getList(), AssignTypeEunm.CUST);
            }
        } while (pages > 0);
        assignLog.setPushStatus(PushStatusEunm.SUCCESS.getCode());
        this.resignAssignLogMapper.update(assignLog);
        //如果报错，则更新resignAssignLog的status状态，避免查询接替查询时一直报错
        this.resignAssignLogMapper.completeReplace(assignLog.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void custReplayResult(ResignAssignLog assignLog,AssignTypeEunm assignType) throws WxErrorException {
        String pageNo = null;
        WxCpUserTransferResultResp resp = null;
        log.info(assignType.getTxt()+"==============assignLog============="+Json.toJsonString(assignLog));
        do {
            if (assignType == AssignTypeEunm.CUST) {
                resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                        .getExternalContactService().transferResult(assignLog.getAddByUserId(), assignLog.getReplaceByUserId(), pageNo);
            }
            if (assignType == AssignTypeEunm.DIS_CUST) {
                resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                        .getExternalContactService().resignedTransferResult(assignLog.getAddByUserId(), assignLog.getReplaceByUserId(), pageNo);
            }
            log.info("==============resp=============" + Json.toJsonString(resp));
            log.info("执行企微分配客户状态查询 操作类型【{}】，结果【{}】" ,assignType.getTxt(), Json.toJsonString(resp.getCustomer()));
            if (resp != null) {
                List<WxCpUserTransferResultResp.TransferResult> results = resp.getCustomer();
                updateDetailReplayResult(results, assignLog);
                pageNo = resp.getNextCursor();
            } else {
                break;
            }
        } while (pageNo == null);

        //更新发送状态
        this.resignAssignLogMapper.completeReplace(assignLog.getId());
        //更新发送成功的数量
        this.resignAssignLogMapper.updateSuccess(assignLog.getId());
        //是否可以关闭
        if(assignType==AssignTypeEunm.DIS_CUST) {
            closeStaff(assignLog.getAddBy(),assignLog.getAddByUserId(),AssignWayEunm.CUST);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void custDimissionExtends(ResignAssignLog assignLog) throws WxErrorException {
        PageDTO page = new PageDTO();
        page.setPageSize(500);
        page.setPageNum(1);
        CustGroupAssignDetailPageDTO request = new CustGroupAssignDetailPageDTO();
        request.setResignId(assignLog.getId());
        request.setStatus(0);
        request.setType(AssignWayEunm.CUST.getCode());
        int pages;
        String assignType=AssignTypeEunm.get(assignLog.getAssignType()).getTxt();
        do {
            log.info(Json.toJsonString(page)+"操作类型【{}】=========custDimissionExtends request==============={}",assignType,Json.toJsonString(request));
            PageVO<CustGroupAssignDetail> pageData = custGroupAssignDetailService.page(page, request);
            log.info(Json.toJsonString(page)+"操作类型【{}】=========custDimissionExtends pageData==============={}",assignType,Json.toJsonString(pageData));
            pages = pageData.getPages();
            if(!CollectionUtils.isEmpty(pageData.getList())) {
                custGroupAssignDetailService.syncCust(assignLog, pageData.getList(), AssignTypeEunm.DIS_CUST);
            }
        } while (pages > 0);
        assignLog.setPushStatus(PushStatusEunm.SUCCESS.getCode());
        this.resignAssignLogMapper.update(assignLog);
        //如果报错，则更新resignAssignLog的status状态，避免查询接替查询时一直报错
        this.resignAssignLogMapper.completeReplace(assignLog.getId());
        log.info(Json.toJsonString(page)+"操作类型【{}】=========custDimissionExtends success===============ResignAssignLog id:{}",assignType,assignLog.getId());
    }

    //分配在职成员的客户群: https://developer.work.weixin.qq.com/document/path/95703
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void groupDimissionExtends(ResignAssignLog assignLog) {
        try {
            PageDTO page = new PageDTO();
            page.setPageSize(100);
            page.setPageNum(1);
            CustGroupAssignDetailPageDTO request = new CustGroupAssignDetailPageDTO();
            request.setResignId(assignLog.getId());
            request.setStatus(0);
            request.setType(AssignWayEunm.GROUP.getCode());
            int pages;

            //群分配同一个人每天不能分配超300条
            String redisKey = GROUP_ASSIGN_PRE+assignLog.getAddByUserId()+"_"+assignLog.getReplaceByUserId();
            Integer nums  = RedisUtil.get(redisKey);
            String nownums  = (nums==null?"":nums)+" redisKey:"+redisKey;
            String assignType=AssignTypeEunm.get(assignLog.getAssignType()).getTxt();
            log.info("操作类型【{}】->群分配同一个人每天不能分配超300条======当前nums:{} nownums:{}",assignType,nums,nownums);
            if(nums!=null && nums>=GROUP_ASSIGN_LIMIT){
                return;
            }
            nums = nums==null?1:nums;
            List<CustGroupAssignDetail> list ;
            do {
                PageVO<CustGroupAssignDetail> pageData = custGroupAssignDetailService.page(page, request);
                log.info("操作类型【{}】->======pageData:{}",assignType,Json.toJsonString(pageData));
                pages = pageData.getPages();
                if(!CollectionUtils.isEmpty(pageData.getList())) {
                    if(nums+pageData.getList().size()<=GROUP_ASSIGN_LIMIT){
                        list = pageData.getList();
                    }else{
                        list = pageData.getList().subList(0,GROUP_ASSIGN_LIMIT-nums);
                    }
                    nums=nums+list.size();
                    custGroupAssignDetailService.syncGroup(assignLog,list);
                    if(nums>=GROUP_ASSIGN_LIMIT) {
                        break;
                    }
                }
            } while (pages > 0);
            //放到redis
            RedisUtil.set(redisKey,nums,getExpireTimeSecond());
            //更新发送状态
            this.resignAssignLogMapper.completePush(assignLog.getId());
            this.resignAssignLogMapper.completeReplace(assignLog.getId());
            //更新发送成功的数量
            this.resignAssignLogMapper.updateSuccess(assignLog.getId());
            //是否可以关闭
            closeStaff(assignLog.getAddBy(),assignLog.getAddByUserId(),AssignWayEunm.GROUP);

            log.info(Json.toJsonString(page)+"操作类型【{}】=========groupDimissionExtends success===============ResignAssignLog id:{}",assignType,assignLog.getId());
        }catch (WxErrorException wx){
            log.error("分配离职客群失败: {}",wx);
        }
    }

    @Override
    public void groupExtends(ResignAssignLog assignLog) {

    }


    /**
     * 更新接替状态的员工  （缺少接替成功失败的批量同步接口）
     * @param results 接替结果
     * @param assignLog 分配日志id
     */
    private  void updateDetailReplayResult(List<WxCpUserTransferResultResp.TransferResult> results,ResignAssignLog assignLog){
        for(WxCpUserTransferResultResp.TransferResult item  :results){
            CustGroupAssignDetail detail =  custGroupAssignDetailService.selectAssignDetail(assignLog.getId(),item.getExternalUserid());
            log.info("更新接替状态的员工 updateDetailReplayResult======:"+ Json.toJsonString(results));
            log.info("detail======:"+ Json.toJsonString(detail));
            if(Objects.isNull(detail)){
                log.info("更新接替状态的员工失败updateDetailReplayResult 未获取到信息======: assignLog->{} getExternalUserid->{}",assignLog.getId(),item.getExternalUserid());
                continue;
            }
            if(detail.getStatus()!=AssignStatusEunm.ASSIGNING.getCode()){
                continue;
            }
            detail.setRemark(item.getStatus().toString());
            //非等待接替
            if(item.getStatus()!=WxCpUserTransferResultResp.STATUS.WAITING) {
                if(item.getStatus()==WxCpUserTransferResultResp.STATUS.COMPLETE){
                    detail.setStatus(AssignStatusEunm.SUCCESS.getCode());
                    //离职继承成功去掉关联
                    if(assignLog.getAssignType()==AssignTypeEunm.DIS_CUST.getCode()) {
                        custGroupAssignDetailService.updateUserStaffCpRelationStatus(detail.getCustGroupId(),assignLog.getAddByUserId(),UserStaffCpRelationStatusEunm.DEL);
                        wxCpPushService.serviceChangeNotify(detail.getCustGroupId(),detail.getReplaceByUserId());
                    }
                }else{
                    detail.setStatus(AssignStatusEunm.FAIL.getCode());
                    custGroupAssignDetailService.updateUserStaffCpRelationStatus(detail.getCustGroupId(),assignLog.getAddByUserId(),UserStaffCpRelationStatusEunm.BIND);
                }
            }
            log.info("更新接替状态的员工 detail==end====:"+ Json.toJsonString(detail));
            custGroupAssignDetailService.update(detail);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reAssign(CustGroupAssignDetail detail) {
        ResignAssignLog resignAssignLog =this.resignAssignLogMapper.getById(detail.getResignId());
        resignAssignLog.setPushStatus(PushStatusEunm.CREATE.getCode());
        resignAssignLog.setStatus(0);
        this.update(resignAssignLog);
        custGroupAssignDetailService.reAssign(detail);
        log.info("=====reAssign======"+Json.toJsonString(resignAssignLog));
        if(detail.getType()==AssignWayEunm.CUST.getCode()) {
            custGroupAssignDetailService.updateUserStaffCpRelationStatus(detail.getCustGroupId(), resignAssignLog.getAddByUserId(), UserStaffCpRelationStatusEunm.ASSIGNING);
        }
        if(detail.getType()==AssignWayEunm.GROUP.getCode()) {
            CpCustGroup group =  new CpCustGroup();
            group.setStatus(2);
            group.setId(detail.getCustGroupId());
            groupService.updateById(group);
        }
    }


    @Override
    public List<ResignAssignLog> sycnCustList( PushStatusEunm pushStatusEunm) {
        return resignAssignLogMapper.sycnCustList(pushStatusEunm.getCode());
    }

    /**
     * 检测员工离职还有没需分配的客户，如果没有，将离职员工状态更新，避免在查询时还显示在离职员工列表中
     * @param staffId 员工id
     * @param staffUserId 员工企业微信id
     */
    private  void closeStaff(Long staffId,String staffUserId,AssignWayEunm assignWay){
        try {
            boolean canClose = false;
            if(assignWay==AssignWayEunm.GROUP) {
                UserStaffCpRelationSearchDTO pageDTO = new UserStaffCpRelationSearchDTO();
                pageDTO.setPageNum(1);
                pageDTO.setPageSize(1);
                pageDTO.setStaffId(staffId);
                ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> userStaffResponse = userStaffCpRelationFeignClient.pageWithStaff(pageDTO);
                canClose = userStaffResponse!=null&&CollectionUtils.isEmpty(userStaffResponse.getData().getList());
            }
            if(assignWay==AssignWayEunm.CUST) {
                List<CpCustGroup> groups =  custGroupService.getCustGroupListByOwnerId(staffId);
                canClose = CollectionUtils.isEmpty(groups);
            }
            log.info("canClose======:"+ canClose);
            if(canClose) {
                //发条Mq更改员工的状态
                StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
                staffSyncDTO.setQiweiUserId(staffUserId);
                staffSyncDTO.setQiweiUserStatus(QiweiUserStatus.REMOVE.getCode());
//                staffSyncTemplate.syncSend(staffSyncDTO);
                staffFeignClient.staffSyncMessage(staffSyncDTO);
            }
        }catch (Exception e){
            log.error("关闭离职会员出错",e);
        }

    }



    /**
     * 获取第二天的过期时间
     * @return
     */
    private long getExpireTimeSecond(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return (calendar.getTime().getTime()-date.getTime())/1000;
    }
}
