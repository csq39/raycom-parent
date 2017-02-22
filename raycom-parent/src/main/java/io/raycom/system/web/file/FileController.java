package io.raycom.system.web.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import io.raycom.common.config.Constant;
import io.raycom.common.utils.IdGen;
import io.raycom.common.utils.date.DateUtils;
import io.raycom.common.utils.excel.ExcelExportor;
import io.raycom.common.utils.excel.ExportForExcel;
import io.raycom.common.utils.file.FileUtils;
import io.raycom.common.utils.string.StringUtils;
import io.raycom.common.web.BaseController;
import io.raycom.components.context.event.listener.RaycomEventPublisher;
import io.raycom.components.context.event.sysEvent.FileUploadEvent;
import io.raycom.system.framework.collection.RData;
import io.raycom.system.web.dao.FileDao;
@Controller
public class FileController extends BaseController{
	
	@Autowired
	private FileDao fileDao;
	
	@Value("${uploadPath}")
	protected String fileUploadDir;
	
	@Value("${fileUploadEventDefaultFired}")
	protected String fileUploadEventDefaultFired;
	
	// 最大文件大小
	@Value("${web.maxUploadSize}")
	private long maxSize ;
	// 定义允许上传的文件扩展名
	@Value("${web.staticFile}")
	private String validExt;// = "gif,jpg,jpeg,png,bmp,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2";
	
    @RequestMapping(value = "${adminPath}/file/upload",method = RequestMethod.POST)  
    @ResponseBody
    public  RData upload(@RequestParam CommonsMultipartFile file) {  
    		
		String originalFilename = file.getOriginalFilename();
	    String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
	    String fileName = UUID.randomUUID().toString()+"."+fileExt;

	    if (file.getSize() > maxSize) { // 检查文件大小
			fileExt = "上传文件大小超过限制";
			rdata.set("error",  "上传文件大小超过限制。");
			return rdata;
		} 
	    
	    if (!Arrays.<String> asList(validExt.split(",")).contains("."+fileExt)) {// 检查扩展名
	    	rdata.set("error",  "上传文件格式错误，请重新上传。");
			return rdata;
		} 
	    
	    File f = getFile(fileName);
	    
	    rdata.set("OriginalFilename", originalFilename);
	    rdata.set("fileName", fileName);
	    rdata.set("fileSize", file.getSize());
	    rdata.set("fileUploadDir", FileUtils.path(fileUploadDir));
	    rdata.set("fileUploadPath", "/"+DateUtils.getDate()+"/"+fileName);
	    rdata.set("fileId", IdGen.uuid());//生成主键，16位
	    rdata.set("error","");
		
        try {  
            FileUtils.copyInputStreamToFile(file.getInputStream(),f );  
            fileDao.addFile(rdata);
            if(Constant.Y_VALUE.equals(fileUploadEventDefaultFired)||
            		Constant.Y_VALUE.equals(rdata.get("eventFired")))
            	RaycomEventPublisher.publishEvent(new FileUploadEvent(rdata));
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
           
        return rdata;    
    } 
    
    @RequestMapping("${adminPath}/file/showImg")
    public String showImg(HttpServletRequest request, HttpServletResponse response) {
    	if(rdata.containsKey("fileId")&&!StringUtils.isEmpty(rdata.getString("fileId")))
    		rdata = fileDao.getFile(rdata);
    	else{//默认图片
    		rdata.set("fileNameOrig", "imgDefault.jpg");
    		rdata.set("fileDir", request.getSession().getServletContext().getRealPath("/"));
    		rdata.set("filePath", "/static/img/default/imgDefault.jpg");
    		
    	}
    	
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="+ rdata.getString("fileNameOrig"));
        try {
            String path = rdata.getString("fileDir")+rdata.getString("filePath");
            InputStream inputStream = new FileInputStream(new File(path));
            OutputStream os = response.getOutputStream();
            response.reset();  
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
             // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            //  返回值要注意，要不然就出现下面这句错误！
            //java+getOutputStream() has already been called for this response
        return null;
    }
    
    @RequestMapping("${adminPath}/file/download")
    public ResponseEntity<byte[]> download() throws IOException {  
    	rdata = fileDao.getFile(rdata);
    	String path = FileUtils.path(fileUploadDir)+rdata.getString("filePath");
    	String fileName=new String(rdata.getString("fileNameOrig").getBytes("UTF-8"),"iso-8859-1");
        HttpHeaders headers = new HttpHeaders();  
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
        headers.setContentDispositionFormData("attachment",fileName);  
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(path)),  
                                          headers, HttpStatus.OK);  
    } 
    
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
    
    public  File getFile( String fileName) {
    	
    	String fileUploadPath="/"+DateUtils.getDate()+"/"+fileName;
 	    fileUploadDir=FileUtils.path(fileUploadDir);
 	    FileUtils.createDirectory(fileUploadDir+"/"+DateUtils.getDate());//创建上传文件夹
        File f=new File(fileUploadDir+fileUploadPath);  
        return f;
   	  
    }
}