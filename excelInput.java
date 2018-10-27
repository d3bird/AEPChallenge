import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class excelInput {
	
	private String FILE_PATH;//"C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls";
	private HSSFWorkbook wb;
	

	private static String cellToString(HSSFCell in) {
		int type;
		Object Result = null;
		type = in.getCellType();
		switch(type) {
		case 0:
			Result =in.getNumericCellValue();
			break;
		case 1:
			Result = in.getStringCellValue();
			break;
		//in.get
		}
		return Result.toString(); 
	}
	
	public excelInput(String filepath){	 
		FILE_PATH = filepath;
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	private void getData() throws IOException {
		File excel = new File(FILE_PATH);
		FileInputStream fis = new FileInputStream(excel);
		 wb = new HSSFWorkbook(fis);
		//HSSFSheet ws = wb.getSheetAt(1);
		
		//int rows = ws.getLastRowNum()+1;
		//int col = ws.getRow(0).getLastCellNum();
	//	HSSFRow row =ws.getRow(0);
	//	HSSFCell cell = row.getCell(0);
	//	System.out.println(cellToString(cell));
	}
	
	public meterEvents getmeterEvents(int rownum) {
		HSSFSheet ws =wb.getSheet("MeterEvents");
		meterEvents me = new meterEvents();
		HSSFRow r = ws.getRow(rownum);
		
		String tstring;
		HSSFCell temp;
		
		for(int i =0;i<r.getLastCellNum();i++) {
			temp = r.getCell(i);
			tstring = cellToString(r.getCell(i));
			
			switch(i) {
			case 0:
				System.out.println(tstring);
				me.setCustormor_key(tstring);
				break;
			case 2:
				System.out.println(temp.getDateCellValue());
				me.setEventDate(temp.getDateCellValue());
				break;
			case 3:
				System.out.println(tstring);
				me.setReason(tstring);
				break;
			}
			
		}
		return me;
	}
	
	public poleImprovments getPoleImprovments(int rownum) {
		HSSFSheet ws =wb.getSheet("PoleTrnsfrmrImprovements");
		poleImprovments pi = new poleImprovments();
		HSSFRow r = ws.getRow(rownum);
		
		String tstring;
		HSSFCell temp;
		
		for(int i =0;i<r.getLastCellNum();i++) {
			temp = r.getCell(i);
			tstring = cellToString(r.getCell(i));
			//System.out.println(tstring);
			switch (i) {
			case 0:
				pi.setCustomor_Key(tstring);
				break;
			case 1:
				pi.setOperatingCompany(tstring);
				break;
			case 2:
				pi.setStatus(tstring);
				break;
			case 3:
				pi.setYear(tstring);
				break;
			case 4:
				pi.setMonth(tstring);
				break;
			case 5:
				pi.setImprovments(tstring);
				break;
			}
		}
		return pi;
	}
	
	public outageDetials getouttageData(int rownum) {
		HSSFSheet ws =wb.getSheet("OutageDetails");
		outageDetials od = new outageDetials();
		HSSFRow r = ws.getRow(rownum);
		
		String tstring;
		HSSFCell temp;
		
		for(int i =0;i<r.getLastCellNum();i++) {
			temp = r.getCell(i);
			tstring = cellToString(r.getCell(i));
			switch(i) {
			case 0:
				od.setCustomer_Key(tstring);
				System.out.println(tstring);
				break;
			case 1:
				od.setStart(temp.getDateCellValue());
				System.out.println(temp.getDateCellValue());

				break;
			case 2:
				od.setEnd(temp.getDateCellValue());
				System.out.println(temp.getDateCellValue());

				break;
			case 3:
				od.setDurration(tstring);
				System.out.println(tstring);

				break;
			case 4:
				od.setWeather(tstring);
				System.out.println(tstring);

				break;
			
			}
		}
		return od;
	}
	
	public MonthlyData getMonthlyData(int rownum) {
		HSSFSheet ws =wb.getSheet("MonthlyData");
		MonthlyData data = new MonthlyData();
		HSSFRow r = ws.getRow(rownum);
		//System.out.println(r.getCell(1).get);
		String tstring;
		HSSFCell temp;
		for(int i =0;i<r.getLastCellNum();i++) {
			temp = r.getCell(i);
			tstring = cellToString(r.getCell(i));
			System.out.println(tstring);
			switch(i) {
			case 0:
				data.setCustomer_Key(tstring);
				break;
			case 1:
				//System.out.println("date");
				Date d =temp.getDateCellValue();
				//String t =d.toString();
				//System.out.println(t);
				data.setDate(temp.getDateCellValue());
				break;
			case 2:
				//System.out.println("zip");
				data.setZip(tstring);
				break;
			case 3:
				data.setAdverageCost(tstring);
				break;
			case 4:
				data.setAdverageUse(tstring);
				break;
			case 5:
				data.setBilledamount(tstring);
				break;
			case 6:
				data.setBillusage(tstring);
				break;
			case 7:
				data.setPaidedOnTime(tstring);
				break;
			case 8:
				data.setTotoalheatingdays(tstring);
				break;
			case 9:
				data.setTotoalheatingdays(tstring);
				break;
			case 10:
				data.setMintemp(tstring);
				break;
			case 11:
				data.setMaxtemp(tstring);
				break;
			case 12:
				data.setServicecalls(tstring);
				break;
			case 13:
				data.setWeblogins(tstring);
				break;
			case 14:
				data.setOutages(tstring);
				break;
			case 15:
				data.setHomewarrenty(tstring);
				break;
			case 16:
				data.setPaperless(tstring);
				break;
			case 17:
				data.setAlerts(tstring);
				break;
			case 18:
				data.setOAlerts(tstring);
				break;
				
			}
			}
		
	return null;
	}
}
