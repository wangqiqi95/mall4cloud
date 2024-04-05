package com.mall4j.cloud.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.DownloadCenterQueryDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.platform.mapper.DownloadCenterMapper;
import com.mall4j.cloud.platform.model.DownloadCenter;
import com.mall4j.cloud.platform.service.DownloadCenterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;

@Service("downloadCenterService")
public class DownloadCenterServiceImpl implements DownloadCenterService {

	private static final Logger logger = LoggerFactory.getLogger(DownloadCenterServiceImpl.class);

	@Autowired
	private DownloadCenterMapper downloadCenterMapper;

	/**
	 * 方法描述：创建计算中的下载中心记录
	 *
	 * @param calcingDownloadRecordDTO
	 * @throws
	 * @return 下载中心记录主键id
	 * @date 2022-03-20 22:46:07
	 */
	@Override
	public Long newCalcingTask(CalcingDownloadRecordDTO calcingDownloadRecordDTO) {
		long start = System.currentTimeMillis();
		DownloadCenter downloadCenter = new DownloadCenter();
		try {
			verify(calcingDownloadRecordDTO);
			downloadCenter.setCalCount(calcingDownloadRecordDTO.getCalCount());
			downloadCenter.setDownloadTime(calcingDownloadRecordDTO.getDownloadTime());
			downloadCenter.setFileName(calcingDownloadRecordDTO.getFileName());
			downloadCenter.setOperatorName(calcingDownloadRecordDTO.getOperatorName());
			downloadCenter.setOperatorNo(calcingDownloadRecordDTO.getOperatorNo());
			downloadCenterMapper.insert(downloadCenter);
			return downloadCenter.getId();
		} catch (LuckException e) {
			logger.error("", e);
			throw e;
		} finally {
			logger.info("下载中心-创建下载记录结束，请求参数为：{}，返回结果为：{}，共耗时：{}", calcingDownloadRecordDTO, downloadCenter.getId(), System.currentTimeMillis() - start);
		}
	}

	private void verify(CalcingDownloadRecordDTO calcingDownloadRecordDTO) {
		if (calcingDownloadRecordDTO.getDownloadTime() == null) {
			throw new LuckException("下载日期不能为空");
		}

		if (calcingDownloadRecordDTO.getCalCount() == null) {
			throw new LuckException("计算量不能为空");
		}

		if (StringUtils.isBlank(calcingDownloadRecordDTO.getOperatorName())) {
			throw new LuckException("操作人不能为空");
		}

		if (StringUtils.isBlank(calcingDownloadRecordDTO.getOperatorNo())) {
			throw new LuckException("操作人工号不能为空");
		}
	}

	@Override
	public void deleteById(Long id) {
		DownloadCenter downloadCenter = downloadCenterMapper.selectById(id);
		if (downloadCenter == null) {
			throw new LuckException("要删除的记录不存在");
		}
		downloadCenter.setDelStatus(2);
		downloadCenterMapper.updateById(downloadCenter);
	}

	@Override
	public PageVO<DownloadCenter> page(PageDTO pageDTO, DownloadCenterQueryDTO downloadCenterQueryDTO) {
		PageVO<StaffVO> pageVO = new PageVO();
		pageVO.setTotal(0l);
		pageVO.setPages(0);
		pageVO.setList(Collections.emptyList());
		if (downloadCenterQueryDTO.getStartTime() != null) {
			try {
				downloadCenterQueryDTO.setStartTime(DateUtil.parseDate(DateUtils.dateToStrYmd(downloadCenterQueryDTO.getStartTime()) + " 00:00:00", DateUtil.YYYY_MM_DD_HH_MM_SS));
			} catch (ParseException e) {
				downloadCenterQueryDTO.setStartTime(null);
			}
		}if (downloadCenterQueryDTO.getEndTime() != null) {
			try {
				downloadCenterQueryDTO.setEndTime(DateUtil.parseDate(DateUtils.dateToStrYmd(downloadCenterQueryDTO.getEndTime()) + " 23:59:59", DateUtil.YYYY_MM_DD_HH_MM_SS));
			} catch (ParseException e) {
				downloadCenterQueryDTO.setEndTime(null);
			}
		}
//		logger.info("下载中心查询数据",JSONObject.toJSONString(downloadCenterQueryDTO));
		PageVO<DownloadCenter> downloadCenterPageVO = PageUtil.doPage(pageDTO, () -> downloadCenterMapper.queryByPrams(downloadCenterQueryDTO)
//				.selectList(new LambdaQueryWrapper<DownloadCenter>()
//				.eq(DownloadCenter::getDelStatus, 1)
//				.like(StringUtils.isNotBlank(downloadCenterQueryDTO.getFileName()), DownloadCenter::getFileName, downloadCenterQueryDTO.getFileName())
////						.or(StringUtils.isNotBlank(downloadCenterQueryDTO.getOperatorNoOrName()), wrapper -> wrapper.like(DownloadCenter::getOperatorName, downloadCenterQueryDTO.getOperatorNoOrName()))
////	.or(StringUtils.isNotBlank(downloadCenterQueryDTO.getOperatorNoOrName()), wrapper -> wrapper.like(DownloadCenter::getOperatorNo, downloadCenterQueryDTO.getOperatorNoOrName()))
//				.like(StringUtils.isNotBlank(downloadCenterQueryDTO.getOperatorNoOrName()), DownloadCenter::getOperatorName, downloadCenterQueryDTO.getOperatorNoOrName())
//				.like(StringUtils.isNotBlank(downloadCenterQueryDTO.getOperatorNoOrName()), DownloadCenter::getOperatorNo, downloadCenterQueryDTO.getOperatorNoOrName())
//				.ge(downloadCenterQueryDTO.getStartTime() != null, DownloadCenter::getDownloadTime, downloadCenterQueryDTO.getStartTime())
//				.le(downloadCenterQueryDTO.getEndTime() != null,DownloadCenter::getDownloadTime, downloadCenterQueryDTO.getEndTime())
//				.orderByDesc(DownloadCenter::getId)
		);
		return downloadCenterPageVO;
	}

	/**
	 * 方法描述：完成下载
	 *
	 * @param finishDownLoadDTO
	 * @return com.mall4j.cloud.wx.response.ServerResponseEntity<java.lang.Void>
	 * @date 2022-03-20 22:54:56
	 */
	@Override
	public void finishDownLoad(FinishDownLoadDTO finishDownLoadDTO) {
		long start = System.currentTimeMillis();
		boolean deal = false;
		try {
			verify(finishDownLoadDTO);
			DownloadCenter downloadCenter = downloadCenterMapper.selectById(finishDownLoadDTO.getId());
			if (downloadCenter == null) {
				throw new LuckException("下载记录不存在");
			}

			downloadCenter.setFileName(finishDownLoadDTO.getFileName());
			downloadCenter.setFileUrl(finishDownLoadDTO.getFileUrl());
			downloadCenter.setCalCount(finishDownLoadDTO.getCalCount());
			downloadCenter.setStatus(finishDownLoadDTO.getStatus());
			downloadCenter.setRemarks(finishDownLoadDTO.getRemarks());
			downloadCenterMapper.updateById(downloadCenter);
			deal = true;
		} catch (LuckException e) {
			deal = false;
			logger.error("", e);
			throw e;
		} finally {
			logger.info("下载中心-下载记录完成变更结束，请求参数为：{}，处理结果为：{}，共耗时：{}", finishDownLoadDTO, deal, System.currentTimeMillis() - start);
		}
	}

	private void verify(FinishDownLoadDTO finishDownLoadDTO) {
		if (finishDownLoadDTO.getId() == null) {
			throw new LuckException("id不能为空");
		}
//		if (StringUtils.isBlank(finishDownLoadDTO.getFileName())) {
//			throw new LuckException("文件名称不能为空");
//		}
//		if (StringUtils.isBlank(finishDownLoadDTO.getFileUrl())) {
//			throw new LuckException("文件下载地址不能为空");
//		}

		if (finishDownLoadDTO.getStatus() == null) {
			throw new LuckException("状态不能为空");
		}

		Optional<DownloadCenter.DownLoadStatus> status = DownloadCenter.DownLoadStatus.getStatus(finishDownLoadDTO.getStatus());
		if (!status.isPresent()) {
			throw new LuckException("状态值不正确");
		}
	}

}
