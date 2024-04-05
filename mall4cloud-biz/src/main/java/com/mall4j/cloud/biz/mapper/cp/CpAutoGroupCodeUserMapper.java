package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.analyze.CpAutoGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeUser;
import com.mall4j.cloud.biz.vo.cp.analyze.CpAutoGroupCodeSendUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自动拉群员工好友关系表
 *
 * @author gmq
 * @date 2023-11-08 10:20:46
 */
public interface CpAutoGroupCodeUserMapper extends BaseMapper<CpAutoGroupCodeUser> {

	/**
	 * 获取自动拉群员工好友关系表列表
	 * @return 自动拉群员工好友关系表列表
	 */
	List<CpAutoGroupCodeUser> list();

	/**
	 * 自动拉群-发送好友列表
	 * @param dto
	 * @return
	 */
	List<CpAutoGroupCodeSendUserVO> listAutoCodeRelUser(@Param("dto") CpAutoGroupCodeAnalyzeDTO dto);

	/**
	 * 根据自动拉群员工好友关系表id获取自动拉群员工好友关系表
	 *
	 * @param id 自动拉群员工好友关系表id
	 * @return 自动拉群员工好友关系表
	 */
	CpAutoGroupCodeUser getById(@Param("id") Long id);

	CpAutoGroupCodeUser getByUserId(@Param("qiWeiUserId") String id,@Param("codeId") Long codeId);
}
