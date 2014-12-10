package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		double regPeriod = 0.02; //s
//		RegulAndIO reg = new RegulAndIO(regPeriod);
		FeedbackController f = new FeedbackController(regPeriod);
		OpCom opCom = new OpCom(null, f);
//		reg.start();
		f.start();
	}
}
