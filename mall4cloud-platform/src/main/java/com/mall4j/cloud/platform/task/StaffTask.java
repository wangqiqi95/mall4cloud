package com.mall4j.cloud.platform.task;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.api.biz.feign.SessionFileFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.mapper.StaffMapper;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.service.StaffService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 企业微信相关定时任务
 * @author hwy
 */
@Component("StaffTask")
@RequiredArgsConstructor
@Slf4j
public class StaffTask {

    @Autowired
    private SessionFileFeignClient sessionFileFeignClient;
    @Autowired
    private StaffService staffService;
    @Resource
    private StaffMapper staffMapper;

    /**
     * 更新员工开启会话存档状态
     * https://developer.work.weixin.qq.com/document/path/91614
     */
    @XxlJob("refeshCpMsgAudit")
    public void refeshCpMsgAudit(){
        log.info("开始执行更新员工会话存档开启状态");
        long start = new Date().getTime();
        ServerResponseEntity<String> responseEntity=sessionFileFeignClient.getMSGAUDITAccessToken();
        ServerResponseEntity.checkResponse(responseEntity);
        String url="https://qyapi.weixin.qq.com/cgi-bin/msgaudit/get_permit_user_list?access_token=ACCESS_TOKEN".replace("ACCESS_TOKEN",responseEntity.getData());
        Map<String, Object> parameMap = new HashMap<>();
        String result= HttpUtil.post(url,parameMap);
        JSONObject resultObject = JSON.parseObject(result);
        log.info("获取会话存档结果：{}",resultObject.toJSONString());
        if (0 != resultObject.getIntValue("errcode")) {
            log.info("开始执行更新员工会话存档开启状态失败,调用微信接口结果失败");
        }
        JSONArray jsonArray=resultObject.getJSONArray("ids");
        if (Objects.isNull(jsonArray)) {
            log.info("开始执行更新员工会话存档开启状态失败,调用微信接口结果为空");
        }
        List<String> users= JSONArray.parseArray(jsonArray.toJSONString(),String.class);
        StaffQueryDTO queryDTO=new StaffQueryDTO();
        queryDTO.setQiweiUserStatus(QiweiUserStatus.ALIVE.getCode());
        List<Staff> staffList = staffMapper.listByStaffQueryDTO(queryDTO);
        staffList=staffList.stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())).collect(Collectors.toList());
        if(CollUtil.isEmpty(staffList)){
            log.info("开始执行更新员工会话存档开启状态失败,调用内部员工结果为空");
        }
        Map<String,String> staffMap=users.stream().collect(Collectors.toMap(Function.identity(),Function.identity()));
        List<Staff> openStaff=new ArrayList<>();
        List<Staff> closeStaff=new ArrayList<>();
        for (Staff staffInfo : staffList) {
            String key=staffInfo.getQiWeiUserId();
            if(staffMap.containsKey(key)){
                Staff staff=new Staff();
                staff.setId(staffInfo.getId());
                staff.setCpMsgAudit(1);
                staff.setUpdateBy("更新会话存档状态");
                staff.setUpdateTime(new Date());
                openStaff.add(staff);
            }
            if(!staffMap.containsKey(key)){
                Staff staff=new Staff();
                staff.setId(staffInfo.getId());
                staff.setCpMsgAudit(0);
                staff.setUpdateBy("更新会话存档状态");
                staff.setUpdateTime(new Date());
                closeStaff.add(staff);
            }
        }
        if(CollUtil.isNotEmpty(openStaff)){
            staffService.updateBatchById(openStaff);
        }
        if(CollUtil.isNotEmpty(closeStaff)){
            staffService.updateBatchById(closeStaff);
        }
        long end = new Date().getTime();
        log.info("结束执行更新员工会话存档开启状态，耗时：{}s", ((end - start) / 1000.0) );
    }


}

