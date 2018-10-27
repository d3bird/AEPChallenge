
public class customer {
	String customorid;
	
	private MonthlyData monthlydata;
	private outageDetials outagedetails;
	private meterEvents meterevents;
	
	public customer(String n) {
		customorid =n;
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
	
}
