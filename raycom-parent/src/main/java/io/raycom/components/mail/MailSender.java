package io.raycom.components.mail;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.raycom.common.utils.file.FileUtils;
import io.raycom.components.context.concurrent.RaycomExecutorService;

@Component
public class MailSender {

	@Autowired
	public MailProperties mailProperties;
	
	@Autowired
	public RaycomExecutorService raycomExecutorService;
	
	/**
	 * 异步发送
	 * @param toMailAddr
	 * @param subject
	 * @param message
	 */
	public void sendMailAsync(String toMailAddr, String subject,String message) {
		raycomExecutorService.getMailService().execute(new MailSenderSync(this, toMailAddr, subject, message,false));
	}
	public void sendMailHtmlAsync(String toMailAddr, String subject,String message) {
		raycomExecutorService.getMailService().execute(new MailSenderSync(this, toMailAddr, subject, message,true));
	}
	
	/**
	 *  异步发送
	 * @param toMailAddr
	 * @param subject 
	 * @param message
	 */
	public void sendFtlMailAsync(String toMailAddr, String subject,String templatePath, Map<String, Object> map) {
		String message   = getMailContentByFtl(templatePath,map);
		raycomExecutorService.getMailService().execute(new MailSenderSync(this, toMailAddr, subject, message,false));
	}
	public void sendFtlMailHtmlAsync(String toMailAddr, String subject,String templatePath, Map<String, Object> map) {
		String message   = getMailContentByFtl(templatePath,map);
		raycomExecutorService.getMailService().execute(new MailSenderSync(this, toMailAddr, subject, message,true));
	}
	
	/**
	 * 发送模板邮件
	 * 
	 * @param toMailAddr
	 *            收信人地址
	 * @param subject
	 *            email主题
	 * @param templatePath
	 *            模板地址
	 * @param map
	 *            模板map
	 */
	public  void sendFtlMail(String toMailAddr, String subject,String templatePath, Map<String, Object> map) {
		String htmlText = getMailContentByFtl(templatePath, map);
		sendMail( toMailAddr, subject, htmlText,false) ;
	}
	
	public  void sendFtlMailHtml(String toMailAddr, String subject,String templatePath, Map<String, Object> map) {
		String htmlText = getMailContentByFtl(templatePath, map);
		sendMail( toMailAddr, subject, htmlText,true) ;
	}
	
	public  void sendMail(String toMailAddr, String subject,String message) {
		sendMail( toMailAddr, subject, message,false) ;
	}
	public  void sendMailHtml(String toMailAddr, String subject,String message) {
		sendMail( toMailAddr, subject, message,true) ;
	}
	
	/**
	 * 发送普通邮件
	 * 
	 * @param toMailAddr
	 *            收信人地址
	 * @param subject
	 *            email主题
	 * @param message
	 *            发送email信息
	 */
	private void sendMail(String toMailAddr, String subject,String message,boolean isHtml) {
		HtmlEmail hemail = new HtmlEmail();
		try {
			hemail.setHostName(mailProperties.getMailHost());
			hemail.setSmtpPort(mailProperties.getMailPort());
			hemail.setCharset(mailProperties.getCharSet());
			hemail.setFrom(mailProperties.getMailFromAccount(),mailProperties.getMailUserName());
			hemail.setAuthentication(mailProperties.getMailAccount(), mailProperties.getMailPassword());
			hemail.addTo(toMailAddr);
			hemail.setSubject(subject);
			if(isHtml)
				hemail.setHtmlMsg(message);
			hemail.setTextMsg(message);
			hemail.send();
			System.out.println("email send true!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("email send error!");
		}
	}
	
	
	/**
	 *  获取邮件正文
	 * @param templatePath
	 * @param map
	 * @return
	 */
	private String getMailContentByFtl(String templatePath,Map<String, Object> map) {
		String htmlText="";
		Configuration freeMarkerConfig = new Configuration();
		try {
			freeMarkerConfig.setDirectoryForTemplateLoading(new File(FileUtils.getFtlPath()));
			Template template = freeMarkerConfig.getTemplate(
					FileUtils.getFileName(templatePath),new Locale("Zh_cn"), "UTF-8");
			// 模板内容转换为string
			 htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
		} catch (IOException | TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return htmlText;
	}
	
	class MailSenderSync implements Runnable{
		
		private final MailSender mailSender;
		
		private final boolean isHtml;
		
		private final String toMailAddr,  subject, message;
		
		MailSenderSync(MailSender mailSender,String toMailAddr, String subject,String message,boolean isHtml){
			this.mailSender=mailSender;
			this.toMailAddr = toMailAddr;
			this.subject = subject;
			this.message = message;
			this.isHtml = isHtml;
		}
		@Override
		public void run() {
			mailSender.sendMail(toMailAddr, subject, message,isHtml);
		}
	}

	
}
