package io.raycom.common.ireport;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.raycom.system.framework.collection.RMultiData;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sourceforge.barbecue.env.Environment;
import net.sourceforge.barbecue.env.EnvironmentFactory;

/**
 * 
 * @ClassName: PdfExportUtil 
 * @Description: TODO
 *		pdf导出工具类，通过调用PdfExportPrepare()输出pdf文件，其中mData中有一个key值为"PDFCONTENTDATA"的RMultiData用来存放pdf数据
 * @author caoshengquan csq39@126.com 
 * @date 2011-5-13 下午11:57:01 
 *
 */
public class PdfExportUtil {
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static String PDF_CONTENT = "PDFCONTENTDATA";

	private static final String CONTENTTYPE = "application/octet-stream";

	/**
	 * @Description: TODO
	 * 	该工具类的调用方法是 PdfExportUtil.PdfExportPrepare();
	 * 	注意传入的mData的格式以及url的路径。
	 *  pdf导出的页面部分请用弹出页的方式访问路径。
	 *  action中相关方法请returne null;
	 * @param request 当前的请求
	 * @param response 当前的response
	 * @param context 当前请求的上下文环境
	 * @param mData pdf的数据
	 * @param url   jasper文件的相对webcontent的路径
	 * @throws IOException
	 * @throws ServletException     
	 * @throws
	 */
	public static void PdfExportPrepare(HttpServletRequest request, HttpServletResponse response,
			 RMultiData mData, String url) throws IOException, ServletException {

		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

		if (url != null && !mData.isEmpty()) {
			
			url = request.getSession().getServletContext().getRealPath(url);
			for (int i = 0; i < mData.getDataCount(PDF_CONTENT); i++) {
				JasperPrint jasperPrint = null;
				try {
					File reportFile = new File(url);
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
					HashMapDataSource hd = new HashMapDataSource((RMultiData) mData.get(PDF_CONTENT, i));
					JRDataSource dataSource = hd.createReportDataSource();

					jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
				} catch (JRException e) {
					e.printStackTrace();
				}
				if (jasperPrint == null) {
					throw new ServletException("No JasperPrint documents found on the HTTP session.");
				}
				jasperPrintList.add(jasperPrint);
			}
		}

		exportPdf(jasperPrintList, request, response);
	}

	public static void PdfExportPreparetoApplet(HttpServletRequest request, HttpServletResponse response,
			RMultiData mData, String url) throws IOException, ServletException {
		response.setContentType(CONTENTTYPE);
		ServletOutputStream ouputStream = response.getOutputStream();

		//		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		JasperPrint jasperPrintList = new JasperPrint();

		if (url != null && !mData.isEmpty()) {
			url = request.getSession().getServletContext().getRealPath(url);
			for (int i = 0; i < mData.getDataCount(PDF_CONTENT); i++) {
				JasperPrint jasperPrint = null;
				try {
					File reportFile = new File(url);
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
					HashMapDataSource hd = new HashMapDataSource((RMultiData) mData.get(PDF_CONTENT, i));
					JRDataSource dataSource = hd.createReportDataSource();

					jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);

				} catch (JRException e) {
					e.printStackTrace();
				}
				if (jasperPrint == null) {
					throw new ServletException("No JasperPrint documents found on the HTTP session.");
				}
				jasperPrintList = jasperPrint;
			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
		oos.writeObject(jasperPrintList);
		oos.flush();
		oos.close();
		//exportPdf(jasperPrintList, request, response);
	}

	public static void PdfExportPrepare2(HttpServletRequest request, HttpServletResponse response,
			ServletContext context, RMultiData mData, String url) throws IOException, ServletException {

		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

		if (url != null && !mData.isEmpty()) {
			url = context.getRealPath(url);
			for (int i = 0; i < mData.getDataCount(PDF_CONTENT); i++) {
				JasperPrint jasperPrint = null;
				try {
					File reportFile = new File(url);
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
					HashMapDataSource hd = new HashMapDataSource((RMultiData) mData.get(PDF_CONTENT, i));
					JRDataSource dataSource = hd.createReportDataSource();
					jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
				} catch (JRException e) {
					e.printStackTrace();
				}
				if (jasperPrint == null) {
					throw new ServletException("No JasperPrint documents found on the HTTP session.");
				}
				try {
					//					JasperPrintManager.printReporByPrintNamet("192.168.0.82", jasperPrint, false);
					JasperPrintManager.printReport(jasperPrint, false);
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//jasperPrintList.add(jasperPrint);
			}
		}

		//exportPdf(jasperPrintList, request, response);
	}

	protected static void exportPdf(List<JasperPrint> jasperPrintList, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		Environment env=new Environment() {
			
			public final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);

			public int getResolution() {
				return 360;
			}

			public Font getDefaultFont() {
			   System.out.println("get default font.......");
		       return DEFAULT_FONT;
			}
		};
		 
		EnvironmentFactory.setDefaultEnvironment(env);

		Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
		if (isBuffered.booleanValue()) {
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
			exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
			try {
				exporter.exportReport();
				fbos.close();

				if (fbos.size() > 0) {
					response.setContentType("application/pdf");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();

					try {
						fbos.writeData(ouputStream);
						fbos.dispose();
						ouputStream.flush();
					} finally {
						if (ouputStream != null) {
							try {
								ouputStream.close();
							} catch (IOException ex) {
							}
						}
					}
				}
			} catch (JRException e) {
				throw new ServletException(e);
			} finally {
				fbos.close();
				fbos.dispose();
			}

		} else {
			response.setContentType("application/pdf");

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);

			OutputStream ouputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

			try {
				exporter.exportReport();
			} catch (JRException e) {
				throw new ServletException(e);
			} finally {
				if (ouputStream != null) {
					try {
						ouputStream.close();
					} catch (IOException ex) {
					}
				}
			}
		}

	}

	/**
	 * @Description: TODO
	 * 	jasper保存为PDF并打印。
	 * @param printerName   打印机名
	 * @throws IOException
	 * @throws ServletException     
	 * @throws
	 */
	public static void PdfExportFile(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			RMultiData mData, String url, String printerName) throws IOException, ServletException {

		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

		if (url != null && !mData.isEmpty()) {
			url = context.getRealPath(url);
			for (int i = 0; i < mData.getDataCount(PDF_CONTENT); i++) {
				JasperPrint jasperPrint = null;
				try {
					File reportFile = new File(url);
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
					HashMapDataSource hd = new HashMapDataSource((RMultiData) mData.get(PDF_CONTENT, i));
					JRDataSource dataSource = hd.createReportDataSource();

					jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
				} catch (JRException e) {
					e.printStackTrace();
				}
				if (jasperPrint == null) {
					throw new ServletException("No JasperPrint documents found on the HTTP session.");
				}
				jasperPrintList.add(jasperPrint);
			}
		}

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String expFileName = "C://TEMP//jsper_" + sdf.format(calendar.getTime()) + ".pdf";
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, expFileName);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "gb2312");

		try {
			exporter.exportReport();
			//打印pdf
			//PrintFile.printPdf(PrintFile.getPrinter(printerName), expFileName);
		} catch (JRException e) {
			throw new ServletException(e);
		}

	}

}
