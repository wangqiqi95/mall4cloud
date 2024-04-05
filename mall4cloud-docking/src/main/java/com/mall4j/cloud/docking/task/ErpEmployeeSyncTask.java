package com.mall4j.cloud.docking.task;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.docking.skq_erp.dto.GetEmployeeInfoDto;
import com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.api.platform.constant.StaffRoleTypeEnum;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.docking.skq_erp.service.IStdEmployeeService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ErpEmployeeSyncTask {

	private static final Logger log = LoggerFactory.getLogger(ErpEmployeeSyncTask.class);

	@Autowired
	IStdEmployeeService stdEmployeeService;
	@Autowired
	private OnsMQTemplate staffSyncTemplate;

	@XxlJob("erpEmployeeSyncTask")
	public void erpEmployeeSyncTask() {
		log.info("开始执行中台员工同步任务》》》》》》》》》》》》》》》》》》》》》");
		GetEmployeeInfoDto getEmployeeInfoDto = new GetEmployeeInfoDto();
		getEmployeeInfoDto.setPage(1);
		getEmployeeInfoDto.setPageSize(1000);
		ServerResponseEntity<StdPageResult<EmployeeInfoVo>> shopInfo = stdEmployeeService.getEmployeeInfo(getEmployeeInfoDto);
		if (shopInfo != null && shopInfo.isSuccess()) {
			StdPageResult<EmployeeInfoVo> data = shopInfo.getData();
			this.save(data.getResult());
			Integer totalPage = data.getTotalPage();
			for (Integer i = 2; i <= totalPage; i++) {
				getEmployeeInfoDto.setPage(i);
				shopInfo = stdEmployeeService.getEmployeeInfo(getEmployeeInfoDto);
				if (shopInfo != null && shopInfo.isSuccess()) {
					this.save(shopInfo.getData().getResult());
				}
			}
		}
		log.info("结束执行中台员工同步任务》》》》》》》》》》》》》》》》》》》》》");
	}

	private void save(List<EmployeeInfoVo> result) {
		if (!CollectionUtils.isEmpty(result)) {
			result.stream().filter(emp -> StrUtil.isNotBlank(emp.getMobile())).forEach(r -> {
				StaffSyncDTO staffChangeSyncDTO = new StaffSyncDTO();
				staffChangeSyncDTO.setStoreCode(r.getShopCode());
				staffChangeSyncDTO.setIsLeave(r.getIsLeave());
				staffChangeSyncDTO.setName(r.geteName());
				staffChangeSyncDTO.setMobile(r.getMobile());
				staffChangeSyncDTO.setStaffCode(r.geteCode());
				staffChangeSyncDTO.setStaffNo(r.geteNumNo());
				staffChangeSyncDTO.setRoleType(StaffRoleTypeEnum.getValue(r.getAppPosition()));
				staffSyncTemplate.syncSend(staffChangeSyncDTO);
			});
		}
	}
}
