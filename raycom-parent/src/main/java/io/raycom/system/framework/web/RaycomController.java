/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.system.framework.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JavaType;

import io.raycom.common.bean.beanvalidator.BeanValidators;
import io.raycom.common.config.Constant;
import io.raycom.common.mapper.JsonMapper;
import io.raycom.common.utils.date.DateUtils;
import io.raycom.common.utils.user.UserUtils;
import io.raycom.components.messageService.bean.MsgBean;
import io.raycom.components.messageService.bean.ResponseRestBean;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.collection.RMultiData;
import io.raycom.system.framework.persistence.Page;

/**
 * 控制器支持类
 * @author caoshengquan
 * @version 2017-03-14
 */
public abstract class RaycomController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String EXCEL="../core/popup/exportExcel";

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;
	
	protected RData rdata;
	
	protected Page<RData> page ;
	
	protected RMultiData mdata;
	
	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;
	
	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	@ModelAttribute
	public void getRdata(@RequestParam HashMap<String, String> map,HttpServletRequest request,@RequestParam HashMap<String, String[]> maplist) {
		rdata = new RData();
		page = null;
		rdata.putAll(map);
		rdata.put("currentUser", UserUtils.getUser().getId());
		if(rdata.containsKey("draw")){
			page= new Page<RData>(rdata);
			processPageData(request.getRequestURI());
		}
		if(rdata.containsKey("urlAction")){
			processUrlActionData(request.getRequestURI());
		}
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		mdata = new RMultiData();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			for (int i = 0; i <entry.getValue().length; i++) {
				mdata.add(entry.getKey(), entry.getValue()[i]);
			}
		}
	}
	
	private void processUrlActionData(String uri){
		UserUtils.putCache(Constant.CACHE_ACTION_URL+uri, rdata.getString("urlAction"));
		UserUtils.putCache(Constant.CACHE_PRE_URL, uri);
	}
	
	private void processPageData(String uri){
		if(page.param!=null){
			if(rdata.getBoolean("isDtRealQuest")){//采用页面传输数据，则将查询条件保存到缓存中
				UserUtils.putCache("isDtRealQuest"+uri, rdata.clone());
			}else{
				RData sessionRdata = (RData)UserUtils.getCache("isDtRealQuest"+uri);
				String preUrl = (String)UserUtils.getCache(Constant.CACHE_PRE_URL);
				String preUrlAction = (String)UserUtils.getCache(Constant.CACHE_ACTION_URL+preUrl);
				if(sessionRdata!=null){
					sessionRdata.remove("draw");
					if(!"cancel".equals(preUrlAction)){
						sessionRdata.remove("start");
					}
					sessionRdata.remove("length");
					page.sessionRdata = sessionRdata.clone();
					page.param = sessionRdata;
					rdata= sessionRdata;
				}
				
			}
		}
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractMessage(ex);
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 逍遥rest接口的参数格式是否满足
	 * @param mpb
	 * @param restBean
	 * @return
	 */
	public boolean validateRestBean(MsgBean mpb) {
		try{
			BeanValidators.validateWithException(validator, mpb);
		}catch(ConstraintViolationException ex){
			return false;
		}
		return true;
	}
	
	/**
	 * 逍遥rest接口的参数格式是否满足
	 * @param mpb
	 * @param restBean
	 * @return
	 */
	public boolean validateRestBean(MsgBean mpb,ResponseRestBean restBean) {
		try{
			BeanValidators.validateWithException(validator, mpb);
		}catch(ConstraintViolationException ex){
			return false;
		}
		return true;
	}
	/**
	 * 返回验证是否通过
	 * @param mpb
	 * @param restBean
	 * @return
	 */
	public boolean validateBeanWithBoole(Object obj) {
		try{
			BeanValidators.validateWithException(validator, obj);
		}catch(ConstraintViolationException ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 返回验证结果，空则诶通过
	 * @param mpb
	 * @param restBean
	 * @return
	 */
	public String validateBeanWithError(Object obj) {
		try{
			BeanValidators.validateWithException(validator, obj);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractMessage(ex);
			return list.toString();
		}
		return "";
	}
	
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractMessage(ex);
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	
	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}
	
	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
	        response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {  
        return "error/400";
    }
	
	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {  
        return "error/403";
    }
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}
	
	protected void getMDataByJsonDt() {
		String jsonDt = rdata.getString("jsonDt");
		mdata.remove("jsonDt");
		JsonMapper js =JsonMapper.getInstance();
		JavaType jt = js.createCollectionType(ArrayList.class,HashMap.class);
		ArrayList<HashMap<String, String>> s = (ArrayList<HashMap<String, String>>)js.fromJson(jsonDt,jt);
		for (HashMap<String, String> map : s) {
			Set<String> set =  map.keySet();
			for (String str : set) {
				mdata.add(str, map.get(str));
			}
		}
		mdata.add("currentUser", UserUtils.getUser().getId());
		rdata = mdata.getRData(0);
	}
	
}
