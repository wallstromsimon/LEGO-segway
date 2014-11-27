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

	double uMin = -100000;
	double uMax = 100000;
	double youter, yinner, ref, uouter, uinner;

	public Regul(long period, RefGen refGen, IOMonitor ioM) {
		this.period = period;
		this.refGen = refGen;
		this.ioM = ioM;
		this.period = period;
		paramInner = new PIDParam(-16.7792819869313, -106.029027583105, -0.0258879740182201, 130.717571074188, 1, 1, period);
		paramOuter = new PIDParam(0.000754332462460638, 7.9250040069361e-06, 0.00820996434126155, 2.9025731973979, 1, 1, period);
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
		while(!Button.ESCAPE.isDown()){


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