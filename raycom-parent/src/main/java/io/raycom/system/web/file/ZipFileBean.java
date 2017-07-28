package io.raycom.system.web.file;

import java.util.ArrayList;

import io.raycom.system.framework.collection.RData;

public class ZipFileBean{
	
	public String fileName="空白文档.zip";
	public ArrayList<ZipFileItemBean> fileList=new ArrayList<ZipFileItemBean>();
	
	public RData convertToRData(){
		ArrayList<RData> fileRdataList=new ArrayList<RData>();
		
		for (ZipFileItemBean fileItem : fileList) {
			fileRdataList.add(fileItem.convertToRData());
		}
		RData file = new RData();
		file.set("fileName", fileName);//文件名
		file.set("fileList", fileRdataList);//压缩包内文件列表
		return file;
	}
	
	
	public void addItemFile(RData fileItem){
		ZipFileItemBean itemFile = new ZipFileItemBean();
		itemFile.convertFromRData(fileItem);
		fileList.add(itemFile);
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ArrayList<ZipFileItemBean> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<ZipFileItemBean> fileList) {
		this.fileList = fileList;
	}
	
	class ZipFileItemBean{
		public String dirPath;
		public String file;
		
		public void convertFromRData(RData fileItem){
			dirPath = fileItem.getString("dirPath");
			file = fileItem.getString("file");
		}
		
		public RData convertToRData(){
			RData fileData = new RData();
			fileData.set("dirPath", dirPath);
			fileData.set("file", file);
			return fileData;
		}
		
		public String getDirPath() {
			return dirPath;
		}
		public void setDirPath(String dirPath) {
			this.dirPath = dirPath;
		}
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
		
		
	}
	
}