package com.mall4j.cloud.platform.manager;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.api.biz.feign.CrmFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.user.WxCpDeptUserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 企微信息同步：部门、员工
 */
@Slf4j
@Component
@RefreshScope
public class WxCpManager {

    @Autowired
    private WxCpApiFeignClient wxCpApiFeignClient;

    @Autowired
    private StaffService staffService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CrmFeignClient crmFeignClient;

    @Value("${scrm.platform.pushdepart}")
    private boolean pushdepart=false;

    /**
     * 企微部门
     * access_token	是	调用接口凭证
     * id	否	部门id。获取指定部门及其下的子部门（以及子部门的子部门等等，递归）。 如果不填，默认获取全量组织架构
     */
    public void getDeparts(){
        try {
            /**
             * 获取系统部门
             */
//            List<Organization> orgList=organizationMapper.selectList(new QueryWrapper<>());
//            Map<Long, Organization> orgMap= LambdaUtils.toMap(orgList,Organization::getOrgId);


            /**
             * 获取企微部门
             */
            ServerResponseEntity<List<WxCpDepart>> responseEntity= wxCpApiFeignClient.wxCpDepartList(null);
            ServerResponseEntity.checkResponse(responseEntity);

            List<WxCpDepart> wxCpDeparts= responseEntity.getData();
            if(CollUtil.isNotEmpty(wxCpDeparts)){
                List<Organization> orgs=new ArrayList<>();
                Map<Long,WxCpDepart> wxCpDepartMap= LambdaUtils.toMap(wxCpDeparts,WxCpDepart::getId);
                for (WxCpDepart wxCpDepart : wxCpDeparts) {
                    Organization organization=new Organization();
                    organization.setCreateTime(new Date());
                    organization.setCreateBy("系统同步");
                    organization.setUpdateTime(new Date());
                    organization.setUpdateBy("系统同步");
                    organization.setOrgId(wxCpDepart.getId());
                    organization.setParentId(wxCpDepart.getParentId());
                    organization.setOrgName(wxCpDepart.getName());
                    organization.setNameEn(wxCpDepart.getEnName());
                    organization.setOrderNum(""+wxCpDepart.getOrder());
                    organization.setDepartmentLeader(Arrays.toString(wxCpDepart.getDepartmentLeader()));

                    List parents=new ArrayList();
                    getParanteId(parents,wxCpDepart.getId(),wxCpDepartMap);
                    Collections.sort(parents);
                    organization.setPath(StringUtils.join(parents,","));
                    orgs.add(organization);
//                    if(orgMap.containsKey(wxCpDepart.getId())){
//                        //需要保存
//
//                    }else{
//                        //需要更新
//
//                    }
                }
                System.out.println(JSON.toJSONString(orgs));
                organizationService.saveOrUpdateBatch(orgs,100);

                //TODO 推送给数云
                if(pushdepart){
                    for (WxCpDepart wxCpDepart : wxCpDeparts) {
                        PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
                        dto.setMsgType("1");
                        dto.setChangetype("create_department");
                        dto.setDepart(wxCpDepart);
                        crmFeignClient.pushCDPCpMsg(dto);
                    }
                }
            }

        }catch (Exception e){
            log.info("企微部门拉取失败：{}",e);
        }
    }

    private List getParanteId(List list,Long id,Map<Long,WxCpDepart> wxCpDepartMap){
        if(wxCpDepartMap.containsKey(id)){
            WxCpDepart depart=wxCpDepartMap.get(id);
            list.add(depart.getId());
            if(wxCpDepartMap.containsKey(depart.getParentId())){
                return getParanteId(list,depart.getParentId(),wxCpDepartMap);
            }
        }
        return list;
    }


    /**
     * 企微员工
     */
    public void getStaffs(){
        try {
            /**
             * 获取系统员工
             */
            LambdaQueryWrapper<Staff> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Staff::getIsDelete,0);
            List<Staff> staffList=staffService.list(lambdaQueryWrapper);
            Map<String,Staff> saffMaps= LambdaUtils.toMap(staffList,Staff::getMobile);

            /**
             * 获取企微员工
             */
            String cursor="";//用于分页查询的游标，字符串类型，由上一次调用返回，首次调用不填
            Integer limit=10000;//分页，预期请求的数据量，取值范围 1 ~ 10000
            ServerResponseEntity<WxCpDeptUserResult> responseEntity=wxCpApiFeignClient.wxCpUserListId(cursor,limit);
            ServerResponseEntity.checkResponse(responseEntity);

            WxCpDeptUserResult userResult= responseEntity.getData();//获取成员ID列表
            if(!checkUserResult(userResult,JSON.toJSONString("cursor:"+cursor+" limit:"+limit))){
                return;
            }

            List<Staff> staffs=new ArrayList<>();
            for (WxCpDeptUserResult.DeptUserList deptUserList : userResult.getDeptUser()) {
                //获取成员详情
                ServerResponseEntity<WxCpUser> response=wxCpApiFeignClient.wxCpByUserId(deptUserList.getUserId());
                ServerResponseEntity.checkResponse(response);
                WxCpUser wxCpUser=response.getData();
                if(!checkCpUser(wxCpUser,"userId:"+deptUserList.getUserId())){
                    continue;
                }
                addStaffs(saffMaps,wxCpUser,staffs);
            }

            //TODO 处理下一页数据
//            if(userResult.getNextCursor()){
//
//            }

            if(CollUtil.isNotEmpty(staffs)){
                staffService.saveOrUpdateBatch(staffs,100);
            }

        }catch (Exception e){
            log.info("企微员工拉取失败：{}",e);
        }
    }

    private void addStaffs(Map<String,Staff> saffMaps,WxCpUser wxCpUser,List<Staff> staffs){
        if(saffMaps.containsKey(wxCpUser.getMobile())){
            //更新
            Staff staff=saffMaps.get(wxCpUser.getMobile());
            staff.setQiWeiUserId(wxCpUser.getUserId());
            staff.setQiWeiUserStatus(wxCpUser.getStatus());
            staff.setOrgId(""+wxCpUser.getDepartIds());
            staff.setAvatar(wxCpUser.getAvatar());
            staff.setPosition(wxCpUser.getPosition());
            staff.setEmail(wxCpUser.getEmail());
            staff.setStaffCode(wxCpUser.getUserId());
            staff.setStaffNo(wxCpUser.getUserId());
            staff.setGender(wxCpUser.getGender().getCode());
            staff.setOpenUserId(wxCpUser.getOpenUserId());
            staff.setUpdateTime(new Date());
            staff.setUpdateBy("系统同步");
            staffs.add(staff);
        }else{
            //保存
            Staff staff=new Staff();
            staff.setId(null);
            staff.setMobile(wxCpUser.getMobile());
            staff.setQiWeiUserId(wxCpUser.getUserId());
            staff.setQiWeiUserStatus(wxCpUser.getStatus());
            staff.setOrgId(""+wxCpUser.getDepartIds());
            staff.setAvatar(wxCpUser.getAvatar());
            staff.setPosition(wxCpUser.getPosition());
            staff.setEmail(wxCpUser.getEmail());
            staff.setStaffCode(wxCpUser.getUserId());
            staff.setStaffNo(wxCpUser.getUserId());
            staff.setGender(wxCpUser.getGender().getCode());
            staff.setOpenUserId(wxCpUser.getOpenUserId());
            staff.setCreateTime(new Date());
            staff.setCreateBy("系统同步");
            staffs.add(staff);
        }
    }



    private boolean checkUserResult(WxCpDeptUserResult userResult,String params){
        if(userResult.getErrcode().equals("0") && CollUtil.isNotEmpty(userResult.getDeptUser())){
            log.info("获取成员ID列表成功，参数:{} params 结果:{} ", JSON.toJSON(userResult.getDeptUser()));
            return true;
        }
        log.info("获取成员ID列表失败，参数:{} 错误码:{} 错误信息:{}",params,userResult.getErrcode(),userResult.getErrmsg());
        return false;
    }

    private boolean checkCpUser(WxCpUser wxCpUser,String params){
        if(Objects.nonNull(wxCpUser)){
            log.info("获取成员详情成功：{}",JSON.toJSON(wxCpUser));
            return true;
        }
        log.info("获取成员详情失败：{}",JSON.toJSON(wxCpUser));
        return false;
    }
}
