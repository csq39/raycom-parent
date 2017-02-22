package io.raycom.system.framework.collection;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class RDataProtocol extends LinkedHashMap {
	private static final long serialVersionUID = 1L;

	protected String name = null;

	protected boolean nullToInitialize = false;

	protected RMetaData metaData;

	public RDataProtocol(int arg0, float arg1) {
		super(arg0, arg1);
	}

	public RDataProtocol(int arg0) {
		super(arg0);
	}

	public RDataProtocol() {
		super();
	}

	@SuppressWarnings("unchecked")
	public RDataProtocol(Map arg0) {
		super(arg0);
	}

	public RDataProtocol(int arg0, float arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isNullToInitialize() {
		return nullToInitialize;
	}

	public void setNullToInitialize(boolean nullToInitialize) {
		this.nullToInitialize = nullToInitialize;
	}

	public RMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(RMetaData metaData) {
		this.metaData = metaData;
	}
}
