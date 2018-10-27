
public class customer {
	private String customorid;
	
	private MonthlyData monthlydata;
	private outageDetials outagedetails;
	private meterEvents meterevents;
	private poleImprovments poleimprov;
	private FeedBack feedback;
	
	public customer(String n) {
		customorid =n;
	}
	
	boolean isEqual(customer two) {
		if(customorid.equals(two.getcustomorid())) {
			return true;
		}else {
			return false;
		}
	}
	
	boolean isLessThan(customer two) {
		int c1 = Integer.parseInt(customorid);
		int c2 = Integer.parseInt(two.getcustomorid()); 	
		 if(c1<c2) {
			 return true;
		 }else {
			 return false;
		 }
	}
	
	String getcustomorid() {
		return customorid;
	}
	public MonthlyData getMonthlydata() {
		return monthlydata;
	}
	public void setMonthlydata(MonthlyData monthlydata) {
		this.monthlydata = monthlydata;
	}
	public outageDetials getOutagedetails() {
		return outagedetails;
	}
	public void setOutagedetails(outageDetials outagedetails) {
		this.outagedetails = outagedetails;
	}
	public meterEvents getMeterevents() {
		return meterevents;
	}
	public void setMeterevents(meterEvents meterevents) {
		this.meterevents = meterevents;
	}
	public poleImprovments getPoleimprov() {
		return poleimprov;
	}
	public void setPoleimprov(poleImprovments poleimprov) {
		this.poleimprov = poleimprov;
	}
	public FeedBack getFeedback() {
		return feedback;
	}
	public void setFeedback(FeedBack feedback) {
		this.feedback = feedback;
	}
	
}
