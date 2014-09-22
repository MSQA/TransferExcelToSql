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



public class NormalTable {

	public static void readFileAndConvert(FileInputStream file) {
		try {
			String columns;

			// create workbook and sheet from excel file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// takes the first column in the spreadsheet as the key and
			// everything
			// after that in the same row as an array list of integers
			HashMap<Integer, ArrayList<String>> hash = new HashMap<Integer, ArrayList<String>>();

			Row firstRow = rowIterator.next();

			String columnNames = "";
			String tableName = firstRow.getCell(firstRow.getFirstCellNum())
					.getStringCellValue();
			firstRow.removeCell(firstRow.getCell(firstRow.getFirstCellNum()));
			// get the column names from the first row of the excel spreadsheet
			for (Cell c : firstRow) {
				columnNames += c.getStringCellValue() + ",";
			}

			// concatenates the column names into one string
			columns = "(" + columnNames.substring(0, columnNames.length() - 1)
					+ ")";

			// iterates over each row
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// the first column in each row after the first stores the key
				// in our hash map
				int rowId = (int) row.getCell(row.getFirstCellNum())
						.getNumericCellValue();

				// if the key is already in our map dont override it
				if (!hash.containsKey(rowId))
					hash.put(rowId, new ArrayList<String>());

				// iterates over each cell in the row
				row.forEach(c -> {
					// if this cell isnt the first one in the row and its not
					// empty add it
					// to the hash map.
					if (c != row.getCell(row.getFirstCellNum())
							&& c.getCellType() != c.CELL_TYPE_BLANK) {
						if (c.getCellType() == c.CELL_TYPE_STRING) {
							// hash.get(rowId).add("'" +
							// StringEscapeUtils.escapeEcmaScript(c.getStringCellValue())
							// + "'");
							hash.get(rowId).add(
									"'"
											+ StringUtils.join(c
													.getStringCellValue()
													.split("'"), "''") + "'");
						} else if (c.getCellType() == c.CELL_TYPE_NUMERIC)
							hash.get(rowId).add(c.getNumericCellValue() + "");
					}
				});
			}
			// StringEscapeUtils.escapeSql(str)

			// TODO: Since database can contain string and numbers we have to
			// figure out how to tell what type of object (string or number)
			// each
			// object in the array list of objects is

			// iterates over each entry in the hash map and creates an insert
			// statement for it
			hash.forEach((key, list) -> {
				String values = "";
				for (String val : list) {
					values += val + ",";
				}
				values = values.substring(0, values.length() - 1);
				String queryStr = "INSERT INTO " + tableName + columns
						+ " VALUES (" + values + ")";
//				Query query = new Query(queryStr);
				System.out.println(queryStr);
			});
		} catch (Exception e) {
			System.out.println("error!!! " + e);
		}
	}
}
