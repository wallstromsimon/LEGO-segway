package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		double regPeriod = 0.017; //s
//		RegulAndIO reg = new RegulAndIO(regPeriod);
		FeedbackController reg = new FeedbackController(regPeriod);
		OpCom opCom = new OpCom(null, reg);
		reg.start();
	}
}
