package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.WelcomeDTO;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.model.cp.CpWelcome;
import com.mall4j.cloud.biz.vo.cp.WelcomeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 欢迎语配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface WelcomeMapper extends BaseMapper<CpWelcome> {

	/**
	 * 获取欢迎语配置表列表
	 * @param request 查询条件
	 * @return 欢迎语配置表列表
	 */
	List<WelcomeVO> list(@Param("welcome") WelcomeDTO request);

	/**
	 * 根据欢迎语配置表id获取欢迎语配置表
	 *
	 * @param id 欢迎语配置表id
	 * @return 欢迎语配置表
	 */
	CpWelcome getById(@Param("id") Long id);

	/**
	 * 根据欢迎语配置表id删除欢迎语配置表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteBySourceId(@Param("sourceId") Long sourceId,@Param("sourceFroms") List<Integer> sourceFroms);
	/**
	 *  根据店 按优先级越高，若数值相同，则创建时间越晚越高，返回一条数据
	 * @param storeId 店
	 * @return 优先级最高的欢迎语
	 */
    CpWelcome getWelcomeByWeight(@Param("storeId") Long storeId);

    List<ChannelCodeWelcomeDTO> selectWelcomes(@Param("sourceId") Long sourceId);

    CpWelcome checkonly(@Param("scene")String scene, @Param("shopIds") List<Long> shopIds);

    void logicDelete(@Param("id")Long id);

    Long getWelcomeIdByOrgs(@Param("orgs")List<Long> orgs, @Param("sence")Integer sence, @Param("source_from")Integer source_from);

	CpWelcome getWelcomeByOrgs(@Param("orgs")List<Long> orgs, @Param("sence")Integer sence, @Param("source_from")Integer source_from);
}
