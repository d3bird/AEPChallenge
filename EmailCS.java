import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailCS {


	private String from;
	private String to;
	
	
	public EmailCS() {
		
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
	String getFeedback(customer in) {
		if(in.getFeedback().isEmpty()) {
			return"-1";
		}else {
			String output ="thank you for your feedback! We be looking into what you have to say.";
			return output;
		}	
	}
	
	String getMonthlymessage(customer in) {
		if(in.getMonthlydata().isEmpty()) {
			return"-1";
		}else {
			String output ="";
			String line1="";
			String line2="";
			String line3="";
			String line4="";
			String line5="";
			String line6="";
			Vector<MonthlyData> data = in.getMonthlydata();
			if(data.size()>=2){//more than one months
				
				String leastpayed =data.get(0).getBilledamount();//find the lowest bill payed 
				String test;
				for(int q=1;q<data.size();q++) {
					test=data.get(q).getBilledamount();
					if(Double.parseDouble(test)<Double.parseDouble(leastpayed)) {
						leastpayed = test;
					}
				}
				if(leastpayed.equals(data.get(data.size()-1).getBilledamount())) {
				line5 ="You paid the lowest bill to date last month";
				}
				
				double AdverageRate =0;
				double prev =0;
				double next =0;
				prev =Double.parseDouble(data.get(0).getBilledamount());
				for(int w=1;w<data.size();w++) {
					next =Double.parseDouble(data.get(w).getBilledamount());
					AdverageRate+=(next-prev);//negative = decrese in cost
					prev = next;
				}
				AdverageRate = AdverageRate/data.size();
				
				if(AdverageRate < 0) {
					line6="You payments have go going down steadly, on average "+ (AdverageRate*-1) +" per month";
				}
				line6="";

			
				String costPM =data.get(data.size()-1).getBilledamount();//last month
				String costLM=data.get(data.size()-2).getBilledamount();
				
				String adverage = data.get(data.size()-1).getAdverageCost();
				
				if(Double.parseDouble(adverage)>Double.parseDouble(costPM)) {
					line1 ="you payed less than the advage person, saving a total of "+(Double.parseDouble(adverage)-Double.parseDouble(costPM))+" dollars";
					
				}
				
				line2="last month your bill "+costPM+" and the month before that it was "+ costLM;// line 2
				if(Double.parseDouble(costPM)<Double.parseDouble(costLM)) {
					line3="congratulations you saved " +(Double.parseDouble(costLM)-Double.parseDouble(costPM))+ " dollars";
				}else {
					line3="It seems that you use more than the previous months, but all is not lost you can do it!";
					
				}
				
				boolean ontime =true;//line 3
				for(int i =0;i<data.size();i++) {
					if(data.get(i).getPaidedOnTime().equals("N")) {
						ontime = false;
					}
				}
				if(ontime) {
					line4 ="You have payed all of your bills on time! Keep up the good work!";
				}else {
					line4 ="You seem to have missed some payments, but we know that you can do better in the future!";
				}
				
			}else {// only one months worth of data
				String adverage =data.get(0).getAdverageCost();
				String billamount =data.get(0).getBilledamount();
				if(Double.parseDouble(adverage)>Double.parseDouble(billamount)){
					line1 ="you payed "+billamount+ "dollars, and the average person payed "+adverage;
					line2 ="you saved a total of "+(Double.parseDouble(adverage) -Double.parseDouble(billamount))+ " dollars";
				}else {
					line1="last month you bill was "+data.get(0).getBilledamount();
					line2="";
				}
				String payd = data.get(0).getPaidedOnTime();
				
				if(payd.equals("Y")) {
					line3 = "All of your bills have been paid on time.";
				}
				
			}
			
			
			output = String.join(System.getProperty("line.separator"),
								line1,//bill vs average person
								line2,//bill vslast month
								line3,//bills payed on time
								line4,//bills payed on time
								line5,//lowest pill yet
								line6//average 
								);
								
			return output;
		}	
		
	}
	
	
	String getouttageStuff(customer in) {
		String output="";
		String line1 ="";
		Vector<outageDetials> outageData =in.getOutagedetails();
		Vector<poleImprovments> poleData =in.getPoleimprov();
		
		if(outageData.isEmpty()) {
			if(poleData.isEmpty()) {
				line1 ="We have no information at this moment about the power poles in your area, but we will keep you up to date if something changes";
			}else {
				double improvments =0;
				String endY;
				String startY = poleData.get(0).getYear();
				endY = startY;
				for (int i =1; i <poleData.size();i++) {
					endY=poleData.get(i).getYear();
					improvments+= Double.parseDouble(poleData.get(i).getImprovments());
				}
				line1="we are happy to report that there was no outages and we have preformed " +improvments+" improvements between "+startY+ " and "+endY;
			}
			
		}else {
			if(poleData.isEmpty()) {
				line1 ="We have experienced a couple of power outages over the past couple months; however, we are working very hard to fix these problems.";
			}else {
				double outTime =0;
				double improvments =0;
				for(int i =0;i<outageData.size();i++) {
					outTime += Double.parseDouble(outageData.get(i).getDurration());
				}
				for(int i=0;i<poleData.size();i++) {
					improvments+= Double.parseDouble(poleData.get(i).getImprovments());
				}
				line1 ="Since you have been a customer, we have experienced "+outTime+" of powerouttages, but we have made "+improvments+ " improvements to differnt power poles to reduce outage time in the future.";
			}
		}
		output =line1;
		return output;
	}
	
	void sendMessage3(customer in) throws AddressException, MessagingException {
		
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		
		
		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("hackathonexample@gmail.com", "wafflebutters");
		    }
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("hackathonexample@gmail.com"));
		message.setRecipients(
		Message.RecipientType.TO, InternetAddress.parse("hackathonexample@gmail.com"));
		message.setSubject("Update Message From AEP");
		
		String hello="Hello "+in.getcustomorid()+",";
		
		String monthly = getMonthlymessage(in);
		String outages =getouttageStuff(in);
		String feed =getFeedback(in);
		if(feed.equals("-1")) {
			feed="";
		}
		
		String goodby="Have a nice day."; 
		//System.out.println(monthly);
		//System.out.println(feed);
		System.out.println();
		String msg = hello+"<br>"+"<br>"+monthly+"<br>"+"<br>"+outages+"<br>"+"<br>"+feed+"<br>"+goodby;
		System.out.println(msg);
 
		
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");
		 
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		 
		message.setContent(multipart);
		 
		Transport.send(message);
		System.out.println("messgae sent");
	} 
		
}
