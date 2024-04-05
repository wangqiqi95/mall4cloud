package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.WelcomeDTO;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.model.cp.CpWelcome;
import com.mall4j.cloud.biz.vo.cp.WelcomeDetailPlusVO;
import com.mall4j.cloud.biz.vo.cp.WelcomeDetailVO;
import com.mall4j.cloud.biz.vo.cp.WelcomeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;


/**
 * 欢迎语配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface WelcomeService extends IService<CpWelcome> {

	/**
	 * 分页获取欢迎语配置表列表
	 * @param pageDTO 分页参数
	 * @param  request 查询条件
	 * @return 欢迎语配置表列表分页数据
	 */
	PageVO<WelcomeVO> page(PageDTO pageDTO, WelcomeDTO request);

	/**
	 * 根据欢迎语配置表id获取欢迎语配置表
	 *
	 * @param id 欢迎语配置表id
	 * @return 欢迎语配置表
	 */
	CpWelcome getById(Long id);

	/**
	 * 保存欢迎语配置表
	 * @param welcome 欢迎语配置表
	 */
	boolean saveWelcome(CpWelcome welcome);

	/**
	 * 更新欢迎语配置表
	 * @param welcome 欢迎语配置表
	 */
	void updateWelcome(CpWelcome welcome);

	/**
	 * 根据欢迎语配置表id删除欢迎语配置表
	 * @param id 欢迎语配置表id
	 */
	void deleteById(Long id);

	/**
	 * 创建欢迎语
 	 * @param welcome 欢迎语
	 * @param attachMent 附件
	 * @param shops 商店
	 */
    void createWelcome(CpWelcome welcome, List<AttachmentExtDTO> attachMent, List<Long> shops,WelcomeDTO welcomeDTO);

	/**
	 *  根据店 按优先级越高，若数值相同，则创建时间越晚越高，返回一条数据
	 * @param storeId 店
	 * @return 优先级最高的欢迎语
	 */
    CpWelcome getWelcomeByWeight(Long storeId);

	/**
	 * 根据所属门店，注册场景，欢迎语来源查询个人欢迎语 只返回第一条
	 * @param orgs
	 * @param sence
	 * @param source_from
	 * @return
	 */
	CpWelcome getWelcomeByOrgs(List<Long> orgs,Integer sence,Integer source_from);


	WelcomeDetailPlusVO getWelcomeDetailByWeight(Long storeId);

	WelcomeDetailVO getWelcomeDetailSimpleByWeight(Long storeId);

	/**
	 * 保存渠道活码附件信息
	 * @param sourceId
	 * @param sourceFrom
	 * @param attachMents
	 */
	void saveStaffCodeWelcome(Long sourceId,List<ChannelCodeWelcomeDTO> attachMents);

    void enable(Long id);

	void disable(Long id);
}
