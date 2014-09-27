package ubm.transfer;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class UpdateTableStatements {

	public static void readFileAndConvert(FileInputStream file) {
		try {
			// create workbook and sheet from excel file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();
			
			Row firstRow = rowIterator.next();
			String tableName = firstRow.getCell(firstRow.getFirstCellNum()).getStringCellValue();
			String column = firstRow.getCell(firstRow.getFirstCellNum() + 1).getStringCellValue();
			String newValColumn = firstRow.getCell(firstRow.getFirstCellNum() + 2).getStringCellValue();
			String newValColumn2 = firstRow.getCell(firstRow.getFirstCellNum() + 3).getStringCellValue();

			// iterates over each row
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// the first column in each row after the first stores the key
				// in our hash map
				
				String firstCellVal = row.getCell(row.getFirstCellNum()).getStringCellValue();
				String secondCellVal = row.getCell(row.getFirstCellNum() + 1).getStringCellValue();
				String thirdCellVal = row.getCell(row.getFirstCellNum() + 2).getStringCellValue();
				
				System.out.println("UPDATE " + tableName + " SET " + newValColumn + "='" + secondCellVal + ", " + 
						newValColumn2 + "='" + thirdCellVal + "' WHERE " + column + "=" + firstCellVal + ";");
			}
		} catch (Exception e) {
			System.out.println("error!!! " + e);
		}
	}
}
