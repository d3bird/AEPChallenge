import java.util.Date;

public class outageDetials {
	
	private String Customer_Key;
	private Date start;
	private Date end;
	private String durration;
	private String weather;
	
	public outageDetials() {
		
	}

	public String getCustomer_Key() {
		return Customer_Key;
	}

	public void setCustomer_Key(String customer_Key) {
		Customer_Key = customer_Key;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getDurration() {
		return durration;
	}

	public void setDurration(String durration) {
		this.durration = durration;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}
	
}
