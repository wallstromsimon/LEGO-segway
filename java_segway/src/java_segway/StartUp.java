package java_segway;

import lejos.nxt.Button;

public class StartUp {
	
	public static void main(String[] args){
		long refPeriod = 10000;
		long regPeriod = refPeriod/1000;
		int deg = 10;
		RefGen refGen = new RefGen(refPeriod, deg);
		IO io = new IO();
		Regul regul = new Regul(regPeriod, refGen, io);
		
		refGen.start();
		regul.start();
	}
}
