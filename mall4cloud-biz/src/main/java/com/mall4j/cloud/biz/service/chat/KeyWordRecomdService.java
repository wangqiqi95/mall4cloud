package com.mall4j.cloud.biz.service.chat;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.chat.KeyWordRecomd;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 会话关键词推荐回复内容
 *
 * @author gmq
 * @date 2024-01-05 15:19:52
 */
public interface KeyWordRecomdService extends IService<KeyWordRecomd> {

	void deleteByKeyWordId(Long keyWordId);
}
