package com.jingshuiqi.resubmit;

/**
 * 重复提交异常
 */
public class DuplicateSubmitException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1465435218178067342L;

	public DuplicateSubmitException(String msg) {
		super(msg);
	}

	public DuplicateSubmitException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
