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



public class AlterTableStatements {

	public static void readFileAndConvert(FileInputStream file) {
		try {
			// create workbook and sheet from excel file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// takes the first column in the spreadsheet as the key and
			// everything
			// after that in the same row as an array list of integers
			HashMap<String, String> tables = new HashMap<String, String>();
			HashMap<String, HashMap<String, String>> columns = new HashMap<String, HashMap<String, String>>();

			// iterates over each row
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// the first column in each row after the first stores the key
				// in our hash map
				String firstCellVal = row.getCell(row.getFirstCellNum()).getStringCellValue();
				
				if(firstCellVal.equalsIgnoreCase("table"))
					tables.put(row.getCell(row.getFirstCellNum() + 1).getStringCellValue(), row.getCell(row.getFirstCellNum() + 2).getStringCellValue());
				else
				{
					Cell cell = row.getCell(row.getFirstCellNum());
					String tableName = cell.getStringCellValue();
					row.removeCell(cell);

					cell = row.getCell(row.getFirstCellNum());
					String oldName = cell.getStringCellValue();
					row.removeCell(cell);
					
					cell = row.getCell(row.getFirstCellNum());
					String newName = cell.getStringCellValue();
					row.removeCell(cell);
					
					if(!columns.containsKey(tableName))
					{
						columns.put(tableName, new HashMap<String, String>());
					}
					
					columns.get(tableName).put(oldName, newName);
				}
			}
			
			tables.forEach((oldTableName, newTableName) -> {
				System.out.println("ALTER TABLE " + oldTableName + " RENAME TO " + newTableName + ";");
			});
			
			columns.forEach((newTableName, hash) -> {
				hash.forEach((oldColumnName, newColumnName) -> {
					System.out.println("ALTER TABLE " + newTableName + " RENAME COLUMN " + oldColumnName + " TO " + newColumnName + ";");
				});
			});
			
		} catch (Exception e) {
			System.out.println("error!!! " + e);
		}
	}
}
