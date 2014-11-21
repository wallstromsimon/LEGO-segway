package java_segway;


public class StartUp {
	
	public static void main(String[] args){
		long refPeriod = 10000;
		long regPeriod = refPeriod/1000;
		long ioPeriod = regPeriod;
		int deg = 3600;
		IOMonitor ioM = new IOMonitor();
		RefGen refGen = new RefGen(refPeriod, deg);
		IO io = new IO(ioPeriod, ioM);
		Regul regul = new Regul(regPeriod, refGen, ioM);
		
		io.start();
		refGen.start();
		regul.start();
	}
}
