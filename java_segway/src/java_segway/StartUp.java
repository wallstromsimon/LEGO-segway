package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		long refPeriod = 10000;
		long regPeriod = 5;
		long ioPeriod = 3;
		int deg = 3600;
		IOMonitor ioM = new IOMonitor();
		RefGen refGen = new RefGen(refPeriod, deg, 0);
		IO io = new IO(ioPeriod, ioM);
		Regul regul = new Regul(regPeriod, refGen, ioM);
		
		io.start();
		refGen.start();
		regul.start();
	}
}
