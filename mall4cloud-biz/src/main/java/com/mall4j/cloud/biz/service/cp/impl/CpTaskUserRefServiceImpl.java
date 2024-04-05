package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.chat.SessionAgreeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeUserRefDTO;
import com.mall4j.cloud.biz.mapper.cp.CpTaskUserRefMapper;
import com.mall4j.cloud.biz.model.cp.CpTaskUserRef;
import com.mall4j.cloud.biz.service.cp.CpTaskUserRefService;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 群发任务客户关联表
 *
 * @author gmq
 * @date 2023-10-30 14:23:04
 */
@Service
public class CpTaskUserRefServiceImpl extends ServiceImpl<CpTaskUserRefMapper, CpTaskUserRef> implements CpTaskUserRefService {

    @Autowired
    private CpTaskUserRefMapper cpTaskUserRefMapper;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 标签建群-发送好友列表【移动端】
     * @return 列表分页数据
     */
    @Override
    public PageVO<CpTagGroupCodeSendUserVO> pageTagCodeRelUser(CpTagGroupCodeUserRefDTO dto) {
        PageDTO pageDTO=new PageDTO();
        pageDTO.setPageNum(dto.getPageNum());
        pageDTO.setPageSize(dto.getPageSize());
        CpTagGroupCodeAnalyzeDTO analyzeDTO=mapperFacade.map(dto,CpTagGroupCodeAnalyzeDTO.class);
        if(!initSelect(analyzeDTO)){
            PageVO<CpTagGroupCodeSendUserVO> pageVO=new PageVO<CpTagGroupCodeSendUserVO>();
            pageVO.inint();
            return pageVO;
        }

        PageVO<CpTagGroupCodeSendUserVO> page= PageUtil.doPage(pageDTO, () -> cpTaskUserRefMapper.list(analyzeDTO));
        if(CollUtil.isNotEmpty(page.getList())){
            List<String> qiWeiUserIds=page.getList().stream().map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
            List<Long> staffIds=page.getList().stream().map(item->item.getStaffId()).distinct().collect(Collectors.toList());
            Map<String,UserStaffCpRelationListVO> userRelVOMap= MapUtil.newHashMap();
            if(CollUtil.isNotEmpty(qiWeiUserIds)){
                UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
                staffCpRelationSearchDTO.setQiWeiUserIds(qiWeiUserIds);
                staffCpRelationSearchDTO.setPageSize(10);
                staffCpRelationSearchDTO.setPageNum(1);
                staffCpRelationSearchDTO.setStatus(1);
                staffCpRelationSearchDTO.setStaffIds(staffIds);
                ServerResponseEntity<List<UserStaffCpRelationListVO>>  responseEntityUc=userStaffCpRelationFeignClient.getUserStaffRelBy(staffCpRelationSearchDTO);
                ServerResponseEntity.checkResponse(responseEntityUc);
                userRelVOMap= LambdaUtils.toMap(responseEntityUc.getData(),UserStaffCpRelationListVO::getQiWeiUserId);
            }
            for (CpTagGroupCodeSendUserVO cpTagGroupCodeSendUserVO : page.getList()) {
                if(userRelVOMap.containsKey(cpTagGroupCodeSendUserVO.getQiWeiUserId())){
                    cpTagGroupCodeSendUserVO.setPhone(userRelVOMap.get(cpTagGroupCodeSendUserVO.getQiWeiUserId()).getCpRemarkMobiles());
                    cpTagGroupCodeSendUserVO.setUserName(userRelVOMap.get(cpTagGroupCodeSendUserVO.getQiWeiUserId()).getQiWeiNickName());
                    cpTagGroupCodeSendUserVO.setAvatar(userRelVOMap.get(cpTagGroupCodeSendUserVO.getQiWeiUserId()).getAvatar());
                    if(userRelVOMap.get(cpTagGroupCodeSendUserVO.getQiWeiUserId()).getStatus()>1){
                        cpTagGroupCodeSendUserVO.setStatus(3);
                    }
                    if(cpTagGroupCodeSendUserVO.getSendStatus()<=0
                            && userRelVOMap.get(cpTagGroupCodeSendUserVO.getQiWeiUserId()).getStatus()>1){
                        cpTagGroupCodeSendUserVO.setSendStatus(2);
                    }
                }
            }
        }

        return page;
    }

    private boolean initSelect(CpTagGroupCodeAnalyzeDTO dto){
        if(CollUtil.isNotEmpty(dto.getTagIds()) || StrUtil.isNotEmpty(dto.getKeyWord())){
            UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
            userStaffCpRelationSearchDTO.initPage();
            userStaffCpRelationSearchDTO.setStatus(1);
            userStaffCpRelationSearchDTO.setQiWeiNickName(dto.getKeyWord());
            userStaffCpRelationSearchDTO.setTagId(dto.getTagIds());
            ServerResponseEntity<List<UserStaffCpRelationListVO>> responseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            List<String> userIds=responseEntity.getData().stream()
                    .filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId()))
                    .map(item->item.getQiWeiUserId())
                    .distinct()
                    .collect(Collectors.toList());
            if(CollUtil.isNotEmpty(userIds)){
                dto.setUserQiWeiUserIds(userIds);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public List<CpTaskUserRef> getListByTaskId(Long taskId) {
        return null;
    }

    @Override
    public CpTaskUserRef getById(Long id) {
        return cpTaskUserRefMapper.getById(id);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        this.update(new LambdaUpdateWrapper<CpTaskUserRef>()
                .eq(CpTaskUserRef::getTaskId,taskId)
                .set(CpTaskUserRef::getIsDelete,1)
                .set(CpTaskUserRef::getUpdateBy, AuthUserContext.get().getUsername())
                .set(CpTaskUserRef::getUpdateTime,new Date()));
    }
}
