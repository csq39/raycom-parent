/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/csq/raycom">Raycom</a> All rights reserved.
 */
package io.raycom.common.utils.file;

import io.raycom.common.utils.SpringContextHolder;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.web.dao.FileDao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取附件工具类
 * @author caoshengquan
 * @version 2013-5-29
 */
@Component
public class AttachmentUtils {
	
	private static FileDao fileDao = SpringContextHolder.getBean(FileDao.class);
	
	private static AttachmentUtils attachmentUtils = SpringContextHolder.getBean(AttachmentUtils.class);
	
	private static String defaultValue = "";
	
	private static String staticAttachentUrl = "";
	
	
	public static String getFileOrigname(String fileId){
		RData rdata = new RData();
		rdata.set("fileId", fileId);
		rdata = fileDao.getFile(rdata);
		if (rdata==null)
			return defaultValue;
		else
			return rdata.getString("fileNameOrig");
			
	}
	@Deprecated
	public static String getFilePathById(String fileId){
		RData rdata = new RData();
		rdata.set("fileId", fileId);
		rdata = fileDao.getFile(rdata);
		return rdata.getString("fileDir")+rdata.getString("filePath");
	}
	public static String getFileById(String fileId){
		RData rdata = new RData();
		rdata.set("fileId", fileId);
		rdata = fileDao.getFile(rdata);
		return rdata.getString("fileDir")+rdata.getString("filePath");
	}
	
	public static String getImgById(String fileId){
		String imgPath  ="";
		RData rdata = new RData();
		rdata.set("fileId", fileId);
		rdata = fileDao.getFile(rdata);
		if(rdata!=null)
			imgPath = rdata.getString("filePath");
		return imgPath;
	}
	
	public static String getImgUrlById(String fileId){
		return staticAttachentUrl+getImgById(fileId);
	}
	
	public  String getStaticAttachentUrl() {
		return staticAttachentUrl;
	}
	
	@Value("${staticAttachentUrl}")
	public  void setStaticAttachentUrl(String staticAttachentUrl) {
		AttachmentUtils.staticAttachentUrl = staticAttachentUrl;
	}
	
}
