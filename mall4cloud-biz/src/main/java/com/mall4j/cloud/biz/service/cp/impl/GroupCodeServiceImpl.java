package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.constant.cp.GroupCodeRelStatusEnum;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.GroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCodePageDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeExcelDTO;
import com.mall4j.cloud.biz.mapper.cp.GroupCodeMapper;
import com.mall4j.cloud.biz.mapper.cp.GroupCodeRefMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpWxMediaUploadResult;
import com.mall4j.cloud.biz.vo.cp.GroupCodeVO;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.manager.DrainageUrlManager;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
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
import me.chanjar.weixin.cp.bean.external.WxCpContactWayInfo;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayResult;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 群活码表
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GroupCodeServiceImpl extends ServiceImpl<GroupCodeMapper, CpGroupCode> implements GroupCodeService {
    private final static String  UPDATE="update";
    private final static String  CREATE="create";

    private final  GroupCodeMapper groupCodeMapper;
    private final GroupCodeRefService refService;
    private final WxCpGroupChatService wxCpGroupChatService;
    private final MapperFacade mapperFacade;
    private final GroupCodeRefMapper codeRefMapper;
    private final DrainageUrlManager drainageUrlManager;
    private final CpCodeChannelService cpCodeChannelService;
    private final CpMediaRefService cpMediaRefService;
    private final StaffFeignClient staffFeignClient;

    @Override
    public PageVO<GroupCodeVO> page(PageDTO pageDTO, GroupCodePageDTO request) {
        PageVO<GroupCodeVO> pageVO=PageUtil.doPage(pageDTO, () -> groupCodeMapper.list(request));
        if(!CollectionUtils.isEmpty(pageVO.getList())){

            List<Long> codeIds=pageVO.getList().stream().map(item->item.getId()).collect(Collectors.toList());
            List<CpGroupCodeRef>  cpGroupCodeRefs=refService.getListByCodeId(codeIds,CodeChannelEnum.GROUP_CODE.getValue());
            Map<Long,List<CpGroupCodeRef>> codeRefMap=LambdaUtils.groupList(cpGroupCodeRefs,CpGroupCodeRef::getCodeId);

            pageVO.getList().forEach(item -> {
                //群聊统计数据
                getCountCodeRef(item,codeRefMap);
            });
        }
        return pageVO;
    }

    @Override
    public CpGroupCode getById(Long id) {
       return groupCodeMapper.selectById(id);
    }

    @Override
    public GroupCodeVO getDetailById(Long id) {
        CpGroupCode cpGroupCode=groupCodeMapper.selectById(id);
        if(Objects.isNull(cpGroupCode)){
            throw new LuckException("群活码信息未找到");
        }
        GroupCodeVO codeVO=mapperFacade.map(cpGroupCode,GroupCodeVO.class);
        codeVO.setSlogan(SlognUtils.parseSlogn(codeVO.getSlogan()));
        //关联群聊信息
        List<CpGroupCodeRef>  cpGroupCodeRefs=refService.getListByCodeId(Arrays.asList(id),CodeChannelEnum.GROUP_CODE.getValue());
        Map<Long,List<CpGroupCodeRef>> codeRefMap=LambdaUtils.groupList(cpGroupCodeRefs,CpGroupCodeRef::getCodeId);
        codeVO.setCodeList(cpGroupCodeRefs);

        //群人数信息
        getCountCodeRef(codeVO,codeRefMap);

        return codeVO;
    }

    /**
     * 群-群聊统计数据
     * @param groupCodeVO
     * @param codeRefMap
     */
    private void getCountCodeRef(GroupCodeVO groupCodeVO,Map<Long,List<CpGroupCodeRef>> codeRefMap){
        groupCodeVO.setCustTotal(0);
        groupCodeVO.setEnabledCustTotal(0);
        groupCodeVO.setEnabledGroupTotal(0);
        groupCodeVO.setGroupTotal(0);

        //关联群统计数据
//        GroupCodeRefVO groupCodeRefVO = refService.statCount(groupCodeVO.getId());
//        groupCodeVO.setGroupTotal(groupCodeRefVO.getGroupTotal());
        //关联群聊数
        if(codeRefMap.containsKey(groupCodeVO.getId())){
            groupCodeVO.setGroupTotal(codeRefMap.get(groupCodeVO.getId()).size());

            //剩余可用群数：拉人中+未开始拉人的群
            Long enabledGroupTotal=codeRefMap.get(groupCodeVO.getId()).stream()
                    .filter(itemRef->itemRef.getStatus()== GroupCodeRelStatusEnum.NO_START.getValue()
                            || itemRef.getStatus()==GroupCodeRelStatusEnum.PULL_USER.getValue())
                    .count();
            groupCodeVO.setEnabledGroupTotal(enabledGroupTotal==null?0:enabledGroupTotal.intValue());

            if(groupCodeVO.getGroupType()==2){
                groupCodeVO.setBasicsCodeUrl(codeRefMap.get(groupCodeVO.getId()).get(0).getQrCode());
            }
        }
        //剩余可用群数：拉人中+未开始拉人的群
//        if(codeRefMap.containsKey(groupCodeVO.getId())){
//            Long enabledGroupTotal=codeRefMap.get(groupCodeVO.getId()).stream()
//                    .filter(itemRef->itemRef.getStatus()== GroupCodeRelStatusEnum.NO_START.getValue()
//                    || itemRef.getStatus()==GroupCodeRelStatusEnum.PULL_USER.getValue())
//                    .count();
//            groupCodeVO.setEnabledGroupTotal(enabledGroupTotal==null?0:enabledGroupTotal.intValue());
//        }
        /**
         * 剩余可邀请客户数为：剩余可用群中的可邀请数志和
         * a.单个群可邀请人数：人数上限-当前群人数
         */
        if(groupCodeVO.getEnabledGroupTotal()>0 && codeRefMap.containsKey(groupCodeVO.getId())){//剩余可用群数>0
            int enabledCustTotal = codeRefMap.get(groupCodeVO.getId()).stream()
                    .filter(itemRef->itemRef.getStatus()== GroupCodeRelStatusEnum.NO_START.getValue()
                    || itemRef.getStatus()==GroupCodeRelStatusEnum.PULL_USER.getValue())
                    .mapToInt(CpGroupCodeRef::getEnabledCustTotal).sum();
            groupCodeVO.setEnabledCustTotal(enabledCustTotal);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        CpGroupCode groupCode = this.getById(id);
        if(Objects.isNull(groupCode)){
            throw new LuckException("活码未找到");
        }
        //获取关联群聊
        List<CpGroupCodeRef> codeRefs= codeRefMapper.selectListByCodeIds(Arrays.asList(id),CodeChannelEnum.GROUP_CODE.getValue())
                .stream().filter(item-> StrUtil.isNotEmpty(item.getConfigId())).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(codeRefs)){
            for (CpGroupCodeRef codeRef : codeRefs) {
                //删除企微
                wxCpGroupChatService.delJoinWay(codeRef.getConfigId());
            }
        }
        //删除活码
        groupCodeMapper.deleteById(id);
        //删除群聊
        refService.deleteByCodeId(groupCode.getId().toString());

    }

    /**
     * 保存活着更新群活码
     * @param groupCodeDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createOrUpdateGroupCode(GroupCodeDTO groupCodeDTO) {
        CpGroupCode groupCode=null;
        groupCodeDTO.setSlogan(SlognUtils.formatSlogn(groupCodeDTO.getSlogan()));
        if(groupCodeDTO.getId()==null) {
            groupCode = mapperFacade.map(groupCodeDTO, CpGroupCode.class);
            if(Objects.isNull(groupCode.getCodeFrom())){
                groupCode.setCodeFrom(CodeChannelEnum.GROUP_CODE.getValue());
            }
            groupCode.setCreateBy(AuthUserContext.get().getUserId());
            groupCode.setStatus(StatusType.YX.getCode());
            groupCode.setCreateName(AuthUserContext.get().getUsername());
            groupCode.setFlag(FlagEunm.USE.getCode());
            groupCode.setCreateTime(new Date());
            groupCode.setState(String.valueOf(RandomUtil.getUniqueNum()));

            //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
            String content=groupCode.getState();
            String logo="";
            ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
            ServerResponseEntity.checkResponse(responseEntity);
            CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
            groupCode.setDrainageUrl(mediaUploadResult.getDrainageUrl());
            groupCode.setDrainagePath(mediaUploadResult.getDrainagePath());

            //备用人员活码
            ServerResponseEntity<StaffVO> staffResponseEntity=staffFeignClient.getStaffById(groupCode.getStandbyStaffId());
            ServerResponseEntity.checkResponse(staffResponseEntity);
            String staffUserId=staffResponseEntity.getData().getQiWeiUserId();
            groupCode.setStandbyStaffState(String.valueOf(RandomUtil.getUniqueNum()));
            //创建备用人员活码
            StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(groupCode, Lists.newArrayList(staffUserId), WxCpContactWayInfo.TYPE.SINGLE, CREATE);
            if(staffCodeExcelDTO==null) {
                this.save(groupCode);
                //渠道源标识
                cpCodeChannelService.saveCpCodeChannel(CodeChannelEnum.GROUP_CODE.getValue(),groupCode.getId().toString(),groupCode.getId().toString(),groupCode.getState());
                //保存引流页面二维码临时素材
                cpMediaRefService.saveCpMediaRef(CpMediaRef.builder()
                        .sourceId(groupCode.getId().toString())
                        .sourceFrom(CodeChannelEnum.GROUP_CODE.getValue())
                        .mediaId(mediaUploadResult.getMediaId())
                        .thumbMediaId(mediaUploadResult.getThumbMediaId())
                        .url(mediaUploadResult.getUrl())
                        .type(mediaUploadResult.getType())
                        .createTime(WechatUtils.formatDate(String.valueOf(mediaUploadResult.getCreatedAt())))
                        .build());
            }
        }else{
            groupCode=this.getById(groupCodeDTO.getId());
            if(Objects.isNull(groupCode)){
                throw new LuckException("操作失败，信息未找到");
            }
            String style=groupCode.getStyle();
            Long standbyStaffId=groupCode.getStandbyStaffId();
            groupCode = mapperFacade.map(groupCodeDTO, CpGroupCode.class);

            //备用人员活码
            if(!standbyStaffId.toString().equals(groupCodeDTO.getStandbyStaffId())){
                //获取备用人员信息
                ServerResponseEntity<StaffVO> staffResponseEntity=staffFeignClient.getStaffById(groupCode.getStandbyStaffId());
                ServerResponseEntity.checkResponse(staffResponseEntity);
                String staffUserId=staffResponseEntity.getData().getQiWeiUserId();
                groupCode.setStandbyStaffState(String.valueOf(RandomUtil.getUniqueNum()));
                //创建备用人员活码
                StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(groupCode, Lists.newArrayList(staffUserId), WxCpContactWayInfo.TYPE.SINGLE, CREATE);
            }
            if(!groupCode.getStyle().equals(groupCodeDTO.getStyle())){
                //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
                String content=groupCode.getState();
                String logo="";
                ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
                ServerResponseEntity.checkResponse(responseEntity);
                CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
                groupCode.setDrainageUrl(mediaUploadResult.getDrainageUrl());
                groupCode.setDrainagePath(mediaUploadResult.getDrainagePath());
            }
            if(Objects.isNull(groupCode.getCodeFrom())){
                groupCode.setCodeFrom(CodeChannelEnum.GROUP_CODE.getValue());
            }

            groupCode.setUpdateTime(new Date());
            groupCode.setUpdateBy(AuthUserContext.get().getUsername());
            this.updateById(groupCode);
        }
        //保存群活码关联群聊信息
        refService.saveTo(groupCode.getId(), CodeChannelEnum.GROUP_CODE.getValue(),groupCodeDTO.getCodeList());

        return groupCode.getId();
    }

    /**
     * 创建备用员工活码qrode
     * @param groupCode
     * @param staffCodeRefs
     */
    private StaffCodeExcelDTO createOrUpdateStaffCode(CpGroupCode groupCode, List<String> staffCodeRefs, WxCpContactWayInfo.TYPE type, String oper) {
        try {
            WxCpContactWayInfo.ContactWay contactWay =  new WxCpContactWayInfo.ContactWay();
//            contactWay.setRemark(cpGroupCode.getRemarks());
            contactWay.setType(type);
            contactWay.setIsTemp(false);
            contactWay.setScene(WxCpContactWayInfo.SCENE.QRCODE);
//            contactWay.setSkipVerify(staffCode.getAuthType()==1?false:true);
            contactWay.setUsers(staffCodeRefs);
            contactWay.setState(groupCode.getStandbyStaffState());
            WxCpContactWayInfo cpContactWayInfo =  new WxCpContactWayInfo();
            cpContactWayInfo.setContactWay(contactWay);
            try {
                if (oper.equals(CREATE)) {
                    WxCpContactWayResult result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().addContactWay(cpContactWayInfo);
                    if (result.getErrcode() == 0) {
                        //企微活码信息
                        groupCode.setStandbyStaffCode(result.getQrCode());
                        groupCode.setStandbyStaffConfigId(result.getConfigId());
                    } else {
                        throw new WxErrorException(result.getErrmsg());
                    }
                } else {
                    CpGroupCode cpGroupCode = this.getById(groupCode.getId());
                    cpContactWayInfo.getContactWay().setConfigId(cpGroupCode.getStandbyStaffConfigId());
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
}
