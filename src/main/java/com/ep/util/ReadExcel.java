package com.ep.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ReadExcel {

	/**
	 * 读取Excel表格 返回List<Map<列名,值>> 列名: qaActionName qaActionName qaKeywords
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<Map<String, String>> getExcelDate(MultipartFile file) {
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;
		List<Map<String, String>> list = null;
		String cellData = null;
		//filePath = "C:\\Users\\cpt\\Desktop\\test.xlsx";
		String columns[] = {"epcid1","epcid1url", "epcid2","epcid2url", "epcid3","epcid3url",  "qaKnowledge", "qaQuestion", "qaKeywords", "qaResource",  "qaAnswer" };
		wb = readExcel(file);
		if (wb != null) {
			// 用来存放表中数据
			list = new ArrayList<Map<String, String>>();
			// 获取第一个sheet
			sheet = wb.getSheetAt(0);
			// 获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			// 获取第一行 第一行为标题
			row = sheet.getRow(0);
			// 获取最大列数
			int colnum = row.getPhysicalNumberOfCells();
			for (int i = 0; i < rownum; i++) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				row = sheet.getRow(i);
				if (row != null) {
					//boolean flag = false;
					for (int j = 0; j < columns.length; j++) {
						cellData = (String) getCellFormatValue(row.getCell(j));
						map.put(columns[j], cellData);
					}
					//if(flag) break;
				} else {
					break;
				}
				list.add(map);
			}
			return list;

		}
		return null;

	}

	// 读取excel
	public static Workbook readExcel(MultipartFile file) {
		if (file == null) {
			return null;
		}
		Workbook wb = null;
		String fileName = file.getOriginalFilename();
		String extString = fileName.substring(fileName.lastIndexOf("."));
		InputStream is = null;
		try {
			is = file.getInputStream();
			if (".xls".equals(extString) || ".csv".equals(extString)) {
				return wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(extString)) {
				return wb = new XSSFWorkbook(is);
			} else {
				return null;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return wb;
	}

	public static Object getCellFormatValue(Cell cell) {
		Object cellValue = null;
		if (cell != null) {
			// 判断cell类型
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: {
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			}
			case Cell.CELL_TYPE_FORMULA: {
				// 判断cell是否为日期格式
				if (DateUtil.isCellDateFormatted(cell)) {
					// 转换为日期格式YYYY-mm-dd
					cellValue = cell.getDateCellValue();
				} else {
					// 数字
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			case Cell.CELL_TYPE_STRING: {
				cellValue = cell.getRichStringCellValue().getString();
				break;
			}
			default:
				cellValue = "";
			}
		} else {
			cellValue = "";
		}
		return cellValue;
	}
}
