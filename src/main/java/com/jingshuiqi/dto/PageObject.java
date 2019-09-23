package com.jingshuiqi.dto;

import java.io.Serializable;

public class PageObject implements Serializable{
	
	private static final long serialVersionUID = -112628924258909389L;

	private int pageCurrent = 1;

	private int rowCount;

	private int pageCount;

	private int pageSize = 10;
	
	private int startIndex;
	

	public int getStartIndex(){
		startIndex = (pageCurrent - 1) * pageSize;
		startIndex = startIndex <= 0 ? 0 : startIndex;
		return startIndex;
	}

	public int getPageCount(){
		pageSize = pageSize <= 0 ? 10 : pageSize;
		this.pageCount = rowCount / pageSize;
		if(rowCount%pageSize != 0){
			this.pageCount += 1;
		}
		return pageCount;
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

	public PageObject(int pageCurrent, int rowCount, int pageCount,
			int pageSize, int startIndex) {
		super();
		this.pageCurrent = pageCurrent;
		this.rowCount = rowCount;
		this.pageCount = pageCount;
		this.pageSize = pageSize;
		this.startIndex = startIndex;
	}

	public PageObject() {
		super();
	}

}
