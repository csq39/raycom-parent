package io.raycom.common.utils.excel;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import io.raycom.system.framework.collection.RData;
import io.raycom.system.framework.collection.RMultiData;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportForExcel {

	public static void buildExcel(OutputStream os, RMultiData result, ArrayList columnName, Map headerNameMap) {

		WritableWorkbook wwb = null;
		int j = 0;
		int[] maxleng = new int[70];

		for (int temp = 0; temp < 70; temp++)
			maxleng[temp] = 0;

		Iterator itr = columnName.iterator();

		while (itr.hasNext()) {
			String colNm = (String) itr.next();
			for (int i = 0; i < result.getMaxDataCount(); i++) {
				if (result.getString(colNm, i) == null)
					result.modifyString(colNm, i, "");
				if (maxleng[j] < result.getString(colNm, i).length())
					maxleng[j] = result.getString(colNm, i).length();
			}
			maxleng[j] = (int) Math.round(maxleng[j] * 1.5);
			j++;
		}

		try {
			wwb = Workbook.createWorkbook(os);
			WritableSheet ws = wwb.createSheet("ST_SN_TMP", 0);

			j = 0;
			Iterator itr1 = columnName.iterator();
			while (itr1.hasNext()) {
				String colNm = (String) itr1.next();
				if (headerNameMap.containsKey(colNm)) {
					String header = (String) headerNameMap.get(colNm);
					WritableFont wfc = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

					WritableCellFormat wcfFC = new WritableCellFormat(wfc);
					ws.setColumnView(0, 15);

					Label labelC = new Label(j, 0, header, wcfFC);
					if (maxleng[j] < header.length())
						maxleng[j] = header.length();
					ws.setColumnView(j, maxleng[j] + 3);
					ws.addCell(labelC);
					j++;
				}
			}

			for (int i = 0; i < result.getMaxDataCount(); i++) {
				RData data = result.getRData(i);
				j = 0;
				Iterator itr2 = columnName.iterator();
				while (itr2.hasNext()) {
					String colNm = (String) itr2.next();
					Label labelCF = new Label(j, i + 1, data.getString(colNm));
					ws.addCell(labelCF);
					j++;
				}
			}

			wwb.write();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				wwb.close();
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void buildExceltoFile( OutputStream os,ExcelExportor exportor)  {

		RMultiData result = exportor.getM_result();
        ArrayList columnName = exportor.getM_columnName();
        Map headerNameMap = exportor.getM_headerNameMap();
        
        ExportForExcel.buildExcel(os,result,columnName,headerNameMap);
	}
	

}