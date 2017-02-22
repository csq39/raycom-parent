package io.raycom.system.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class CTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void printBlankString() throws CTagException {
		printTagString("");
	}

	public void printTagString(String printStr) throws CTagException {
		JspWriter out = this.pageContext.getOut();
		try {
			out.print(printStr);
		} catch (IOException e) {
			throw CTagException.getInstance("CSFRM_TLD_001", "IO Error: " + e.getMessage(), e.getCause());
		}
	}

	public void printTagBody(String printStr) throws CTagException {
		JspWriter out = this.getPreviousOut();
		try {
			out.print(printStr);
		} catch (IOException e) {
			throw CTagException.getInstance("CSFRM_TLD_001", "IO Error: " + e.getMessage(), e.getCause());
		}
	}
}
