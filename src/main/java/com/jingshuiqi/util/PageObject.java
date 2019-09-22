package com.jingshuiqi.util;

import java.io.Serializable;
import java.util.Date;

public class PageObject implements Serializable {
	private static final long serialVersionUID = -8753809986545361268L;

	private int pageCurrent=1;

	private int rowCount;

	private int pageCount;

	private int pageSize=1;
	
	private int startIndex;
	
	private String uuid;

	private String openId;

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getStartIndex(){
		return (pageCurrent-1)*pageSize;
	}

	public int getPageCount(){
		int pages=rowCount/pageSize;
		if(rowCount%pageSize!=0){
			pages+=1;
		}
		return pages;
	}

	public int getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public String toString() {
		return "PageObject{" +
				"pageCurrent=" + pageCurrent +
				", rowCount=" + rowCount +
				", pageCount=" + pageCount +
				", pageSize=" + pageSize +
				", startIndex=" + startIndex +
				", uuid='" + uuid + '\'' +
				", openId='" + openId + '\'' +
				", id=" + id +
				'}';
	}
}
