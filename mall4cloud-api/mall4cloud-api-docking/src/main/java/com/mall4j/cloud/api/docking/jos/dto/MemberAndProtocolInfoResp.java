package com.mall4j.cloud.api.docking.jos.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 会员以及协议基本信息接口请求响应
 * @date 2021/12/23 23:12
 */
public class MemberAndProtocolInfoResp implements Serializable {

	/**
	 * 1	成功
	 * 0	失败
	 * -1	参数错误
	 * -2	平台不存在
	 * -4	请求id重复
	 * -5	平台已关闭
	 * -6	会员信息已存在
	 * -8	解析保存文件流失败
	 * -9	url下载失败
	 */
	@ApiModelProperty("响应编码, 1\t成功\n" + "\t  0\t失败\n" + "\t  -1\t参数错误\n" + "\t  -2\t平台不存在\n" + "\t  -4\t请求id重复\n" + "\t  -5\t平台已关闭\n" + "\t  -6\t会员信息已存在\n"
			+ "\t  -8\t解析保存文件流失败\n" + "\t  -9\turl下载失败")
	private Integer code;

	@ApiModelProperty("响应描述")
	private String msg;

	public MemberAndProtocolInfoResp() {
	}

	public MemberAndProtocolInfoResp(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override public String toString() {
		return "MemberAndProtocolInfoResp{" + "code=" + code + ", msg='" + msg + '\'' + '}';
	}
}
