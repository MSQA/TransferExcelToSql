package ubm.transfer;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ManyToMany {

	public static void readFileAndConvert(FileInputStream file, String tableName) {
		try {
			String columns;
			String type = "Occurrence";

			// create workbook and sheet from excel file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// takes the first column in the spreadsheet as the key and
			// everything
			// after that in the same row as an array list of integers
			HashMap<Integer, ArrayList<Integer>> hash = new HashMap<Integer, ArrayList<Integer>>();

			Row firstRow = rowIterator.next();

			String columnNames = "";
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
					hash.put(rowId, new ArrayList<Integer>());

				// iterates over each cell in the row
				row.forEach(c -> {
					// if this cell isnt the first one in the row and its not
					// empty add it
					// to the hash map.
					if (c != row.getCell(row.getFirstCellNum())
							&& c.getCellType() != c.CELL_TYPE_BLANK)
						hash.get(rowId).add((int) c.getNumericCellValue());
				});
			}

			// iterates over each entry in the hash map
			hash.forEach((key, list) -> {
				// for each integer in the array list print out the correct
				// insert
				// statement
				list.forEach(s -> {
					System.out.println("INSERT INTO " + tableName + columns
							+ " VALUES (" + key + ", " + s + ", '" + type
							+ "');");
				});
			});
		} catch (Exception e) {
			System.out.println("error!!! " + e);
		}
	}
}
