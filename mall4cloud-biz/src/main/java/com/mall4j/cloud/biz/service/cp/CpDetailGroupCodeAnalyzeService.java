package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeStaffSelectDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpAutoGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeStaffDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeStaff;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeStaffVO;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpAutoGroupCodeSendUserVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeStaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 群活码数据分析
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
public interface CpDetailGroupCodeAnalyzeService {

	/**
	 * 自动拉群-发送好友列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<CpAutoGroupCodeSendUserVO> pageAutoCodeRelUser(PageDTO pageDTO, CpAutoGroupCodeAnalyzeDTO dto);

	/**
	 * 自动拉群-关联群聊列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<AnalyzeGroupCodeVO> pageAutoCodeRelGroup(PageDTO pageDTO, CpAutoGroupCodeAnalyzeDTO dto);

	/**
	 * 标签建群-发送好友列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<CpTagGroupCodeSendUserVO> pageTagCodeRelUser(PageDTO pageDTO, CpTagGroupCodeAnalyzeDTO dto);

	/**
	 * 标签建群-关联群聊列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<AnalyzeGroupCodeVO> pageTagGroupRelGroup(PageDTO pageDTO, CpTagGroupCodeAnalyzeDTO dto);

	/**
	 * 标签建群-关联员工
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<CpTagGroupCodeStaffVO> pageTagGroupRelStaff(PageDTO pageDTO, CpTagGroupCodeAnalyzeStaffDTO dto);


}
