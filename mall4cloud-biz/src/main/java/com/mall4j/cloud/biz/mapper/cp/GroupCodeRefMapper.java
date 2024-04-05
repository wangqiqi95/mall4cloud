package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.analyze.AnalyzeGroupCodeDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeRef;
import com.mall4j.cloud.biz.vo.cp.GroupCodeRefVO;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群活码关联群表
 *
 * @author hwy
 * @date 2022-02-16 15:17:20
 */
public interface GroupCodeRefMapper extends BaseMapper<CpGroupCodeRef> {

	/**
	 * 获取群活码关联群表列表
	 * @return 群活码关联群表列表
	 */
	List<CpGroupCodeRef> list();

	List<AnalyzeGroupCodeVO> selectListBy(@Param("dto") AnalyzeGroupCodeDTO dto);

	List<CpGroupCodeRef> selectListByCodeIds(@Param("codeIds")List<Long> codeIds,@Param("sourceFrom")Integer sourceFrom);

//	List<CpGroupCodeRef> selectListByCodeIds(@Param("codeIds")List<Long> codeIds,@Param("sourceFrom")Integer sourceFrom);

	/**
	 * 根据群活码关联群表id获取群活码关联群表
	 *
	 * @param id 群活码关联群表id
	 * @return 群活码关联群表
	 */
	CpGroupCodeRef getById(@Param("id") Long id);

	/**
	 * 根据群活码关联群表id删除群活码关联群表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
	/**
	 * 统计群情况
	 * @param codeId 群活马id
	 * @return GroupCodeRefVO
	 */
    GroupCodeRefVO statCount(@Param("codeId") Long codeId);

	/**
	 * 根据群活码关联群表id删除群活码关联群表
	 * @param codeId qun id
	 */
	void deleteByCodeId(@Param("codeId") Long codeId);

}
