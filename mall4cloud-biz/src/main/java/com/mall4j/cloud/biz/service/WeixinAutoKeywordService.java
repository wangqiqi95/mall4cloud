package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinAutoKeywordPutDTO;
import com.mall4j.cloud.biz.dto.WeixinAutoMsgPutDTO;
import com.mall4j.cloud.biz.vo.WeixinAutoKeywordVO;
import com.mall4j.cloud.biz.vo.WeixinAutoMsgVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoKeyword;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信关键字表
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
public interface WeixinAutoKeywordService {

	/**
	 * 分页获取微信关键字表列表
	 * @param pageDTO 分页参数
	 * @return 微信关键字表列表分页数据
	 */
	PageVO<WeixinAutoKeywordVO> page(PageDTO pageDTO,
								   String appId,
								   String name,
								   String keyword,
								   Integer isWork);

	/**
	 * 根据微信关键字表id获取微信关键字表
	 *
	 * @param id 微信关键字表id
	 * @return 微信关键字表
	 */
	WeixinAutoKeyword getById(String id);

	/**
	 * 保存微信关键字表
	 * @param weixinAutoKeyword 微信关键字表
	 */
	void save(WeixinAutoKeyword weixinAutoKeyword);

	/**
	 * 更新微信关键字表
	 * @param weixinAutoKeyword 微信关键字表
	 */
	void update(WeixinAutoKeyword weixinAutoKeyword);

	/**
	 * 根据微信关键字表id删除微信关键字表
	 * @param id 微信关键字表id
	 */
	void deleteById(String id);

	ServerResponseEntity<WeixinAutoKeywordVO> getWeixinAutoKeywordVO(String id);

	ServerResponseEntity<Void> saveWeixinAutoKeyword(WeixinAutoKeywordPutDTO autoKeywordPutDTO);

	WeixinAutoKeywordVO getByKeyword(String appId,String keyword);
}
