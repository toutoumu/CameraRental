package com.dsfy.exception;

/**
 * Json格式请求是抛异常使用此异常类包装
 * @author toutoumu
 *
 */
public class JsonException extends RuntimeException {
	private static final long serialVersionUID = 3824768058891341778L;

	public JsonException() {
		super();
	}

	public JsonException(String msg) {
		super(msg);
	}

	public JsonException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JsonException(Throwable cause) {
		super(cause);
	}
}
