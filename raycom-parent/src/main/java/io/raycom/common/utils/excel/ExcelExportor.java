package io.raycom.common.utils.excel;

import io.raycom.system.framework.collection.RMultiData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

/**
 * 对ExportForExcel的包装类，稍易于使用
 */
public class ExcelExportor {

	private String m_fileName;
	private RMultiData m_result;
	private ArrayList m_columnName;
	private Map m_headerNameMap;

	private ExcelExportor() {
	}

	public ExcelExportor(String fileName, RMultiData result,Model model) {
		this.m_fileName = fileName;
		this.m_result = result;
		this.m_columnName = new ArrayList();
		this.m_headerNameMap = new HashMap();
		model.addAttribute("exportor",this);
		//ExportForExcel.exportToXlsTemp(req, res, m_fileName, m_result, m_columnName, m_headerNameMap);
	}

	public void addColumn(String columnName, String headerName) {
		this.m_columnName.add(columnName);
		this.m_headerNameMap.put(columnName, headerName);
	}

	public String getM_fileName() {
		return m_fileName;
	}

	public void setM_fileName(String m_fileName) {
		this.m_fileName = m_fileName;
	}

	public RMultiData getM_result() {
		return m_result;
	}

	public void setM_result(RMultiData m_result) {
		this.m_result = m_result;
	}

	public ArrayList getM_columnName() {
		return m_columnName;
	}

	public void setM_columnName(ArrayList m_columnName) {
		this.m_columnName = m_columnName;
	}

	public Map getM_headerNameMap() {
		return m_headerNameMap;
	}

	public void setM_headerNameMap(Map m_headerNameMap) {
		this.m_headerNameMap = m_headerNameMap;
	}
	
	
}