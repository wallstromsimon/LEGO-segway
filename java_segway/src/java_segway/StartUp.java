package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		long refPeriod = 10000;
		long regPeriod = 20;
		long ioPeriod = 25;
		int deg = 3600;
//		IOMonitor ioM = new IOMonitor();
		RefGen refGen = new RefGen(refPeriod, deg, 0);
//		IO io = new IO(ioPeriod, ioM);
//		Regul regul = new Regul(regPeriod, refGen, ioM);
		
//		io.start();
//		refGen.start();
//		regul.start();
		
		RegulAndIO reg = new RegulAndIO(regPeriod, refGen);
		OpCom opCom = new OpCom(refGen, reg);
		reg.start();
	}
}
