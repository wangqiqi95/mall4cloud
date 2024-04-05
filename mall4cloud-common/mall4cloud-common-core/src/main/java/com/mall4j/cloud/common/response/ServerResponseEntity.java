package com.mall4j.cloud.common.response;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.exception.LuckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import org.slf4j.MDC;

/**
 * 统一的返回数据
 *
 * @author FrozenWatermelon
 * @date 2020/7/3
 */
public class ServerResponseEntity<T> implements Serializable {

	private static final Logger log = LoggerFactory.getLogger(ServerResponseEntity.class);

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 信息
	 */
	private String msg;

	/**
	 * 链路id
	 */
	protected String traceId = MDC.get("X-B3-TraceId");

	/**
	 * 数据
	 */
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getTraceId() {
		return traceId;
	}

	public boolean isSuccess() {
		return Objects.equals(ResponseEnum.OK.value(), this.code);
	}
	public boolean isFail() {
		return !Objects.equals(ResponseEnum.OK.value(), this.code);
	}

	public static <T> ServerResponseEntity<T> success(T data) {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setData(data);
		serverResponseEntity.setCode(ResponseEnum.OK.value());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> success() {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setCode(ResponseEnum.OK.value());
		serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> success(Integer code, T data) {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setCode(String.valueOf(code));
		serverResponseEntity.setData(data);
		return serverResponseEntity;
	}

	/**
	 * 前端显示失败消息
	 * @param msg 失败消息
	 * @return
	 */
	public static <T> ServerResponseEntity<T> showFailMsg(String msg) {
		log.warn(msg);
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(msg);
		serverResponseEntity.setCode(ResponseEnum.SHOW_FAIL.value());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
		log.warn(responseEnum.toString());
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(responseEnum.getMsg());
		serverResponseEntity.setCode(responseEnum.value());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T data) {
		log.warn(responseEnum.toString());
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(responseEnum.getMsg());
		serverResponseEntity.setCode(responseEnum.value());
		serverResponseEntity.setData(data);
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> showDefinedMsg(ResponseEnum responseEnum, String msg) {
		log.warn(responseEnum.toString());
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(msg);
		serverResponseEntity.setCode(responseEnum.value());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> fail(Integer code, T data) {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setCode(String.valueOf(code));
		serverResponseEntity.setData(data);
		return serverResponseEntity;
	}

	@SuppressWarnings("unchecked")
	public static <T> ServerResponseEntity<T> transform(ServerResponseEntity<?> oldServerResponseEntity) {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(oldServerResponseEntity.getMsg());
		serverResponseEntity.setCode(oldServerResponseEntity.getCode());
		serverResponseEntity.setData((T) oldServerResponseEntity.getData());
		log.error(serverResponseEntity.toString());
		return serverResponseEntity;
	}

	public static void checkResponse(ServerResponseEntity response){
		if (response.isFail()) {
			log.info("操作失败：{}",response.getMsg().toString());
//			response.setMsg("操作失败，请稍后重试");
			if(StrUtil.isNotEmpty(response.getMsg())){
				throw new LuckException(response.getMsg().toString());
			}else{
				throw new LuckException(ResponseEnum.EXCEPTION,response.getMsg().toString());
			}
		}
	}

	@Override
	public String toString() {
		return "ServerResponseEntity{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

}
