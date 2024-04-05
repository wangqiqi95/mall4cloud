package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.biz.vo.SoldSessionFileVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.Date;
import java.util.List;

/**
 * 会话存档检索service
 */
public interface SessionSearchService {

	/**
	 * 分页获取会话存档列表
	 * @param pageDTO 分页参数
	 * @return 会话存档列表分页数据
	 */
	PageVO<SessionFileVO> page(PageDTO pageDTO, SessionFileDTO dto);

	/**
	 * 导出
	 * @param dto
	 * @return
	 */
	List<SoldSessionFileVO> soldSessionFileExcel(SessionFileDTO dto);

	PageVO<SessionFileVO> getSessionFile(PageDTO pageDTO, SessionFileDTO dto);

	StaffSessionVO getSessionCount(SessionFileDTO dto);

	/**
	 * 查询员工收发消息
	 * @param pageDTO
	 * @param dto
	 * @return
	 */
	PageVO<StaffSessionVO> getUserSession(PageDTO pageDTO, SessionFileDTO dto);

	List<StaffSessionVO> getUserSessionList( SessionFileDTO dto);

	/**
	 * 查询客户与员工会话总量
	 * @param dto
	 * @return
	 */
	StaffSessionVO getStaffCount(SessionFileDTO dto);

	/**
	 * 查询与客户聊天的员工及会话总量
	 * @param dto
	 * @return
	 */
	List<StaffSessionVO> getUserAndStaffCount(SessionFileDTO dto);

	List<SessionFileVO> getSessionFileByMsgId(List<String> msgId);

	SessionFile getSessionFile(String msgId);

	/**
	 * 获取员工与客户的最近联系时间
	 * @param dto
	 * @return
	 */
	Date getLastTime(SessionFileDTO dto);
	/**
	 * 获取群聊最近联系时间
	 * @param dto
	 * @return
	 */
	Date getRoomLastTime(SessionFileDTO dto);
}
