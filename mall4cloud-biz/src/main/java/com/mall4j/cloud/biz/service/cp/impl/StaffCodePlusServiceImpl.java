package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.constant.cp.CodeTimeSourceFromEnum;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.mapper.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpWxMediaUploadResult;
import com.mall4j.cloud.biz.vo.cp.StaffCodeDetailPlusVO;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StaffCodeOriginEumn;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.StaffCodePlusVO;
import com.mall4j.cloud.biz.manager.DrainageUrlManager;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayInfo;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StaffCodePlusServiceImpl extends ServiceImpl<CpStaffCodePlusMapper, CpStaffCodePlus> implements StaffCodePlusService {
    private final static String  UPDATE="update";
    private final static String  CREATE="create";

    private final MapperFacade mapperFacade;
    private final CpStaffCodePlusMapper staffCodeMapper;
    private final StaffCodeRefService staffCodeRefService;
    private final CpStaffCodeRefMapper cpStaffCodeRefMapper;
    private final WelcomeService welcomeService;
    private final WelcomeAttachmentService welcomeAttachmentService;
    private final WelcomeMapper welcomeMapper;
    private final CpStaffCodeTimeMapper cpStaffCodeTimeMapper;
    private final CpStaffCodeTimeService cpStaffCodeTimeService;
    private final CpCodeChannelService cpCodeChannelService;
    private final DrainageUrlManager drainageUrlManager;
    private final DomainConfig domainConfig;
    private final CpMediaRefService cpMediaRefService;
    private final WeixinCpExternalManager weixinCpExternalManager;

    @Override
    public PageVO<StaffCodePlusVO> page(PageDTO pageDTO, StaffCodePagePlusDTO request) {
        PageVO<StaffCodePlusVO> pageVO=PageUtil.doPage(pageDTO, () -> staffCodeMapper.list(request));
        return pageVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(StaffCodePlusDTO request) {

        //TODO 校验分时段接待员工重复
        if(request.getReceptionType()==1){
            checkStaffTime(request.getTimeDTOS());
        }

        //TODO 校验分时段欢迎语重复
        checkAttachMentTime(request.getAttachMents());

        CpStaffCodePlus staffCode = mapperFacade.map(request, CpStaffCodePlus.class);
        staffCode.setId(null);
        staffCode.setStatus(StatusType.YX.getCode());
        staffCode.setCreateName(AuthUserContext.get().getUsername());
        staffCode.setCreateBy(AuthUserContext.get().getUserId());
        staffCode.setFlag(FlagEunm.USE.getCode());
        staffCode.setCreateTime(new Date());
        staffCode.setAutoDescriptionState(Objects.nonNull(request.getAutoDescriptionState())?request.getAutoDescriptionState():0);
        staffCode.setOrigin(StaffCodeOriginEumn.BACK.getCode());

        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(SlognUtils.formatSlogn(staffCode.getSlogan()));
        }
        List<CpStaffCodeRef> staffList=staffCodeRefService.getCodeStaffRefs(request);

        staffCode.setState(String.valueOf(RandomUtil.getUniqueNum()));

        StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, Lists.newArrayList(staffList), WxCpContactWayInfo.TYPE.MULTI, CREATE);
        if(staffCodeExcelDTO==null) {

            //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
            String content=staffCode.getState();
            String logo=staffCode.getCodeStyle();
//            String logo=domainConfig.getDomain()+staffCode.getCodeStyle();
            ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
            ServerResponseEntity.checkResponse(responseEntity);
            CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
            staffCode.setDrainageUrl(mediaUploadResult.getDrainageUrl());
            staffCode.setDrainagePath(mediaUploadResult.getDrainagePath());

            ServerResponseEntity<String> qrResponse=drainageUrlManager.insertQrCodeLogo(staffCode.getQrCode(),logo);
            ServerResponseEntity.checkResponse(responseEntity);
            staffCode.setQrCode(qrResponse.getData());

            this.save(staffCode);

            //渠道源标识
            String sourceId=staffCode.getId().toString();
            cpCodeChannelService.saveCpCodeChannel(CodeChannelEnum.CHANNEL_CODE.getValue(),sourceId,sourceId,staffCode.getState());

            /**
             * 保存附件
             * 1. 欢迎语【欢迎语 & 分时间段欢迎语】
             * 2. 欢迎语附件项
             */
            welcomeService.saveStaffCodeWelcome(staffCode.getId(),request.getAttachMents());

            //保存引流页面二维码临时素材
            cpMediaRefService.saveCpMediaRef(CpMediaRef.builder()
                    .sourceId(staffCode.getId().toString())
                    .sourceFrom(CodeChannelEnum.CHANNEL_CODE.getValue())
                    .mediaId(mediaUploadResult.getMediaId())
                    .thumbMediaId(mediaUploadResult.getThumbMediaId())
                    .url(mediaUploadResult.getUrl())
                    .type(mediaUploadResult.getType())
                    .createTime(WechatUtils.formatDate(String.valueOf(mediaUploadResult.getCreatedAt())))
                    .build());
        }
        //保存活码的员工信息
        staffCodeRefService.saveCodeRef(staffCode.getId(), CodeTimeSourceFromEnum.CHANNEL_CODE.getValue(),request);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(StaffCodePlusDTO request) {

        //TODO 校验分时段接待员工重复
        if(request.getReceptionType()==1){
            checkStaffTime(request.getTimeDTOS());
        }

        //TODO 校验分时段欢迎语重复
        checkAttachMentTime(request.getAttachMents());
        CpStaffCodePlus staffCode=staffCodeMapper.getById(request.getId());
        if(Objects.isNull(staffCode)){
            throw new LuckException("操作失败，信息未找到");
        }
        String qrCode=staffCode.getQrCode();
        String state=staffCode.getState();
        String style=staffCode.getCodeStyle();
        staffCode = mapperFacade.map(request, CpStaffCodePlus.class);
        staffCode.setUpdateTime(new Date());
        staffCode.setUpdateName(AuthUserContext.get().getUsername());
        staffCode.setUpdateBy(AuthUserContext.get().getUserId());
        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(SlognUtils.formatSlogn(staffCode.getSlogan()));
        }

        //员工
        List<CpStaffCodeRef> staffList=staffCodeRefService.getCodeStaffRefs(request);

        //更新企业微信的配置
        StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(staffCode, staffList, WxCpContactWayInfo.TYPE.MULTI, UPDATE);
        if(staffCodeExcelDTO==null) {

            if(!style.equals(request.getCodeStyle())){
                //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
                String content=state;
                String logo=staffCode.getCodeStyle();
//                String logo=domainConfig.getDomain()+staffCode.getCodeStyle();
                ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
                ServerResponseEntity.checkResponse(responseEntity);
                CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
                staffCode.setDrainageUrl(mediaUploadResult.getDrainageUrl());
                staffCode.setDrainagePath(mediaUploadResult.getDrainagePath());

                ServerResponseEntity<String> qrResponse=drainageUrlManager.insertQrCodeLogo(qrCode,logo);
                ServerResponseEntity.checkResponse(responseEntity);
                staffCode.setQrCode(qrResponse.getData());
            }

            staffCode.setUpdateTime(new Date());
            staffCodeMapper.updateById(staffCode);
            /**
             * 保存附件
             * 1. 欢迎语【欢迎语 & 分时间段欢迎语】
             * 2. 欢迎语附件项
             */
            welcomeService.saveStaffCodeWelcome(staffCode.getId(),request.getAttachMents());
        }

        //保存活码的员工信息
        staffCodeRefService.saveCodeRef(staffCode.getId(), CodeTimeSourceFromEnum.CHANNEL_CODE.getValue(),request);

    }


    /**
     * 创建qrode
     * @param staffCode
     * @param staffCodeRefs
     */
    private StaffCodeExcelDTO createOrUpdateStaffCode(CpStaffCodePlus staffCode, List<CpStaffCodeRef> staffCodeRefs, WxCpContactWayInfo.TYPE type, String oper) {
        try {
            WxCpContactWayInfo.ContactWay contactWay =  new WxCpContactWayInfo.ContactWay();
            contactWay.setRemark(staffCode.getRemarks());
            contactWay.setType(type);
            contactWay.setIsTemp(false);
            contactWay.setScene(WxCpContactWayInfo.SCENE.QRCODE);
            contactWay.setSkipVerify(staffCode.getAuthType()==1?false:true);
            contactWay.setUsers(staffCodeRefs.parallelStream().map(item->item.getUserId()).collect(Collectors.toList()));
            contactWay.setState(staffCode.getState());
            WxCpContactWayInfo cpContactWayInfo =  new WxCpContactWayInfo();
            cpContactWayInfo.setContactWay(contactWay);

            try {
                if (oper.equals(CREATE)) {
                    WxCpContactWayResult result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().addContactWay(cpContactWayInfo);
                    if (result.getErrcode() == 0) {
                        //企微活码信息
                        staffCode.setQrCode(result.getQrCode());
                        staffCode.setConfigId(result.getConfigId());
                    } else {
                        throw new WxErrorException(result.getErrmsg());
                    }
                } else {
                    CpStaffCodePlus staffCodeDTO = this.getById(staffCode.getId());
                    cpContactWayInfo.getContactWay().setConfigId(staffCodeDTO.getConfigId());
                    WxCpBaseResp resp = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().updateContactWay(cpContactWayInfo);
                    if (!resp.success()) {
                        throw new WxErrorException(resp.getErrmsg());
                    }
                }
            }catch (WxErrorException e){
                throw new WxErrorException(e);
            }
        }catch (WxErrorException e){
            throw new LuckException("错误代码："+e.getError().getErrorCode()+" 提示消息："+e.getMessage());
        }
        return null;
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        CpStaffCodePlus staffCode = staffCodeMapper.getById(id);
        //删除素材
        welcomeService.update(new LambdaUpdateWrapper<CpWelcome>().set(CpWelcome::getFlag,1).eq(CpWelcome::getSourceId,id));
        welcomeAttachmentService.update(new LambdaUpdateWrapper<CpWelcomeAttachment>().set(CpWelcomeAttachment::getFlag,1).eq(CpWelcomeAttachment::getSourceId,id));
        //删除关联人员
        staffCodeRefService.update(new LambdaUpdateWrapper<CpStaffCodeRef>().set(CpStaffCodeRef::getIsDelete,1).eq(CpStaffCodeRef::getCodeId,id));
        cpStaffCodeTimeService.update(new LambdaUpdateWrapper<CpStaffCodeTime>().set(CpStaffCodeTime::getIsDelete,1).eq(CpStaffCodeTime::getSourceId,id));
        //删除活码配置
        this.update(new LambdaUpdateWrapper<CpStaffCodePlus>().set(CpStaffCodePlus::getFlag,1).eq(CpStaffCodePlus::getId,id));
        //删除企微活码
        weixinCpExternalManager.delContactWay(staffCode.getConfigId());
    }


    @Override
    public CpStaffCodePlus getById(Long id) {
        CpStaffCodePlus staffCode=staffCodeMapper.getById(id);
        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(SlognUtils.parseSlogn(staffCode.getSlogan()));
        }

        //获取人员

        //获取附件

        return staffCode;
    }

    @Override
    public StaffCodeDetailPlusVO getDetailById(Long id) {
        CpStaffCodePlus staffCode=staffCodeMapper.getById(id);
        if(Objects.isNull(staffCode)){
            throw new LuckException("活码未找到");
        }
        if(StrUtil.isNotBlank(staffCode.getSlogan())){
            staffCode.setSlogan(SlognUtils.parseSlogn(staffCode.getSlogan()));
        }
        //活码信息
        StaffCodeDetailPlusVO staffCodeDetailVO=new StaffCodeDetailPlusVO();
        staffCodeDetailVO.setStaffCode(staffCode);

        //获取接待人员
        List<CpStaffCodeTimeDTO> codeTimeDTOS=cpStaffCodeTimeMapper.selectStaffTimesBySourceId(id);
        if(CollUtil.isNotEmpty(codeTimeDTOS)){
            for (CpStaffCodeTimeDTO timeDTO : codeTimeDTOS) {
                if(StrUtil.isNotEmpty(timeDTO.getTimeEnd()) && timeDTO.getTimeEnd().equals("23:59")){
                    timeDTO.setTimeEnd("24:00");
                }
            }
            staffCodeDetailVO.setCodeTimestaffs(codeTimeDTOS);
        }

        //获取备用人员
        List<CpStaffCodeRef> staffs = cpStaffCodeRefMapper.listByCode(id,1);
        staffCodeDetailVO.setStandbyStaffs(staffs);

        //获取附件
        List<ChannelCodeWelcomeDTO> welcomeDTOS=welcomeMapper.selectWelcomes(id);
        if(CollUtil.isNotEmpty(welcomeDTOS)){
            for (ChannelCodeWelcomeDTO welcomeDTO : welcomeDTOS) {
                welcomeDTO.setSlogan(SlognUtils.parseSlogn(welcomeDTO.getSlogan()));
                if(StrUtil.isNotEmpty(welcomeDTO.getTimeEnd()) && welcomeDTO.getTimeEnd().equals("23:59")){
                    welcomeDTO.setTimeEnd("24:00");
                }
            }
            staffCodeDetailVO.setAttachMents(welcomeDTOS);
        }

        //TODO 素材库：素材

        return staffCodeDetailVO;
    }

    @Override
    public CpStaffCodePlus selectByStaffIdAndState(Long staffId, String state) {
        return staffCodeMapper.selectByStaffIdAndState(staffId,state);
    }

    @Override
    public List<CpStaffCodePlus> selectByStaffId(Long staffId) {
        return staffCodeMapper.selectByStaffId(staffId);
    }

    /**
     * 校验接待员工分时段冲突
     * @param timeDTOS
     */
    private static void checkStaffTime(List<CpStaffCodeTimeDTO> timeDTOS){
        if(CollUtil.isEmpty(timeDTOS)){
            return;
        }
        for (CpStaffCodeTimeDTO timeDTO : timeDTOS) {
            if(StrUtil.isNotEmpty(timeDTO.getTimeEnd()) && timeDTO.getTimeEnd().equals("24:00")){
                timeDTO.setTimeEnd("23:59");
            }
        }
        for (int i = 0; i < timeDTOS.size() - 1; i++) {
            for (int j = i + 1; j < timeDTOS.size(); j++) {

                int finalJ = j;
//                    long userSum = timeDTOS.get(i).getStaffList().stream()
//                            .filter(one -> timeDTOS.get(finalJ).getStaffList().stream()
//                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();
//                    if (userSum > 0) {
                List<String> weeks=Arrays.asList(timeDTOS.get(i).getTimeCycle().split(","));
                List<String> weeks1=Arrays.asList(timeDTOS.get(finalJ).getTimeCycle().split(","));
                long workCycleSum = weeks.stream()
                        .filter(one -> weeks1.stream()
                                .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                if (workCycleSum > 0) {
                    String beginTime1 = timeDTOS.get(i).getTimeStart();
                    String endTime1 = timeDTOS.get(i).getTimeEnd();
                    String beginTime2 = timeDTOS.get(finalJ).getTimeStart();
                    String endTime2 = timeDTOS.get(finalJ).getTimeEnd();
                    log.info("beginTime1:{} endTime1:{} beginTime2:{} endTime2:{}",beginTime1,endTime1,beginTime2,endTime2);
                    if (DateUtils.match(beginTime1, endTime1, beginTime2, endTime2)) {
                        throw new LuckException("接待时间有冲突!");
                    }
                }
//                    }
            }
        }
    }

    /**
     * 校验接待欢迎语分时段冲突
     * @param attachMents
     */
    private static void checkAttachMentTime(List<ChannelCodeWelcomeDTO> attachMents){
        if(CollUtil.isEmpty(attachMents)){
            return;
        }
        for (ChannelCodeWelcomeDTO attachMent : attachMents) {
            if(StrUtil.isNotEmpty(attachMent.getTimeEnd()) && attachMent.getTimeEnd().equals("24:00")){
                attachMent.setTimeEnd("23:59");
            }
        }
        List<ChannelCodeWelcomeDTO> filterAttachMents=attachMents.stream().filter(item->item.getSourceFrom()==2).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(filterAttachMents)) {
            for (int i = 0; i < filterAttachMents.size() - 1; i++) {
                for (int j = i + 1; j < filterAttachMents.size(); j++) {

                    int finalJ = j;
//                    long userSum = filterAttachMents.get(i).getAttachMents().stream()
//                            .filter(one -> filterAttachMents.get(finalJ).getAttachMents().stream()
//                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();
//                    if (userSum > 0) {
                        List<String> weeks=Arrays.asList(filterAttachMents.get(i).getTimeCycle().split(","));
                        List<String> weeks1=Arrays.asList(filterAttachMents.get(finalJ).getTimeCycle().split(","));
                        long workCycleSum = weeks.stream()
                                .filter(one -> weeks1.stream()
                                        .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                        if (workCycleSum > 0) {
                            String beginTime1 = filterAttachMents.get(i).getTimeStart();
                            String endTime1 = filterAttachMents.get(i).getTimeEnd();
                            String beginTime2 = filterAttachMents.get(finalJ).getTimeStart();
                            String endTime2 = filterAttachMents.get(finalJ).getTimeEnd();
                            log.info("beginTime1:{} endTime1:{} beginTime2:{} endTime2:{}",beginTime1,endTime1,beginTime2,endTime2);
                            if (DateUtils.match(beginTime1, endTime1, beginTime2, endTime2)) {
                                throw new LuckException("分时段欢迎语有冲突!");
                            }
                        }
//                    }
                }
            }
        }
    }

    public static void main(String[] s){
        String json="[{\"attachMents\":[{\"msgType\":\"image\",\"materialId\":\"\",\"localUrl\":\"/2023/12/21/6585481a5f6a429686c6dd259fa7190c\",\"picUrl\":\"https://wework.qpic.cn/wwpic3az/618741_xoa9Orz8RiyyGro_1703496700/0\"}],\"sourceFrom\":1},{\"attachMents\":[{\"msgType\":\"material\",\"materialId\":46}],\"sourceFrom\":2,\"timeCycle\":\"4\",\"timeStart\":\"01:30\",\"timeEnd\":\"02:00\",\"slogan\":\"${客户昵称}${员工姓名}\"},{\"attachMents\":[{\"msgType\":\"material\",\"materialId\":46}],\"sourceFrom\":2,\"timeCycle\":\"2,3\",\"timeStart\":\"01:30\",\"timeEnd\":\"02:00\",\"slogan\":\"${客户昵称}${员工姓名}\"}]";

        List<ChannelCodeWelcomeDTO> attachMents= JSONArray.parseArray(json,ChannelCodeWelcomeDTO.class);

//        checkAttachMentTime(attachMents);

        String json1="[{\"timeEnd\":\"01:30\",\"timeStart\":\"01:00\",\"id\":63,\"staffList\":[{\"relId\":170,\"staffName\":\"周洪捐\",\"codeTimeId\":null,\"type\":0,\"userId\":\"HaiXiang\",\"staffId\":91}],\"timeCycle\":\"7,6\",\"sourceFrom\":1},{\"timeEnd\":\"02:00\",\"timeStart\":\"01:30\",\"id\":64,\"staffList\":[{\"relId\":171,\"staffName\":\"周洪捐\",\"codeTimeId\":null,\"type\":0,\"userId\":\"HaiXiang\",\"staffId\":91}],\"timeCycle\":\"7,6\",\"sourceFrom\":1}]";

        List<CpStaffCodeTimeDTO> timeDTOS= JSONArray.parseArray(json1,CpStaffCodeTimeDTO.class);

//        checkStaffTime(timeDTOS);

    }

}
