package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.dto.cp.CpCustGroupCountDTO;
import com.mall4j.cloud.api.biz.vo.CustGroupStaffCountVO;
import com.mall4j.cloud.api.user.vo.SelectUserCountVO;
import com.mall4j.cloud.biz.dto.cp.CustGroupDTO;
import com.mall4j.cloud.biz.dto.cp.GetSelectedGroupListDTO;
import com.mall4j.cloud.biz.mapper.cp.CustGroupMapper;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.GroupTagRef;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.GroupTagRefService;
import com.mall4j.cloud.biz.vo.cp.CustGroupCountVO;
import com.mall4j.cloud.biz.vo.cp.CustGroupVO;
import com.mall4j.cloud.biz.vo.cp.SoldGroupCustVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * 客户群表
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@RequiredArgsConstructor
@Service
public class CustGroupServiceImpl extends ServiceImpl<CustGroupMapper,CpCustGroup> implements CustGroupService {

    private final CustGroupMapper custGroupMapper;
    private final GroupTagRefService groupTagRefService;
    private final MapperFacade mapperFacade;

    @Override
    public PageVO<CustGroupVO> page(PageDTO pageDTO, CustGroupDTO request) {
        return PageUtil.doPage(pageDTO, () -> custGroupMapper.list(request));
    }

    /**
     * 导出群信息
     * @param request
     * @return
     */
    @Override
    public List<SoldGroupCustVO> soldList(CustGroupDTO request) {
        List<CustGroupVO> list=custGroupMapper.list(request);
        List<SoldGroupCustVO> soldList=mapperFacade.mapAsList(list,SoldGroupCustVO.class);
        return soldList;
    }

    @Override
    public CpCustGroup getById(String id) {
        return custGroupMapper.getById(id);
    }

    @Override
    public void deleteById(String id) {
        custGroupMapper.deleteById(id);
    }

    @Override
    public CustGroupCountVO count(CustGroupDTO request) {
        return custGroupMapper.count(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addTag(String groupId, List<Long> tagIds) {
        groupTagRefService.deleteByGroupId(groupId);
        tagIds.forEach(id->groupTagRefService.save(new GroupTagRef(groupId, id.toString())));
    }

    @Override
    public PageVO<CpCustGroup> getSelectGroupList(PageDTO pageDTO, GetSelectedGroupListDTO groupCodeDTO){
        return PageUtil.doPage(pageDTO, () -> custGroupMapper.getSelectGroupList(groupCodeDTO));
    }

    @Override
    public List<CpCustGroup> getCustGroupList(Long id) {
        return custGroupMapper.getCustGroupList(id);
    }

    @Override
    public List<CpCustGroup> getCustGroupListByOwnerId(Long addBy) {
        return custGroupMapper.getCustGroupListByOwnerId(addBy);
    }

    @Override
    public void updateBatchStatusToAssigning(List<String> ids) {
        custGroupMapper.updateBatchStatusToAssigning(ids);
    }

    /**
     * 好友统计-员工群信息
     * @return
     */
    @Override
    public List<CustGroupStaffCountVO> groupCountByStaff(CpCustGroupCountDTO dto) {

        //初始化数据
        Map<Long, CustGroupStaffCountVO> countVOMap=new HashMap<>();
        for (Long staffId : dto.getStaffIds()) {
            CustGroupStaffCountVO custGroupStaffCountVO=new CustGroupStaffCountVO();
            custGroupStaffCountVO.setStaffId(staffId);
            custGroupStaffCountVO.setGroupCount(0);
            custGroupStaffCountVO.setNewGroupUserCount(0);
            custGroupStaffCountVO.setNewGroupCount(0);
            countVOMap.put(staffId,custGroupStaffCountVO);
        }

        //TODO 新增客户群
        List<SelectUserCountVO>  newAddList=custGroupMapper.selectCountByStaff(dto.getStaffIds(),dto.getStartTime(),dto.getEndTime());
        if(CollUtil.isNotEmpty(newAddList)){
            for (SelectUserCountVO selectUserCountVO : newAddList) {
                if(countVOMap.containsKey(selectUserCountVO.getStaffId())){
                    countVOMap.get(selectUserCountVO.getStaffId()).setNewGroupCount(selectUserCountVO.getCount());
                }
            }
        }
        //TODO 总客户群
        List<SelectUserCountVO>  countList=custGroupMapper.selectCountByStaff(dto.getStaffIds(),null,null);
        if(CollUtil.isNotEmpty(countList)){
            for (SelectUserCountVO selectUserCountVO : countList) {
                if(countVOMap.containsKey(selectUserCountVO.getStaffId())){
                    countVOMap.get(selectUserCountVO.getStaffId()).setGroupCount(selectUserCountVO.getCount());
                }
            }
        }

        //TODO 新增入群人数
        List<SelectUserCountVO>  countUserList=custGroupMapper.selectCountUserByStaff(dto.getStaffIds(),dto.getStartTime(),dto.getEndTime());
        if(CollUtil.isNotEmpty(countUserList)){
            for (SelectUserCountVO selectUserCountVO : countUserList) {
                if(countVOMap.containsKey(selectUserCountVO.getStaffId())){
                    countVOMap.get(selectUserCountVO.getStaffId()).setNewGroupUserCount(selectUserCountVO.getCount());
                }
            }
        }

        return new ArrayList<>(countVOMap.values());
    }

    @Override
    public List<CpCustGroup> getByIds(List<String> ids) {
        return custGroupMapper.getByIds(ids);
    }
}
