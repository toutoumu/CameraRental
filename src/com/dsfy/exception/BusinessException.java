package com.dsfy.exception;

/**
 * 业务层的异常必须包装成此对象抛出
 * @author toutoumu
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -5250858057392304473L;

	public BusinessException() {
		super();
	}

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
