package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mall4cloud-platform",contextId = "download-center")
public interface DownloadCenterFeignClient {

	/**
	 * 方法描述：创建计算中的下载中心记录
	 * @param calcingDownloadRecordDTO
	 * @return	下载中心记录主键id
	 * @throws
	 * @date 2022-03-20 22:46:07
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/downloadCenter/newCalcingTask")
	ServerResponseEntity<Long> newCalcingTask(@RequestBody CalcingDownloadRecordDTO calcingDownloadRecordDTO);

	/**
	 * 方法描述：完成下载
	 * @param finishDownLoadDTO
	 * @return com.mall4j.cloud.wx.response.ServerResponseEntity<java.lang.Void>
	 * @date 2022-03-20 22:54:56
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/downloadCenter/finishDownLoad")
	ServerResponseEntity<Void> finishDownLoad(@RequestBody FinishDownLoadDTO finishDownLoadDTO);
}
