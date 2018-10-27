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
		
		
		excelInput input = new excelInput("C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls");
		//input.getMonthlyData(1);
		//input.getouttageData(1);
		//input.getmeterEvents(1);
		//input.getPoleImprovments(1);
		input.getFeedback(1);
		
		
	}

}
