package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.vo.MaterialBrowseRecordApiVO;
import com.mall4j.cloud.biz.dto.cp.MaterialBrowsePageDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialBrowseRecord;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseStatisticsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 素材 会员浏览记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
public interface CpMaterialBrowseRecordService {

	/**
	 * 分页获取素材 会员浏览记录列表
	 * @param pageDTO 分页参数
	 * @return 素材 会员浏览记录列表分页数据
	 */
	PageVO<CpMaterialBrowseRecord> page(PageDTO pageDTO, MaterialBrowsePageDTO request);

	/**
	 * 通过unionId查询会员的浏览记录
	 * @param unionId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<MaterialBrowseRecordApiVO> listByUnionId(String unionId, String startTime, String endTime);

	/**
	 * 根据素材 会员浏览记录id获取素材 会员浏览记录
	 *
	 * @param id 素材 会员浏览记录id
	 * @return 素材 会员浏览记录
	 */
	CpMaterialBrowseRecord getById(Long id);

	/**
	 * 保存素材 会员浏览记录
	 * @param cpMaterialBrowseRecord 素材 会员浏览记录
	 */
	void save(CpMaterialBrowseRecord cpMaterialBrowseRecord);

	/**
	 * 更新素材 会员浏览记录
	 * @param cpMaterialBrowseRecord 素材 会员浏览记录
	 */
	void update(CpMaterialBrowseRecord cpMaterialBrowseRecord);

	/**
	 * 根据素材 会员浏览记录id删除素材 会员浏览记录
	 * @param id 素材 会员浏览记录id
	 */
	void deleteById(Long id);

	MaterialBrowseStatisticsVO materialBrowseStatistics(MaterialBrowsePageDTO request);
}
