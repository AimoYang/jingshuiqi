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
	
	private Date startTime;
	
	private Date endTime;
	
	private String uuid;
	
	private Integer express;
	
	private short state;

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	
	public Integer getExpress() {
		return express;
	}

	public void setExpress(Integer express) {
		this.express = express;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "PageObject [pageCurrent=" + pageCurrent + ", rowCount="
				+ rowCount + ", pageCount=" + pageCount + ", pageSize="
				+ pageSize + ", startIndex=" + startIndex + ", startTime="
				+ startTime + ", endTime=" + endTime + ", uuid=" + uuid
				+ ", express=" + express + ", state=" + state + "]";
	}
}
