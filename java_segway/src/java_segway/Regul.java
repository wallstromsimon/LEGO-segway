package java_segway;

import lejos.nxt.Button;


public class Regul extends Thread{

	long period;
	PIDParam paramInner;
	PIDParam paramOuter;
	PIDController inner;
	PIDController outer;

	private RefGen refGen;
	private IOMonitor ioM;

	double uMin = -100;
	double uMax = 100;
	double youter, yinner, ref, uouter, uinner;

	public Regul(long period, RefGen refGen, IOMonitor ioM) {
		this.period = period;
		this.refGen = refGen;
		this.ioM = ioM;
		this.period = period;
		paramInner = new PIDParam(-7.34137741596504, -44.6834487503629, -0.0504302228504053, 28.7822625226348, 1, 1, period);
		paramOuter = new PIDParam(0.00205773648435991, 3.50732954404154e-05, 0.00998848510035523, 5.41093442969671, 1, 1, period);
		inner = new PIDController(paramInner);
		outer = new PIDController(paramOuter);

	}

	private double limit(double u) {
		if (u < uMin) {
			u = uMin;
		} else if (u > uMax) {
			u = uMax;
		} 
		return u;
	}

	public void run(){
		long t = System.currentTimeMillis();
		long duration;
		while(!isInterrupted()){
			synchronized (ioM) {
				youter = ioM.getPos();
				ref = 0;//refGen.getRef();
				yinner = ioM.getAngle();
				uouter = limit(outer.calculateOutput(youter, ref));
				uinner = limit(inner.calculateOutput(yinner, uouter));
				ioM.setMotor(uinner);
			}
			t = t + period;
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}