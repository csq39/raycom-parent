package io.raycom.system.framework.tag.form;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.raycom.common.bean.SystemUser;
import io.raycom.common.utils.FreeMarkers;
import io.raycom.common.utils.SpringContextHolder;
import io.raycom.common.utils.string.StringUtils;
import io.raycom.common.utils.user.UserUtils;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.collection.RMultiData;
import io.raycom.system.framework.tag.CTag;
import io.raycom.system.web.dao.DictDao;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspTagException;

/**
 * 下拉框，数据来源有两种，一个是页面传入，一个后台server读取，其中后台server读取需要传入comcode，其值：
 * 1：所有仓库：all-wh
 * 1：根据授权查看仓库：role-wh
 * 
 * 另一种是页面读取，其中页面读取如果分组，则需要list<Rdata>的元素RDATA中需要存在key=“group”的字段，根据group进行分组，否则，则不践行分组
 * @author Administrator
 *
 */
public class SelectTag extends CTag {
	private static final long serialVersionUID = 1L;
	
	protected String  id ;
	protected String  name ;
	//样式
	protected String  className ;
	//扩展字段，原样输出
	protected String  htmlAppend ;
	protected String  dataQuery ;
	//option的列表
	protected List<RData>  items ;
	protected String  itemLabel ;
	protected String  itemValue ;
	//共同代码表的代码组code
	protected String  comCode ;
	//第一行选项
	protected String  headerKey ;
	protected String  headerValue ;
	//默认选中值
	protected String  value ;
	//默认选中值
	protected String  onChange ;
	
	protected RData optGroup  ;
	
	private final static String  GROUP_NULL="group_null" ;
	private final static String  WHALL="all-wh" ;
	private final static String  WHROLEL="role-wh" ;
	private final static String  UNITS="unit" ;
	private final static String  MANUFACTURE="manufacture" ;
	private final static String  NATION="nation" ;

	public SelectTag() {
		super();
	}

	public void release() {
		super.release();
	}

	public int doEndTag() throws JspTagException {
		try {
			this.printTagString(getSelectBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getSelectBody() {
		StringBuilder sb = new StringBuilder();
		appendHtmlBeforeSelect(sb);
		sb.append("<select ");
		setProperty(sb,"id",id);
		setProperty(sb,"name",name);
		setProperty(sb,"class",className);
		setProperty(sb,"onChange",onChange);
		if(htmlAppend!=null)
			sb.append(htmlAppend+" ");
		setProperty(sb,"data-query",dataQuery);
		sb.append(">");
		
		sb.append(getOptionBody());
		
		sb.append("</select>");
		return sb.toString();
	}
	
	public void appendHtmlBeforeSelect(StringBuilder sb) {}

	protected void setProperty(StringBuilder sb,String property,String value) {
		if(value!=null)
			sb.append(property+"='"+value+"' ");
	}
	
	public String getOptionBody() {
		StringBuilder sb = new StringBuilder();
		optGroup = new RData();
		
		getOptionsByServer();
		getGrouplabel();
		try {
			
			Template optgroup = new Template("optgroup", new StringReader(" <optgroup label='${optgroup}'>"), new Configuration());
			Template temp = new Template("label", new StringReader(" <option value='${code}'>${label}</option>"), new Configuration());
			Template selected = new Template("selected", new StringReader(" <option value='${code}' selected>${label}</option>"), new Configuration());
			
			//头行
			if(!StringUtils.isEmpty(headerKey)){
				setOption(sb,temp,selected,headerValue,headerKey);
			}
			
			appendOption(sb, optgroup, temp, selected);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		return sb.toString();
	}
	
	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	protected void setOption(StringBuilder sb,Template temp,Template selected,String label,String code) {
		    if(StringUtils.isEmpty(code)||StringUtils.isEmpty(label)){
		    	return;
		    }
			RData rdata = new RData();
			rdata.set("code", code);
			rdata.set("label", label);
			
			if(!StringUtils.isEmpty(value)&&value.contains(code)){//选中
				sb.append(FreeMarkers.renderTemplate(selected, rdata));
			}else{
				sb.append(FreeMarkers.renderTemplate(temp, rdata));
			}
	}
	
	@SuppressWarnings("unchecked")
	private void appendOption(StringBuilder sb, Template optgroup,Template temp, Template selected) {
		for (String key : (Set<String>)optGroup.keySet()) {
			if(GROUP_NULL.equals(key)){
				RMultiData optData= (RMultiData)optGroup.get(GROUP_NULL);
				for (int i = 0; i < optData.getDataCount(); i++) {
					setOption(sb,temp,selected,optData.getRData(i).getString(itemLabel),optData.getRData(i).getString(itemValue));
				}
			}
			if(!GROUP_NULL.equals(key)){
				RMultiData optData= (RMultiData)optGroup.get(key);
				RData rdata = new RData();
				rdata.set("optgroup", key);
				sb.append(FreeMarkers.renderTemplate(optgroup, rdata));
				for (int i = 0; i < optData.getDataCount(); i++) {
					setOption(sb,temp,selected,optData.getRData(i).getString(itemLabel),optData.getRData(i).getString(itemValue));
				}
				sb.append(" </optgroup> ");
			}
		}
	}

	private void getGrouplabel() {
		for (RData rData : items) {
			String group = StringUtils.isEmpty(rData.getString("group"))?GROUP_NULL:rData.getString("group");
	
			RMultiData optData=(RMultiData) optGroup.get(group);
			if(!optGroup.containsKey(group)){
				optData = new RMultiData();
				optGroup.set(group, optData);
			}
			optData.addRData(rData);
		}
	}

	private void getOptionsByServer() {
		if(!StringUtils.isEmpty(comCode)){
			if(WHALL.equals(comCode)){//库存单位
				DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
				items = dictDao.findWHList(null);
				itemLabel="name" ;
				itemValue="code";
				
				for (RData rData : items) {
					if(rData.getString("topPid").equals(rData.getString("pid")))
						rData.set("group", rData.getString("topPName"));
					else
						rData.set("group", rData.getString("topPName")+":"+rData.getString("pName"));
				}
			}else if(WHROLEL.equals(comCode)){//带权限筛选的库存列表
				DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
				RData rdata = new RData();
				SystemUser user = UserUtils.getUser();
				if (!user.isAdmin(user.getId()))
					rdata.set("userId", user.getId());
					items = dictDao.findWHList(rdata);
				itemLabel="name" ;
				itemValue="code";
				
				for (RData rData : items) {
					if(rData.getString("topPid").equals(rData.getString("pid")))
						rData.set("group", rData.getString("topPName"));
					else
						rData.set("group", rData.getString("topPName")+":"+rData.getString("pName"));
				}
			}else if(UNITS.equals(comCode)){//单位
				DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
				items = dictDao.findUnitList();
				itemLabel="name" ;
				itemValue="code";
			}else if(MANUFACTURE.equals(comCode)){//厂家
				DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
				items = dictDao.findManufactureList();
				itemLabel="name" ;
				itemValue="code";
			}else if(NATION.equals(comCode)){//国家
				DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
				items = dictDao.findNationList();
				itemLabel="name" ;
				itemValue="code";
				if(StringUtils.isEmpty(value))
					value="cn";
			}else {
				DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
				RData query = new RData();
				query.set("type", comCode);
				items = dictDao.findList(query);
				itemLabel="label" ;
				itemValue="code";
			}
		}
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getHtmlAppend() {
		return htmlAppend;
	}

	public void setHtmlAppend(String htmlAppend) {
		this.htmlAppend = htmlAppend;
	}


	public List<RData> getItems() {
		return items;
	}

	public void setItems(List<RData> items) {
		this.items = items;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getHeaderKey() {
		return headerKey;
	}

	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getDataQuery() {
		return dataQuery;
	}

	public void setDataQuery(String dataQuery) {
		this.dataQuery = dataQuery;
	}
	

}
