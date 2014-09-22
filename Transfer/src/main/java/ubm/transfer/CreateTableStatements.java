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



public class CreateTableStatements {

	public static void readFileAndConvert(FileInputStream file) {
		try {
			// create workbook and sheet from excel file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// takes the first column in the spreadsheet as the key and
			// everything
			// after that in the same row as an array list of integers
			HashMap<String, ArrayList<String>> hash = new HashMap<String, ArrayList<String>>();
			
			// iterates over each row
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// the first column in each row after the first stores the key
				// in our hash map
				String tableName = row.getCell(row.getFirstCellNum()).getStringCellValue();
				row.removeCell(row.getCell(row.getFirstCellNum()));
				
				// if the key is already in our map dont override it
				if(!hash.containsKey(tableName))
				{
					hash.put(tableName, new ArrayList<String>());
				}

				
				// iterates over each cell in the row
				row.forEach(c -> {
					// if this cell isnt the first one in the row and its not
					// empty add it
					// to the hash map.
					if (c.getCellType() != c.CELL_TYPE_BLANK) {
							// hash.get(rowId).add("'" +
							// StringEscapeUtils.escapeEcmaScript(c.getStringCellValue())
							// + "'");
							hash.get(tableName).add(c + "");
					}
				});
				hash.get(tableName).add("\n");
			}

			// iterates over each entry in the hash map and creates an insert
			// statement for it
			hash.forEach((key, list) -> {
				System.out.println("CREATE TABLE " + key + "(");
				list.forEach(s -> {
					System.out.print(s + " ");
				});
				System.out.println(");");
			});
		} catch (Exception e) {
			System.out.println("error!!! " + e);
		}
	}
}
