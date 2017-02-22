/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.common.utils;

import io.raycom.common.mapper.JsonMapper;
import io.raycom.common.utils.cache.CacheUtils;
import io.raycom.common.utils.dao.DaoUtil;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.collection.RMultiData;
import io.raycom.system.web.dao.DictDao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * 字典工具类
 * @author caoshengquan
 * @version 2013-5-29
 */
public class DictUtils {
	
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);

	public static final String CACHE_DICT_MAP = "dictMap";
	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			RMultiData dict = getDictList(type);
			for (int i = 0; i < dict.getDataCount(); i++) {
				if(value.equals(dict.get("label", i))){
					return dict.getString("label", i);
				}
			}
		}
		return defaultValue;
	}
	
	public static String getDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			RMultiData dict = getDictList(type);
			for (int i = 0; i < dict.getDataCount(); i++) {
				if(label.equals(dict.get("label", i))){
					return dict.getString("value", i);
				}
			}
		}
		return defaultLabel;
	}
	
	@SuppressWarnings("unchecked")
	public static RMultiData getDictList(String type){
		RData dictMap = (RData)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap =new RData();
			RMultiData rm = DaoUtil.ConvertList2CMulti(dictDao.findAllList());
			for (int i = 0; i < rm.getDataCount(); i++) {
				String dictType = rm.getString("type", i);
				RMultiData mulDict = (RMultiData)dictMap.get(dictType);
				if (mulDict == null){
					RMultiData rmTemp = new RMultiData();
					rmTemp.addRData(rm.getRData(i));
					dictMap.put(dictType, rmTemp);
				}else if (mulDict != null&&!mulDict.containsValue(rm.getString("value", i))){
					mulDict.addRData(rm.getRData(i));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		RMultiData dictList = (RMultiData)dictMap.get(type);
		if (dictList == null){
			dictList = new RMultiData();
		}
		return dictList;
	}
	
	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type){
		return JsonMapper.toJsonString(getDictList(type));
	}
	
}
