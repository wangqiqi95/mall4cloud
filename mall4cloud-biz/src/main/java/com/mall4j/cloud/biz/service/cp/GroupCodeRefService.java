package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpGroupCodeListDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.AnalyzeGroupCodeDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCode;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeRef;
import com.mall4j.cloud.biz.vo.cp.CpGroupCodeListVO;
import com.mall4j.cloud.biz.vo.cp.GroupCodeRefVO;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 群活码关联群表
 *
 * @author hwy
 * @date 2022-02-16 15:17:20
 */
public interface GroupCodeRefService extends IService<CpGroupCodeRef> {

	List<CpGroupCodeRef> getListByCodeId(List<Long> codeId,Integer sourceFrom);

	/**
	 * 获取活码关联群聊
	 * @param dto
	 * @return
	 */
	List<AnalyzeGroupCodeVO> selectListBy(AnalyzeGroupCodeDTO dto);

	/**
	 * 分页获取群活码关联群表列表
	 * @param pageDTO 分页参数
	 * @return 群活码关联群表列表分页数据
	 */
	PageVO<CpGroupCodeRef> page(PageDTO pageDTO);

	/**
	 * 根据群活码关联群表id获取群活码关联群表
	 *
	 * @param id 群活码关联群表id
	 * @return 群活码关联群表
	 */
	CpGroupCodeRef getById(Long id);

	/**
	 * 保存群活码关联群表
	 * @param groupCodeRef 群活码关联群表
	 */
	boolean save(CpGroupCodeRef groupCodeRef);

	/**
	 * 更新群活码关联群表
	 * @param groupCodeRef 群活码关联群表
	 */
	void update(CpGroupCodeRef groupCodeRef);

	/**
	 * 根据群活码关联群表id删除群活码关联群表
	 * @param id 群活码关联群表id
	 */
	void deleteById(Long id);

	/**
	 * 统计群情况
	 * @param id 群活马id
	 * @return GroupCodeRefVO
	 */
	GroupCodeRefVO statCount(Long id);

	/**
	 * 根据群活码关联群表id删除群活码关联群表
	 * @param groupId 群活码关联群表id
	 */
	void deleteByCodeId(String groupId);

	/**
	 * 保存群活码设置群聊表
	 * @param cpGroupCodeList 群活码设置群聊表
	 */
	void saveTo(Long groupId, Integer sourceFrom,List<CpGroupCodeListDTO> cpGroupCodeList);

	/**
	 * 更新群活码设置群聊表
	 * @param cpGroupCodeList 群活码设置群聊表
	 */
	void updateTo(CpGroupCode groupCode, Integer sourceFrom, List<CpGroupCodeListDTO> cpGroupCodeList);

	/**
	 * 设置群聊启用禁用
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id,Integer status);

	/**
	 * 群活码详情-群聊信息统计
	 * @param pageDTO
	 * @param codeId
	 * @param groupName
	 * @return
	 */
	PageVO<AnalyzeGroupCodeVO> pageAnalyzes(PageDTO pageDTO,Long codeId,String groupName);

	int getGroupCodeRelStatus(Integer scanCount, Integer total, int status, Date expireEnd,int chatUserTotal);

}
