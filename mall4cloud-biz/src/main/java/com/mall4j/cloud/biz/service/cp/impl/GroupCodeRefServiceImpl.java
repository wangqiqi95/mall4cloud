package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.constant.cp.GroupCodeRelStatusEnum;
import com.mall4j.cloud.biz.dto.cp.CpGroupCodeListDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.AnalyzeGroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.wx.AddJoinWay;
import com.mall4j.cloud.biz.dto.cp.wx.WxCpGroupChatResult;
import com.mall4j.cloud.biz.mapper.cp.GroupCodeRefMapper;
import com.mall4j.cloud.biz.model.cp.CpCodeChannel;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.CpGroupCode;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeRef;
import com.mall4j.cloud.biz.service.cp.CpCodeChannelService;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.GroupCodeRefService;
import com.mall4j.cloud.biz.service.cp.WxCpGroupChatService;
import com.mall4j.cloud.biz.vo.cp.CpGroupCodeListVO;
import com.mall4j.cloud.biz.vo.cp.GroupCodeRefVO;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 群活码关联群表
 *
 * @author hwy
 * @date 2022-02-16 15:17:20
 */
@RequiredArgsConstructor
@Service
public class GroupCodeRefServiceImpl extends ServiceImpl<GroupCodeRefMapper, CpGroupCodeRef> implements GroupCodeRefService {

    private final  GroupCodeRefMapper groupCodeRefMapper;
    private final MapperFacade mapperFacade;
    private final WxCpGroupChatService wxCpGroupChatService;
    private final CpCodeChannelService cpCodeChannelService;
    private final CustGroupService custGroupService;

    @Override
    public List<CpGroupCodeRef> getListByCodeId(List<Long> codeIds,Integer sourceFrom) {
        List<CpGroupCodeRef> cpGroupCodeRefs=groupCodeRefMapper.selectListByCodeIds(codeIds,sourceFrom);
        if(CollUtil.isEmpty(cpGroupCodeRefs)){
            return ListUtil.empty();
        }
        for (CpGroupCodeRef cpGroupCodeRef : cpGroupCodeRefs) {
            cpGroupCodeRef.setEnableStatus(cpGroupCodeRef.getStatus());
            cpGroupCodeRef.setStatus(this.getGroupCodeRelStatus(cpGroupCodeRef.getScanCount(),
                    cpGroupCodeRef.getTotal(),
                    cpGroupCodeRef.getStatus(),
                    cpGroupCodeRef.getExpireEnd(),
                    cpGroupCodeRef.getChatUserTotal()));

            //计算：单个群可邀请人数：人数上限-当前群人数
            int enabledCustTotal=cpGroupCodeRef.getTotal()-cpGroupCodeRef.getScanCount();
            cpGroupCodeRef.setEnabledCustTotal(enabledCustTotal);

        }
        return cpGroupCodeRefs;
    }

    /**
     * 获取活码关联群聊
     * @param dto
     * @return
     */
    @Override
    public List<AnalyzeGroupCodeVO> selectListBy(AnalyzeGroupCodeDTO dto) {
        return groupCodeRefMapper.selectListBy(dto);
    }

    @Override
    public PageVO<CpGroupCodeRef> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, groupCodeRefMapper::list);
    }

    @Override
    public CpGroupCodeRef getById(Long id) {
        return groupCodeRefMapper.getById(id);
    }

    @Override
    public boolean save(CpGroupCodeRef groupCodeRef) {
        return this.save(groupCodeRef);
    }

    @Override
    public void update(CpGroupCodeRef groupCodeRef) {
        this.updateById(groupCodeRef);
    }

    @Override
    public void deleteById(Long id) {
        CpGroupCodeRef cpGroupCodeRef=this.getById(id);
        if(Objects.isNull(cpGroupCodeRef)){
            throw new LuckException("操作失败，信息未找到");
        }
        cpGroupCodeRef.setIsDelete(1);
        cpGroupCodeRef.setUpdateBy(AuthUserContext.get().getUsername());
        cpGroupCodeRef.setUpdateTime(new Date());
        this.updateById(cpGroupCodeRef);
    }

    @Override
    public GroupCodeRefVO statCount(Long codeId) {
        return groupCodeRefMapper.statCount(codeId);
    }


    @Override
    public void deleteByCodeId(String groupId) {
        groupCodeRefMapper.deleteByCodeId(Long.parseLong(groupId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTo(Long codeId, Integer sourceFrom,List<CpGroupCodeListDTO> cpGroupCodeListDTO) {
        List<CpGroupCodeListDTO> addList=cpGroupCodeListDTO.stream().filter(item-> Objects.isNull(item.getId())).collect(Collectors.toList());
        List<CpGroupCodeListDTO> updateList=cpGroupCodeListDTO.stream().filter(item-> Objects.nonNull(item.getId())).collect(Collectors.toList());

        //先删除
        this.deleteByCodeId(codeId.toString());

        List<CpGroupCodeRef> codeAddList=mapperFacade.mapAsList(addList, CpGroupCodeRef.class);
        List<CpGroupCodeRef> codeUpdateList=mapperFacade.mapAsList(updateList, CpGroupCodeRef.class);

        //添加
        if(CollUtil.isNotEmpty(codeAddList)){
            codeAddList.forEach(item->{
                item.setCodeId(codeId);
                item.setIsDelete(0);
                item.setCreateBy(AuthUserContext.get().getUsername());
                item.setCreateTime(new Date());
                item.setSourceFrom(sourceFrom);
                item.setStatus(1);
                item.setState(String.valueOf(RandomUtil.getUniqueNum()));
                if(Objects.isNull(item.getAutoCreateRoom())){
                    item.setAutoCreateRoom(1);//企微默认：当群满了后，是否自动新建群。0-否；1-是。 默认为1
                }
                if(StrUtil.isEmpty(item.getRoomBaseId()) && item.getAutoCreateRoom()==1){
                    item.setRoomBaseId("1");//默认1开始
                }
                item.setCodeType(2);
                if(StrUtil.isNotEmpty(item.getChatId())){
                    item.setCodeType(1);
                    AddJoinWay addJoinWay=null;
                    if(item.getAutoCreateRoom()==1){ //开启自动建群
                        addJoinWay=new AddJoinWay(null, item.getName(),
                                Arrays.asList(item.getChatId()),
                                item.getState(),
                                item.getAutoCreateRoom(),
                                item.getRoomBaseName(),
                                Integer.parseInt(item.getRoomBaseId()));
                    }else{
                        addJoinWay=new AddJoinWay(null,
                                item.getName(),
                                Arrays.asList(item.getChatId()),
                                item.getState());
                    }

                    String configId = wxCpGroupChatService.addJoinWay(addJoinWay);
                    WxCpGroupChatResult result = wxCpGroupChatService.getJoinWay(configId);
                    item.setQrCode(result.getJoinWay().getQrCode());
                    item.setConfigId(configId);
                }
            });
            this.saveBatch(codeAddList);

            //渠道源标识
            List<CpCodeChannel> cpCodeChannels=new ArrayList<>();
            for (CpGroupCodeRef cpGroupCodeRef : codeAddList) {
                CpCodeChannel cpCodeChannel=new CpCodeChannel();
                cpCodeChannel.setSourceFrom(sourceFrom);
                cpCodeChannel.setSourceId(cpGroupCodeRef.getId().toString());
                cpCodeChannel.setSourceState(cpGroupCodeRef.getState());
                cpCodeChannel.setBaseId(cpGroupCodeRef.getCodeId().toString());
                cpCodeChannels.add(cpCodeChannel);
            }
            cpCodeChannelService.saveCpCodeChannel(cpCodeChannels);
        }

        //更新
        if(CollUtil.isNotEmpty(codeUpdateList)){
            codeUpdateList.forEach(item->{
                item.setScanCount(null);
                item.setIsDelete(0);
                item.setCreateBy(AuthUserContext.get().getUsername());
                item.setCreateTime(new Date());
                item.setCodeType(2);
                if(StrUtil.isNotEmpty(item.getChatId())) {
                    item.setCodeType(1);
                    AddJoinWay addJoinWay=null;
                    if(item.getAutoCreateRoom()==1){ //开启自动建群
                        addJoinWay=new AddJoinWay(item.getConfigId(), item.getName(),
                                Arrays.asList(item.getChatId()),
                                item.getState(),
                                item.getAutoCreateRoom(),
                                item.getRoomBaseName(),
                                Integer.parseInt(item.getRoomBaseId()));
                    }else{
                        addJoinWay=new AddJoinWay(item.getConfigId(),
                                item.getName(),
                                Arrays.asList(item.getChatId()),
                                item.getState());
                    }
                    wxCpGroupChatService.updateJoinWay(addJoinWay);
                }
            });
            this.updateBatchById(codeUpdateList);
        }
    }

    @Override
    public void updateTo(CpGroupCode groupCode, Integer sourceFrom, List<CpGroupCodeListDTO> cpGroupCodeList) {

    }

    /**
     * 修改群聊状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        CpGroupCodeRef cpGroupCodeRef=this.getById(id);
        if(Objects.isNull(cpGroupCodeRef)){
            throw new LuckException("操作失败，信息未找到");
        }
        cpGroupCodeRef.setStatus(status);
        cpGroupCodeRef.setUpdateBy(AuthUserContext.get().getUsername());
        cpGroupCodeRef.setUpdateTime(new Date());
        this.updateById(cpGroupCodeRef);
    }

    /**
     * TODO
     * 群活码详情-群聊信息统计
     * @param pageDTO
     * @param codeId
     * @param groupName
     * @return
     */
    @Override
    public PageVO<AnalyzeGroupCodeVO> pageAnalyzes(PageDTO pageDTO, Long codeId, String groupName) {
        AnalyzeGroupCodeDTO dto=new AnalyzeGroupCodeDTO();
        dto.setCodeId(codeId);
        dto.setSourceFrom(CodeChannelEnum.GROUP_CODE.getValue());
        PageVO<AnalyzeGroupCodeVO> pageVO=PageUtil.doPage(pageDTO, () -> groupCodeRefMapper.selectListBy(dto));

        for (AnalyzeGroupCodeVO analyzeGroupCodeVO : pageVO.getList()) {
            analyzeGroupCodeVO.setEnableStatus(analyzeGroupCodeVO.getStatus());
            //群聊状态
            analyzeGroupCodeVO.setStatus(getGroupCodeRelStatus(analyzeGroupCodeVO.getScanCount(),
                    analyzeGroupCodeVO.getUpperTotal(),
                    analyzeGroupCodeVO.getStatus(),
                    analyzeGroupCodeVO.getExpireEnd(),
                    analyzeGroupCodeVO.getChatUserTotal()));
        }

        return pageVO;
    }


    /**
     * 处理群聊状态
     * @param scanCount 入群人数
     * @param total
     * @return
     */
    @Override
    public int getGroupCodeRelStatus(Integer scanCount,Integer total,int status,Date expireEnd,int chatUserTotal){
        if(status==0){
            return GroupCodeRelStatusEnum.STOP.getValue();
        }
        if(status==GroupCodeRelStatusEnum.STOP.getValue()){//已停用
            return status;
        }
        //已过期
        if(Objects.nonNull(expireEnd) && DateUtil.date().getTime()>expireEnd.getTime()){
            status= GroupCodeRelStatusEnum.EXPIRED.getValue();
//        }else if(scanCount>0 && scanCount<total){
        }else if(chatUserTotal>0 && chatUserTotal<total){
            //拉人中
            status= GroupCodeRelStatusEnum.PULL_USER.getValue();
        }else if(chatUserTotal>=200){
//        }else if(scanCount>=total || scanCount==200){
            //已满员
            status= GroupCodeRelStatusEnum.FULL_USER.getValue();
        }else if(chatUserTotal>=total){
//        }else if(scanCount>=total){
            //已达到上线
            status= GroupCodeRelStatusEnum.UPPER_LIMIT.getValue();
        }else if(scanCount<=0){
            //未开始
            status= GroupCodeRelStatusEnum.NO_START.getValue();
        }else{
            //未开始
            status= GroupCodeRelStatusEnum.NO_START.getValue();
        }
        return status;
    }

}
