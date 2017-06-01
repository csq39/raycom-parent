package io.raycom.common.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.raycom.system.framework.collection.RMultiData;
import jxl.read.biff.BiffException;

/**
 * 处理excel的相关函数
 * 
 * @author shijinglonng
 * @version 1.0
 */
public class ExcelUtil {

	final public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 获取表单中的title 行poi对象
	 * @param sheet
	 * @param titleRowIndex
	 * @return
	 */
	public static HSSFRow getSheetTitleRow(HSSFSheet sheet, int titleRowIndex) {
		return sheet.getRow(titleRowIndex);
	}

	/**
	 * 判断表单中一行是否为空
	 * 如果为空就不用解析, 用于排除excel中空白行
	 * @param row
	 * @return
	 */
	public static boolean isEmptyRow(HSSFRow row) {
		boolean result = true;
		if (row == null) {
			result = true;
		} else {
			for (short i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
				HSSFCell cell = row.getCell(i);
				result &= isEmptyCell(cell);
				if (!result) {
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param file文件
	 * @return RMultiData 从第二行开始，一次加入数据中，key值为序号，从0开始
	 * @throws IOException 
	 * @throws BiffException 
	 */
	public static RMultiData getExcelData(File file) throws IOException {
		RMultiData excelData=new RMultiData();
		if(!file.isFile()) return excelData;
		String fileName = file.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		
		InputStream stream = new FileInputStream(file);
		Workbook wb = null;
		
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		Sheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				if(row.getRowNum()>0)
					excelData.add(cell.getColumnIndex()+"", getCellData(cell));
			}
		}
		return excelData;
		
	}
	public static Object getCellData(Cell cell) {
		if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
			return cell.getStringCellValue();
		}
		if(cell.getCellType()==Cell.CELL_TYPE_BOOLEAN){
			return cell.getBooleanCellValue();
		}
		if(cell.getCellType()==Cell.CELL_TYPE_ERROR){
			return cell.getErrorCellValue();
		}
		if(cell.getCellType()==Cell.CELL_TYPE_FORMULA){
			return cell.getCellFormula();
		}
		if(cell.getCellType()==Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
			return cell.getNumericCellValue();
		}
		return "";
		
	}

	/**
	 * 判断是否是空单元格
	 * @param cell
	 * @return
	 */
	public static boolean isEmptyCell(HSSFCell cell) {
		String cellStr = null;
		cellStr = cell2string(cell, null);
		return cellStr == null || cellStr.trim().equals("");
	}

	/**
	 * 对单元格的数据转换成字符串, 以便后面统一处理
	 * 
	 * @param cell
	 * @return
	 */
	public static String cell2string(HSSFCell cell, HSSFFormulaEvaluator evaluator) {
		if (cell == null) {
			return null;
		}
		String str = null;
		final int cellType = cell.getCellType();

		switch (cellType) {
		case HSSFCell.CELL_TYPE_STRING:
			str = "" + cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				str = "" + dateFormat.format(cell.getDateCellValue());
			} else {
				String numberStr = String.valueOf(cell.getNumericCellValue());

				//excel中数值转换过来时采用E计数法, 导致后面的规则校验失败;
				//为此进行把E计数法转换普通计数法, 并且使用小数点后15位, (15位是小数的最大精度, 能够保证非本组件带来的误差)
				// 使用15位小数后, 会带来多个末尾的0, 相邻的一条语句是去掉这些末尾0
				str = "" + new BigDecimal(numberStr).setScale(15, BigDecimal.ROUND_HALF_UP);

				if (str.indexOf('.') != -1) {
					str = str.replaceAll("(\\.)?0*$", "");
				}
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			str = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			str = "" + cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			str = "" + cell.getErrorCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			if (evaluator == null) {
				str = "" + cell.getRichStringCellValue().getString();
			} else {
				str = "" + evaluator.evaluate(cell).getNumberValue();
			}

			break;
		}

		return (str == null || str.trim().equals("")) ? null : str.trim();
	}

	/**
	 * 获取excel中的方程
	 * @param sheet
	 * @param workbook
	 * @param row
	 * @return
	 */
	public static HSSFFormulaEvaluator getFormulaEvaluator(HSSFSheet sheet, HSSFWorkbook workbook, HSSFRow row) {
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(sheet, workbook);
		evaluator.setCurrentRow(row);
		return evaluator;
	}
}
