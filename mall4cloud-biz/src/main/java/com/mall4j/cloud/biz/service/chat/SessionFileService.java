package com.mall4j.cloud.biz.service.chat;

import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.io.InputStream;
import java.util.List;

/**
 * 会话存档记录service
 */
public interface SessionFileService {

	/**
	 * 分页获取会话存档列表
	 * @param pageDTO 分页参数
	 * @param sessionFile 会话存档对象
	 * @return 会话存档列表分页数据
	 */
	PageVO<SessionFileVO> page(PageDTO pageDTO, SessionFileDTO sessionFile);

	/**
	 * 保存会话存档记录
	 * @param sessionFile
	 */
	void save(SessionFile sessionFile);

	/**
	 * 获取开启会话存档人员
	 * @return
	 */
	List<String> permitUser();

	/**
	 * 获取会话同意情况
	 */
	void singleAgree();

	InputStream getFile(String fileName) throws Exception;

	void delete(String msgId);

}
