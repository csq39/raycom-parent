package io.raycom.common.ireport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.util.JRLoader;

public class JRPrinterApplet extends JApplet {
	/**   
	*   
	*/
	//	private URL url = null;

	/*	private String printName = null;
		private String strUrl = null;*/
	private boolean iSRunFinished = false;

	/** Creates new form AppletViewer */
	public JRPrinterApplet() {

	}

	/**   
	*   
	*/
	public void init() {

	}

	public URL doFirst(String strUrl) {
		URL url = null;
		if (strUrl != null) {
			try {
				url = new URL(getCodeBase(), strUrl);
			} catch (Exception e) {
				StringWriter swriter = new StringWriter();
				PrintWriter pwriter = new PrintWriter(swriter);
				e.printStackTrace(pwriter);
				JOptionPane.showMessageDialog(this, swriter.toString());
			}
		} else {
			JOptionPane.showMessageDialog(this, "Source URL not specified");
		}
		return url;
	}

	public void start() {

	}

	public void printDO(String strUrl, String printName) {
		JasperPrint jasperPrint = null;
		URL url = doFirst(strUrl);
		if (url != null) {
			try {
				jasperPrint = (JasperPrint) JRLoader.loadObject(url);
			} catch (Exception e) {
				StringWriter swriter = new StringWriter();
				PrintWriter pwriter = new PrintWriter(swriter);
				e.printStackTrace(pwriter);
				JOptionPane.showMessageDialog(this, swriter.toString());
			}

			if (jasperPrint != null) {
				final JasperPrint print = jasperPrint;
				final String printServiceName = printName;

				Thread thread = new Thread(new Runnable() {
					public void run() {
						try {
							if (null == printServiceName)
								JasperPrintManager.printReport(print, false);
							else
								JasperPrintManager.printReporByPrintNamet(printServiceName, print, false);
						} catch (Exception e) {
							StringWriter swriter = new StringWriter();
							PrintWriter pwriter = new PrintWriter(swriter);
							e.printStackTrace(pwriter);
							JOptionPane.showMessageDialog(null, swriter.toString());
						}
					}
				});

				thread.start();
			} else {
				JOptionPane.showMessageDialog(this, "Empty report.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Source URL not specified");
		}
	}

}
