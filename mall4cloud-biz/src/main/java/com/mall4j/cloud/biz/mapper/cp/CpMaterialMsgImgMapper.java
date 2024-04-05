package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.CpMaterialMsgImgSelectDTO;
import com.mall4j.cloud.biz.model.cp.CpMaterialMsgImg;
import com.mall4j.cloud.biz.vo.cp.CpMaterialMsgImgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-12-06 16:14:28
 */
public interface CpMaterialMsgImgMapper extends BaseMapper<CpMaterialMsgImg> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<CpMaterialMsgImgVO> mobileList(@Param("dto") CpMaterialMsgImgSelectDTO dto);

	void deleteByMatId(@Param("matId") Long matId);

	void deleteByMatMsgId(@Param("matMsgId") Long matMsgId);

}
