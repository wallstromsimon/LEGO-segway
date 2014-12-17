package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		float regPeriod = 0.01f; //s
//		RegulAndIO reg = new RegulAndIO(regPeriod);
		FeedbackController reg = new FeedbackController(regPeriod);
		OpCom opCom = new OpCom(null, reg);
//		opCom.start(); //Start to be able to change L in real time
		reg.start();
	}
}
