import java.util.Vector;

public class customer {
	private String customorid;
	
	private Vector<MonthlyData> monthlydata;
	private Vector< outageDetials> outagedetails;
	private Vector<meterEvents> meterevents;
	private Vector<poleImprovments> poleimprov;
	private Vector<FeedBack> feedback;
	
	public customer() {
		customorid =null;
		monthlydata =new Vector<MonthlyData>();
		outagedetails =new Vector<outageDetials>();
		meterevents =new Vector<meterEvents>();
		poleimprov =new Vector<poleImprovments>();
		feedback =new Vector<FeedBack>();
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
	
	public String getcustomorid() {
		return customorid;
	}
	public void setcustomorid(String n) {
		customorid =n;
	}
	public Vector<MonthlyData> getMonthlydata() {
		return monthlydata;
	}
	public void addMonthlydata(MonthlyData monthlydata) {
		this.monthlydata.addElement(monthlydata);
	}
	public Vector<outageDetials> getOutagedetails() {
		return outagedetails;
	}
	public void addOutagedetails(outageDetials outagedetails) {
		this.outagedetails.addElement(outagedetails);
	}
	public Vector<meterEvents> getMeterevents() {
		return meterevents;
	}
	public void addMeterevents(meterEvents meterevents) {
		this.meterevents.addElement(meterevents);
	}
	public Vector<poleImprovments> getPoleimprov() {
		return poleimprov;
	}
	public void addPoleimprov(poleImprovments poleimprov) {
		this.poleimprov.addElement(poleimprov);
	}
	public Vector<FeedBack> getFeedback() {
		return feedback;
	}
	public void addFeedback(FeedBack feedback) {
		this.feedback.addElement(feedback);
	}
	
}
