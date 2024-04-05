package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.DownloadCenterQueryDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.DownloadCenter;

public interface DownloadCenterService {

	/**
	 * 方法描述：创建计算中的下载中心记录
	 * @param calcingDownloadRecordDTO
	 * @return	下载中心记录主键id
	 * @throws
	 * @date 2022-03-20 22:46:07
	 */
	Long newCalcingTask(CalcingDownloadRecordDTO calcingDownloadRecordDTO);

	/**
	 * 方法描述：完成下载
	 * @param finishDownLoadDTO
	 * @return com.mall4j.cloud.wx.response.ServerResponseEntity<java.lang.Void>
	 * @date 2022-03-20 22:54:56
	 */
	void finishDownLoad(FinishDownLoadDTO finishDownLoadDTO);

	PageVO<DownloadCenter> page(PageDTO pageDTO, DownloadCenterQueryDTO downloadCenterQueryDTO);

	void deleteById(Long id);
}
