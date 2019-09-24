package com.jingshuiqi.dto;

import javax.validation.constraints.NotNull;

public class TypePage extends PageObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "类型不能为空")
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
