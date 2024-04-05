package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.QrcodeTicket;
import com.mall4j.cloud.biz.vo.QrcodeTicketVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 二维码数据信息
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
public interface QrcodeTicketMapper {

	/**
	 * 获取二维码数据信息列表
	 * @return 二维码数据信息列表
	 */
	List<QrcodeTicket> list();

	/**
	 * 根据二维码数据信息id获取二维码数据信息
	 *
	 * @param ticketId 二维码数据信息id
	 * @return 二维码数据信息
	 */
	QrcodeTicket getByTicketId(@Param("ticketId") Long ticketId);

	/**
	 * 保存二维码数据信息
	 * @param qrcodeTicket 二维码数据信息
	 */
	void save(@Param("qrcodeTicket") QrcodeTicket qrcodeTicket);

	/**
	 * 更新二维码数据信息
	 * @param qrcodeTicket 二维码数据信息
	 */
	void update(@Param("qrcodeTicket") QrcodeTicket qrcodeTicket);

	/**
	 * 根据二维码数据信息id删除二维码数据信息
	 * @param ticketId
	 */
	void deleteById(@Param("ticketId") Long ticketId);

	/**
	 * 根据二维码获取二维码信息
	 * @param ticket 二维码
	 * @return 二维码信息
	 */
	QrcodeTicketVO getByTicket(@Param("ticket") String ticket);
}
