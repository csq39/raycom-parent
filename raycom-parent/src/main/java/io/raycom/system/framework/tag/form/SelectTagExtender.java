package io.raycom.system.framework.tag.form;

import java.util.List;

import io.raycom.system.framework.collection.RData;

public interface SelectTagExtender{
	
	public String getComCode();
	public String getItemLabel();
	public String getItemValue();
	
	public List<RData> getItems();
}