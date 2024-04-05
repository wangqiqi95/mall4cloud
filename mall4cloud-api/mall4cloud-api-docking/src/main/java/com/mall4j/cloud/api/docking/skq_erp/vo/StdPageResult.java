package com.mall4j.cloud.api.docking.skq_erp.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 类描述：中台分页信息
 *
 * @date 2022/1/9 11:02：40
 */
public class StdPageResult<T> {

	@ApiModelProperty(value = "当前页")
	private Integer page;

	@ApiModelProperty(value = "每页显示数量")
	private Integer pageSize;

	@ApiModelProperty(value = "总页数")
	private Integer totalPage;

	@ApiModelProperty(value = "总数量")
	private Integer totalSize;

	@ApiModelProperty(value = "明细")
	private List<T> result;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "StdPageResult{" + "page=" + page + ", pageSize=" + pageSize + ", totalPage=" + totalPage + ", totalSize=" + totalSize + ", result=" + result
				+ '}';
	}
}
