package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 欢迎语附件列表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface WelcomeAttachmentMapper extends BaseMapper<CpWelcomeAttachment> {

	/**
	 * 根据欢迎语id删除
	 * @param welId 欢迎语id
	 */
    void deleteByWelId(@Param("welId") Long welId,@Param("origin")int origin);

    void deleteBySourceId(@Param("sourceId") Long sourceId,@Param("origin")int origin);

	/**
	 * 根据欢迎语查询附件
	 * @param welId 欢迎语id
	 * @return List<WelcomeAttachment>
	 */
    List<CpWelcomeAttachment> listByWelId(@Param("welId")Long welId, @Param("origin")int origin);

	CpWelcomeAttachment selectAttachmentByWelId(@Param("welId")Long welId, @Param("origin")int origin);
	/**
	 * 查询3天未更新的小程序图片
	 * @return List<WelcomeAttachment>
	 */
    List<CpWelcomeAttachment> listAfterThreeDayPicMediaIds();

}
