package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationVO;
import com.mall4j.cloud.user.dto.analyze.AnalyzeUserStaffCpRelationDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
public interface AnalyzeUserStaffCpRelationMapper {

	List<AnalyzeUserStaffCpRelationListVO> selectAnalyzeUSRFPage(@Param("dto") AnalyzeUserStaffCpRelationDTO dto);

	List<AnalyzeUserStaffCpRelationVO> selectAnalyzeNewUSRFList(@Param("dto") AnalyzeUserStaffCpRelationDTO dto);

	int countAnalyzeNewUSRF(@Param("dto") AnalyzeUserStaffCpRelationDTO dto);

}
