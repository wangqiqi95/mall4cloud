package com.mall4j.cloud.biz.service.cp.impl;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.task.CpCustGroupTask;
import com.mall4j.cloud.biz.wx.cp.constant.AssignStatusEunm;
import com.mall4j.cloud.biz.wx.cp.constant.AssignTypeEunm;
import com.mall4j.cloud.biz.wx.cp.constant.UserStaffCpRelationStatusEunm;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.CustGroupAssignDetailPageDTO;
import com.mall4j.cloud.biz.dto.cp.wx.WxBizCpUserExternalGroupChatTransferResp;
import com.mall4j.cloud.biz.mapper.cp.CustGroupAssignDetailMapper;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.CustGroupAssignDetail;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import com.mall4j.cloud.biz.service.cp.CustGroupAssignDetailService;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.WxCpGroupChatService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpUserTransferCustomerReq;
import me.chanjar.weixin.cp.bean.external.WxCpUserTransferCustomerResp;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 客群分配明细表 
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustGroupAssignDetailServiceImpl implements CustGroupAssignDetailService {
    private final  CustGroupAssignDetailMapper custGroupAssignDetailMapper;
//    private  final OnsMQTemplate qiWeiFriendsSyncTemplate;
    private final CustGroupService custGroupService;
    private final WxCpGroupChatService wxCpGroupChatService;
    private final StaffFeignClient staffFeignClient;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final CpCustGroupTask cpCustGroupTask;

    @Override
    public PageVO<CustGroupAssignDetail> page(PageDTO pageDTO, CustGroupAssignDetailPageDTO request) {
        PageVO<CustGroupAssignDetail> pageVO=PageUtil.doPage(pageDTO, () -> custGroupAssignDetailMapper.list(request));
        //接替员工部门
        List<Long> staffIds=pageVO.getList().stream().map(item->item.getReplaceBy()).collect(Collectors.toList());
        List<String> userId=pageVO.getList().stream().map(item->item.getCustGroupId()).collect(Collectors.toList());

        ServerResponseEntity<List<StaffOrgVO>> responseEntity= staffFeignClient.getStaffAndOrgs(staffIds);
        ServerResponseEntity.checkResponse(responseEntity);

        UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
        staffCpRelationSearchDTO.initPage();
        staffCpRelationSearchDTO.setQiWeiUserIds(userId);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> UserStaffCpRelationResponse=userStaffCpRelationFeignClient.getUserStaffRelBy(staffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(UserStaffCpRelationResponse);

        Map<Long,List<StaffOrgVO>> staffOrgMap= LambdaUtils.groupList(responseEntity.getData(),StaffOrgVO::getStaffId);
        Map<String,UserStaffCpRelationListVO> userRelVOMap= LambdaUtils.toMap(UserStaffCpRelationResponse.getData(),UserStaffCpRelationListVO::getQiWeiUserId);
        for (CustGroupAssignDetail detail : pageVO.getList()) {
            detail.setStaffOrgs(staffOrgMap.get(detail.getReplaceBy()));

            //客户信息：手机号
            if(userRelVOMap.containsKey(detail.getCustGroupId())){
                detail.setMobile(userRelVOMap.get(detail.getCustGroupId()).getCpRemarkMobiles());
            }
        }
        return pageVO;
    }

    @Override
    public CustGroupAssignDetail getById(Long id) {
        return custGroupAssignDetailMapper.getById(id);
    }

    @Override
    public void save(CustGroupAssignDetail custGroupAssignDetail) {
        custGroupAssignDetailMapper.save(custGroupAssignDetail);
    }

    @Override
    public void update(CustGroupAssignDetail custGroupAssignDetail) {
        this.custGroupAssignDetailMapper.update(custGroupAssignDetail);
    }

    @Override
    public void reAssign(CustGroupAssignDetail detail) {
        custGroupAssignDetailMapper.update(detail);
    }

    @Override
    public CustGroupAssignDetail selectAssignDetail(Long id, String externalUserid) {
        CustGroupAssignDetailPageDTO request = new CustGroupAssignDetailPageDTO();
        request.setResignId(id);
        request.setCustGroupId(externalUserid);
        List<CustGroupAssignDetail> list = custGroupAssignDetailMapper.list(request);
        if(CollUtil.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }


    //------------------------------分配详情同步微信----------------------------------
    @Override
    public void syncCust(ResignAssignLog assignLog,List<CustGroupAssignDetail> details,AssignTypeEunm assignType)  {
        log.info("= syncCust details===" + Json.toJsonString(details));
        WxCpUserTransferCustomerReq req = new WxCpUserTransferCustomerReq();
        req.setHandOverUserid(assignLog.getAddByUserId());
        req.setTakeOverUserid(assignLog.getReplaceByUserId());
        req.setExternalUserid(details.stream().map(item -> item.getCustGroupId()).collect(Collectors.toList()));
        WxCpUserTransferCustomerResp resp = null;
        log.info("=syncCust req===" + Json.toJsonString(req));
        try {
               if(AssignTypeEunm.CUST == assignType) {
                    req.setTransferMsg(assignLog.getMsg());
                    resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().transferCustomer(req);
               }
               if(AssignTypeEunm.DIS_CUST == assignType) {
                    resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().resignedTransferCustomer(req);
               }
               Map<String,CustGroupAssignDetail> detailsMap = details.stream().collect(Collectors.toMap(CustGroupAssignDetail::getCustGroupId,Function.identity()));
               log.info("执行企微分配客户 操作类型【】，结果【{}】" ,assignType.getTxt(), Json.toJsonString(resp.getCustomer()));
               for (WxCpUserTransferCustomerResp.TransferCustomer customer : resp.getCustomer()){
                    CustGroupAssignDetail detail = detailsMap.get(customer.getExternalUserid());
                    if(detail!=null){
                        detail.setAssignTime(new Date());
                        detail.setStatus(customer.getErrcode()==0?AssignStatusEunm.ASSIGNING.getCode():AssignStatusEunm.FAIL.getCode());
                        detail.setRemark(customer.getErrcode()==0?"":customer.getErrcode().toString());
                        custGroupAssignDetailMapper.update(detail);
                        if(customer.getErrcode()!=0) {
                            updateUserStaffCpRelationStatus(detail.getCustGroupId(), detail.getAddByUserId(), UserStaffCpRelationStatusEunm.BIND);
                        }
                    }
               }
        }catch (WxErrorException wxe){
            details.forEach(detail->{
                detail.setStatus(AssignStatusEunm.FAIL.getCode());
                detail.setRemark(wxe.getError().getErrorCode()+"-"+wxe.getError().getErrorMsg());
                custGroupAssignDetailMapper.update(detail);
                updateUserStaffCpRelationStatus(detail.getCustGroupId(),detail.getAddByUserId(),UserStaffCpRelationStatusEunm.BIND);
            });
        }
    }


    @Override
    public void syncGroup(ResignAssignLog assignLog, List<CustGroupAssignDetail> groups) throws WxErrorException{
        log.info(assignLog.getReplaceByUserId()+"======groups:"+ Json.toJsonString(groups));
        log.info("在职离职-执行调用企微分配客群接口入参信息--> assignLog:【{}】,groups: 【{}】",Json.toJsonString(assignLog),Json.toJsonString(groups));
        //发送微信进行离职继承
        String[] chatIds=groups.stream().map(item->item.getCustGroupId()).collect(Collectors.toList()).toArray(new String[0]);
        WxBizCpUserExternalGroupChatTransferResp resp =  null;
        if(assignLog.getAssignType()==AssignTypeEunm.CUST_GROUP.getCode()){//在职分配-客群
            resp=wxCpGroupChatService.onjobTransferGroupChat(chatIds, assignLog.getReplaceByUserId());
        }else if(assignLog.getAssignType()==AssignTypeEunm.DIS_GROUP.getCode()){//离职分配-客群
            resp=wxCpGroupChatService.transferGroupChat(chatIds, assignLog.getReplaceByUserId());
        }
        log.info("在职离职-执行企微分配客群 操作类型【{}】，结果【{}】" ,AssignTypeEunm.get(assignLog.getAssignType()).getTxt(), Json.toJsonString(resp));
        for (CustGroupAssignDetail detail : groups) {
            detail.setStatus(AssignStatusEunm.SUCCESS.getCode());
            //失败的
            if(resp.getFailedChatList()!=null){
                long nums = resp.getFailedChatList().stream().filter(item->item.getChatId().equals(detail.getCustGroupId())).count();
                if(nums>0) {
                    CpCustGroup group = new CpCustGroup();
                    group.setId(detail.getCustGroupId());
                    group.setStatus(1);
                    custGroupService.updateById(group);
                    detail.setStatus(AssignStatusEunm.FAIL.getCode());
                }
            }
            detail.setAssignTime(new Date());
            this.custGroupAssignDetailMapper.update(detail);
        }
        //TODO 更新群主信息
        if(resp.getErrcode()==0 && chatIds!=null){
            log.info("在离职分配客户群成功，分配失败的群信息：getFailedChatList:{}  chatIds:{}", JSON.toJSONString(resp.getFailedChatList()),chatIds);
           Map<String,WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer> failedTransferMap=LambdaUtils.toMap(resp.getFailedChatList(),
                   WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer::getChatId);
           List<String> Ids=Arrays.asList(chatIds);
           List<String> updateIds=new ArrayList<>();
            for (String id : Ids) {
                if(!failedTransferMap.containsKey(id)){
                    updateIds.add(id);
                }
            }
            if(CollUtil.isNotEmpty(updateIds)){
                log.info("在离职分配客户群成功，需要更新群主信息：{}", JSON.toJSONString(updateIds));
                cpCustGroupTask.refreshCustGroupOwner(updateIds);
            }
        }
        log.info("在职离职-执行调用企微接口======success:{}");
    }

    @Override
    public List<CustGroupAssignDetail> getGroupSycnList() {
        return custGroupAssignDetailMapper.getGroupSycnList();
    }


    public void updateUserStaffCpRelationStatus(String externalUserId,String userId,UserStaffCpRelationStatusEunm status) {
        UserStaffCpRelationDTO userStaffCpRelationDTO = new UserStaffCpRelationDTO();
        //客户外部联系人id
        userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
        //员工的企业微信id
        userStaffCpRelationDTO.setQiWeiStaffId(userId);
        //客户unionId
        userStaffCpRelationDTO.setStatus(status.getCode());
//        if (userStaffCpRelationDTO.getStatus() == 2) {//解绑
//            userStaffCpRelationDTO.setContactChangeType(ContactChangeTypeEnum.BIND_OUT.getDesc());
//        }
//        if (userStaffCpRelationDTO.getStatus() == 1) {//绑定
//            userStaffCpRelationDTO.setContactChangeType(ContactChangeTypeEnum.ADD_EXTERNAL_CONTACT.getDesc());
//        }
        log.info("==userStaffCpRelationDTO==="+Json.toJsonString(userStaffCpRelationDTO));
        userStaffCpRelationFeignClient.updateUserStaffCpRelationStatus(userStaffCpRelationDTO);
//        qiWeiFriendsSyncTemplate.syncSend(userStaffCpRelationDTO);
    }



}
