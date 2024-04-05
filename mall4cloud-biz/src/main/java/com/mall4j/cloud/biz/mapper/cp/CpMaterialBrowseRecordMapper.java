package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.api.biz.vo.MaterialBrowseRecordApiVO;
import com.mall4j.cloud.biz.dto.cp.MaterialBrowsePageDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialBrowseRecord;
import com.mall4j.cloud.biz.vo.cp.CpMaterialBrowseRecordVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材 会员浏览记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
public interface CpMaterialBrowseRecordMapper {

	/**
	 * 获取素材 会员浏览记录列表
	 * @return 素材 会员浏览记录列表
	 */
	List<CpMaterialBrowseRecordVO> list(@Param("materialBrowsePageDTO") MaterialBrowsePageDTO materialBrowsePageDTO);

	/**
	 * 根据素材 会员浏览记录id获取素材 会员浏览记录
	 *
	 * @param id 素材 会员浏览记录id
	 * @return 素材 会员浏览记录
	 */
	CpMaterialBrowseRecord getById(@Param("id") Long id);

	CpMaterialBrowseRecord getByBrowseId(@Param("browseId") String browseId);

	CpMaterialBrowseRecord getByUnionIdAndMatId(@Param("matId") Long matId,@Param("unionId") String browseId);

	/**
	 * 保存素材 会员浏览记录
	 * @param cpMaterialBrowseRecord 素材 会员浏览记录
	 */
	void save(@Param("cpMaterialBrowseRecord") CpMaterialBrowseRecord cpMaterialBrowseRecord);

	/**
	 * 更新素材 会员浏览记录
	 * @param cpMaterialBrowseRecord 素材 会员浏览记录
	 */
	void update(@Param("cpMaterialBrowseRecord") CpMaterialBrowseRecord cpMaterialBrowseRecord);

	/**
	 * 根据素材 会员浏览记录id删除素材 会员浏览记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	Integer totalBrowseCount(@Param("matId") Long matId,@Param("startTime") String startTime,@Param("endTime") String endTime);

	Integer totalVisitorCount(@Param("matId") Long matId,@Param("startTime") String startTime,@Param("endTime") String endTime);

	List<MaterialBrowseRecordByDayVO> browseCountByDay(@Param("matId") Long matId, @Param("startTime") String startTime, @Param("endTime") String endTime);

	List<MaterialBrowseRecordByDayVO> visitorCountByDay(@Param("matId") Long matId, @Param("startTime") String startTime, @Param("endTime") String endTime);


	void updateBrowseDuration(@Param("id")Integer id,@Param("browseDuration") Integer browseDuration);

	List<CpMaterialBrowseRecord> getUnSetTagRecord();

	void finish(@Param("id")Integer id,@Param("labalName")String labalName);

    List<MaterialBrowseRecordApiVO> listByUnionId(@Param("unionId")String unionId,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
