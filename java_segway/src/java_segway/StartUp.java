package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		float regPeriod = 0.02f; //s
//		RegulAndIO reg = new RegulAndIO(regPeriod);
		FeedbackController reg = new FeedbackController(regPeriod);
		OpCom opCom = new OpCom(null, reg);
		reg.start();
	}
}
