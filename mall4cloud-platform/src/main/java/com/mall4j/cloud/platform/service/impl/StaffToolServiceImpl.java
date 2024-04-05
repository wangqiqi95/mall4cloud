package com.mall4j.cloud.platform.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.StaffToolDTO;
import com.mall4j.cloud.platform.model.StaffTool;
import com.mall4j.cloud.platform.mapper.StaffToolMapper;
import com.mall4j.cloud.platform.service.StaffToolService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 员工企微工具信息
 *
 * @author ZengFanChang
 * @date 2022-02-12 00:13:55
 */
@Service
public class StaffToolServiceImpl implements StaffToolService {

    @Autowired
    private StaffToolMapper staffToolMapper;

    @Override
    public PageVO<StaffTool> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> staffToolMapper.list());
    }

    @Override
    public StaffTool getById(Long id) {
        return staffToolMapper.getById(id);
    }

    @Override
    public void save(StaffToolDTO staffTool) {
        StaffTool tool = getStaffToolByStaff(staffTool.getStaffId());
        if (null != tool){
            tool.setToolData(staffTool.getToolData());
            staffToolMapper.updateByStaff(tool);
        } else {
            StaffTool newTool = new StaffTool();
            newTool.setStaffId(staffTool.getStaffId());
            newTool.setToolData(staffTool.getToolData());
            staffToolMapper.save(newTool);
        }
    }

    @Override
    public void update(StaffTool staffTool) {
        staffToolMapper.update(staffTool);
    }

    @Override
    public void deleteById(Long id) {
        staffToolMapper.deleteById(id);
    }

    @Override
    public StaffTool getStaffToolByStaff(Long staffId) {
        return staffToolMapper.getStaffToolByStaff(staffId);
    }
}
