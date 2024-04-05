package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;

import java.util.List;


/**
 * 欢迎语附件列表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface WelcomeAttachmentService extends IService<CpWelcomeAttachment> {

	/**
	 * 保存欢迎语附件列表
	 * @param welcomeAttachment 欢迎语附件列表
	 */
	boolean saveWelcomeAttachment(CpWelcomeAttachment welcomeAttachment);

	/**
	 * 根据welId删除
	 * @param welId 欢迎语配置id
	 */
    void deleteByWelId( Long welId,int origin);

	/**
	 * 根据欢迎语id查询附件
	 * @param welId 欢迎语id
	 * @return WelcomeAttachment
	 */
    CpWelcomeAttachment selectOneByWelId(Long welId, int origin);

	/**
	 * 根据欢迎语id查询附件
	 * @param welId 欢迎语id
	 * @return WelcomeAttachment
	 */
	List<CpWelcomeAttachment> listByWelId(Long welId, int origin);

	CpWelcomeAttachment getAttachmentByWelId(Long welId, int origin);


	/**
	 * 查询3天未更新的小程序图片
	 * @return List<WelcomeAttachment>
	 */
	List<CpWelcomeAttachment> listAfterThreeDayPicMediaIds();

	/**
	 * 更新
	 * @param welcomeAttachment 附件
	 */
    void updateWelcomeAttachment(CpWelcomeAttachment welcomeAttachment);
}
