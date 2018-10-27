import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class main {

	//"C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls";

	
	
	
	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException{
		/*File excel = new File(FILE_PATH);
		FileInputStream fis = new FileInputStream(excel);
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		HSSFSheet ws = wb.getSheetAt(1);
		
		int rows = ws.getLastRowNum()+1;
		int col = ws.getRow(0).getLastCellNum();
		HSSFRow row =ws.getRow(0);
		HSSFCell cell = row.getCell(0);*/
		
		//String[][] data = new String[row][col];
		
		//System.out.println(cellToString(cell));
		
		excelInput input = new excelInput("C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls");
		//input.getMonthlyData(1);
		input.getouttageData(1);
		
	}

}
