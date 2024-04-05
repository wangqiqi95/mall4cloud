package com.mall4j.cloud.platform.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 员工企微工具信息
 *
 * @author ZengFanChang
 * @date 2022-02-12 00:13:55
 */
public class StaffTool extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 导购ID
     */
    private Long staffId;

    /**
     * 工具数据(json格式)
     */
    private String toolData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getToolData() {
		return toolData;
	}

	public void setToolData(String toolData) {
		this.toolData = toolData;
	}

	@Override
	public String toString() {
		return "StaffTool{" +
				"id=" + id +
				",staffId=" + staffId +
				",toolData=" + toolData +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
