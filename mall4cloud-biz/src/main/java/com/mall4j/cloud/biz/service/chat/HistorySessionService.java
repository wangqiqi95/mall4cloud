package com.mall4j.cloud.biz.service.chat;

import org.springframework.web.multipart.MultipartFile;


/**
 * 会话存档历史数据处理
 *
 */
public interface HistorySessionService {

	void importExcel(MultipartFile file);
}
