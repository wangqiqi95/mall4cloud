package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.biz.dto.chat.SessionTimeOutDTO;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.model.chat.SessionTimeOut;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 会话超时规则service
 */
public interface SessionTimeOutService {

	/**
	 * 分页获取超时规则列表
	 * @param pageDTO 分页参数
	 * @param timeOutDTO 会话超时
	 * @return 会话存档列表分页数据
	 */
	PageVO<SessionTimeOutVO> page(PageDTO pageDTO, SessionTimeOutDTO timeOutDTO);

	/**
	 * 新增超时规则
	 * @param timeOut
	 */
	void save(SessionTimeOutDTO timeOut);

	/**
	 * 修改超时规则
	 * @param timeOut
	 */
	void update(SessionTimeOutDTO timeOut);

	/**
	 * 删除超时规则
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 根据id查询超时规则
	 * @param id
	 * @return
	 */
	SessionTimeOutVO getById(Long id);

}
