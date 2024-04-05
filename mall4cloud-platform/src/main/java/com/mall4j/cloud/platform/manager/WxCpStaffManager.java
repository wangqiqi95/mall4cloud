package com.mall4j.cloud.platform.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.api.biz.dto.cp.CpUserCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.WxCpUserDTO;
import com.mall4j.cloud.api.biz.feign.SessionFileFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.cache.constant.BizCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.platform.mapper.StaffMapper;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.service.StaffService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.Gender;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalUnassignList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 企微信息同步：部门、员工
 */
@Slf4j
@Component
public class WxCpStaffManager {

    private final String DEFAULT_DEPART="其他";
    private final String CREATE_USER="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";
    private final String UPDATE_USER="https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";

    @Autowired
    private WxCpApiFeignClient wxCpApiFeignClient;



    /**
     * 同步销售至企业微信
     * @param dtos
     */
    public void pushQiWeiUserList(List<CpUserCreateDTO> dtos) {
        for(CpUserCreateDTO dto:dtos){

            pushQiWeiUser(dto);

        }
    }

    public ServerResponseEntity<WxCpUserDTO> pushQiWeiUser(CpUserCreateDTO dto){
        WxCpUser wxCpUser=getWxCpUser(dto.getStaffId(),dto.getMobile());
        log.info("成员姓名:{} userId:{} 是否存在:{} 信息:{}",
                dto.getStaffName(),dto.getStaffNo(),
                (Objects.nonNull(wxCpUser)?"存在":"不存在"),
                Objects.nonNull(wxCpUser)? JSON.toJSON(wxCpUser):null);
        JSONObject backJson=null;
        boolean isCreate=Objects.isNull(wxCpUser)?true:false;
        WxCpUserDTO wxCpUserDTO=null;
        if(Objects.isNull(wxCpUser)){//添加
            wxCpUserDTO=buildWxCpUser(dto,dto.getStaffNo());
            backJson=postConnetUser(CREATE_USER,wxCpUserDTO);
        }else{//编辑
            wxCpUserDTO=buildWxCpUser(dto,wxCpUser.getUserId());
            wxCpUserDTO.setMobile(null);//手机号不允许修改
            wxCpUserDTO.setAvatar(wxCpUser.getAvatar());
            backJson=postConnetUser(UPDATE_USER,wxCpUserDTO);
            wxCpUserDTO.setStatus(wxCpUser.getStatus());
        }
        if(backJson.getIntValue("errcode")==0){
            log.info("成员userid:{} -> {}成功 msg:{}",dto.getStaffId(),isCreate?"创建":"更新",backJson.toJSONString());
            return ServerResponseEntity.success(wxCpUserDTO);
        }else{
            log.error("成员userid:{} -> {}失败 msg:{}",dto.getStaffId(),isCreate?"创建":"更新",backJson.toJSONString());
            if(backJson.getIntValue("errcode")==40014){
                RedisUtil.del("connectuser_access_token");
            }
            return ServerResponseEntity.showFailMsg("员工同步企微失败:"+backJson.toJSONString());
        }
    }

    /**
     * 调用通讯录接口API：access_token，
     * 写通讯录接口，只能由通讯录同步助手的access_token来调用。同时需要保证通讯录同步功能是开启的
     * @return
     */
    public String getConnectAccessToken() {
        ServerResponseEntity<String> responseEntity= wxCpApiFeignClient.wxCpConnectAccessToken();
        ServerResponseEntity.checkResponse(responseEntity);
        return responseEntity.getData();
    }

    private JSONObject postConnetUser(String url,WxCpUserDTO wxCpUser){
        String token=getConnectAccessToken();
        log.info("--postConnetUser: {}",url.replace("ACCESS_TOKEN",token));
        String backMsg= HttpUtil.post(url.replace("ACCESS_TOKEN",token),JSON.toJSONString(wxCpUser));
        return JSON.parseObject(backMsg);
    }


    private WxCpUser getWxCpUser(String userId,String phone){
        try {
            ServerResponseEntity<String> responseEntity=wxCpApiFeignClient.wxCpByMobile(phone);
            ServerResponseEntity.checkResponse(responseEntity);
            String qiweiuserid=responseEntity.getData();
            if(StrUtil.isNotBlank(qiweiuserid)){
                ServerResponseEntity<WxCpUser> responseEntityCpUser=wxCpApiFeignClient.wxCpByUserId(qiweiuserid);
                ServerResponseEntity.checkResponse(responseEntityCpUser);
                return responseEntityCpUser.getData();
            }else{
                ServerResponseEntity<WxCpUser> responseEntityCpUser=wxCpApiFeignClient.wxCpByUserId(userId);
                ServerResponseEntity.checkResponse(responseEntityCpUser);
                return responseEntityCpUser.getData();
            }
        }catch (Exception e){
            log.info("获取企微成员信息失败 {} {}",e,e.getMessage());
            return null;
        }
    }

    private WxCpUserDTO buildWxCpUser(CpUserCreateDTO dto,String qiWeiUserId){
        WxCpUserDTO wxCpUser=new WxCpUserDTO();
        wxCpUser.setUserid(qiWeiUserId);//系统内部员工id
        wxCpUser.setName(dto.getStaffName());
        wxCpUser.setMobile(dto.getMobile());
        wxCpUser.setPosition(dto.getPosition());
        wxCpUser.setEmail(dto.getEmail());
        if(Objects.nonNull(dto.getSex())){
            wxCpUser.setGender(dto.getSex()==1? Gender.MALE.getCode():Gender.FEMALE.getCode());
        }else{
            wxCpUser.setGender(Gender.UNDEFINED.getCode());
        }
        //部门信息
        wxCpUser.setDepartment(getDetparts(dto.getOrgIds()));

        //对内隐藏自定义属性
        WxCpUserDTO.Extattr extattr=new WxCpUserDTO.Extattr();

        //文本
        WxCpUserDTO.Attr attrText=new WxCpUserDTO.Attr();
        attrText.setType(0);
        attrText.setName("staffNo");
        attrText.setText(new WxCpUserDTO.TextValue(dto.getStaffNo()));

        extattr.getAttrs().add(attrText);
        wxCpUser.setExtattr(extattr);

        log.info("同步企微员工信息：{} ", JSON.toJSON(wxCpUser));
        return wxCpUser;
    }

    //组合部门信息
    private Long[] getDetparts(String orgId){
        String[] ids=orgId.split(",");
        Long[] arrId = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            arrId[i] = Long.parseLong(ids[i]);
        }
        return arrId;
    }

    /**
     * 同步企微员工离职状态
     */
    public void getUnassignedList(){
        String cursor="";//用于分页查询的游标，字符串类型，由上一次调用返回，首次调用不填
        Integer pageSize=10000;//分页，预期请求的数据量，取值范围 1 ~ 10000
        ServerResponseEntity<WxCpUserExternalUnassignList> responseEntity=wxCpApiFeignClient.getUnassignedList(cursor,pageSize);
        ServerResponseEntity.checkResponse(responseEntity);

        List<Staff> staffList=new ArrayList<>();
        getNextUnassignedList(cursor,pageSize,staffList);
    }

    private void getNextUnassignedList(String cursor,Integer pageSize,List<Staff> staffList){
        ServerResponseEntity<WxCpUserExternalUnassignList> responseEntity=wxCpApiFeignClient.getUnassignedList(cursor,pageSize);
        ServerResponseEntity.checkResponse(responseEntity);
        log.info("获取待分配的离职成员列表: {}",JSON.toJSONString(responseEntity.getData()));
        for (WxCpUserExternalUnassignList.UnassignInfo unassignInfo : responseEntity.getData().getUnassignInfos()) {
            Staff staff=new Staff();
            staff.setQiWeiUserId(unassignInfo.getHandoverUserid());
            staff.setDimissionTime(formatDate(unassignInfo.getDimissionTime().toString()));
            staff.setUpdateTime(new Date());
            staff.setUpdateBy("系统同步");
            staffList.add(staff);
        }
        if(!responseEntity.getData().isLast()){
            getNextUnassignedList(cursor,pageSize,staffList);
        }
    }

    public static Date formatDate(String createTime) {
        // 将微信传入的CreateTime转换成long类型，再乘以1000
        long msgCreateTime = Long.parseLong(createTime) * 1000L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date(msgCreateTime);
    }

    /**
     * 删除企业微信
     * @param userId
     */
    public ServerResponseEntity<Void>  deleteByUserId(String userId){
       return wxCpApiFeignClient.deleteByUserId(userId);
    }

    public WxCpUser getWxCpUserById(String qiweiuserid){
        ServerResponseEntity<WxCpUser> responseEntityCpUser=wxCpApiFeignClient.wxCpByUserId(qiweiuserid);
        ServerResponseEntity.checkResponse(responseEntityCpUser);
        return responseEntityCpUser.getData();
    }


}
