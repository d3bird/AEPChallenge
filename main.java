import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


public class main {

	//"C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls";

	
	
	
	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException, AddressException, MessagingException{
		
		EmailCS emailer = new EmailCS();
		excelInput input = new excelInput("C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls");
		
		customer output =input.createOneCustomor("1.3095811E7");
		
		String to = "dogbird9@gmail.com";
		
		emailer.setTo(to);
		
	//	emailer.sendMessage();
		emailer.sendMessage3(output);
		//try {
		//	emailer.sendmessage2();
		//} catch (MessagingException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
	//	}
		
	}

}
