package com.mall4j.cloud.biz.service.cp.handler.plus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.crm.response.CrmResult;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.wx.externalcontact.SendWelcomeDTO;
import com.mall4j.cloud.biz.mapper.cp.CpWelcomeTimeStateMapper;
import com.mall4j.cloud.biz.mapper.cp.CpWelcomeUseRecordMapper;
import com.mall4j.cloud.biz.mapper.cp.WelcomeMapper;
import com.mall4j.cloud.biz.model.cp.CpWelcome;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.CpWelcomeUseRecord;
import com.mall4j.cloud.biz.service.cp.MaterialService;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;
import com.mall4j.cloud.biz.service.cp.WelcomeService;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeTimeStateVO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpWelcomeMsg;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @Description: 企业微信添加客户，接受消息处理服务【渠道逻辑处理】
 * @Date: 2022-02-10 17:28
 * @Version: 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class CpExternalChannelPlusHandler {

    private final  StaffFeignClient staffFeignClient;
    private final CpAutoGroupCodePlusHanlder autoGroupCodeHanlder;
    private final CpStaffCodePlusHandler staffCodeHandler;

    private  final WelcomeAttachmentService attachmentService ;
    private  final CpWelcomeTimeStateMapper welcomeTimeStateMapper;
    private final MaterialService materialService;
    private  final WelcomeService welcomeService;
    private final CpWelcomeUseRecordMapper welcomeUseRecordMapper;
    private final CrmUserFeignClient crmUserFeignClient;
    private final WelcomeMapper welcomeMapper;

    /**
     * 外部联系人渠道处理
     */
    public void haneleExternalState(SendWelcomeDTO dto) {
        //员工信息
        StaffVO staffVO=getStaffVOByUserId(dto.getWxMessage().getUserId());
        log.info("外部联系人渠道处理，获取到员工信息：{}",JSON.toJSONString(staffVO));
        if(Objects.isNull(staffVO)){
            log.info("外部联系人渠道处理失败，未获取到员工信息：");
            return;
        }
        dto.setStaffVO(staffVO);

        if(Objects.isNull(dto.getCpCodeChannel())){
            sendStaffMsg(dto);
            log.info("外部联系人渠道处理失败，渠道源为空");
            return;
        }

        if(dto.getCpCodeChannel().getSourceFrom()== CodeChannelEnum.AUTO_GROUP_CODE.getValue()){
            //自动拉群加好友
            autoGroupCodeHanlder.handleAutoGroupCode(dto);
        }else if(dto.getCpCodeChannel().getSourceFrom()== CodeChannelEnum.CHANNEL_CODE.getValue()){
            //渠道活码
            staffCodeHandler.sendWelcomeMsgByStaffCode(dto);//发送欢迎语

            //欢迎语类型：0通用渠道/1默认员工/不发欢迎语
            if(Objects.nonNull(dto.getWelcomeType())){
                if(dto.getWelcomeType()==1){
                    sendStaffMsg(dto);
                }
            }
        }
    }

    /**
     * 发送个人欢迎语
     * @param dto
     */
    private void sendStaffMsg(SendWelcomeDTO dto){
        WxCpXmlMessage wxMessage=dto.getWxMessage();
        WxCpExternalContactInfo wxCpExternalContactInfo=dto.getWxCpExternalContactInfo();
        StaffVO staffVO=dto.getStaffVO();

        String welcomeCode=wxMessage.getWelcomeCode();//欢迎语CODE
        String userName= Objects.nonNull(wxCpExternalContactInfo)?wxCpExternalContactInfo.getExternalContact().getName():"";//客户昵称
        String moblie = Objects.nonNull(dto.getFollowedUser().getRemarkMobiles())?JSON.toJSONString(dto.getFollowedUser().getRemarkMobiles()):null;//手机号码

        WxCpWelcomeMsg wxCpWelcomeMsg=getWxCpWelcomeMsg(staffVO,userName,moblie,wxCpExternalContactInfo);
        if(wxCpWelcomeMsg!=null) {
            wxCpWelcomeMsg.setWelcomeCode(welcomeCode);
            log.info("----发送个人欢迎语欢迎语----{}",wxCpWelcomeMsg.toJson());
            try {
                WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().sendWelcomeMsg(wxCpWelcomeMsg);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
            log.info("----发送个人欢迎语欢迎语 success----{}",wxCpWelcomeMsg.toJson());
        }else{
            log.info("----发送个人欢迎语欢迎语 faile----未获取到欢迎语数据");
        }
    }

    private boolean isRegister(String unionId){
        boolean flag=false;
        try {
            if(StrUtil.isEmpty(unionId)){
                return false;
            }
            String active=null;
            String mobileNumber=null;
            ServerResponseEntity<CrmResult<JSONArray>> responseEntity=crmUserFeignClient.editageUser(unionId,active,mobileNumber);
            log.info("获取数据用户数据，入参：unionId：{} 结果：{}",unionId,JSON.toJSONString(responseEntity));
            if(responseEntity.isFail() || Objects.isNull(responseEntity.getData())){
                return false;
            }
            CrmResult<JSONArray> crmResult=responseEntity.getData();
            log.info("获取数据用户数据，入参：unionId：{} 结果：{}",unionId,JSON.toJSONString(crmResult));
            if(Objects.nonNull(crmResult)){
                JSONArray jsonArray=crmResult.getResult();
                if(Objects.isNull(jsonArray) || CollUtil.isEmpty(jsonArray)){
                    return false;
                }
                Object object = jsonArray.get(0);
                JSONObject jsonObject = (JSONObject) JSON.toJSON(object); // 优化为 通过JSON.toJSON转为JSONObject
                /**
                 * 欢迎语是否已注册
                 * 已注册：查数云用户数据，eos_user表的 client_code，不为空。
                 * 未注册：查数云用户数据，eos_user表的 client_code，空
                 */
                if(Objects.nonNull(jsonObject) && StrUtil.isNotEmpty(jsonObject.getString("client_code"))){
                    flag=true;
                }
            }
        }catch (Exception e){
            log.info("发送个人欢迎语校验是否注册 失败：{}",e);
            return false;
        }

        return flag;
    }

    /**
     * 获取商店欢迎语配置信息
     * @param staffVO 员工信息
     * @return
     */
    private  WxCpWelcomeMsg  getWxCpWelcomeMsg(StaffVO staffVO,String userName,String moblie,WxCpExternalContactInfo wxCpExternalContactInfo){

        //todo 调用接口查询用户是否注册
        boolean isRegister=isRegister(wxCpExternalContactInfo.getExternalContact().getUnionId());

        //场景 0未注册，1注册
        Integer scene = isRegister?1:0;
        log.info("发送个人欢迎语欢迎语 是否注册：scene:{} isRegister:{}",scene,isRegister);
        //非员工活码进入 根据添加客户的员工的店来查
        ServerResponseEntity<List<Long>> responseEntity=staffFeignClient.getOrgAndChildByStaffIds(Arrays.asList(staffVO.getId()));
        if(responseEntity.isFail()){
            log.error(StrUtil.format("当前员工【{}】获取部门信息失败，发送欢迎语失败。",staffVO.getStaffName()));
            return null;
        }
        List<Long> orgs =responseEntity.getData();
        if(CollUtil.isEmpty(orgs)){
            log.error(StrUtil.format("当前员工【{}】未配置部门，发送欢迎语失败。",staffVO.getStaffName()));
            return null;
        }
        orgs.add(staffVO.getId());//适用人员可配置员工
        log.info("当前员工【{}】获取到部门信息: {}",staffVO.getStaffName(),JSON.toJSONString(orgs));

        CpWelcome welcome = welcomeMapper.getWelcomeByOrgs(orgs,scene,0);
//        CpWelcome welcome = welcomeService.getWelcomeByOrgs(orgs,scene,0);
        if(welcome==null){
            log.info("发送个人欢迎语欢迎语--员工【"+staffVO.getStaffName()+"】尚未配置欢迎语");
            return null;
        }

        //todo 保存欢迎语使用信息
        CpWelcomeUseRecord useRecord = new CpWelcomeUseRecord();
        useRecord.setUserUnionId(wxCpExternalContactInfo.getExternalContact().getUnionId());
        useRecord.setWelId(welcome.getId());
        useRecord.setStaffId(staffVO.getId());
        useRecord.setStaffName(staffVO.getStaffName());
        useRecord.setNickName(userName);
        useRecord.setPhone(moblie);
        welcomeUseRecordMapper.save(useRecord);

        List<CpWelcomeAttachment> welcomeAttachment=attachmentService.listByWelId(welcome.getId(), OriginType.WEL_CONFIG.getCode());
        String slogan="";
        boolean timeMsg=false;//是否发送分时段欢迎语
        //判断当前欢迎语是否有设置分时段欢迎语。 如果设置分时段欢迎语 并且命中时间段内时间条件，按单独维护的欢迎语发送欢迎语
        if(welcome.getWelcomeTimeState()==1){
            List<CpWelcomeTimeStateVO> timeStateVOS = welcomeTimeStateMapper.listByWellId(welcome.getId());
            CpWelcomeTimeStateVO welcomeTimeState = null;
            //获取当前是星期几
            /**
             * 周日-1/周1-2/周2-3/周3-4/周4-5/周5-6/周6-7
             */
            Date now=new Date();
            long nowTime= DateUtil.parse(DateUtil.format(now,"HH:mm"),"HH:mm").getTime();
            int dayWeek=DateUtil.dayOfWeek(now);//当前周几
            for (CpWelcomeTimeStateVO welcomeTimeStateVO : timeStateVOS) {
                //匹配周条件
                Map<String,String> timesMap= LambdaUtils.toMap(Arrays.asList(welcomeTimeStateVO.getTimeCycle().split(",")), item->item);
                if(!timesMap.containsKey(String.valueOf(dayWeek))){
                    continue;
                }
                //匹配时间段条件
                Long startTime=DateUtil.parse(welcomeTimeStateVO.getTimeStart(),"HH:mm").getTime();
                Long endTime=DateUtil.parse(welcomeTimeStateVO.getTimeEnd(),"HH:mm").getTime();
                if(nowTime<startTime || nowTime>endTime){//时间未到/时间超出
                    continue;
                }
                welcomeTimeState = welcomeTimeStateVO;
                break;
            }
            if(welcomeTimeState!=null){
                timeMsg=true;
                //判断当前欢迎语是否有设置分时段欢迎语。 如果没有分时段欢迎语
                slogan= SlognUtils.getSlogan(staffVO.getStaffName(),welcomeTimeState.getSlogan(),userName);
                CpWelcomeTimeStateVO finalWelcomeTimeState = welcomeTimeState;
                welcomeAttachment = welcomeAttachment.stream().filter(s->s.getTimeStateId()==finalWelcomeTimeState.getId()).collect(Collectors.toList());
            }
        }
        if(!timeMsg){
            slogan=SlognUtils.getSlogan(staffVO.getStaffName(),welcome.getSlogan(),userName);
            welcomeAttachment=welcomeAttachment.stream().filter(s->s.getTimeStateId()==null).collect(Collectors.toList());
        }

        if(CollUtil.isEmpty(welcomeAttachment)){
            Text text =  new Text();
            text.setContent(slogan);
            WxCpWelcomeMsg sendWelcomeMsg = new WxCpWelcomeMsg();
            sendWelcomeMsg.setText(text);
            return sendWelcomeMsg;
        }
        log.info("发送个人欢迎语欢迎语,是否分时段：{} 匹配到附件信息：{}",timeMsg,JSON.toJSONString(welcomeAttachment));
        List<Attachment> attachments = Lists.newArrayList();
        for (CpWelcomeAttachment cpWelcomeAttachment : welcomeAttachment) {
            //这里如果素材为引用的素材库素材，需要通过素材库id查询素材
            if(cpWelcomeAttachment.getMaterialId()!=null){
                MsgAttachment attachment = materialService.useAndFindAttachmentById(cpWelcomeAttachment.getMaterialId(),staffVO.getId());
                attachments.add(attachment);
            }else{
                AttachmentExtDTO attachmentExtDTO = Json.parseObject(cpWelcomeAttachment.getData(), AttachmentExtDTO.class);
                attachments.add(attachmentExtDTO.getAttachment());
            }
        }
        Text text =  new Text();
        text.setContent(slogan);
        WxCpWelcomeMsg sendWelcomeMsg = new WxCpWelcomeMsg();
        sendWelcomeMsg.setText(text);
        sendWelcomeMsg.setAttachments(attachments);
        return sendWelcomeMsg;
    }

    private StaffVO getStaffVOByUserId(String userId){
        try {
            ServerResponseEntity<StaffVO> response =  staffFeignClient.getStaffByQiWeiUserId(userId);
            log.info("----获取员工成功: {}", JSONObject.toJSONString(response));
            return response.getData();
        }catch (Exception e){
            log.info("----获取员工失败: {}", e);
            return null;
        }
    }

    public static void main(String[] s ){
        String json="{\"result\":[{\"field_pay_pref_payment_addr_postal_code\":\"200126\",\"field_client_profile_pri_email\":\"ada.cao@cactusglobal.com\",\"field_pay_pref_department\":\"department\",\"field_client_profile_native_name\":\"test pfirstname\",\"field_cp_pub_designation\":\"Job Designation\",\"field_pay_pref_lastname\":\"lastname\",\"last_modified_date\":\"2024-02-27 14:00:04\",\"field_client_profile_fax_country_codes\":\"cn\",\"client_type\":\"individual\",\"type\":\"test\",\"field_client_profile_website\":\"pwebsiteURL\",\"field_client_profile_address_country\":\"cn\",\"field_pay_pref_payment_addr_province\":\"11\",\"field_client_profile_salutation\":\"14\",\"field_client_profile_email_pref\":\"H\",\"field_cp_orcid_email\":\"ada.cao@cactusglobal.com\",\"field_cp_current_affiliation_country\":\"cn\",\"field_cp_pay_pref_firstname\":\"firstname\",\"wechat_open_id\":\"oEjtE559u3ecNYPFyQIkL7Q4DIvA\",\"field_client_profile_alt_email\":\"ada.cao1@cactusglobal.com\",\"field_client_profile_ref_source\":\"13432\",\"field_client_profile_department\":\"pdepartment\",\"field_cp_orcid_lname\":\"ada\",\"active\":true,\"field_client_profile_email_name\":\"Dr. test\",\"field_client_profile_sec_phone_country_codes\":\"cn\",\"field_client_profile_sec_phone_number\":\"18616101833\",\"network_id\":\"3\",\"field_client_profile_address_province\":\"11\",\"status\":\"active\",\"email_id\":\"ada.cao@cactusglobal.com\",\"field_client_profile_lastname\":\"test\",\"field_client_profile_firstname\":\"test\",\"timezone\":\"Asia/Shanghai\",\"defected\":false,\"roles\":\"23,24\",\"field_pay_pref_payment_addr_street\":\"building name\",\"languag\":\"zh-hans\",\"field_cp_orcid_fname\":\"ada\",\"field_pay_pref_payment_mail\":\"ada.cao@cactusglobal.com\",\"field_cp_job_description\":\"25672\",\"partner_id\":\"10\",\"eos_enabled_date\":\"2021-02-18 00:32:04\",\"field_client_profile_address_postal_code\":\"200121\",\"field_pay_pref_payment_addr_country\":\"cn\",\"field_client_profile_mob_email\":\"ada.cao2@cactusglobal.com\",\"field_client_profile_ref_details\":\"Additional Details\",\"field_pay_pref_organization\":\"Shanghai Jiao Tong University (上海交通大学)\",\"field_client_profile_fax_number\":\"18616101832\",\"client_code_id\":782258,\"field_client_profile_org\":\"Shanghai Jiao Tong University (上海交通大学)\",\"wechat_id\":\"obaIg1uDyL3GkNIqTV4fGaLhy8Gw\",\"eos_enabled\":true,\"field_client_profile_cell_phone_country_codes\":\"cn\",\"field_cp_pay_pref_salutation\":\"14\",\"field_client_profile_cell_phone_number\":\"18616101835\",\"field_client_profile_primary_ph_country_codes\":\"cn\",\"field_client_profile_address_city\":\"pcity\",\"userId\":\"808188\",\"field_pay_pref_payment_addr_city\":\"shanghai\",\"field_client_profile_primary_ph_number\":\"13012312323\",\"field_client_profile_dob\":\"2006-01-01\",\"created_date\":\"2020-04-29 11:31:19\",\"client_code\":\"BCDAZ\",\"field_client_profile_address_street\":\"pstreet\"}],\"success\":true}";

        CrmResult<JSONArray> crmResult = JSONObject.parseObject(json, CrmResult.class);
        boolean flag=false;
        if(Objects.nonNull(crmResult)){
            JSONArray jsonArray=crmResult.getResult();
            if(Objects.isNull(jsonArray) || CollUtil.isEmpty(jsonArray)){
                flag= false;
            }
            Object object = jsonArray.get(0);
            JSONObject jsonObject = (JSONObject) JSON.toJSON(object); // 优化为 通过JSON.toJSON转为JSONObject
            /**
             * 欢迎语是否已注册
             * 已注册：查数云用户数据，eos_user表的email_address，不为空。
             * 未注册：查数云用户数据，eos_user表的email_address，空
             */
            if(Objects.nonNull(jsonObject) && StrUtil.isNotEmpty(jsonObject.getString("email_id"))){
                flag=true;
            }
        }
        System.out.println(flag);
    }


}
