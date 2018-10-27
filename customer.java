
public class customer {
	String customorid;
	
	private MonthlyData monthlydata;
	
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
	
	
	
}
