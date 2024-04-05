package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.constant.cp.GroupCodeRelStatusEnum;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.AppGroupDetail;
import com.mall4j.cloud.biz.vo.cp.AppGroupRefInfo;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppGroupInfoServiceImpl implements AppGroupInfoService {

    private final CpCodeChannelService cpCodeChannelService;
    private final CpAutoGroupCodeService autoGroupCodeService;
    private final GroupCodeService groupCodeService;
    private final GroupCreateTaskService groupCreateTaskService;
    private final GroupCodeRefService refService;
    private final MapperFacade mapperFacade;
    private final StaffCodePlusService staffCodeService;


    /**
     *  群活码、自动拉群、标签建群
     *  群数据、群聊数据
     * @param state 渠道唯一标识【cp_code_channel】
     * @return
     */
    @Override
    public AppGroupDetail getAppGroupDetail(String state) {
        CpCodeChannel cpCodeChannel=cpCodeChannelService.getBySourceState(state);
        log.info("引流页面信息-> cpCodeChannel: {}", JSON.toJSONString(cpCodeChannel));
        if(Objects.isNull(cpCodeChannel)){
            return null;
        }
        AppGroupDetail appGroupDetail=getAppGroupDetail(cpCodeChannel);
        log.info("引流页面信息-> appGroupDetail: {}", JSON.toJSONString(appGroupDetail));
        if(Objects.isNull(appGroupDetail)){
            return null;
        }
        //获取群聊信息: 取出未开始、拉人中的群聊信息【渠道活码、自动拉群显示渠道二维码】
        if(cpCodeChannel.getSourceFrom()!= CodeChannelEnum.CHANNEL_CODE.getValue()){
            List<CpGroupCodeRef> cpGroupCodeRefs=refService.getListByCodeId(Arrays.asList(Long.parseLong(cpCodeChannel.getSourceId())),
                    cpCodeChannel.getSourceFrom())
                    .stream().filter(item->
                            (item.getStatus()==GroupCodeRelStatusEnum.NO_START.getValue()
                                    || item.getStatus()==GroupCodeRelStatusEnum.PULL_USER.getValue()))
                    .collect(Collectors.toList());
            if(CollUtil.isEmpty(cpGroupCodeRefs)){
                //处理未备用人员
                if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.GROUP_CODE.getValue() && StrUtil.isNotEmpty(appGroupDetail.getQrCode())){
                    AppGroupRefInfo appGroupRefInfo=new AppGroupRefInfo();
                    appGroupRefInfo.setQrCode(appGroupDetail.getQrCode());
                    appGroupDetail.setInfoList(ListUtil.toList(appGroupRefInfo));
                    return appGroupDetail;
                }
                return null;
            }else{
                //随机抽取其中一个群聊信息
                Random rand =new Random();
                CpGroupCodeRef cpGroupCodeRef= cpGroupCodeRefs.get(rand.nextInt(cpGroupCodeRefs.size()));
                AppGroupRefInfo appGroupRefInfo=mapperFacade.map(cpGroupCodeRef,AppGroupRefInfo.class);
                appGroupDetail.setInfoList(ListUtil.toList(appGroupRefInfo));
            }

        }

        log.info("引流页面信息-> back info: {}", JSON.toJSONString(appGroupDetail));
        return appGroupDetail;
    }

    /**
     * 获取渠道源详细信息: 群活码、自动拉群、标签建群
     * @param cpCodeChannel
     * @return
     */
    private AppGroupDetail getAppGroupDetail(CpCodeChannel cpCodeChannel){
        AppGroupDetail appGroupDetail=new AppGroupDetail();
        appGroupDetail.setSourceFrom(cpCodeChannel.getSourceFrom());
        appGroupDetail.setSourceId(cpCodeChannel.getSourceId());
        if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.CHANNEL_CODE.getValue()){//渠道活码
            CpStaffCodePlus staffCode=staffCodeService.getById(cpCodeChannel.getSourceId());
            if(Objects.isNull(staffCode)){
                return null;
            }
            if(staffCode.getFlag()==1){
                return null;
            }
//            appGroupDetail.setSlogan(staffCode.getSlogan());
            appGroupDetail.setQrCode(staffCode.getQrCode());
            appGroupDetail.setCodeName(staffCode.getCodeName());
        }
        if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.GROUP_CODE.getValue()){//群活码
            CpGroupCode groupCode=groupCodeService.getById(cpCodeChannel.getSourceId());
            if(Objects.isNull(groupCode)){
                return null;
            }
            if(groupCode.getFlag()==1){
                return null;
            }
            appGroupDetail.setQrCode(groupCode.getStandbyStaffCode());
            appGroupDetail.setCodeName(groupCode.getName());
            appGroupDetail.setSlogan(SlognUtils.parseSlogn(groupCode.getSlogan()));

        }
        if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.AUTO_GROUP_CODE.getValue()){//自动拉群
            CpAutoGroupCode cpAutoGroupCode=autoGroupCodeService.getById(cpCodeChannel.getSourceId());
            if(Objects.isNull(cpAutoGroupCode)){
                return null;
            }
            if(cpAutoGroupCode.getFlag()==1){
                return null;
            }
//            appGroupDetail.setSlogan(cpAutoGroupCode.getSlogan());
//            appGroupDetail.setQrCode(cpAutoGroupCode.getQrCode());
            appGroupDetail.setCodeName(cpAutoGroupCode.getCodeName());
        }
        if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.TAG_GROUP_CODE.getValue()){//标签建群
            CpGroupCreateTask groupCreateTask=groupCreateTaskService.getById(cpCodeChannel.getSourceId());
            if(Objects.isNull(groupCreateTask)){
                return null;
            }
            if(groupCreateTask.getIsDelete()==1){
                return null;
            }
//            appGroupDetail.setSlogan(SlognUtils.parseSlogn(groupCreateTask.getSlogan()));
            appGroupDetail.setCodeName(groupCreateTask.getTaskName());
        }
        return appGroupDetail;
    }



}
