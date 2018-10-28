import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailCS {

    static final String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Amazon SES SMTP Email Test</h1>",
    	    "<p>This email was sent with Amazon SES using the ", 
    	    "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
    	    " for <a href='https://www.java.com'>Java</a>."
    	);

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
			String line1;
			String line2;
			Vector<MonthlyData> data = in.getMonthlydata();
			if(data.size()>=2){
				String costPM =data.get(data.size()-1).getBilledamount();
				String costLM=data.get(data.size()-2).getAdverageCost();
				line1="last month your bill "+costPM+" and the month before that it was "+ costLM;
				if(Double.parseDouble(costPM)<Double.parseDouble(costLM)) {
					line2="congratulations you saved " +(Integer.parseInt(costLM)-Integer.parseInt(costPM))+ " dollars";
				}else {
					line2="It seems that you use more than the previous months";
					
				}
				
			}else {
				line1="last month you bill was "+data.get(0).getAdverageCost();
				line2="";
			}
			output = String.join(System.getProperty("line.separator"),
								line1,
								line2);
								
			return output;
		}	
		
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
		 
		String monthly = getMonthlymessage(in);
		String feed =getFeedback(in);
		System.out.println(feed);
		System.out.println(monthly);

		System.out.println();
		String msg = "AEP anual update";
		 
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");
		 
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		 
		message.setContent(multipart);
		 
		Transport.send(message);
		System.out.println("messgae sent");
	} 
		
		
	
	
	
}
