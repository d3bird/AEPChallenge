import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class excelInput {
	
	private TreeSet<customer> storage;
	
	private String FILE_PATH;
	private HSSFWorkbook wb;
	

	private static String cellToString(HSSFCell in) {
		int type;
		Object Result = null;
		if (in ==null || in.getCellType() ==in.CELL_TYPE_BLANK) {
			return "";
		}else {
		type = in.getCellType();
		
		switch(type) {
		case 0:
			Result =in.getNumericCellValue();
			break;
		case 1:
			Result = in.getStringCellValue();
			break;
		
		}
		}
		//System.out.println(Result.toString());
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
		storage = new TreeSet<customer>();
		File excel = new File(FILE_PATH);
		FileInputStream fis = new FileInputStream(new File(FILE_PATH));
		 wb = new HSSFWorkbook(fis);
	}
	
	public FeedBack getFeedback(int rownum) {
		HSSFSheet ws =wb.getSheet("SurveyData");
		FeedBack fb = new FeedBack();
		HSSFRow r = ws.getRow(rownum);
		
		String tstring;
		HSSFCell temp;
		
		for(int i =0;i<r.getLastCellNum();i++) {
			temp = r.getCell(i);
			tstring = cellToString(r.getCell(i));
			System.out.println(tstring);
			switch (i) {
			case 0:
				fb.setCustomor_key(tstring);
				break;
			case 1:
				fb.setFeedback(tstring);
				break;
			}
		}
		return fb;
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
				//System.out.println(tstring);
				me.setCustormor_key(tstring);
				break;
			case 2:
				//System.out.println(temp.getDateCellValue());
				me.setEventDate(temp.getDateCellValue());
				break;
			case 3:
			//	System.out.println(tstring);
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
				//System.out.println(tstring);
				break;
			case 1:
				od.setStart(temp.getDateCellValue());
				System.out.println(temp.getDateCellValue());

				break;
			case 2:
				od.setEnd(temp.getDateCellValue());
				//System.out.println(temp.getDateCellValue());

				break;
			case 3:
				od.setDurration(tstring);
				//System.out.println(tstring);

				break;
			case 4:
				od.setWeather(tstring);
				//System.out.println(tstring);

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
			//System.out.println(tstring);
			switch(i) {
			case 0:
				data.setCustomer_Key(tstring);
				//System.out.println(data.getCustomer_Key());
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
		
	return data;
	}

	
	String getfirstMonth(int n) {
		HSSFSheet ws =wb.getSheet("MonthlyData");
		HSSFRow r = ws.getRow(n);
		HSSFCell c = r.getCell(0);
		return cellToString(c);
		
	}
	String getfirstFeed(int n) {
		HSSFSheet ws =wb.getSheet("SurveyData");
		HSSFRow r = ws.getRow(n);
		HSSFCell c = r.getCell(0);
		return cellToString(c);
	}
	String getfirstMeter(int n) {
		HSSFSheet ws =wb.getSheet("MeterEvents");
		HSSFRow r = ws.getRow(n);
		HSSFCell c = r.getCell(0);
		return cellToString(c);
		}
	String getfirstPole(int n) {
		HSSFSheet ws =wb.getSheet("PoleTrnsfrmrImprovements");
		HSSFRow r = ws.getRow(n);
		HSSFCell c = r.getCell(0);
		return cellToString(c);
	}
	String getfirstout(int n) {
		HSSFSheet ws =wb.getSheet("OutageDetails");
		HSSFRow r = ws.getRow(n);
		HSSFCell c = r.getCell(0);
		return cellToString(c);
	}
	
	public void printStorage() {
		 Iterator value = storage.iterator(); 
		 for(int i =0; i<storage.size();i++) {
			 System.out.println(value.next());
		 }
		
	}
	
	customer createOneCustomor(String targ) {
		customer output = new customer();
		
		//find the  monthly information
		output.setcustomorid(targ);
		HSSFSheet ws =wb.getSheet("MonthlyData");
		MonthlyData md;
		int col = ws.getLastRowNum();
		int i =1;
		String test;
		boolean found =false;
		while (i<col) {
			test =getfirstMonth(i);
			//System.out.println(test);
			if(targ.equals(test)) {
				//System.out.println("found one");
				found =true;
				md = getMonthlyData(i);
				//System.out.println("add " +i);
				output.addMonthlydata(md);
			}else {
				if (found) {
					//System.out.println("found last one");
					break;
				}
			}
			i++;
		}
		if(found ==false) {
			//System.out.println("did not find target in the monthly data");
		}
		i = 1;
		found =false;
		//System.out.println();
		//System.out.println("looking in feedback");
		//finds any survay information
		ws =wb.getSheet("SurveyData");
		FeedBack fb = new FeedBack();
		col =ws.getLastRowNum();
		found =false;
		while (i<col) {
			test= getfirstFeed(i);
			//System.out.println(test);
			if(targ.equals(test)) {
				//System.out.println("found one");
				found =true;
				fb = getFeedback(i);
				//System.out.println("add " +i);
				output.addFeedback(fb);
			}else {
				if (found) {
				//	System.out.println("found last one");
					break;
				}
			}
			i++;			
		}
		if(found ==false) {
			//System.out.println("did not find target in the feedback data");
		}
		i=1;
		found = false;
	//	System.out.println();
	//	System.out.println("looking in meter");
		//finds any information in the meter problems
		ws =wb.getSheet("MeterEvents");
		meterEvents me = new meterEvents();
		col =ws.getLastRowNum();
		while (i<col) {
			test= getfirstMeter(i);
			//System.out.println(test);
			if(targ.equals(test)) {
			//	System.out.println("found one");
				found =true;
				me = getmeterEvents(i);
				//System.out.println("add " +i);
				output.addMeterevents(me);
			}else {
				if (found) {
				//	System.out.println("found last one");
					break;
				}
			}
			i++;			
		}
		if(found ==false) {
			//System.out.println("did not find target in the meterEvents data");
		}
	//	System.out.println();
	//	System.out.println("looking in poleimprovments");
		ws =wb.getSheet("PoleTrnsfrmrImprovements");
		poleImprovments pi = new poleImprovments();
		col =ws.getLastRowNum();
		found =false;
		i=1;
		while (i<col) {
			test= getfirstPole(i);
			//System.out.println(test);
			if(targ.equals(test)) {
				//System.out.println("found one");
				found =true;
				pi = getPoleImprovments(i);
				//System.out.println("add " +i);
				output.addPoleimprov(pi);
			}else {
				if (found) {
					//System.out.println("found last one");
					break;
				}
			}
			i++;			
		}
		if(found ==false) {
			//System.out.println("did not find target in the poleimprovments data");
		}
		
		
		ws =wb.getSheet("OutageDetails");
		outageDetials od = new outageDetials();
		col =ws.getLastRowNum();
		i =1;
		while (i<col) {
			test= getfirstout(i);
			//System.out.println(test);
			if(targ.equals(test)) {
				//System.out.println("found one");
				found =true;
				od = getouttageData(i);
				//System.out.println("add " +i);
				output.addOutagedetails(od);
			}else {
				if (found) {
					//System.out.println("found last one");
					break;
				}
			}
			i++;			
		}
		if(found ==false) {
			//System.out.println("did not find target in the outage details data");
		}
		return output;
	}
	
}
