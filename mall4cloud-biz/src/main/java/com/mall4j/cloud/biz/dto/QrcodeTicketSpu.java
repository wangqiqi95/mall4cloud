package com.mall4j.cloud.biz.dto;

import lombok.Data;

import java.util.List;

/**
 * 二维码数据信息DTO
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
@Data
public class QrcodeTicketSpu {
    private static final long serialVersionUID = 1L;

	private String spu_id;
	private String spu_code;
}
