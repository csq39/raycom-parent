package io.raycom.system.framework.tag.form;

import java.io.IOException;
import java.io.StringReader;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.raycom.common.utils.FreeMarkers;
import io.raycom.system.framework.collection.RData;

/**
 * 采用select2的下拉框
 * @author Administrator
 *
 */
public class Select2Tag extends SelectTag {
	private static final long serialVersionUID = 1L;
	
	public Select2Tag() {
		super();
	}

	public void release() {
		super.release();
	}
	
	public void appendHtmlBeforeSelect(StringBuilder sb) {
		try {
			Template	script = new Template("script", new StringReader("<script type=\"text/javascript\">	$(document).ready(function() {"
					+ "		$('#${id}').select2({});	});</script>   "), new Configuration());
			RData rdata = new RData();
			rdata.set("id", id);
			sb.append(FreeMarkers.renderTemplate(script, rdata));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	

	

}
