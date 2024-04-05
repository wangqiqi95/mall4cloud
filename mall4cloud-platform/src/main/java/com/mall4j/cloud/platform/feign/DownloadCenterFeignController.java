package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.DownloadCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadCenterFeignController implements DownloadCenterFeignClient {

	@Autowired
	DownloadCenterService downloadCenterService;
	/**
	 * 方法描述：创建计算中的下载中心记录
	 *
	 * @param calcingDownloadRecordDTO
	 * @throws
	 * @return 下载中心记录主键id
	 * @date 2022-03-20 22:46:07
	 */
	@Override
	public ServerResponseEntity<Long> newCalcingTask(CalcingDownloadRecordDTO calcingDownloadRecordDTO) {
		Long id = downloadCenterService.newCalcingTask(calcingDownloadRecordDTO);
		return ServerResponseEntity.success(id);
	}

	/**
	 * 方法描述：完成下载
	 *
	 * @param finishDownLoadDTO
	 * @return com.mall4j.cloud.wx.response.ServerResponseEntity<java.lang.Void>
	 * @date 2022-03-20 22:54:56
	 */
	@Override
	public ServerResponseEntity<Void> finishDownLoad(FinishDownLoadDTO finishDownLoadDTO) {
		downloadCenterService.finishDownLoad(finishDownLoadDTO);
		return ServerResponseEntity.success();
	}
}
