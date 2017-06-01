package io.raycom.system.framework.tag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;

import io.raycom.common.bean.SystemMenu;
import io.raycom.common.config.Global;
import io.raycom.common.utils.SpringContextHolder;
import io.raycom.common.utils.user.UserUtils;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.web.dao.SecurityDao;

public class MenuTag extends CTag {
	private static final long serialVersionUID = 1L;
	
	private String  ctxpath ;

	public MenuTag() {
		super();
	}

	public void release() {
		super.release();
	}

	public int doEndTag() throws JspTagException {
		try {
			HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
			ctxpath = req.getContextPath()+Global.getAdminPath();
			List<SystemMenu>  menuList =  getMenuList();
			this.printTagString(getMenuList(menuList,SystemMenu.getRootId()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getMenuList(List<SystemMenu> 	 menuList,String parantId) {
		StringBuilder sb = new StringBuilder();
		for (SystemMenu menu : menuList) {
			if(menu.getParentId().equals(parantId)){
				if(menu.getHasChild().equals("0")){
					sb.append( "<li><a  class=\"ajax-link\"     target=\"mainFrame\" id=\""+menu.getId()+"\"  href=\""+getMenuUrl(menu.getHref())+"\">"+getMenuName(menu.getName(),menu.getIcon())+"</a></li>");
				}else{//hasNext
					sb.append("<li class=\"dropdown\"><a href=\"#\" class=\"dropdown-toggle\">"+	getMenuName(menu.getName(),menu.getIcon()==null?"fa fa-plus-square":menu.getIcon())+"</a><ul class=\"dropdown-menu\">"
							+ getMenuList(menuList,menu.getId())
							+ "</ul></li>");
				}
			}
		}
		return sb.toString();
	}
	
	public List<SystemMenu> getMenuList() { 
		List<SystemMenu>  menuList;
		SecurityDao securityDao = SpringContextHolder.getBean(SecurityDao.class);
		RData rd = new RData();
		
		rd.set("jdbcType", Global.getConfig("jdbc.type"));
		if(UserUtils.getUser().isAdmin()){
			menuList = securityDao.getAllMenuList(rd);
		}
		else{
			rd.set("userId", UserUtils.getUser().getId());
			menuList = securityDao.getMenuByUserId(rd);
		}
		return menuList;
	}
	
	
	public String getMenuUrl(String menuUrl) { 
		if(menuUrl==null||"#".equals(menuUrl)){
			return "#";
		}else{
			return ctxpath +menuUrl;
		}
	}
	
	public String getMenuName(String menuName,String icon) {
		if(icon==null){
			return menuName;
		}else{
			return "<i class=\""+icon+"\"></i><span class=\"hidden-xs\">"+menuName+"</span>";
		}
	}

}
