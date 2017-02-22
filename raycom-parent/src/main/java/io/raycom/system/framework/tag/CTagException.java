package io.raycom.system.framework.tag;

import javax.servlet.jsp.JspTagException;

public class CTagException extends JspTagException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CTagException() {
		super();
	}

	public CTagException(String code) {
		super(code);

	}

	public CTagException(String code, String msg) {
		super("[" + code + "] " + msg);
	}

	private CTagException(String code, String msg, Throwable e) {
		super("[" + code + "] " + msg, e);
	}

	public static CTagException getInstance(String code, String msg, Throwable e) {
		try {
			return new CTagException(code, msg, e);
		} catch (Throwable t) {
			return new CTagException(code, msg);
		}
	}

	public static CTagException getInstance(String code, Throwable e) {
		try {
			return new CTagException(code, e);
		} catch (Throwable t) {
			return new CTagException(code);
		}
	}

	public static CTagException getInstance(Throwable e) {
		try {
			return new CTagException(e);
		} catch (Throwable t) {
			return new CTagException();
		}
	}

	private CTagException(String code, Throwable e) {
		super(code, e);
	}

	private CTagException(Throwable arg0) {
		super(arg0);

	}
}
