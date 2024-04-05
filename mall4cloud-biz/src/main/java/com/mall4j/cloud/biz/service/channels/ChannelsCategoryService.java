package com.mall4j.cloud.biz.service.channels;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.biz.dto.channels.EcCategoryAuditResult;
import com.mall4j.cloud.api.biz.dto.channels.EcCats;
import com.mall4j.cloud.api.biz.dto.channels.EcFile;
import com.mall4j.cloud.biz.dto.channels.ChannelsCategoryDTO;
import com.mall4j.cloud.biz.dto.channels.event.ProductCategoryAuditInfoDTO;
import com.mall4j.cloud.biz.dto.channels.query.ChannelsCategoryQueryDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsCategory;
import com.mall4j.cloud.biz.vo.channels.ChannelsCategoryDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 视频号4.0 类目
 *
 * @author FrozenWatermelon
 * @date 2023-02-15 16:01:16
 */
public interface ChannelsCategoryService extends IService<ChannelsCategory> {

	List<EcCats> allList();

	/**
	 * 分页获取视频号4.0 类目列表
	 * @param dto query
	 * @return 视频号4.0 类目列表分页数据
	 */
	PageVO<ChannelsCategory> page(ChannelsCategoryQueryDTO dto);

	/**
	 * 根据视频号4.0 类目id获取视频号4.0 类目
	 *
	 * @param id 视频号4.0 类目id
	 * @return 视频号4.0 类目
	 */
	ChannelsCategory getById(Long id);


	/**
	 * 根据视频号4.0 类目id删除视频号4.0 类目
	 * @param id 视频号4.0 类目id
	 */
	void deleteById(Long id);

	/**
	 * 保存视频号4.0 类目
	 *
	 * @param channelsCategoryDTO 视频号4.0 类目
	 * @param isApply
	 */
	ServerResponseEntity<Void> applyOrReApply(ChannelsCategoryDTO channelsCategoryDTO, Boolean isApply);

	ChannelsCategoryDetailVO detail(Long id);

	/**
	 * 撤回申请
	 * @param id id
	 */
	void cancel(Long id);

	void audit(ProductCategoryAuditInfoDTO productCategoryAuditInfoDTO);

	EcFile uploadQualification(MultipartFile file);

	EcCategoryAuditResult getAudit(Long id);
}
