package io.raycom.system.web.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import io.raycom.common.utils.IdGen;
import io.raycom.common.utils.date.DateUtils;
import io.raycom.common.utils.excel.ExcelExportor;
import io.raycom.common.utils.excel.ExportForExcel;
import io.raycom.common.utils.file.FileUtils;
import io.raycom.common.utils.string.StringUtils;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.web.dao.FileDao;


@Service
public class FileService{
	
	@Autowired
	private FileDao fileDao;
	
	@Value("${uploadPath}")
	protected String fileUploadDir;

	public  RData buildExceltoFile( ExcelExportor exportor) throws IOException {

    	RData fileData = new RData();
		String target_file = UUID.randomUUID().toString()+".xls";
		File file=getFile(target_file);   
        OutputStream os=new FileOutputStream(file);  
        ExportForExcel.buildExceltoFile(os, exportor);
        fileData.set("OriginalFilename", target_file);
        fileData.set("fileName", target_file);
        fileData.set("fileSize", file.length());
        fileData.set("fileUploadDir", FileUtils.path(fileUploadDir));
        fileData.set("fileUploadPath", "/"+DateUtils.getDate()+"/"+target_file);
        fileData.set("error","");
        fileData.set("fileId", IdGen.uuid());//生成主键，16位
        fileDao.addFile(fileData);
        
        return fileData;
   	  
    }
	
	public RData getFileData(HttpServletRequest request,RData rdata) {
		if(rdata.containsKey("fileId")&&!StringUtils.isEmpty(rdata.getString("fileId")))
			rdata = fileDao.getFile(rdata);
    	else{//默认图片
    		rdata.set("fileNameOrig", "imgDefault.jpg");
    		rdata.set("fileDir", request.getSession().getServletContext().getRealPath("/"));
    		rdata.set("filePath", "/static/img/default/imgDefault.jpg");
    	}
		return rdata;
	}
	
	public void addFile(RData rdata){
		  rdata.set("fileUploadDir", FileUtils.path(fileUploadDir));
		  rdata.set("createDate", new Date());
		  fileDao.addFile(rdata);
	}
	
	public RData getFileDataByDB(RData rdata) {
		rdata = fileDao.getFile(rdata);
    	rdata.set("path", FileUtils.path(fileUploadDir)+rdata.getString("filePath"));
    	rdata.set("fileName", rdata.getString("fileNameOrig"));
    	return rdata;
	}
    
    public  File getFile( String fileName) {
    	String fileUploadPath="/"+DateUtils.getDate()+"/"+fileName;
 	    fileUploadDir=FileUtils.path(fileUploadDir);
 	    FileUtils.createDirectory(fileUploadDir+"/"+DateUtils.getDate());//创建上传文件夹
        File f=new File(fileUploadDir+fileUploadPath);  
        return f;
    }
    
    public ResponseEntity<byte[]> downLoadZipFile(RData rdata) throws IOException{
    	String fileName=new String(rdata.getString("fileName").getBytes("UTF-8"),"iso-8859-1");
        HttpHeaders headers = new HttpHeaders();  
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
        headers.setContentDispositionFormData("attachment",fileName);  
        return new ResponseEntity<byte[]>(FileUtils.zipFilesToByteArray(rdata),  
                                          headers, HttpStatus.OK);  
    }
    
}
