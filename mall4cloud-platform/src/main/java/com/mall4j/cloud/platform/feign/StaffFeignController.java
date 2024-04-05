package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.dto.StaffBindQiWeiDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryPageDTO;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.manager.StaffSyncManager;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.service.StaffOrgRelService;
import com.mall4j.cloud.platform.service.StaffService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StaffFeignController implements StaffFeignClient {

    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffOrgRelService orgRelService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StaffSyncManager staffSyncManager;

    @Override
    public ServerResponseEntity<PageVO<StaffVO>> getStaffPage(StaffQueryPageDTO queryDTO) {
        PageDTO pageDTO=new PageDTO();
        pageDTO.setPageSize(queryDTO.getPageSize());
        pageDTO.setPageNum(queryDTO.getPageNum());
        StaffQueryDTO dto=mapperFacade.map(queryDTO,StaffQueryDTO.class);
        return ServerResponseEntity.success(staffService.page(pageDTO,dto));
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> findByStaffQueryDTO(StaffQueryDTO staffQueryDTO) {
        return ServerResponseEntity.success(staffService.findByStaffQueryDTO(staffQueryDTO));
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> listStaffByStatus(StaffQueryDTO staffQueryDTO) {
        return ServerResponseEntity.success(staffService.listStaffByStatus(staffQueryDTO));
    }

    @Override
    public ServerResponseEntity<Integer> countByStaffNum(StaffQueryDTO staffQueryDTO) {
        return ServerResponseEntity.success(staffService.countByStaffNum(staffQueryDTO));
    }

    @Override
    public ServerResponseEntity<StaffVO> getStaffById(Long id) {
        return ServerResponseEntity.success(staffService.getById(id));
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getStaffByIds(List<Long> ids) {
        return ServerResponseEntity.success(staffService.getByIds(ids));
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getStaffBypByIds(List<Long> ids) {
        return ServerResponseEntity.success(staffService.getByIds(ids));
    }

    @Override
    public ServerResponseEntity<StaffVO> getStaffByQiWeiUserId(String qiWeiUserId) {
        return ServerResponseEntity.success(staffService.getByQiWeiUserId(qiWeiUserId));
    }

    @Override
    public ServerResponseEntity<StaffVO> getStaffByMobile(String mobile) {
        return ServerResponseEntity.success(staffService.getByMobile(mobile));
    }

    @Override
    public ServerResponseEntity<StaffVO> getBySysUserId(String sysUserId) {
        return ServerResponseEntity.success(staffService.getBySysUserId(sysUserId));
    }

    @Override
    public ServerResponseEntity<Void> bindStaffQiWeiUserId(StaffBindQiWeiDTO staffBindQiWeiDTO) {
        staffService.bindStaffQiWeiUserId(staffBindQiWeiDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getStaffListByMobiles(List<String> mobiles) {
        List<Staff> staffList = staffService.getStaffListByMobiles(mobiles);
        List<StaffVO> staffVOList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(staffList)) {
            staffVOList = staffList.stream().map(staff -> {
                StaffVO staffVO = mapperFacade.map(staff, StaffVO.class);
                return staffVO;
            }).collect(Collectors.toList());
        }
        return ServerResponseEntity.success(staffVOList);
    }

    /**
     * 企微会话存档回掉-更新会话存档开启状态
     * @param qiWeiUserId
     * @param cpMsgAudit
     * @return
     */
    @Override
    public ServerResponseEntity<List<StaffVO>> updateCpMsgAuditByUserId(String qiWeiUserId, Integer cpMsgAudit) {
        staffService.updateCpMsgAuditByUserId(qiWeiUserId,cpMsgAudit);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getByStaffNOOrNickName(String staff) {
        return staffService.getByStaffNOOrNickName(staff);
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getStaffListManager(List<Long> serviceStoreIdList) {
        return staffService.getStoreManagerByStoreIdList(serviceStoreIdList);
    }

    @Override
    public ServerResponseEntity<List<StaffOrgVO>> getStaffAndOrgs(List<Long> staffIds) {
        return ServerResponseEntity.success(staffService.getStaffAndOrgs(staffIds));
    }

    @Override
    public ServerResponseEntity<List<Long>> getOrgAndChildByStaffIds(List<Long> staffIds) {
        return ServerResponseEntity.success(orgRelService.getOrgAndChildByStaffIds(staffIds));
    }

    /**
     * 企微通讯录事件更新员工信息
     * @return
     */
    @Override
    public ServerResponseEntity<Void> staffSyncMessage(StaffSyncDTO staffSyncDTO) {
        staffSyncManager.staffSyncMessage(staffSyncDTO);
        return ServerResponseEntity.success();
    }
}
