package io.raycom.system.web.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import io.raycom.common.config.Constant;
import io.raycom.common.utils.IdGen;
import io.raycom.common.utils.date.DateUtils;
import io.raycom.common.utils.file.FileUtils;
import io.raycom.common.web.BaseController;
import io.raycom.components.context.event.listener.RaycomEventPublisher;
import io.raycom.components.context.event.sysEvent.FileUploadEvent;
import io.raycom.system.framework.collection.RData;
@Controller
public class FileController extends BaseController{
	@Value("${fileUploadEventDefaultFired}")
	protected String fileUploadEventDefaultFired;
	@Autowired
	public FileService fileService;
	
	// 最大文件大小
	@Value("${web.maxUploadSize}")
	private long maxSize ;
	// 定义允许上传的文件扩展名
	@Value("${web.staticFile}")
	private String validExt;// = "gif,jpg,jpeg,png,bmp,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2";
	
    @RequestMapping(value = "${adminPath}/file/upload")  
    @ResponseBody
	public  RData upload(@RequestParam("file")  MultipartFile file ) {  
		String originalFilename = file.getOriginalFilename();
	    String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
	    String fileName = IdGen.uuid()+"."+fileExt;

	    if (file.getSize() > maxSize) { // 检查文件大小
			fileExt = "上传文件大小超过限制";
			rdata.set("error",  "上传文件大小超过限制。");
			return rdata;
		} 
	    
	    if (!Arrays.<String> asList(validExt.split(",")).contains("."+fileExt)) {// 检查扩展名
	    	rdata.set("error",  "上传文件格式错误，请重新上传。");
			return rdata;
		} 
	    
	    File f = fileService.getFile(fileName);
	    
	    rdata.set("OriginalFilename", originalFilename);
	    rdata.set("fileName", fileName);
	    rdata.set("fileSize", file.getSize());
	    rdata.set("fileUploadPath", "/"+DateUtils.getDate()+"/"+fileName);
	    rdata.set("fileId", IdGen.uuid());//生成主键，16位
	    rdata.set("error","");
		
        try {  
            FileUtils.copyInputStreamToFile(file.getInputStream(),f );  
            fileService.addFile(rdata);
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
    	rdata =fileService.getFileData(request,rdata);
    	
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
        	System.out.println(rdata.getString("fileDir")+rdata.getString("filePath"));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            //  返回值要注意，要不然就出现下面这句错误！
            //java+getOutputStream() has already been called for this response
        return null;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class) 
    @ResponseBody
    public RData handleException(MaxUploadSizeExceededException ex) throws Exception {   
    	StringBuffer sb = new StringBuffer();
        if (ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException){
            sb.append("文件大小不应大于"+((MaxUploadSizeExceededException)ex).getMaxUploadSize()/(1024*1024)+"MB");
         } else{
             sb.append("上传异常！");
        }
        System.out.println(sb.toString());
        RData rdata1 = new RData();
        rdata1.set("error",sb.toString());
        return rdata1;
    }
    
    @RequestMapping("${adminPath}/file/download")
    public ResponseEntity<byte[]> download() throws IOException {  
    	rdata = fileService.getFileDataByDB(rdata);
    	String path = rdata.getString("path");
    	String fileName=new String(rdata.getString("fileName").getBytes("UTF-8"),"iso-8859-1");
        HttpHeaders headers = new HttpHeaders();  
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
        headers.setContentDispositionFormData("attachment",fileName);  
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(path)),  
                                          headers, HttpStatus.OK);  
    } 
    
    @RequestMapping("${adminPath}/file/downloadZip")
	public ResponseEntity<byte[]> downloadZip(HttpServletRequest request) throws IOException {  
	    return fileService.downLoadZipFile((RData)request.getAttribute("file"));
	} 

}