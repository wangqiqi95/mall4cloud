package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.dto.StaffBindQiWeiDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryPageDTO;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "mall4cloud-platform",contextId = "staff")
public interface StaffFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffPage")
    ServerResponseEntity<PageVO<StaffVO>> getStaffPage(@RequestBody StaffQueryPageDTO queryDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/findByStaffQueryDTO")
    ServerResponseEntity<List<StaffVO>> findByStaffQueryDTO(@RequestBody StaffQueryDTO staffQueryDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/listStaffByStatus")
    ServerResponseEntity<List<StaffVO>> listStaffByStatus(@RequestBody StaffQueryDTO staffQueryDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/countByStaffNum")
    ServerResponseEntity<Integer> countByStaffNum(@RequestBody StaffQueryDTO staffQueryDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffById")
    ServerResponseEntity<StaffVO> getStaffById(@RequestParam("id") Long id);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffByIds")
    ServerResponseEntity<List<StaffVO>> getStaffByIds(@RequestBody List<Long> ids);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffBypByIds")
    ServerResponseEntity<List<StaffVO>> getStaffBypByIds(@RequestBody List<Long> ids);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffByQiWeiUserId")
    ServerResponseEntity<StaffVO> getStaffByQiWeiUserId(@RequestParam("qiWeiUserId") String qiWeiUserId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffByMobile")
    ServerResponseEntity<StaffVO> getStaffByMobile(@RequestParam("mobile") String mobile);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getBySysUserId")
    ServerResponseEntity<StaffVO> getBySysUserId(@RequestParam("sysUserId") String sysUserId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/bindStaffQiWeiUserId")
    ServerResponseEntity<Void> bindStaffQiWeiUserId(@RequestBody StaffBindQiWeiDTO staffBindQiWeiDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffListByMobiles")
    ServerResponseEntity<List<StaffVO>> getStaffListByMobiles(@RequestParam("mobiles") List<String> mobiles);

    /**
     * 企微会话存档回掉-更新会话存档开启状态
     * @param qiWeiUserId
     * @param cpMsgAudit
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/updateCpMsgAuditByUserId")
    ServerResponseEntity<List<StaffVO>> updateCpMsgAuditByUserId(@RequestParam("qiWeiUserId") String qiWeiUserId,@RequestParam("cpMsgAudit") Integer cpMsgAudit);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/get/staff/list/by/no/or/name")
    ServerResponseEntity<List<StaffVO>> getByStaffNOOrNickName(@RequestParam("staff") String staff);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/get/staff/manager/list/by/store")
    ServerResponseEntity<List<StaffVO>> getStaffListManager(@RequestParam("serviceStoreIdList") List<Long> serviceStoreIdList);

    /**
     * 获取员工部门信息
     * @param staffIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getStaffAndOrgs")
    ServerResponseEntity<List<StaffOrgVO>> getStaffAndOrgs(@RequestParam("staffIds") List<Long> staffIds);

    /**
     * 获取员工部门信息
     * @param staffIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/getOrgAndChildByStaffIds")
    ServerResponseEntity<List<Long>> getOrgAndChildByStaffIds(@RequestParam("staffIds") List<Long> staffIds);

    /**
     * 企微通讯录事件更新员工信息
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/staff/staffSyncMessage")
    ServerResponseEntity<Void> staffSyncMessage(@RequestBody StaffSyncDTO staffSyncDTO);

}
