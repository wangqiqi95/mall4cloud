package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 话术部门 表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public class CpChatScriptStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 部门id
     */
    private Long storeId;

    /**
     * 话术id
     */
    private Long scriptId;

    /**
     * 话术类型 0普通话术 1问答话术
     */
    private Integer type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getScriptId() {
		return scriptId;
	}

	public void setScriptId(Long scriptId) {
		this.scriptId = scriptId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CpChatScriptStore{" +
				"id=" + id +
				",storeId=" + storeId +
				",scriptId=" + scriptId +
				",type=" + type +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
