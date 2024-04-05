package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.QrcodeTicketVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.QrcodeTicket;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * 二维码数据信息
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
public interface QrcodeTicketService {

	/**
	 * 分页获取二维码数据信息列表
	 * @param pageDTO 分页参数
	 * @return 二维码数据信息列表分页数据
	 */
	PageVO<QrcodeTicket> page(PageDTO pageDTO);

	/**
	 * 根据二维码数据信息id获取二维码数据信息
	 *
	 * @param ticketId 二维码数据信息id
	 * @return 二维码数据信息
	 */
	QrcodeTicket getByTicketId(Long ticketId);

	/**
	 * 保存二维码数据信息
	 * @param qrcodeTicket 二维码数据信息
	 */
	void save(QrcodeTicket qrcodeTicket);

	/**
	 * 更新二维码数据信息
	 * @param qrcodeTicket 二维码数据信息
	 */
	void update(QrcodeTicket qrcodeTicket);

	/**
	 * 根据二维码数据信息id删除二维码数据信息
	 * @param ticketId 二维码数据信息id
	 */
	void deleteById(Long ticketId);

	/**
	 * 根据二维码获取二维码信息
	 * @param ticket 二维码
	 * @return 二维码信息
	 */
    QrcodeTicketVO getByTicket(String ticket);

	/**
	 * 小程序短链生成
	 * @param path
	 * @param isExpire
	 * @return
	 */
	ServerResponseEntity<String> generateUrlLink(String path, boolean isExpire,String id,Integer expireTime);

	ServerResponseEntity<String> generateShortLink(String path,String id);

	ServerResponseEntity<String> generateQrcodeUrlLink(String path, boolean isExpire,String id,Integer qrcodeSize,Integer expireTime);

	ServerResponseEntity<String> getWxaCodeBaseImg(String scene,String path,Integer width);

	ServerResponseEntity<String> getBase64InsertCode(String scene,String path,Integer width,Integer width_y,String code);

	ServerResponseEntity<File> getWxaCodeFile(String scene, String path, Integer width);

	ServerResponseEntity<byte[]> getWxaCodeByte(String scene, String path, Integer width);

	/**
	 * 生成太阳码 上传返回链接
	 * @param scene
	 * @param path
	 * @param width
	 * @return
	 */
	ServerResponseEntity<String> getWxaCodeUrl(String scene, String path, Integer width,Long storeId);
}
