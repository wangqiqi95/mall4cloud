package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.dto.questionnaire.*;
import com.mall4j.cloud.group.model.Questionnaire;
import com.mall4j.cloud.group.vo.questionnaire.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 问卷信息表
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireService {

	/**
	 * 分页获取问卷信息表列表
	 * @param pageDTO 分页参数
	 * @return 问卷信息表列表分页数据
	 */
	PageVO<QuestionnaireDetailVO> page(PageDTO pageDTO, QuestionnairePageDTO param);

	PageVO<QuestionnaireUserAnswerRecordPageVO> answerPage(PageDTO pageDTO, AnswerPageDTO param);

	/**
	 * 根据问卷信息表id获取问卷信息表
	 *
	 * @param id 问卷信息表id
	 * @return 问卷信息表
	 */
	Questionnaire getById(Long id);

	/**
	 * 根据问卷信息表id获取问卷信息表
	 *
	 * @param id 问卷信息表id
	 * @return 问卷信息表
	 */
	QuestionnaireDetailVO getDetailById(Long id);

	/**
	 * 小程序端查询问卷详情
	 *
	 */
	QuestionnaireDetailVO appInfo(Long id,Long storeId,Long userId);

	/**
	 * 保存问卷信息表
	 *
	 * @return bo
	 */
	Questionnaire save(QuestionnaireDTO questionnaireDTO);

	/**
	 * 更新问卷信息表
	 */
	void update(QuestionnaireDTO questionnaireDTO);

	void enable(Long id);

	void disable(Long id);

	/**
	 * 根据问卷信息表id删除问卷信息表
	 * @param id 问卷信息表id
	 */
	void deleteById(Long id);

	void submit(SubmitDTO submitDTO);

	void receivePrize(ReceivePrizeDTO receivePrizeDTO);

	void fillShippingAddress(ShippingAddressDTO shippingAddressDTO);

	/**
	 * 用户未提交记录统计分页查询
	 *
	 * @param pageDTO    分页参数
	 * @param activityId 活动Id
	 * @return page
	 */
	PageVO<QuestionnaireUserUnSubmitVO> pageCountUserUnSubmit(PageDTO pageDTO, Long activityId);

	/**
	 * 检查用户是否有问卷资格
	 * @param id 问卷ID
	 * @param storeId 门店ID
	 * @param userId 用户ID
	 * @return boolean 如果用户有资格则返回 TRUE, 否则返回异常状态
	 */
	Boolean checkUserAuth(Long id, Long storeId, Long userId);

	/**
	 * 用户实物奖品信息地址
	 * @param activityId 活动Id
	 * @param userId 用户ID
	 * @return vo
	 */
	QuestionnaireUserGiftAddrVO userGiftAddrInfo(Long activityId, Long userId);

	/**
	 * 导出用户名单
	 * @param redisKey 缓存Key
	 * @param activityId 活动Id
	 * @param response response
	 */
	void exportUser(String redisKey, Long activityId, HttpServletResponse response);

	/**
	 * 实物奖品发货
	 * @param dto dto
	 */
	void deliverGift(QuestionnaireUserGiftAddrDTO dto);

	/**
	 * 简易活动信息
	 * @param id 活动ID
	 * @return vo
	 */
	QuestionnaireDetailVO simpleInfo(Long id);

	/**
     * 更新redis缓存数据
     *
     * @param questionnaireUpdateExcelDTO dto
     * @return vo
     */
    QuestionnaireResolveExcelVO updateCacheUser(QuestionnaireUpdateExcelDTO questionnaireUpdateExcelDTO);

	/**
	 * 分页查询缓存的用户数据
	 *
	 * @param redisKey   redis key name
	 * @param pageDTO    page
	 * @param phone      phone
	 * @param activityId 活动ID
	 * @return page vo
	 */
	PageVO<QuestionnaireUserExcelVO> pageCacheUser(String redisKey, PageDTO pageDTO, String phone, Long activityId);

	/**
	 * 埋点，用于增加问卷浏览次数
	 * @param userId 用户ID
	 * @param activityId 活动ID
	 */
	void point(Long userId, Long activityId);
}
