package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.cp.CountStaffRelByStatesVO;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeMapper;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeStaffMapper;
import com.mall4j.cloud.biz.mapper.cp.GroupCodeRefMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeStaffVO;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeVO;
import com.mall4j.cloud.biz.vo.cp.CpWxMediaUploadResult;
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
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayInfo;
import me.chanjar.weixin.cp.bean.external.WxCpContactWayResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自动拉群活码表
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Service
public class CpAutoGroupCodeServiceImpl extends ServiceImpl<CpAutoGroupCodeMapper, CpAutoGroupCode> implements CpAutoGroupCodeService {
    private final static String  UPDATE="update";
    private final static String  CREATE="create";
    @Autowired
    private CpAutoGroupCodeMapper cpAutoGroupCodeMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private GroupCodeRefService refService;
    @Autowired
    private CpAutoGroupCodeStaffMapper cpAutoGroupCodeStaffMapper;
    @Autowired
    private CpAutoGroupCodeStaffService codeStaffService;
    @Autowired
    private GroupCodeRefMapper codeRefMapper;
    @Autowired
    private WxCpGroupChatService wxCpGroupChatService;
    @Autowired
    private CpCodeChannelService cpCodeChannelService;
    @Autowired
    private CpMediaRefService cpMediaRefService;
    @Autowired
    private DomainConfig domainConfig;
    @Autowired
    private DrainageUrlManager drainageUrlManager;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    @Autowired
    private CpAutoGroupCodeUserService cpAutoGroupCodeUserService;
    @Autowired
    private WeixinCpExternalManager weixinCpExternalManager;

    @Override
    public PageVO<CpAutoGroupCodeVO> page(PageDTO pageDTO,CpAutoGroupCodeSelectDTO dto) {
        PageVO<CpAutoGroupCodeVO> pageVO=PageUtil.doPage(pageDTO, () -> cpAutoGroupCodeMapper.list(dto));

        if(CollUtil.isNotEmpty(pageVO.getList())){
            //TODO 添加好友数: 根据渠道标识统计:user_staff_cp_relation -> state字段
            List<String> states=pageVO.getList().stream().map(item->item.getState()).collect(Collectors.toList());
            ServerResponseEntity<List<CountStaffRelByStatesVO>> responseEntity=userStaffCpRelationFeignClient.getCountByStates(states);
            ServerResponseEntity.checkResponse(responseEntity);
            Map<String,CountStaffRelByStatesVO> staffRelByStatesVOMap= LambdaUtils.toMap(responseEntity.getData(),CountStaffRelByStatesVO::getCpState);
            for (CpAutoGroupCodeVO cpAutoGroupCodeVO : pageVO.getList()) {
                cpAutoGroupCodeVO.setAddUserCount(0);
                if(staffRelByStatesVOMap.containsKey(cpAutoGroupCodeVO.getState())){
                    cpAutoGroupCodeVO.setAddUserCount(staffRelByStatesVOMap.get(cpAutoGroupCodeVO.getState()).getUserCount());
                }
            }
        }


        return pageVO;
    }

    @Override
    public CpAutoGroupCode getById(Long id) {
        return cpAutoGroupCodeMapper.selectById(id);
    }

    /**
     * 获取活码详情
     * @param id
     * @return
     */
    @Override
    public CpAutoGroupCodeVO getDetialById(Long id) {
        CpAutoGroupCode cpAutoGroupCode=this.getById(id);
        if(Objects.isNull(cpAutoGroupCode)){
            throw new LuckException("群活码信息未找到");
        }
        CpAutoGroupCodeVO cpAutoGroupCodeVO=mapperFacade.map(cpAutoGroupCode,CpAutoGroupCodeVO.class);
        if(StrUtil.isNotBlank(cpAutoGroupCodeVO.getSlogan())){
            cpAutoGroupCodeVO.setSlogan(SlognUtils.parseSlogn(cpAutoGroupCodeVO.getSlogan()));
        }
        //获取执行员工
        CpAutoGroupCodeStaffSelectDTO dto=new CpAutoGroupCodeStaffSelectDTO();
        dto.setCodeId(id);
        List<CpAutoGroupCodeStaffVO> staffVOS=cpAutoGroupCodeStaffMapper.list(dto);
        cpAutoGroupCodeVO.setStaffs(staffVOS);

        //关联群聊信息
        List<CpGroupCodeRef>  cpGroupCodeRefs=refService.getListByCodeId(Arrays.asList(cpAutoGroupCodeVO.getId()),CodeChannelEnum.AUTO_GROUP_CODE.getValue());
        cpAutoGroupCodeVO.setCodeList(cpGroupCodeRefs);

        //邀请入群人数：cp_auto_group_code_user -> send_status=1
        cpAutoGroupCodeVO.setInviteCount(cpAutoGroupCodeUserService.countSendByCodeId(cpAutoGroupCodeVO.getId()));
        //入群人数：cp_auto_group_code_user -> join_group=1
        cpAutoGroupCodeVO.setJoinGroupCount(cpAutoGroupCodeUserService.countJoinGroupByCodeId(cpAutoGroupCodeVO.getId()));

        //添加好友数
        cpAutoGroupCodeVO.setAddUserCount(0);
        ServerResponseEntity<List<CountStaffRelByStatesVO>> responseEntity=userStaffCpRelationFeignClient.getCountByStates(ListUtil.toList(cpAutoGroupCodeVO.getState()));
        ServerResponseEntity.checkResponse(responseEntity);
        if(CollUtil.isNotEmpty(responseEntity.getData())){
            Map<String,CountStaffRelByStatesVO> staffRelByStatesVOMap= LambdaUtils.toMap(responseEntity.getData(),CountStaffRelByStatesVO::getCpState);
            if(staffRelByStatesVOMap.containsKey(cpAutoGroupCodeVO.getState())){
                cpAutoGroupCodeVO.setAddUserCount(staffRelByStatesVOMap.get(cpAutoGroupCodeVO.getState()).getUserCount());
            }
        }

        return cpAutoGroupCodeVO;
    }

    /**
     * 1. 实际创建的是渠道活码
     * 2. 设置的群聊需要与群活码逻辑一致处理
     * 3. 客户通过渠道活码添加好友成功后，发送消息：入群引导语+群活码引流链接
     * @param cpAutoGroupCodeDTO 自动拉群活码表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrUpate(CpAutoGroupCodeDTO cpAutoGroupCodeDTO) {
        CpAutoGroupCode cpAutoGroupCode = null;
        cpAutoGroupCodeDTO.setSlogan(SlognUtils.formatSlogn(cpAutoGroupCodeDTO.getSlogan()));
        List<CpAutoGroupCodeStaff> codeStaffs=mapperFacade.mapAsList(cpAutoGroupCodeDTO.getStaffs(),CpAutoGroupCodeStaff.class);
        List<String> userIds=codeStaffs.stream().map(item->item.getUserId()).collect(Collectors.toList());
        if(Objects.isNull(cpAutoGroupCodeDTO.getId())){
            cpAutoGroupCode = mapperFacade.map(cpAutoGroupCodeDTO, CpAutoGroupCode.class);
            cpAutoGroupCode.setCreateBy(AuthUserContext.get().getUserId());
            cpAutoGroupCode.setStatus(StatusType.YX.getCode());
            cpAutoGroupCode.setCreateName(AuthUserContext.get().getUsername());
            cpAutoGroupCode.setFlag(FlagEunm.USE.getCode());
            cpAutoGroupCode.setCreateTime(new Date());
            cpAutoGroupCode.setState(String.valueOf(RandomUtil.getUniqueNum()));

            //产生渠道活码
            StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(cpAutoGroupCode,userIds , WxCpContactWayInfo.TYPE.MULTI, CREATE);
            if(staffCodeExcelDTO==null) {

                //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
                String content=cpAutoGroupCode.getState();
//                String logo=domainConfig.getDomain()+cpAutoGroupCode.getCodeStyle();
                String logo=cpAutoGroupCode.getCodeStyle();
                ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
                ServerResponseEntity.checkResponse(responseEntity);
                CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
                cpAutoGroupCode.setDrainageUrl(mediaUploadResult.getDrainageUrl());
                cpAutoGroupCode.setDrainagePath(mediaUploadResult.getDrainagePath());

                this.save(cpAutoGroupCode);

                //渠道源标识
                cpCodeChannelService.saveCpCodeChannel(CodeChannelEnum.AUTO_GROUP_CODE.getValue(),cpAutoGroupCode.getId().toString(),cpAutoGroupCode.getId().toString(),cpAutoGroupCode.getState());

                //保存引流页面二维码临时素材
                cpMediaRefService.saveCpMediaRef(CpMediaRef.builder()
                        .sourceId(cpAutoGroupCode.getId().toString())
                        .sourceFrom(CodeChannelEnum.AUTO_GROUP_CODE.getValue())
                        .mediaId(mediaUploadResult.getMediaId())
                        .thumbMediaId(mediaUploadResult.getThumbMediaId())
                        .url(mediaUploadResult.getUrl())
                        .type(mediaUploadResult.getType())
                        .createTime(WechatUtils.formatDate(String.valueOf(mediaUploadResult.getCreatedAt())))
                        .build());
            }

        }else{
            cpAutoGroupCode=this.getById(cpAutoGroupCodeDTO.getId());
            if(Objects.isNull(cpAutoGroupCode)){
                throw new LuckException("活码未找到");
            }
            String state=cpAutoGroupCode.getState();
            String style=cpAutoGroupCode.getCodeStyle();
            cpAutoGroupCode = mapperFacade.map(cpAutoGroupCodeDTO, CpAutoGroupCode.class);
            if(!style.equals(cpAutoGroupCodeDTO.getCodeStyle())){
                //TODO 引流链接【需要确认生成链接】必须带上渠道唯一标识参数:state，移动端引流需根据渠道唯一参数获取对应渠道活码信息
                String content=state;
                String logo=cpAutoGroupCode.getCodeStyle();
//                String logo=domainConfig.getDomain()+cpAutoGroupCode.getCodeStyle();
                ServerResponseEntity<CpWxMediaUploadResult> responseEntity=drainageUrlManager.getDrainageUrlQrCode(content,logo);
                ServerResponseEntity.checkResponse(responseEntity);
                CpWxMediaUploadResult mediaUploadResult=responseEntity.getData();
                cpAutoGroupCode.setDrainageUrl(mediaUploadResult.getDrainageUrl());
                cpAutoGroupCode.setDrainagePath(mediaUploadResult.getDrainagePath());
            }
            //更新渠道活码
            StaffCodeExcelDTO staffCodeExcelDTO = createOrUpdateStaffCode(cpAutoGroupCode,userIds , WxCpContactWayInfo.TYPE.MULTI, UPDATE);
            if(staffCodeExcelDTO==null) {
                cpAutoGroupCode.setCodeName(cpAutoGroupCodeDTO.getCodeName());
                cpAutoGroupCode.setSlogan(cpAutoGroupCodeDTO.getSlogan());
                cpAutoGroupCode.setUpdateTime(new Date());
                cpAutoGroupCode.setUpdateBy(AuthUserContext.get().getUsername());
                this.updateById(cpAutoGroupCode);
            }

        }

        //保存群活码关联群聊信息
        refService.saveTo(cpAutoGroupCode.getId(), CodeChannelEnum.AUTO_GROUP_CODE.getValue(),cpAutoGroupCodeDTO.getCodeList());

        Long codeId=cpAutoGroupCode.getId();

        //处理接待人员员工
        cpAutoGroupCodeStaffMapper.deleteByCodeId(codeId);
        for (CpAutoGroupCodeStaff codeStaff : codeStaffs) {
            codeStaff.setCodeId(codeId);
            codeStaff.setCreateTime(new Date());
            codeStaff.setIsDelete(0);
            codeStaff.setCreateBy(AuthUserContext.get().getUsername());
        }
        codeStaffService.saveBatch(codeStaffs);


        /**
         * TODO 后续逻辑
         * 1.
         */


    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        CpAutoGroupCode autoGroupCode = this.getById(id);
        if(Objects.isNull(autoGroupCode)){
            throw new LuckException("活码未找到");
        }
        //获取关联群聊
        List<CpGroupCodeRef> codeRefs= codeRefMapper.selectListByCodeIds(Arrays.asList(id),CodeChannelEnum.AUTO_GROUP_CODE.getValue())
                .stream().filter(item-> StrUtil.isNotEmpty(item.getConfigId())).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(codeRefs)){
            for (CpGroupCodeRef codeRef : codeRefs) {
                //删除企微
                wxCpGroupChatService.delJoinWay(codeRef.getConfigId());
            }
        }
        //删除企微活码
        weixinCpExternalManager.delContactWay(autoGroupCode.getConfigId());
        //删除活码
        cpAutoGroupCodeMapper.deleteById(id);
        //删除群聊
        refService.deleteByCodeId(id.toString());
        //删除接待人员
        cpAutoGroupCodeStaffMapper.deleteByCodeId(id);
    }

    /**
     * 创建qrode
     * @param staffCode
     */
    private StaffCodeExcelDTO createOrUpdateStaffCode(CpAutoGroupCode staffCode, List<String> userIds, WxCpContactWayInfo.TYPE type, String oper) {
        try {
            WxCpContactWayInfo.ContactWay contactWay =  new WxCpContactWayInfo.ContactWay();
            contactWay.setRemark(staffCode.getRemarks());
            contactWay.setType(type);
            contactWay.setIsTemp(false);
            contactWay.setScene(WxCpContactWayInfo.SCENE.QRCODE);
            contactWay.setSkipVerify(staffCode.getAuthType()==1?false:true);
            contactWay.setUsers(userIds);
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
                    CpAutoGroupCode staffCodeDTO = this.getById(staffCode.getId());
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
}
